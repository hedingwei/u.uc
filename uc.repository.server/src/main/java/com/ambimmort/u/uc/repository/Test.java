/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository;

import java.util.HashMap;

/**
 *
 * @author 定巍
 */
public class Test {

    public static void p(HashMap... m){
        m[0] = new HashMap();
        m[0].put("1", "1");
    }
    
    public static void main(String[] args) {
        HashMap[] m =  new HashMap[]{new HashMap()};
        p(m);
        System.out.println(m[0]);
//        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//        org.apache.cxf.endpoint.Client client = dcf.createClient("http://localhost:9001/reposerver/?wsdl");
//        QName name=new QName("http://webservice.bean.repository.uc.u.ambimmort.com/","start");
//        try {
//            client.invoke(name, "http://localhost:9000/bsd1?wsdl");
//            Object[] rst = client.invoke(new QName("http://webservice.bean.repository.uc.u.ambimmort.com/","isStarted"));
//            System.out.println(rst[0]);
//        JaxWsProxyFactoryBean  factoryBean=new JaxWsProxyFactoryBean();
//
//        factoryBean.setServiceClass(RepositoryServerManagementWebServiceBean.class);
//        factoryBean.setAddress("http://localhost:9001/?wsdl");
//        RepositoryServerManagementWebServiceBean server = (RepositoryServerManagementWebServiceBean) factoryBean.create(); 
//        server.start("http://localhost:9000/?wsdl=api.wsdl");
//        
//        System.out.println(server.isStarted());
//        
//        try {
//            Thread.sleep(10000);
//            server.stop();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
      
    }
}
