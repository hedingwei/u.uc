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
@DatabaseTable(tableName = "UcMessageAckLogBean")
public class UcMessageAckLogBean {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(index = true)
    private String ip;

    @DatabaseField(index = true)
    private Date receiveTime;

    @DatabaseField(index = true)
    private int messageType;

    @DatabaseField(index = true)
    private int messageNo;

    @DatabaseField(index = true)
    private long messageSequenceNo;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] ack;

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

    public long getMessageSequenceNo() {
        return messageSequenceNo;
    }

    public void setMessageSequenceNo(long messageSequenceNo) {
        this.messageSequenceNo = messageSequenceNo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public byte[] getAck() {
        return ack;
    }

    public void setAck(byte[] ack) {
        this.ack = ack;
    }
    
    

}
