/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.channel.client;

import com.ambimmort.uc.zfserver.bean.UcMessageSendingLogBean;
import com.ambimmort.uc.zfserver.component.database.MyDaoManager;
import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPoint;
import com.ambimmort.uc.zfserver.component.transport.codec.UcRawMessage;
import com.ambimmort.uc.zfserver.type.ConnectionState;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import com.ambimmort.ucserver.util.MessageSequenceNoUtil;
import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class ClientChannel {

    public ConnectionState state = ConnectionState.DisConnected;

    private ClientConnection connection = null;

    private DPIEndPoint dpiEndPoint = null;

    private Dao<UcMessageSendingLogBean, Long> logDao = null;

    public ClientChannel(DPIEndPoint dpiEndPoint) {
        this.dpiEndPoint = dpiEndPoint;
        ClientChannelHandler handler = new ClientChannelHandler();
        connection = new ClientConnection(dpiEndPoint.getDpiEndPointBean().getIp(), dpiEndPoint.getDpiEndPointBean().getPort(), handler);
        connection.start();
        try {
            logDao = MyDaoManager.getInstance().getDao(UcMessageSendingLogBean.class);
        } catch (SQLException ex) {
            Logger.getLogger(ClientChannel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setState(ConnectionState state) {
        this.state = state;
        dpiEndPoint.getDpiEndPointBean().setClientChannelState(state);
    }

    private void generateLog(UcMsg msg) throws SQLException {
        UcMessageSendingLogBean log = new UcMessageSendingLogBean();
        log.setDevName(this.dpiEndPoint.getDpiEndPointBean().getDevName());
        log.setIp(this.dpiEndPoint.getDpiEndPointBean().getIp());
        log.setMessageNo(msg.getHeader().getMessageNo().toInteger());
        log.setMessageType(msg.getHeader().getMessageType().toInteger());
        log.setMessageSequenceNo(msg.getHeader().getMessageSequenceNo().toInteger());
        log.setAckTime(new Date(1970, 0, 0));
        log.setSendTime(new Date(System.currentTimeMillis()));
        log.setAck(null);
        log.setMsg(msg.toBytes());
        logDao.create(log);
    }

    public boolean send(UcMsg msg) {
        if (state.equals(ConnectionState.Connected)) {
            try {
                msg.getHeader().setMessageSequenceNo(UcType.newUINT4(MessageSequenceNoUtil.getAUsableMessageSequenceNo(msg.getHeader().getMessageType().toInteger())));
                msg.normalize();
            } catch (UcTypeException ex) {
                Logger.getLogger(ClientChannel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ClientChannel.class.getName()).log(Level.SEVERE, null, ex);
            }
            UcRawMessage ucrm = new UcRawMessage();
            ucrm.setHeader(msg.getHeader().getBytes());
            ucrm.setBody(msg.getBodyBytes());
            try {
                generateLog(msg);
            } catch (SQLException ex) {
                Logger.getLogger(ClientChannel.class.getName()).log(Level.SEVERE, null, ex);
            }
            return connection.send(ucrm);
        } else {
            return false;
        }

    }

    public void stop() {
        connection.stopMe();
    }

}
