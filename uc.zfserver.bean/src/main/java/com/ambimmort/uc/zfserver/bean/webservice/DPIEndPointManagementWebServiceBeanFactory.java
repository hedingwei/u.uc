/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.bean.webservice;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 *
 * @author 定巍
 */
public class DPIEndPointManagementWebServiceBeanFactory {

    public static DPIEndPointManagementWebServiceBean getDPIEndPointManagementWebServiceBean(String address) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(DPIEndPointManagementWebServiceBean.class);
        factory.setAddress(address);
        DPIEndPointManagementWebServiceBean client = (DPIEndPointManagementWebServiceBean) factory.create();
        return client;
    }

}
