package com.ambimmort.uc.zfserver.component.vysper;

import com.ambimmort.uc.zfserver.bean.ZFAgentBean;
import com.ambimmort.uc.zfserver.component.database.MyDaoManager;
import com.j256.ormlite.dao.Dao;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

public class Commander implements MessageListener, ConnectionListener, RosterListener{

    private static Commander instance = null;

    private XMPPConnection connection;

    private Commander() {
        ConnectionConfiguration config = new ConnectionConfiguration("localhost", 5222);
        config.setCompressionEnabled(false);
        SASLAuthentication.supportSASLMechanism("PLAIN", 0);
//        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
//        config.setSASLAuthenticationEnabled(true);
//        config.setDebuggerEnabled(false);
//        config.setKeystorePath("src/main/resources/bogus_mina_tls.cert");
//        config.setTruststorePath("src/main/resources/bogus_mina_tls.cert");
//        config.setTruststorePassword("boguspw");
        connection = new XMPPConnection(config);
        
    }

    public static Commander getInstance() {
        if (instance == null) {
            instance = new Commander();
        }
        return instance;
    }

    public void login() {
        
        try {
            ZFAgentBean bean = ((Dao<ZFAgentBean,String>)MyDaoManager.getInstance().getDao(ZFAgentBean.class)).queryForId("commander@ambimmort.com");
            connection.connect();
            connection.login(bean.getName(), "123");
            connection.getRoster().addRosterListener(this);
            connection.getChatManager().addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean bln) {
                    chat.addMessageListener(Commander.this);
                }
            });
        } catch (XMPPException ex) {
            Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sendMessage(String message, String to) throws XMPPException {
        Chat chat = connection.getChatManager().createChat(to, this);
        chat.sendMessage(message);
    }

    public void displayBuddyList() {
        Roster roster = connection.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();

        System.out.println("\n\n" + entries.size() + " buddy(ies):");
        for (RosterEntry r : entries) {
            System.out.println(r.getUser());
        }
    }

    public void disconnect() {
        connection.disconnect();
    }

    public void processMessage(Chat chat, Message message) {
        System.out.println(chat.getParticipant() + " says: " + message.getBody());
        if (message.getType() == Message.Type.chat) {
            if("_showRoster".equals(message.getBody())){
                displayBuddyList();
            }else {
                System.out.println(chat.getParticipant() + " says: " + message.getBody());
                try {
                    chat.sendMessage("my replay");
                } catch (XMPPException ex) {
                    Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    

    @Override
    public void connectionClosed() {
        try {
            Thread.sleep(1000);
            login();
        } catch (InterruptedException ex) {
            Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void connectionClosedOnError(Exception excptn) {
        try {
            Thread.sleep(1000);
            login();
        } catch (InterruptedException ex) {
            Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void reconnectingIn(int i) {
        
    }

    @Override
    public void reconnectionSuccessful() {
        
    }

    @Override
    public void reconnectionFailed(Exception excptn) {
        try {
            Thread.sleep(1000);
            login();
        } catch (InterruptedException ex) {
            Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void entriesAdded(Collection<String> clctn) {
        
    }

    @Override
    public void entriesUpdated(Collection<String> clctn) {
        
    }

    @Override
    public void entriesDeleted(Collection<String> clctn) {
        
    }

    @Override
    public void presenceChanged(Presence prsnc) {
        System.out.println(prsnc.getFrom()+"\t"+prsnc.getStatus());
        
    }

}
