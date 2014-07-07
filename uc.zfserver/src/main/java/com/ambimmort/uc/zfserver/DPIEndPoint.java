/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver;

import com.ambimmort.uc.zfserver.channel.client.ClientChannel;
import com.ambimmort.uc.zfserver.channel.server.ServerChannel;
import com.ambimmort.uc.zfserver.bean.DPIEndPointBean;

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
    
    
}
