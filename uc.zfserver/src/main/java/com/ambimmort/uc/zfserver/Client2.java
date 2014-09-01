/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

/**
 *
 * @author 定巍
 */
public class Client2 {
    public static void main(String[] args) throws XMPPException, InterruptedException{
        
        ConnectionConfiguration connectionConfiguration = new ConnectionConfiguration("localhost", 5222);
//        connectionConfiguration.setCompressionEnabled(false);
//        connectionConfiguration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
//        connectionConfiguration.setSASLAuthenticationEnabled(true);
//        connectionConfiguration.setDebuggerEnabled(false);
//        connectionConfiguration.setKeystorePath("src/main/resources/bogus_mina_tls.cert");
//        connectionConfiguration.setTruststorePath("src/main/resources/bogus_mina_tls.cert");
//        connectionConfiguration.setTruststorePassword("boguspw");

        XMPPConnection.DEBUG_ENABLED = true;
        XMPPConnection client = new XMPPConnection(connectionConfiguration);

        client.connect();
        
        client.login("user2@ambimmort.com", "123");
        System.out.println("isConnected="+client.isConnected()+"\t"+client.isAuthenticated());
        client.getRoster().createEntry("user1@ambimmort.com", "user1", new String[]{"default"});
        
        client.getChatManager().addChatListener(new ChatManagerListener() {

            @Override
            public void chatCreated(Chat chat, boolean bln) {
          
                try {
                    for(int i=0;;i++){
                        chat.sendMessage("we chat");
                        Thread.sleep(1000);
                    }
                    
                } catch (XMPPException ex) {
                    Logger.getLogger(Client2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        client.getChatManager().createChat("user1@ambimmort.com", new MessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message msg) {
                        System.out.println(chat.getParticipant()+"\t"+msg);
                    }
                });
        
        
        
        
        while(true){
            Thread.sleep(1000);
        }
    }
}
