/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.dpi.simulator.server.core;

import com.ambimmort.uc.dpi.simulator.server.UcServerConfig;
import com.ambimmort.ucserver.client.Connection;
import java.util.Map;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class LocalUcMessageHandler extends IoHandlerAdapter {

//    UdMessageCache cache = UdMessageCache.getInstance();
    private static Connection client = null;

    static {
        client = new Connection((String)((Map) UcServerConfig.getConfig().get("dpi")).get("ip"), (Integer)((Map) UcServerConfig.getConfig().get("dpi")).get("port"));
        if (client != null) {
            client.start();
        }
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        UcRawMessage msg = (UcRawMessage) message;
        
        
        
        if(msg.getMessageType()==0x00){

            
        }else{
            client.send(msg);
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        session.close(true);
    }

}
