/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.servletlisteners;

import com.ambimmort.uc.zfserver.db.util.PropertyUtil;
import com.ambimmort.uc.zfserver.wsclient.reposerver.PolicyRepoClient;
import com.ambimmort.uc.zfserver.wsclient.reposerver.PolicyRepoServerClient;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Web application lifecycle listener.
 *
 * @author 定巍
 */
@WebListener()
public class ReqeustServletListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
//        ServletRequest req = sre.getServletRequest();
//        if (req instanceof HttpServletRequest) {
//            HttpServletRequest hreq = (HttpServletRequest) req;
//            String url = hreq.getRequestURI();
//            System.out.println("BBBBBBBBB");
//            System.out.println(url);
//            if (url.startsWith(hreq.getContextPath() + "/system/setting/PolicyRepoClientCall")) {
//                System.out.println("PolicyRepoClientCall Destroyed");
//                if (sre.getServletRequest().getAttribute("prClient") != null) {
//                    ((PolicyRepoClient) sre.getServletRequest().getAttribute("prClient")).destroy();
//                }
//            } else if (url.startsWith(hreq.getContextPath() + "/system/setting/PolicyRepoServerClient")) {
//                if (sre.getServletRequest().getAttribute("prsClient") != null) {
//                    ((PolicyRepoServerClient) sre.getServletRequest().getAttribute("prsClient")).destroy();
//                }
//                System.out.println("PolicyRepoServerClient Destroyed");
//            }
//        }

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {

//        ServletRequest req = sre.getServletRequest();
//        if (req instanceof HttpServletRequest) {
//            HttpServletRequest hreq = (HttpServletRequest) req;
//            String url = hreq.getRequestURI();
//            System.out.println("TTTTTTTT");
//            System.out.println(url);
//            if (url.startsWith(hreq.getContextPath() + "/system/setting/PolicyRepoClientCall")) {
//                System.out.println("PolicyRepoClient created");
//                try {
//                    PolicyRepoClient prClient = new PolicyRepoClient(PropertyUtil.getInstance().getProperty("PolicyRepoServer.webserver.url"));
//                    sre.getServletRequest().setAttribute("prClient", prClient);
//                } catch (SQLException ex) {
//                    Logger.getLogger(ReqeustServletListener.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            } else if (url.startsWith(hreq.getContextPath() + "/system/setting/PolicyRepoServerClient")) {
//                System.out.println("PolicyRepoServerClient created");
//                PolicyRepoServerClient client = new PolicyRepoServerClient();
//                sre.getServletRequest().setAttribute("prsClient", client);
//            }
//        }

    }
}
