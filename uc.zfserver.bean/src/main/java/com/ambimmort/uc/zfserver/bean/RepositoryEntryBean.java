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

    @DatabaseField(uniqueCombo = true, uniqueIndex = true)
    private String type;

    @DatabaseField(uniqueCombo = true, uniqueIndex = true)
    private String instanceName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

}
