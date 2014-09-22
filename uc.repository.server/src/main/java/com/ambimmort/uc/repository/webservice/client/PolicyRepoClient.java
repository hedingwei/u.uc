/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.repository.webservice.client;

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

    public boolean isOK() {
        try {
            invoke("test1");
            return true;
        } catch (Exception ex) {
            
            return false;
        }
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
            client = dcf.createClient("http://localhost:9004/?wsdl");
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
        return client.invoke(new QName("http://rmapi.webservice.bean.repository.uc.u.ambimmort.com/", method), paras);
    }

    public void destroy() {
        client.destroy();
    }

}
