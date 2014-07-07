/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.bean;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Set;

/**
 *
 * @author 定巍
 */
@DatabaseTable(tableName = "DPIEndPointBean")
public class DPIEndPointBean {

    @DatabaseField(id = true)
    private String name;

    @DatabaseField(index = true)
    private String serverChannelIP;

    @DatabaseField(index = true)
    private int serverChannelPort;

    @DatabaseField(index = true)
    private String clientChannelIP;

    @DatabaseField(index = true)
    private int clientChannelPort;

    @DatabaseField(index = true)
    private boolean isOn;

    @DatabaseField(dataType = DataType.ENUM_INTEGER, index = true)
    private ConnectionState serverChannelState;

    @DatabaseField(dataType = DataType.ENUM_INTEGER, index = true)
    private ConnectionState clientChannelState;

    @ForeignCollectionField
    private ForeignCollection<RepositoryEntryBean> repositories;

    public int getClientChannelPort() {
        return clientChannelPort;
    }

    public void setClientChannelPort(int clientChannelPort) {
        this.clientChannelPort = clientChannelPort;
    }

    public ConnectionState getServerChannelState() {
        return serverChannelState;
    }

    public void setServerChannelState(ConnectionState serverChannelState) {
        this.serverChannelState = serverChannelState;
    }

    public ConnectionState getClientChannelState() {
        return clientChannelState;
    }

    public void setClientChannelState(ConnectionState clientChannelState) {
        this.clientChannelState = clientChannelState;
    }

    public String getServerChannelIP() {
        return serverChannelIP;
    }

    public void setServerChannelIP(String serverChannelIP) {
        this.serverChannelIP = serverChannelIP;
    }

    public int getServerChannelPort() {
        return serverChannelPort;
    }

    public void setServerChannelPort(int serverChannelPort) {
        this.serverChannelPort = serverChannelPort;
    }

    public String getClientChannelIP() {
        return clientChannelIP;
    }

    public void setClientChannelIP(String clientChannelIP) {
        this.clientChannelIP = clientChannelIP;
    }

    public boolean isIsOn() {
        return isOn;
    }

    public void setIsOn(boolean isOn) {
        this.isOn = isOn;
    }

    public ForeignCollection<RepositoryEntryBean> getRepositories() {
        return repositories;
    }

    public void setRepositories(ForeignCollection<RepositoryEntryBean> repositories) {
        this.repositories = repositories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DPIEndPointBean{" + "name=" + name + ", serverChannelIP=" + serverChannelIP + ", serverChannelPort=" + serverChannelPort + ", clientChannelIP=" + clientChannelIP + ", isOn=" + isOn + ", repositories=" + repositories + '}';
    }

}
