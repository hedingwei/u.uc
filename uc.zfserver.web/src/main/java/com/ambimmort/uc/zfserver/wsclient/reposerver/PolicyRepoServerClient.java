/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.wsclient.reposerver;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 *
 * @author 定巍
 */
public class PolicyRepoServerClient {

    private static PolicyRepoServerClient instance = null;

    public static PolicyRepoServerClient getInstance() {
        if (instance == null) {
            instance = new PolicyRepoServerClient();
        }
        return instance;
    }

    private org.apache.cxf.endpoint.Client client = null;

    private PolicyRepoServerClient() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        client = dcf.createClient("http://localhost:9001/reposerver/?wsdl");
    }

    public boolean isStarted() throws Exception {
        Object[] rst = client.invoke(new QName("http://rsmapi.webservice.bean.repository.uc.u.ambimmort.com/", "isStarted"));
        return (Boolean) rst[0];
    }

    public boolean start(String url) throws Exception {
        Object[] rst = client.invoke(new QName("http://rsmapi.webservice.bean.repository.uc.u.ambimmort.com/", "start"), url);
        return (Boolean) rst[0];
    }

    public Object[] invoke(String method, String... paras) throws Exception {
        return client.invoke(new QName("http://rsmapi.webservice.bean.repository.uc.u.ambimmort.com/", method), paras);
    }

    public void destroy() {
        client.destroy();
    }

    public void c() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client1 = dcf.createClient("http://localhost:9001/reposerver/?wsdl");
        try {
            Object[] rst = client1.invoke(new QName("http://rsmapi.webservice.bean.repository.uc.u.ambimmort.com/", "start"),"http://localhost:9004");
            System.out.println(rst[0]);
        } catch (Exception ex) {
            Logger.getLogger(PolicyRepoServerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void d() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client1 = dcf.createClient("http://localhost:9004/?wsdl");
        try {
            Object[] rst = client1.invoke(new QName("http://rmapi.webservice.bean.repository.uc.u.ambimmort.com/", "test1"));
            System.out.println(rst[0]);
        } catch (Exception ex) {
            Logger.getLogger(PolicyRepoServerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        PolicyRepoServerClient.getInstance().c();
        PolicyRepoServerClient.getInstance().d();
    }
}
