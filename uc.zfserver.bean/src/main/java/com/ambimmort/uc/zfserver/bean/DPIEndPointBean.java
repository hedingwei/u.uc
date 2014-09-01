/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.bean;

import com.ambimmort.uc.zfserver.type.ConnectionState;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author 定巍
 */
@DatabaseTable(tableName = "DPIEndPointBean")
public class DPIEndPointBean {

    @DatabaseField(id = true)
    private String name;

    @DatabaseField(index = true)
    private String devName;

    @DatabaseField(index = true, canBeNull = false, defaultValue = "0")
    private byte probeType;  //0x00:DPI; 0x01:EU

    @DatabaseField(index = true, canBeNull = false, defaultValue = "")
    private String deploySiteName;

    @DatabaseField(index = true, canBeNull = false, defaultValue = "NonHouse_Id")
    private String idcHouseId;

    @DatabaseField(index = true)
    private String ip;

    @DatabaseField(index = true)
    private int port;

    @DatabaseField(dataType = DataType.ENUM_INTEGER, index = true)
    private ConnectionState serverChannelState;

    @DatabaseField(dataType = DataType.ENUM_INTEGER, index = true)
    private ConnectionState clientChannelState;
    

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public byte getProbeType() {
        return probeType;
    }

    public void setProbeType(byte probeType) {
        this.probeType = probeType;
    }

    public String getDeploySiteName() {
        return deploySiteName;
    }

    public void setDeploySiteName(String deploySiteName) {
        this.deploySiteName = deploySiteName;
    }

    public String getIdcHouseId() {
        return idcHouseId;
    }

    public void setIdcHouseId(String idcHouseId) {
        this.idcHouseId = idcHouseId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public DPIEndPointBean() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DPIEndPointBean{" + "name=" + name + ", devName=" + devName + ", probeType=" + probeType + ", deploySiteName=" + deploySiteName + ", idcHouseId=" + idcHouseId + ", ip=" + ip + ", port=" + port + ", serverChannelState=" + serverChannelState + ", clientChannelState=" + clientChannelState + '}';
    }



    
    

}
