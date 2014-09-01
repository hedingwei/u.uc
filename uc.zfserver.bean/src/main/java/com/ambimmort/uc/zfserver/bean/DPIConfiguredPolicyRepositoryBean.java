/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 事先配置好的每一套DPI所支持的策略仓库
 * @author 定巍
 */
@DatabaseTable(tableName = "DPIConfiguredPolicyRepositoryBean")
public class DPIConfiguredPolicyRepositoryBean {

    @DatabaseField(id = true)
    private String name;
    @DatabaseField(dataType = DataType.LONG_STRING)
    private String value = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
