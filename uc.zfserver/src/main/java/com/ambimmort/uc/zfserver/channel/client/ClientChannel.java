/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.channel.client;

import com.ambimmort.uc.zfserver.channel.ConnectionListener;
import com.ambimmort.uc.zfserver.bean.ConnectionState;
import com.ambimmort.uc.zfserver.DPIEndPoint;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class ClientChannel implements ConnectionListener{
    
    public ConnectionState state = ConnectionState.DisConnected;
    
    private ClientConnection connection;
    
    private DPIEndPoint dpiEndPoint;

    public ClientChannel(DPIEndPoint dpiEndPoint) {
       
        this.dpiEndPoint = dpiEndPoint;
    }
    
    

    @Override
    public void onConnected(IoSession session) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onConnecting(IoSession session) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDisConnected(IoSession session) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
