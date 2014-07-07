/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository.bean.webservice;

import com.ambimmort.u.uc.repository.bean.PolicyBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationLogBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import java.util.List;
import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author 定巍
 */
@WebService(name = "api")
public interface RepositoryManagementWebServiceBean {


    public void createRepository( String type,  String instance);

  
    public List<String> listRepositories( String type);


    public List<String> listRepositoryTypes();

 
    public RepositoryOperationLogBean addPolicy( String type,  String instance,  String policy);

    public RepositoryOperationLogBean updatePolicy( String type,  String instance,  int messageNo, String policy);


    public RepositoryOperationLogBean deletePolicy( String type,  String instance, int messageNo);

    public RepositoryOperationLogBean getMessage( String type,  String instance, int messageNo);

    public List<RepositoryOperationLogBean> getMessageLogs( String type,  String instance, final int messageNo);

    public List<PolicyBean> getHeadPolicies( String type,  String instance);

    public List<RepositoryOperationBean> getUpdatingOperationBeans( String type,  String instance, long startVersion);

    public long getHeadVersion(String type, String instance);

}
