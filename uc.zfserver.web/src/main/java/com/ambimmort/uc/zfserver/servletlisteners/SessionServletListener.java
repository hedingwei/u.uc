package com.ambimmort.uc.zfserver.servletlisteners;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author 定巍
 */
@WebListener()
public class SessionServletListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
//         System.out.println("Session Created : TTTTTTTTTTTT:");
//        PolicyRepoServerClient client = new PolicyRepoServerClient();
//       System.out.println("Session Created : TTTTTTTTTTTT:"+ client);
//        se.getSession().setAttribute("prsClient", client);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
//        ((PolicyRepoServerClient)se.getSession().getAttribute("prsClient")).destroy();
    }
}
