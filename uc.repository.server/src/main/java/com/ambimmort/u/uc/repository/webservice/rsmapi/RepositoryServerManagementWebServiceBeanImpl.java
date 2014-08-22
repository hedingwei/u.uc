/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository.webservice.rsmapi;

import com.ambimmort.u.uc.repository.UcPolicyRepositoryServer;
import com.ambimmort.u.uc.repository.bean.webservice.rsmapi.RepositoryServerManagementWebServiceBean;
import javax.jws.WebService;

/**
 *
 * @author 定巍
 */
@WebService
public class RepositoryServerManagementWebServiceBeanImpl implements RepositoryServerManagementWebServiceBean {

    @Override
    public boolean start(String address) {
        if (UcPolicyRepositoryServer.getInstance().isStarted()) {
            UcPolicyRepositoryServer.getInstance().stop();
        }
        UcPolicyRepositoryServer.getInstance().setAddress(address);
        UcPolicyRepositoryServer.getInstance().start();
        return true;
    }

    @Override
    public boolean stop() {
        if (UcPolicyRepositoryServer.getInstance().isStarted()) {
            UcPolicyRepositoryServer.getInstance().stop();
        }
        return true;
    }

    @Override
    public boolean isStarted() {
        return UcPolicyRepositoryServer.getInstance().isStarted();
    }

}
