/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.policyrepo;

import java.net.MalformedURLException;
import java.net.URI;

import com.ambimmort.uc.repository.services.client.RepositoryManagementWebServiceBeanService;
import com.ambimmort.uc.repository.services.client.Rmapi;

/**
 *
 * @author 定巍
 */
public class PolicyRepoClient {
	
	RepositoryManagementWebServiceBeanService service = null;
	
	Rmapi api = null;

    private static PolicyRepoClient instance = null;


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
    	try {
			service = new RepositoryManagementWebServiceBeanService(URI.create("http://localhost:9004/?wsdl").toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    	api = service.getRmapiPort();

        
    }

    public void refresh() {
       api = null;
       api  = service.getRmapiPort();
    }
    
    public Rmapi getApi() {
		return api;
	}


}
