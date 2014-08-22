/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository.bean.webservice.rmapi;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 *
 * @author 定巍
 */
public class RepositoryManagementWebServiceBeanFactory {

    public static RepositoryManagementWebServiceBean getRepositoryManagementWebServiceBean(String address) {
        JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setServiceClass(RepositoryManagementWebServiceBean.class);
        bean.setAddress(address);
        RepositoryManagementWebServiceBean client = (RepositoryManagementWebServiceBean) bean.create();
        return client;
    }

}
