/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.u.uc.webservice;
import com.ambimmort.u.uc.repository.bean.webservice.RepositoryManagementWebServiceBean;
import com.ambimmort.u.uc.repository.bean.webservice.RepositoryManagementWebServiceBeanFactory;
/**
 *
 * @author 定巍
 */
public class Main {
    private static final String ADDRESS = "http://localhost:9000/cxfdemo"; 
    public static void main(String[] args){
        RepositoryManagementWebServiceBean bean = RepositoryManagementWebServiceBeanFactory.getRepositoryManagementWebServiceBean("http://localhost:9000/UcRepositoryAdminWebService");
       
//        System.out.println(bean.listRepositoryTypes());
        System.out.println(bean.listRepositories("0x00"));
//        System.out.println(bean.getHeadPolicies("0x00", "db1"));
//        System.out.println(bean.getHeadVersion("0x00", "db1"));
//        System.out.println(bean.getMessage("0x00", "db1", 0));
//        System.out.println(bean.getMessageLogs("0x00", "db1", 0));
        
    }
}
