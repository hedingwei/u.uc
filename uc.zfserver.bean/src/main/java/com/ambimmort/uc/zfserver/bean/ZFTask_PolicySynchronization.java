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
@DatabaseTable(tableName = "ZFTask_PolicySynchronization")
public class ZFTask_PolicySynchronization {

    @DatabaseField(generatedId = true)
    private long id;
    
    @DatabaseField(index = true)
    private String devName;

    @DatabaseField(index = true)
    private Date createTime;

    @DatabaseField(index = true)
    private Date proccedTime;

    @DatabaseField(index = true)
    private boolean isProceed;

    @DatabaseField(index = true)
    private boolean isSuccess;

    @DatabaseField(dataType = DataType.LONG_STRING)
    private String info;
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getProccedTime() {
        return proccedTime;
    }

    public void setProccedTime(Date proccedTime) {
        this.proccedTime = proccedTime;
    }

    public boolean isIsProceed() {
        return isProceed;
    }

    public void setIsProceed(boolean isProceed) {
        this.isProceed = isProceed;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    

}
