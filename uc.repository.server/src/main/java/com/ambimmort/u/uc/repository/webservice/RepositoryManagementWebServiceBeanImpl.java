/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository.webservice;

import com.ambimmort.u.uc.repository.UcPolicyRepository;
import com.ambimmort.u.uc.repository.bean.PolicyBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationLogBean;
import com.ambimmort.u.uc.repository.bean.webservice.RepositoryManagementWebServiceBean;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.jws.WebService;
import javax.ws.rs.core.Response;

/**
 *
 * @author 定巍
 */
@WebService
public class RepositoryManagementWebServiceBeanImpl implements RepositoryManagementWebServiceBean {

    @Override
    public void createRepository(String type, String instance) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("C:\\Users\\定巍\\Downloads\\test", type, instance), true);
    }

    @Override
    public List<String> listRepositories(String type) {
        List<UcPolicyRepository> repos = UcPolicyRepository.getAllRepositorys();
        List<String> repoNames = new ArrayList<String>();
        for (UcPolicyRepository repo : repos) {
            if (repo.getEntry().getMessageType().equals(type)) {
                repoNames.add(repo.getEntry().getInstanceName());
            }
        }
        
        return repoNames;
    }

    @Override
    public List<String> listRepositoryTypes() {
        List<UcPolicyRepository> repos = UcPolicyRepository.getAllRepositorys();
        List<String> repoNames = new ArrayList<String>();
        for (UcPolicyRepository repo : repos) {
            if(!repoNames.contains(repo.getEntry().getMessageType())){
                repoNames.add(repo.getEntry().getMessageType());
            }
        }
        return repoNames;
    }

    @Override
    public RepositoryOperationLogBean addPolicy(String type, String instance, String policy) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("C:\\Users\\定巍\\Downloads\\test", type, instance), false);
        return repo.getSvnKit().create(policy);
    }

    @Override
    public RepositoryOperationLogBean updatePolicy(String type, String instance, int messageNo, String policy) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("C:\\Users\\定巍\\Downloads\\test", type, instance), false);
        return repo.getSvnKit().update(messageNo, policy);
    }

    @Override
    public RepositoryOperationLogBean deletePolicy(String type, String instance, int messageNo) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("C:\\Users\\定巍\\Downloads\\test", type, instance), false);
        return repo.getSvnKit().delete(messageNo);
    }

    @Override
    public RepositoryOperationLogBean getMessage(String type, String instance, int messageNo) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("C:\\Users\\定巍\\Downloads\\test", type, instance), false);
        return repo.getSvnKit().getMessageWithLog(messageNo);
    }
    

    @Override
    public List<PolicyBean> getHeadPolicies(String type, String instance) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("C:\\Users\\定巍\\Downloads\\test", type, instance), false);
        return repo.getSvnKit().checkOutHEAD();
    }

    @Override
    public List<RepositoryOperationBean> getUpdatingOperationBeans(String type, String instance, long startVersion) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("C:\\Users\\定巍\\Downloads\\test", type, instance), false);
        List<RepositoryOperationBean> actions = new ArrayList();
        TreeMap<Integer, RepositoryOperationBean> t = repo.getSvnKit().update(startVersion, repo.getHEAD());
        for(Integer k: t.keySet()){
            actions.add(t.get(k));
        }
        return actions;
    }

    @Override
    public long getHeadVersion(String type, String instance) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("C:\\Users\\定巍\\Downloads\\test", type, instance), false);
        return repo.getSvnKit().getHeadVersionNumber();
    }

    @Override
    public List<RepositoryOperationLogBean> getMessageLogs(String type, String instance, int messageNo) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("C:\\Users\\定巍\\Downloads\\test", type, instance), false);
        return repo.getSvnKit().getMessageLogs(messageNo);
    }

}
