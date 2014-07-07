/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository;

import com.ambimmort.u.uc.repository.webservice.RepositoryManagementWebServiceBeanImpl;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.xml.ws.Endpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 *
 * @author 定巍
 */
public class Main {

    private static final String ADDRESS = "http://localhost:9000/";

    public static void main(String[] args) {
        
        UcPolicyRepository.init();
        
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();  
        factory.setServiceClass(RepositoryManagementWebServiceBeanImpl.class);  
        factory.setAddress(ADDRESS);
        Server server = factory.create();  
        server.start();
//        JAXRSServerFactoryBean jaxrsServerFactory = RuntimeDelegate.getInstance().createEndpoint(new JaxRsApiApplication(), JAXRSServerFactoryBean.class);
//        jaxrsServerFactory.setAddress("http://localhost:9000");
//        List<Object> list = new LinkedList<Object>();
//        list.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
//        list.add(new com.wordnik.swagger.jaxrs.listing.ResourceListingProvider());
//        list.add(new com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider());
//        com.wordnik.swagger.jaxrs.config.BeanConfig config = new com.wordnik.swagger.jaxrs.config.BeanConfig();
//        config.setBasePath("com.ambimmort.u.uc.repository.bean.webservice");
//        config.setTitle("tttt");
//        config.setLicense("dfsdf");
//        config.setContact("dfsdf");
//        config.setVersion("1.0.0");
//        config.setScan(true);
//        jaxrsServerFactory.setProviders(list);
//        
////        jaxrsServerFactory.setAddress("http://localhost:9000/ws/jaxrs");  
//        org.apache.cxf.endpoint.Server server = jaxrsServerFactory.create();
//        server.start();
//        System.out.println("dddd");
        
 
      
        
        
    }
}
