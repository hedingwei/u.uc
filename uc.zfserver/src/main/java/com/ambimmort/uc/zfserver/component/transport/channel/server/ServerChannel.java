/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.transport.channel.server;

import com.ambimmort.uc.zfserver.type.ConnectionState;
import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPoint;
import org.apache.mina.core.session.IoSession;

/**
 * @author 定巍
 */
public class ServerChannel {

    public ConnectionState state = ConnectionState.DisConnected;
    private DPIEndPoint dpiEndPoint = null;

    private IoSession session = null;

    public void setState(ConnectionState state) {
        this.state = state;
        dpiEndPoint.getDpiEndPointBean().setServerChannelState(state);
    }

    public ConnectionState getState() {
        return state;
    }

    public void stopMe() {
        if (session != null) {
            session.close(true);
        }
    }

    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public ServerChannel(DPIEndPoint dpiEndPoint) {
        this.dpiEndPoint = dpiEndPoint;
    }

    public DPIEndPoint getDpiEndPoint() {
        return dpiEndPoint;
    }

}
