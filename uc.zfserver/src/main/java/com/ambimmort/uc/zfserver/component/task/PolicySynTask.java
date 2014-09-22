/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.task;

import com.ambimmort.u.uc.repository.bean.RepositoryOperationBean;
import com.ambimmort.uc.zfserver.bean.DPIConfiguredPolicyRepositoryBean;
import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.bean.ZFTask_PolicySynchronization;
import com.ambimmort.uc.zfserver.bean.ucdata.UcData_DPIReportedPolicyVersionBean;

import com.ambimmort.uc.zfserver.component.database.MyDaoManager;
import com.ambimmort.uc.zfserver.component.policyrepo.PolicyRepoClient;
import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPointManager;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import com.ambimmort.ucserver.ucmessages.uc.xC3;
import com.ambimmort.ucserver.util.HexDisplay;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jivesoftware.smack.util.Base64;

/**
 *
 * @author 定巍
 */
public class PolicySynTask extends TimerTask {

    @Override
    public void run() {
        System.out.println("started policySynTask");
        try {
            Map<String, Entity> taskMap = null;
            Map<String, Long> currentVersion = null;
            Map<String, Long> reportedVersion = null;

            for (String devName : allEnabledDPIDevNames()) {
                HashMap<String, String>[] config = new HashMap[]{new HashMap<String, String>()};
                currentVersion = getCurrentVersion(devName, config);
                reportedVersion = getReportedVersion(devName);
                taskMap = inter(currentVersion, reportedVersion);
                ZFTask_PolicySynchronization bean = makeSynTask(devName, taskMap);

                d(devName, taskMap, config[0], bean);
            }
        } catch (Exception ex) {
            Logger.getLogger(PolicySynTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doSingle(String devName) {
        try {
            Map<String, Entity> taskMap = null;
            Map<String, Long> currentVersion = null;
            Map<String, Long> reportedVersion = null;

            HashMap<String, String>[] config = new HashMap[]{new HashMap<String, String>()};
            currentVersion = getCurrentVersion(devName, config);
            reportedVersion = getReportedVersion(devName);
            taskMap = inter(currentVersion, reportedVersion);
            ZFTask_PolicySynchronization bean = makeSynTask(devName, taskMap);
            d(devName, taskMap, config[0], bean);
        } catch (Exception ex) {
            Logger.getLogger(PolicySynTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void d(String devName, Map<String, Entity> taskMap, HashMap<String, String> config, ZFTask_PolicySynchronization xbean) throws Exception {
        if (taskMap.isEmpty() || xbean == null) {
            return;
        }
        String instance = null;
        for (String type : taskMap.keySet()) {
            instance = config.get(type);
            JSONArray actions = getUpdateOperation(type, instance, taskMap.get(type).start);
            xC3 synBack = new xC3(Integer.parseInt(type.replace("0x", ""), 16), taskMap.get(type).end);
            for (int i = 0; i < actions.size(); i++) {
                JSONObject bean = actions.getJSONObject(i);
                UcMsg msg = UcMsg.buildUcMsg(type, new String(Base64.decode(bean.getString("content")), "utf-8"));
                if (msg.isVersionManaged()) {
                    msg.setMessageSerialNo(UcType.UINT4.newUINT4(taskMap.get(type).end));
                }
                synBack.addMessage(bean.getInt("operation"), msg);
            }

            System.out.println("===type[" + devName + "]====");
            System.out.println(actions);
            System.out.println("===message[" + devName + "]====");
            System.out.println(HexDisplay.getHex(synBack.toBytes()));
            Dao<ZFTask_PolicySynchronization, Long> dao = MyDaoManager.getInstance().getDao(ZFTask_PolicySynchronization.class);
            if (DPIEndPointManager.getInstance().getByName(devName).getClientChannel().send(synBack)) {
                xbean.setIsProceed(true);
                xbean.setIsSuccess(true);
                xbean.setProccedTime(new Date(System.currentTimeMillis()));
                dao.update(xbean);
            } else {
                xbean.setIsProceed(false);
                xbean.setProccedTime(new Date(System.currentTimeMillis()));
                dao.update(xbean);
            }

        }

    }

    private ZFTask_PolicySynchronization makeSynTask(String devName, Map<String, Entity> taskMap) throws SQLException {
        if (taskMap.isEmpty()) {
            return null;
        }
        Dao<ZFTask_PolicySynchronization, Long> dao = MyDaoManager.getInstance().getDao(ZFTask_PolicySynchronization.class);
        List<ZFTask_PolicySynchronization> beans = dao.queryBuilder().where().eq("devName", devName).and().eq("isProceed", false).query();
        if (beans.isEmpty()) {
            ZFTask_PolicySynchronization bean = new ZFTask_PolicySynchronization();
            bean.setCreateTime(new Date(System.currentTimeMillis()));
            bean.setDevName(devName);
            bean.setIsProceed(false);
            bean.setIsSuccess(false);
            bean.setProccedTime(new Date(System.currentTimeMillis()));
            bean.setInfo(JSONObject.fromObject(taskMap).toString());
            dao.create(bean);
            return bean;
        } else {

            beans.get(0).setCreateTime(new Date(System.currentTimeMillis()));
            beans.get(0).setInfo(JSONObject.fromObject(taskMap).toString());
            dao.update(beans.get(0));
            return beans.get(0);
        }

    }

    private Map<String, Entity> inter(Map<String, Long> currentVersion, Map<String, Long> reportedVersion) {
        Map<String, Entity> taskMap = new HashMap();
        for (String type : currentVersion.keySet()) {
            if (reportedVersion.containsKey(type)) {
                if (reportedVersion.get(type) < currentVersion.get(type)) {
                    Entity e = new Entity();
                    e.start = reportedVersion.get(type);
                    e.end = currentVersion.get(type);
                    taskMap.put(type, e);
                }
            }
        }

        return taskMap;
    }

    private long getHeadVersion(String type, String instance) throws Exception {
    	return PolicyRepoClient.getInstance().getApi().getHeadVersion(type, instance);
//        return (Long) PolicyRepoClient.getInstance().invoke("getHeadVersion", type, instance)[0];
    }

    private JSONArray getUpdateOperation(String type, String instance, long startVersion) throws Exception {
        return JSONArray.fromObject(PolicyRepoClient.getInstance().getApi().getUpdatingOperationBeans(type, instance, startVersion));
//    	return JSONArray.fromObject(PolicyRepoClient.getInstance().invoke("getUpdatingOperationBeans", type, instance, startVersion)[0]);
    }

    private Map<String, Long> getCurrentVersion(String devName, Map<String, String>... configMaps) throws SQLException, Exception {
        Dao<DPIConfiguredPolicyRepositoryBean, String> dao = MyDaoManager.getInstance().getDao(DPIConfiguredPolicyRepositoryBean.class);
        Map<String, Long> versions = new HashMap<String, Long>();

        String config = dao.queryForId(devName).getValue();

        Map<String, String> configMap = (HashMap<String, String>) JSONObject.toBean(JSONObject.fromObject(config), HashMap.class);
        configMaps[0] = configMap;
        for (String type : configMap.keySet()) {
            versions.put(type, getHeadVersion(type, configMap.get(type)));
        }

        return versions;
    }

    private Map<String, Long> getReportedVersion(String devName) throws SQLException {
        Dao<UcData_DPIReportedPolicyVersionBean, Long> rdao = MyDaoManager.getInstance().getDao(UcData_DPIReportedPolicyVersionBean.class);
        Map<String, Long> versions = new HashMap<String, Long>();
        List<UcData_DPIReportedPolicyVersionBean> list = rdao.queryBuilder().where().eq("devName", devName).query();
        for (UcData_DPIReportedPolicyVersionBean bean : list) {
            versions.put(bean.getMessageType(), bean.getMessageSerialNo());
        }
        return versions;
    }

    private List<String> allEnabledDPIDevNames() throws SQLException {
        List<String> names = new ArrayList<String>();
        for (DPIEndPointBean bean : MyDaoManager.getInstance().getDao(DPIEndPointBean.class).queryForAll()) {
            if (!bean.isEnable()) {
                continue;
            }
            names.add(bean.getDevName());
        }
        return names;
    }

}
