/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.channel.client;

import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPoint;
import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPointManager;
import com.ambimmort.uc.zfserver.type.ConnectionState;
import com.ambimmort.uc.zfserver.codec.UcRawMessage;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class ClientChannelHandler extends IoHandlerAdapter {

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
        if (DPIEndPointManager.getInstance().getEndPoints().containsKey(addr.getAddress().getHostAddress())) {
            DPIEndPoint ep = DPIEndPointManager.getInstance().getEndPoints().get(addr.getAddress().getHostAddress());
            ep.getClientChannel().setState(ConnectionState.Connected);
            ep.persistence();
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message instanceof UcRawMessage) {
            UcRawMessage msg = (UcRawMessage) message;
            if (msg.getMessageType() == (byte) 0xcd) {  // this is ack message
                UcMsg.xCD ack = new UcMsg.xCD();
                ack.parse(msg.getHeader(), msg.getBody());
                System.out.println(ack.toString());
                
            }
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
        if (DPIEndPointManager.getInstance().getEndPoints().containsKey(addr.getAddress().getHostAddress())) {
            DPIEndPoint ep = DPIEndPointManager.getInstance().getEndPoints().get(addr.getAddress().getHostAddress());
            ep.getClientChannel().setState(ConnectionState.DisConnected);
            ep.persistence();
        }
    }

}
