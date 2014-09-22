/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.u.uc.repository.webservice.rmapi;

import com.ambimmort.u.uc.repository.UcPolicyRepository;
import com.ambimmort.u.uc.repository.bean.PolicyBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationBean;
import com.ambimmort.u.uc.repository.bean.RepositoryOperationLogBean;
import com.ambimmort.u.uc.repository.bean.webservice.rmapi.InstanceBean;
import com.ambimmort.u.uc.repository.bean.webservice.rmapi.RepositoryManagementWebServiceBean;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jws.WebService;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author 定巍
 */
@WebService
public class RepositoryManagementWebServiceBeanImpl implements RepositoryManagementWebServiceBean {

    @Override
    public void createRepository(String type, String instance) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
    }

    @Override
    public List<InstanceBean> listRepositories(String type) {
        List<UcPolicyRepository> repos = UcPolicyRepository.getAllRepositorys();
        List<InstanceBean> repoNames = new ArrayList<InstanceBean>();
        for (UcPolicyRepository repo : repos) {
            if (repo.getEntry().getMessageType().equals(type)) {
                InstanceBean bean = new InstanceBean();
                bean.setType(type);
                bean.setInstance(repo.getEntry().getInstanceName());
                bean.setSerialNo(getHeadVersion(type, repo.getEntry().getInstanceName()));
                repoNames.add(bean);
            }
        }

        return repoNames;
    }

    @Override
    public List<String> listRepositoryTypes() {
        List<UcPolicyRepository> repos = UcPolicyRepository.getAllRepositorys();
        List<String> repoNames = new ArrayList<String>();
        for (UcPolicyRepository repo : repos) {
            if (!repoNames.contains(repo.getEntry().getMessageType())) {
                repoNames.add(repo.getEntry().getMessageType());
            }
        }
        return repoNames;
    }

    @Override
    public RepositoryOperationLogBean addPolicy(String type, String instance, String policy,String comment) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        return repo.getSvnKit().create(policy, comment);
    }

    @Override
    public RepositoryOperationLogBean updatePolicy(String type, String instance, int messageNo, String policy,String comment) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        return repo.getSvnKit().update(messageNo, policy, comment);
    }

    @Override
    public RepositoryOperationLogBean deletePolicy(String type, String instance, int messageNo) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        return repo.getSvnKit().delete(messageNo);
    }

    @Override
    public RepositoryOperationLogBean getMessage(String type, String instance, int messageNo) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        return repo.getSvnKit().getMessageWithLog(messageNo);
    }

    @Override
    public List<PolicyBean> getHeadPolicies(String type, String instance) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        return repo.getSvnKit().checkOutHEAD();
    }

    @Override
    public List<RepositoryOperationBean> getUpdatingOperationBeans(String type, String instance, long startVersion) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        List<RepositoryOperationBean> actions = new ArrayList();
        TreeMap<Integer, RepositoryOperationBean> t = repo.getSvnKit().update(startVersion, repo.getHEAD());
        for (Integer k : t.keySet()) {
            actions.add(t.get(k));
        }
        return actions;
    }

    @Override
    public long getHeadVersion(String type, String instance) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        return repo.getSvnKit().getHeadVersionNumber();
    }

    @Override
    public List<RepositoryOperationLogBean> getMessageLogs(String type, String instance, int messageNo) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        return repo.getSvnKit().getMessageLogs(messageNo);
    }

    @Override
    public void createUcRepoSuite(String name) {
        int[] types = new int[256];
        String tmp = null;
        for (Integer i = 0; i < 256; i++) {
            tmp = Integer.toHexString(i);
            if (tmp.length() == 1) {
                tmp = "0x0" + tmp;
            } else {
                tmp = "0x" + tmp;
            }
            createRepository(tmp, name);
        }
    }

    @Override
    public String getUcRepoSuite(String name) {
        JSONObject obj = new JSONObject();
       
        List<String> types = listRepositoryTypes();
        List<InstanceBean> instances = null;
        for(String type: types){
            instances = listRepositories(type);
            for(InstanceBean instance: instances){
                if(instance.getInstance().equals(name)){
                    try {
                        obj.put(name, type);
                    } catch (JSONException ex) {
                        Logger.getLogger(RepositoryManagementWebServiceBeanImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return obj.toString();
    }

    @Override
    public String test1() {
        return "ok1";
    }

    @Override
    public String test2() {
        return "ok2";
    }

    @Override
    public String test3() {
        return "ok3";
    }

    @Override
    public List<RepositoryOperationLogBean> getHeadPoliciesWithLog(String type, String instance, int pageCount, int page) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        List<PolicyBean> beans = repo.getSvnKit().checkOutHEAD(pageCount, page);
        List<RepositoryOperationLogBean> logs = new ArrayList<RepositoryOperationLogBean>();
        for(PolicyBean b:beans){
            logs.add(getMessage(type, instance, b.getMessageNo()));
        }
        return logs;
    }

    @Override
    public long getHeadPoliciesCount(String type, String instance) {
        UcPolicyRepository repo = UcPolicyRepository.getInstance(UcPolicyRepository.repositoryEntry("repo", type, instance), false);
        return repo.getSvnKit().checkOutHEADCount();
    }

}
