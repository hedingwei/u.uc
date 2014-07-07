/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 定巍
 */
@XmlRootElement(name="PolicyBean")
@DatabaseTable(tableName = "SVNFILE")
public class PolicyBean implements Comparable<PolicyBean>{

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(index = true)
    private int messageNo = 1;

    @DatabaseField()
    private String content;

    @DatabaseField(defaultValue = "false", canBeNull = false, index = true)
    private boolean isNewest;

    @DatabaseField(defaultValue = "false", canBeNull = false, index = true)
    private boolean isDeleted;

    public boolean isIsNewest() {
        return isNewest;
    }

    public void setIsNewest(boolean isNewest) {
        this.isNewest = isNewest;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMessageNo() {
        return messageNo;
    }

    public void setMessageNo(int messageNo) {
        this.messageNo = messageNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SVNFile{" + "id=" + id + ", messageNo=" + messageNo + ", content=" + content + ", isNewest=" + isNewest + ", isDeleted=" + isDeleted + '}';
    }

    @Override
    public int compareTo(PolicyBean o) {
        return (int)(this.id-o.id);
    }

}
