/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.channel.client;

import com.ambimmort.uc.zfserver.bean.UcMessageSendingLogBean;
import com.ambimmort.uc.zfserver.component.transport.codec.UcRawMessage;
import com.ambimmort.uc.zfserver.component.database.MyDaoManager;
import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPoint;
import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPointManager;
import com.ambimmort.uc.zfserver.type.ConnectionState;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.j256.ormlite.dao.Dao;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class ClientChannelHandler extends IoHandlerAdapter {

    Dao<UcMessageSendingLogBean, Long> dao = null;

    public ClientChannelHandler() {
        try {
            dao = MyDaoManager.getInstance().getDao(UcMessageSendingLogBean.class);
        } catch (SQLException ex) {
            Logger.getLogger(ClientChannelHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
//                long messageSerquenceNo = msg.getMessageSequenceNo();
                UcMsg.xCD ack = new UcMsg.xCD();
                ack.parse(msg.getHeader(), msg.getBody());
                UcMessageSendingLogBean ql = new UcMessageSendingLogBean();
                ql.setMessageNo(ack.getMessageNo().toInteger());
                ql.setMessageSequenceNo((int)ack.getMessageSequenceNo().toLong());
                ql.setMessageType(ack.getMessageType().toInteger());
                List<UcMessageSendingLogBean> beans = dao.queryForMatching(ql);
                if (!beans.isEmpty()) {
                    UcMessageSendingLogBean bean = beans.get(0);
                    bean.setAckTime(new Date(System.currentTimeMillis()));
                    bean.setAck(msg.getBytes());
                    dao.update(bean);
                    System.out.println("updated ack in database");
                }
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
