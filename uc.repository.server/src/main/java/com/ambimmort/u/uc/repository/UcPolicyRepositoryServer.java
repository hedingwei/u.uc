/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository;

import com.ambimmort.u.uc.repository.webservice.rmapi.RepositoryManagementWebServiceBeanImpl;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 *
 * @author 定巍
 */
public class UcPolicyRepositoryServer {

    private static UcPolicyRepositoryServer instance = null;

    private UcPolicyRepositoryServer() {
        UcPolicyRepository.init();
    }

    public static UcPolicyRepositoryServer getInstance() {
        if (instance == null) {
            instance = new UcPolicyRepositoryServer();
        }
        return instance;
    }

    private Server server = null;
    private String address;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void start() {
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setServiceClass(RepositoryManagementWebServiceBeanImpl.class);
        factory.setAddress(address);
        server = factory.create();
        server.start();
    }

    public void stop() {
        server.stop();
    }
    
    public boolean isStarted(){
        if(server==null){
            return false;
        }
        return server.isStarted();
    }
            

}
