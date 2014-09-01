/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.bean.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 *
 * @author 定巍
 */
@DatabaseTable(tableName = "AppData_User_Link")
public class AppData_User_Link {

    @DatabaseField(id = true)
    private String username;

    @DatabaseField(index = true)
    private String groupName;

    @DatabaseField
    private long messageSerialNo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getMessageSerialNo() {
        return messageSerialNo;
    }

    public void setMessageSerialNo(long messageSerialNo) {
        this.messageSerialNo = messageSerialNo;
    }

}
