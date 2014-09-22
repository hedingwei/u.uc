/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.wsclient.reposerver;

import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 *
 * @author 定巍
 */
public class PolicyRepoClient {

    private static PolicyRepoClient instance = null;

    private org.apache.cxf.endpoint.Client client = null;


    public static PolicyRepoClient getInstance() {
        if (instance == null) {
            instance = new PolicyRepoClient();
        }
        return instance;
    }

    private PolicyRepoClient() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        try {
            client = dcf.createClient("http://localhost:9004/?wsdl");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void refresh() {
        if (client != null) {

            client.destroy();
        }
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        try {
            client = dcf.createClient("http://localhost:9004" + "/?wsdl");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private PolicyRepoClient(String url) {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        try {
            client = dcf.createClient(url);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
 

    public Object[] invoke(String method, Object... paras) throws Exception {
        if(client == null){
            refresh();
            throw new NullPointerException();
        }else{
            return client.invoke(new QName("http://rmapi.webservice.bean.repository.uc.u.ambimmort.com/", method), paras);
        }
        
    }

    public void destroy() {
        client.destroy();
    }

    
    public static void main(String[] args){
        try {
            System.out.println("DDDDDDDDDDDD1111111111");
            System.out.println( PolicyRepoClient.getInstance().invoke("listRepositories", new Object[]{"0x00"}));
        } catch (Exception ex) {
            Logger.getLogger(PolicyRepoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
