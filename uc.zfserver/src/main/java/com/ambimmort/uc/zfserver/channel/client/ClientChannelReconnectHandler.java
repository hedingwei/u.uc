/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.channel.client;


import com.ambimmort.uc.zfserver.bean.ConnectionState;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class ClientChannelReconnectHandler extends IoHandlerAdapter {

    Logger logger = Logger.getLogger(ClientConnection.class.getName());

    ClientConnection connection = null;
    
    IoHandler handler = null;

    public ClientChannelReconnectHandler(ClientConnection connection,IoHandler handler) {
        this.connection = connection;
        this.handler = handler;
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        handler.messageReceived(session, message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        connection.needReconnect();
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        connection.needReconnect();
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        connection.setConnetionState(ConnectionState.Connected);
        connection.setSession(session);
        connection.notifyConnected(session);
        System.out.println("session [" + session.getId() + "][" + session.isConnected() + "] opened local:" + session.getLocalAddress() + " remote:" + session.getRemoteAddress());
        System.out.println("**********----***********");
    }
}
