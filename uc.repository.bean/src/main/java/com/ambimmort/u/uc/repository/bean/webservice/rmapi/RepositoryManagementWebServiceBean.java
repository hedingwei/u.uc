/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository.bean.webservice.rmapi;

import com.ambimmort.u.uc.repository.bean.PolicyBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationLogBean;
import java.util.List;
import javax.jws.WebService;

/**
 *
 * @author 定巍
 */
@WebService(name = "rmapi")
public interface RepositoryManagementWebServiceBean {


    public String test1();
    
    public String test2();
    
    public String test3();
    
    public void createRepository( String type, String instance);

    public List<String> listRepositories( String type);

    public List<String> listRepositoryTypes();

    public void createUcRepoSuite(String name);
    
    public String getUcRepoSuite(String name);
 
    public RepositoryOperationLogBean addPolicy( String type,  String instance,  String policy);

    public RepositoryOperationLogBean updatePolicy( String type,  String instance,  int messageNo, String policy);
    
    public RepositoryOperationLogBean deletePolicy( String type,  String instance, int messageNo);

    public RepositoryOperationLogBean getMessage( String type,  String instance, int messageNo);

    public List<RepositoryOperationLogBean> getMessageLogs( String type,  String instance, final int messageNo);

    public List<PolicyBean> getHeadPolicies( String type,  String instance);
    
    public long getHeadPoliciesCount(String type, String instance);
    
    public List<RepositoryOperationLogBean> getHeadPoliciesWithLog( String type,  String instance, int pageCount, int page);

    public List<RepositoryOperationBean> getUpdatingOperationBeans( String type,  String instance, long startVersion);

    public long getHeadVersion(String type, String instance);

}
