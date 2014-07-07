/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author 定巍
 */
@DatabaseTable(tableName = "RepositoryEntryBean")
public class RepositoryEntryBean {

    @DatabaseField(generatedId = true)
    private long id;
    
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "DPIEndPointBean_Id")
    private DPIEndPointBean endPoint;
    
    @DatabaseField(index = true)
    private String type;
    
    @DatabaseField(index = true)
    private String name;

    public DPIEndPointBean getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(DPIEndPointBean endPoint) {
        this.endPoint = endPoint;
    }
    
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RepositoryEntryBean{" + "id=" + id + ", type=" + type + ", name=" + name + '}';
    }

    
    
}
