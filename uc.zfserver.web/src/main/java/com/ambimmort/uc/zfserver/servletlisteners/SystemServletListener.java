/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.servletlisteners;

import com.ambimmort.uc.zfserver.db.entities.PolicyRepositoryBean;
import com.ambimmort.uc.zfserver.db.util.PolicyRepositoryUtil;
import com.ambimmort.uc.zfserver.db.util.PropertyUtil;
import com.ambimmort.uc.zfserver.wsclient.reposerver.PolicyRepoClient;
import com.ambimmort.uc.zfserver.wsclient.reposerver.PolicyRepoServerClient;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Web application lifecycle listener.
 *
 * @author 定巍
 */
@WebListener()
public class SystemServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

//        try {
//            PropertyUtil.getInstance().newProperty("PolicyRepoServer.webserver.url", "http://localhost:9001");
//        } catch (SQLException ex) {
//            Logger.getLogger(SystemServletListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        PolicyRepoServerClient prsClient = PolicyRepoServerClient.getInstance();
//        try {
//            prsClient.start(PropertyUtil.getInstance().getProperty("PolicyRepoServer.webserver.url"));
//            System.out.println("started");
//        } catch (Exception ex) {
//            Logger.getLogger(SystemServletListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        PolicyRepositoryBean bean = new PolicyRepositoryBean();
//        bean.setName("GreeNet-DPI");
//        try {
//            bean.setId(bean.getName());
//            PolicyRepositoryUtil.getInstance().getPropertyDao().createIfNotExists(bean);
//        } catch (SQLException ex) {
//            Logger.getLogger(SystemServletListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        try {
//            PolicyRepoClient.getInstance().invoke("createUcRepoSuite", bean.getId());
//        } catch (Exception ex) {
//            Logger.getLogger(SystemServletListener.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
