/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.policyrepo;

import com.ambimmort.uc.repository.services.client.RepositoryManagementWebServiceBeanImplService;
import com.ambimmort.uc.repository.services.client.Rmapi;
import com.ambimmort.uc.zfserver.component.database.dao.ZFPropertyBeanDao;

import javax.xml.namespace.QName;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 *
 * @author 定巍
 */
public class PolicyRepoClient {
	
	RepositoryManagementWebServiceBeanImplService service = null;
	
	Rmapi api = null;

    private static PolicyRepoClient instance = null;

//    private org.apache.cxf.endpoint.Client client = null;

    public static PolicyRepoClient getInstance() {
        if (instance == null) {
            instance = new PolicyRepoClient();
        }
        return instance;
    }

    public boolean isOK() {
    
    	
        try {
        	api.test1();
            return true;
        } catch (Exception ex) {
            
            return false;
        }
    }

    private PolicyRepoClient() {
    	
    	api = service.getRepositoryManagementWebServiceBeanImplPort();

        
    }

    public void refresh() {
       api = null;
       api  = service.getRepositoryManagementWebServiceBeanImplPort();
    }
    
    public Rmapi getApi() {
		return api;
	}

//
//    public Object[] invoke(String method, Object... paras) throws Exception {
//        return client.invoke(new QName("http://rmapi.webservice.bean.repository.uc.u.ambimmort.com/", method), paras);
//    }
//
//    public void destroy() {
//        client.destroy();
//    }

}
