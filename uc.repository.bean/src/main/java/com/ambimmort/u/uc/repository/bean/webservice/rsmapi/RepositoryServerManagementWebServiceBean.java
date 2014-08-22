/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository.bean.webservice.rsmapi;

import javax.jws.WebService;

/**
 *
 * @author 定巍
 */
@WebService(name = "rsmapi")
public interface RepositoryServerManagementWebServiceBean {

    public boolean start(String address);
    
    public boolean stop();
    
    public boolean isStarted();
    
}
