/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.webservice;

import com.ambimmort.u.uc.repository.bean.RepositoryOperationLogBean;
import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.component.database.MyDaoManager;
import com.ambimmort.uc.zfserver.component.policyrepo.PolicyRepoClient;
import com.ambimmort.uc.zfserver.component.task.PolicySynTask;
import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPoint;
import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPointManager;
import com.ambimmort.uc.zfserver.type.ConnectionState;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author 定巍
 */
public class WebServiceBeanImpl implements WebServiceBean {

    @Override
    public String sendSyncResponse(String devName) {
        PolicySynTask task = new PolicySynTask();
        task.doSingle(devName);
        return "{\"code\":1}";
    }

    @Override
    public String send(String devName, String messageType, String instance, String messageNo) {
        DPIEndPoint ep = DPIEndPointManager.getInstance().getByName(devName);
        if (ep != null) {
            try {
                MyDaoManager.getInstance().getDao(DPIEndPointBean.class).refresh(ep.getDpiEndPointBean());
            } catch (SQLException ex) {
                Logger.getLogger(WebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (ep != null && ep.getDpiEndPointBean().isEnable() && ep.getClientChannel().state == ConnectionState.Connected) {
            try {
            	JSONObject bean = JSONObject.fromObject(PolicyRepoClient.getInstance().getApi().getMessage( messageType, instance, Integer.parseInt(messageNo)));
//                JSONObject bean = JSONObject.fromObject(PolicyRepoClient.getInstance().invoke("getMessage", messageType, instance, Integer.parseInt(messageNo))[0]);
                String content = new String(Base64.decodeBase64(bean.getJSONObject("svnFile").getString("content")), "utf-8");
                UcMsg msg = UcMsg.buildUcMsg(messageType, content);
                if (DPIEndPointManager.getInstance().getByName(devName).getClientChannel().send(msg)) {
                    return "{\"code\":1}";
                } else {
                    return "{\"code\":0}";
                }
            } catch (Exception ex) {
                Logger.getLogger(WebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "{\"code\":1}";
    }

    @Override
    public String getSynActions(String devName) {

        return "";
    }

}
