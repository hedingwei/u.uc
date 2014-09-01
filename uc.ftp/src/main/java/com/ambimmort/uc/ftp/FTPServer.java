/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.ftp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

/**
 *
 * @author 定巍
 */
public class FTPServer {

    FtpServerFactory serverFactory = new FtpServerFactory();
    ListenerFactory factory = new ListenerFactory();
    PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
    FtpServer server = null;

    public FTPServer(String config) {
        JSONObject conf = JSONObject.fromObject(config);
        int port = conf.getInt("port");
        factory.setPort(port);
        
        serverFactory.addListener("default", factory.createListener());
        userManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
        UserManager um = userManagerFactory.createUserManager();
        JSONArray users = conf.getJSONArray("users");
        JSONObject user = null;
        for (int i = 0; i < users.size(); i++) {
            user = users.getJSONObject(i);
            BaseUser bu = new BaseUser();
            bu.setName(user.getString("name"));
            bu.setPassword(user.getString("password"));
            bu.setHomeDirectory("C:\\program1");
            
            List<Authority> aus = new ArrayList<Authority>();
            aus.add(new WritePermission("test"));
            aus.add(new TransferRatePermission(65535, 65535));
            bu.setAuthorities(aus);
            bu.setEnabled(true);
            try {
                um.save(bu);
               
            } catch (FtpException ex) {
                Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        serverFactory.setUserManager(um);
    }

    public void start() {
        try {
            server = serverFactory.createServer();
            server.start();
        } catch (FtpException ex) {
            Logger.getLogger(FTPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        if (server != null && !server.isStopped()) {
            server.stop();
        }
    }

    public static void main(String[] args) {
        FTPServer server = new FTPServer("{\"port\":2100,\"users\":[{\"name\":\"admin\",\"password\":\"123\",\"home\":\"C:\\Users\\定巍\\Downloads\\ftptest\"}]}");
        server.start();
    }

}
