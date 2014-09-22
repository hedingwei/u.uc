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
@XmlRootElement(name="RepositoryOperationLogBean")
@DatabaseTable(tableName = "SVNLOG")
public class RepositoryOperationLogBean {

    public static final int ADD = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(canBeNull = false)
    private int operation;

    @DatabaseField(index = true)
    private long createTime = System.currentTimeMillis();

    @DatabaseField(foreign = true, canBeNull = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2)
    private PolicyBean svnFile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public PolicyBean getSvnFile() {
        return svnFile;
    }

    public void setSvnFile(PolicyBean svnFile) {
        this.svnFile = svnFile;
    }

    @Override
    public String toString() {
        return "SVNLog{" + "id=" + id + ", operation=" + operation + ", createTime=" + createTime + ", svnFile=" + svnFile + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RepositoryOperationLogBean other = (RepositoryOperationLogBean) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    

}
