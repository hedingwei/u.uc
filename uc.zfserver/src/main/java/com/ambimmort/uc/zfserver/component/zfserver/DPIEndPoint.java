/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.zfserver;

import com.ambimmort.uc.zfserver.channel.client.ClientChannel;
import com.ambimmort.uc.zfserver.channel.server.ServerChannel;
import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;
import com.ambimmort.uc.zfserver.component.database.dao.DPIEndPointBeanDao;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class DPIEndPoint {

    private DPIEndPointBean dpiEndPointBean = null;

    public DPIEndPoint(DPIEndPointBean dpiEndPointBean) {
        this.dpiEndPointBean = dpiEndPointBean;
        clientChannel = new ClientChannel(this);
        serverChannel = new ServerChannel(this);
    }

    private ClientChannel clientChannel;
    private ServerChannel serverChannel;

    public DPIEndPointBean getDpiEndPointBean() {
        return dpiEndPointBean;
    }

    public void setDpiEndPointBean(DPIEndPointBean dpiEndPointBean) {
        this.dpiEndPointBean = dpiEndPointBean;
    }

    public ClientChannel getClientChannel() {
        return clientChannel;
    }

    public void setClientChannel(ClientChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public ServerChannel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(ServerChannel serverChannel) {
        this.serverChannel = serverChannel;
    }
    
    public void persistence(){
        try {
            DPIEndPointBeanDao.getInstance().getDpiEndPointDao().update(this.dpiEndPointBean);
        } catch (SQLException ex) {
            Logger.getLogger(DPIEndPoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return this.dpiEndPointBean.toString();
    }
    
    
    
}
