/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.wsclient.reposerver;

import com.ambimmort.uc.zfserver.db.util.PropertyUtil;
import com.ambimmort.uc.zfserver.messages.EventHandler;
import com.ambimmort.uc.zfserver.messages.MessageDriven;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 *
 * @author 定巍
 */
public class PolicyRepoClient {

    private static PolicyRepoClient instance = null;

    private org.apache.cxf.endpoint.Client client = null;
    
    private Timer timer = new Timer();

    public static PolicyRepoClient getInstance() {
        if (instance == null) {
            instance = new PolicyRepoClient();
        }
        return instance;
    }

    private PolicyRepoClient() {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {
                    invoke("test1");
                } catch (Exception ex) {
                    MessageDriven.getInstance().fire("rpurl.changed", null);
                }
            }
        }, 10000, 30000);
        MessageDriven.getInstance().listen("rpurl.changed", new EventHandler() {
            @Override
            public void onEvent(String name, Map<String, Object> args) {
                refresh();
            }
        });

        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        try {
            client = dcf.createClient(PropertyUtil.getInstance().getProperty("PolicyRepoServer.webserver.url") + "/?wsdl");
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
            client = dcf.createClient(PropertyUtil.getInstance().getProperty("PolicyRepoServer.webserver.url") + "/?wsdl");
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
