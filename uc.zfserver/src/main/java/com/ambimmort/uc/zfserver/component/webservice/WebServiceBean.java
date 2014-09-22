/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.component.webservice;

import javax.jws.WebService;

/**
 *
 * @author 定巍
 */
@WebService(name = "zfserverAPI")
public interface WebServiceBean {

    public String send(String devName, String messageType, String instance, String messageNo);
    
    public String sendSyncResponse(String devName);
    
    public String getSynActions(String devName);
    
    
    
}
