/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.channel.client;

import com.ambimmort.uc.zfserver.channel.client.ClientChannelReconnectHandler;
import com.ambimmort.uc.zfserver.channel.ConnectionListener;
import com.ambimmort.uc.zfserver.bean.ConnectionState;
import com.ambimmort.uc.zfserver.codec.UcProtocolCodecFactory;
import com.ambimmort.uc.zfserver.codec.UcRawMessage;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 *
 * @author 定巍
 */
public class ClientConnection extends Thread {

    public ConnectionState connetionState;
    private IoSession session = null;
    private IoConnector connector = null;
    private EndPoint endPoint = null;
    private List<ConnectionListener> connectionListeners = new ArrayList<ConnectionListener>();

    public void notifyReconnecting(IoSession session) {
        for (ConnectionListener l : connectionListeners) {
            l.onConnecting(session);
        }
    }

    public void notifyDisconnected(IoSession session) {
        for (ConnectionListener l : connectionListeners) {
            l.onDisConnected(session);
        }
    }

    public void notifyConnected(IoSession session) {
        for (ConnectionListener l : connectionListeners) {
            l.onConnected(session);
        }
    }

    public void addConnectionListener(ConnectionListener listener) {
        connectionListeners.add(listener);
    }

    public ConnectionState getConnetionState() {
        return connetionState;
    }

    public void setConnetionState(ConnectionState connetionState) {
        this.connetionState = connetionState;
    }

    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public IoConnector getConnector() {
        return connector;
    }

    public void setConnector(IoConnector connector) {
        this.connector = connector;
    }

    public EndPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(EndPoint endPoint) {
        this.endPoint = endPoint;
    }

    public ClientConnection(String host, int port, IoHandler handler) {
        connetionState = ConnectionState.DisConnected;
        this.endPoint = new EndPoint();
        this.endPoint.setHost(host);
        this.endPoint.setPort(port);
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(1000);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new UcProtocolCodecFactory()));
        connector.setHandler(new ClientChannelReconnectHandler(this, handler));
    }

    @Override
    public void run() {
        while (true) {
            if (connetionState == ConnectionState.DisConnected) {
                connect();
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public synchronized void connect() {
        connetionState = ConnectionState.Connecting;
        notifyReconnecting(null);
        ConnectFuture future = connector.connect(new InetSocketAddress(this.endPoint.getHost(), this.endPoint.getPort()));

        boolean b = future.awaitUninterruptibly(3, TimeUnit.SECONDS);

        if (b && !future.isConnected()) {
            needReconnect();
        }
    }

    public void needReconnect() {
        connetionState = ConnectionState.DisConnected;
        notifyDisconnected(session);
        session = null;
    }

    public synchronized boolean send(Object obj) {
        if (session != null) {
            if (session.isConnected()) {
                session.write(obj);
                return true;
            }
        }
        return false;
    }

    public static class DPIHandler extends IoHandlerAdapter {

        Logger logger = Logger.getLogger(ClientConnection.class.getName());

        ClientConnection connection = null;

        public DPIHandler(ClientConnection connection) {
            this.connection = connection;
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            if (message instanceof UcRawMessage) {
                UcRawMessage msg = (UcRawMessage) message;
                if (msg.getMessageType() == (byte) 0xcd) {  // this is ack message
                    UcMsg.xCD ack = new UcMsg.xCD();
                    ack.parse(msg.getHeader(), msg.getBody());
                    logger.log(Level.SEVERE, ack.toString());
                }
            }
        }

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            cause.printStackTrace();
            connection.needReconnect();
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            connection.needReconnect();
        }

        @Override
        public void sessionOpened(IoSession session) throws Exception {
            connection.connetionState = ConnectionState.Connected;
            connection.session = session;

            System.out.println("session [" + session.getId() + "][" + session.isConnected() + "] opened local:" + session.getLocalAddress() + " remote:" + session.getRemoteAddress());
            System.out.println("**********----***********");
        }

    }

    @Override
    public String toString() {
        return "ConnectionPoint{" + "endPoint=" + endPoint + "," + session + '}';
    }

    public static class EndPoint {

        private String host;
        private int port;

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + (this.host != null ? this.host.hashCode() : 0);
            hash = 67 * hash + this.port;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final EndPoint other = (EndPoint) obj;
            if ((this.host == null) ? (other.host != null) : !this.host.equals(other.host)) {
                return false;
            }
            if (this.port != other.port) {
                return false;
            }
            return true;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        @Override
        public String toString() {
            return "EndPoint{" + "host=" + host + ", port=" + port + '}';
        }

    }

}
