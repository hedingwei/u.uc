/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 *
 * @author 定巍
 */
@DatabaseTable(tableName = "UcMessageSendingLogBean")
public class UcMessageSendingLogBean {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(index = true)
    private String devName;

    @DatabaseField(index = true)
    private String ip;

    @DatabaseField(index = true)
    private Date sendTime;

    @DatabaseField(index = true)
    private Date ackTime;
    
    @DatabaseField(index = true)
    private int messageType;
            
    @DatabaseField(index = true)
    private int messageNo;
    
    @DatabaseField(index = true)
    private int messageSequenceNo;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] ack;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] msg;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getMessageNo() {
        return messageNo;
    }

    public void setMessageNo(int messageNo) {
        this.messageNo = messageNo;
    }

    public int getMessageSequenceNo() {
        return messageSequenceNo;
    }

    public void setMessageSequenceNo(int messageSequenceNo) {
        this.messageSequenceNo = messageSequenceNo;
    }

    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getAckTime() {
        return ackTime;
    }

    public void setAckTime(Date ackTime) {
        this.ackTime = ackTime;
    }

    public byte[] getAck() {
        return ack;
    }

    public void setAck(byte[] ack) {
        this.ack = ack;
    }

    public byte[] getMsg() {
        return msg;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }
    
    

}
