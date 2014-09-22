/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.wsclient.zfserver;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 *
 * @author 定巍
 */
public class ZFClient {

    private static ZFClient instance = null;

    private org.apache.cxf.endpoint.Client client = null;

    public static ZFClient getInstance() {
        if (instance == null) {
            instance = new ZFClient();
        }
        return instance;
    }



    private ZFClient() {
       
       

        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        try {
            client = dcf.createClient("http://localhost:9003/zfserver?wsdl");
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
            client = dcf.createClient("http://localhost:9003/?wsdl");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private ZFClient(String url) {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        try {
            client = dcf.createClient(url);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public Object[] invoke(String method, Object... paras) throws Exception {
        return client.invoke(new QName("http://webservice.component.zfserver.uc.ambimmort.com/", method), paras);
    }

    public void destroy() {
        client.destroy();
    }
    
    public static void main(String[] args){
        ZFClient client = new ZFClient();
        try {
            String s = (String) client.invoke("send","dev-1", "0x01","GreeNet-1","0")[0];
            System.out.println(s);
        } catch (Exception ex) {
            Logger.getLogger(ZFClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
