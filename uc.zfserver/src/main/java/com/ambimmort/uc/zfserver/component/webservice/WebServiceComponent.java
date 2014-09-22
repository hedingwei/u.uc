/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.webservice;

import com.ambimmort.uc.zfserver.component.ZFComponent;
import java.sql.SQLException;
import net.sf.json.JSONObject;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 *
 * @author 定巍
 */
public class WebServiceComponent extends ZFComponent {

    private Server server = null;
    
    private static final String ADDRESS = "http://localhost:9003/zfserver";

    public WebServiceComponent() throws SQLException {
        
    }

    @Override
    public String getName() {
        return "WebServiceComponent";
    }

    @Override
    protected void refreshState() {
        
    }

    
    
    @Override
    protected void poststart() throws Throwable {

        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();  
        factory.setServiceClass(WebServiceBeanImpl.class);  
        factory.setAddress(ADDRESS);
        Server server = factory.create();  
        server.start();
    }

    

    @Override
    public JSONObject getStates() {
        return new JSONObject();
    }

}
