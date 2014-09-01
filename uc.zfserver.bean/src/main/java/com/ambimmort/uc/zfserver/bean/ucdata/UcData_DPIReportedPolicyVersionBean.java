/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.bean.ucdata;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * DPI实际需要同步的策略仓库版本
 *
 * @author 定巍
 */
@DatabaseTable(tableName = "UcData_DPIReportedPolicyVersionBean")
public class UcData_DPIReportedPolicyVersionBean {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(index = true, uniqueCombo = true)
    private String devName;

    @DatabaseField(index = true, uniqueCombo = true)
    private String messageType;

    @DatabaseField
    private long messageSerialNo;

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

 

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public long getMessageSerialNo() {
        return messageSerialNo;
    }

    public void setMessageSerialNo(long messageSerialNo) {
        this.messageSerialNo = messageSerialNo;
    }

    @Override
    public String toString() {
        return "UcData_DPIReportedPolicyVersionBean{" + "id=" + id + ", devName=" + devName + ", messageType=" + messageType + ", messageSerialNo=" + messageSerialNo + '}';
    }

    
    
}
