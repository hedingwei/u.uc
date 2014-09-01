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
@DatabaseTable(tableName = "SequenceNoPtrBean")
public class SequenceNoPtrBean {

    @DatabaseField(id = true)
    private int messageType;

    @DatabaseField(index = true)
    private long sequenceNo;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

   

    public long getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(long sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    @Override
    public String toString() {
        return "SequenceNoPtrBean{" + "messageType=" + messageType + ", sequenceNo=" + sequenceNo + '}';
    }
    
    

}
