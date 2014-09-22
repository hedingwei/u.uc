/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.policyrepo;

import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.bean.DPIConfiguredPolicyRepositoryBean;
import com.ambimmort.uc.zfserver.bean.ZFComponentBean;
import com.ambimmort.uc.zfserver.component.ZFComponent;
import com.ambimmort.uc.zfserver.component.database.MyDaoManager;

import com.ambimmort.uc.zfserver.component.database.dao.ZFPropertyBeanDao;
import com.ambimmort.uc.zfserver.component.messageDriven.EventHandler;
import com.ambimmort.uc.zfserver.component.messageDriven.MDEComponent;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;

/**
 *
 * @author 定巍
 */
public class PolicyRepositoryComponent extends ZFComponent {

    private static PolicyRepositoryComponent instance = null;
    private JSONObject state = new JSONObject();

    private PolicyRepositoryComponent() {

        MDEComponent.getInstance().listen("rpurl.changed", new EventHandler() {
            @Override
            public void onEvent(String name, Map<String, Object> args) {
                PolicyRepoClient.getInstance().refresh();
            }
        });
    }

    public static PolicyRepositoryComponent getInstance() throws SQLException {
        if (instance == null) {
            instance = new PolicyRepositoryComponent();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "DBComponent";
    }

    @Override
    protected void refreshState() {
        try {
            boolean policyRepoServer_isStarted = PolicyRepoServerClient.getInstance().isStarted();
            if (!policyRepoServer_isStarted) {
                PolicyRepoServerClient.getInstance().start(ZFPropertyBeanDao.getInstance().getProperty("PolicyRepoServer.webserver.url") + "/?wsdl");
            }
            state.put("prsc_isStarted", policyRepoServer_isStarted);
            if (PolicyRepoClient.getInstance().isOK()) {
                state.put("prc_isStarted", true);
            } else {
                state.put("prc_isStarted", false);
                MDEComponent.getInstance().fire("rpurl.changed", null);
            }
            ZFComponentBean bean = new ZFComponentBean();
            bean.setName(getName());
            bean.setStates(state.toString(4));
            MyDaoManager.getInstance().getDao(ZFComponentBean.class).createOrUpdate(bean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initRepository() {

        try {
            Map<String, String> m = new HashMap<String, String>();
            List<DPIEndPointBean> beans = MyDaoManager.getInstance().getDao(DPIEndPointBean.class).queryForAll();
            for (DPIEndPointBean bean : beans) {
                DPIConfiguredPolicyRepositoryBean prb = ((Dao<DPIConfiguredPolicyRepositoryBean, String>) MyDaoManager.getInstance().getDao(DPIConfiguredPolicyRepositoryBean.class)).queryForId(bean.getDevName());
                JSONObject tmplate = JSONObject.fromObject(prb.getValue());
                JSONObject repo = tmplate.getJSONObject("repo");
                for (Object obj : repo.keySet()) {
                    String key = (String) obj;
                    String name = repo.getString(key);
                    m.put(key, name);
                }
            }
            for (String k : m.keySet()) {
                try {
                    System.out.println("k;" + k + "\tv:" + m.get(k));
                    PolicyRepoClient.getInstance().getApi().createRepository(k, m.get(k));
//                    PolicyRepoClient.getInstance().invoke("createRepository", k, m.get(k));
                } catch (Exception ex) {
                    Logger.getLogger(PolicyRepositoryComponent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PolicyRepositoryComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void prestart() throws Throwable {
        PolicyRepoServerClient.getInstance().start(ZFPropertyBeanDao.getInstance().getProperty("PolicyRepoServer.webserver.url") + "/?wsdl");
        initRepository();
    }

    @Override
    public JSONObject getStates() {
        return state;
    }

}
