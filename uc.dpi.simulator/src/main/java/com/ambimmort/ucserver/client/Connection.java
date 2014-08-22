/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.client;

import com.ambimmort.uc.dpi.simulator.server.core.UcProtocolCodecFactory;
import com.ambimmort.uc.dpi.simulator.server.core.UcRawMessage;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 *
 * @author 定巍
 */
public class Connection extends Thread {

    private ConnetionState connetionState;
    private IoSession session = null;
    private IoConnector connector = null;
    private EndPoint endPoint = null;

    public ConnetionState getConnetionState() {
        return connetionState;
    }

    public void setConnetionState(ConnetionState connetionState) {
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

    public Connection(String host, int port) {
        connetionState = ConnetionState.DisConnected;
        this.endPoint = new EndPoint();
        this.endPoint.setHost(host);
        this.endPoint.setPort(port);
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(1000);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new UcProtocolCodecFactory()));
        connector.setHandler(new DPIHandler(this));
    }

    @Override
    public void run() {
        while (true) {
            if (connetionState == ConnetionState.DisConnected) {
                connect();
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public synchronized void connect() {
        connetionState = ConnetionState.Connecting;
        ConnectFuture future = connector.connect(new InetSocketAddress(this.endPoint.getHost(), this.endPoint.getPort()));

        boolean b = future.awaitUninterruptibly(3, TimeUnit.SECONDS);

        if (b && !future.isConnected()) {
            needReconnect();
        }
    }

    public void needReconnect() {
        connetionState = ConnetionState.DisConnected;
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

        Connection connection = null;

        public DPIHandler(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
            if (message instanceof UcRawMessage) {
                UcRawMessage msg = (UcRawMessage) message;
                if (msg.getMessageType() == (byte) 0xcd) {  // this is ack message
                    UcMsg.xCD ack = new UcMsg.xCD();
                    ack.parse(msg.getHeader(), msg.getBody());
                    System.out.println(ack.toString());
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
            connection.connetionState = ConnetionState.Connected;
            connection.session = session;
            System.out.println("session [" + session.getId() + "][" + session.isConnected() + "] opened local:" + session.getLocalAddress() + " remote:" + session.getRemoteAddress());
            System.out.println("**********----***********");
        }

    }

    public static enum ConnetionState {

        Connecting, Connected, DisConnected
    }

    public static void main(String[] args) {
        new Connection("localhost", 50000).start();
    }

    @Override
    public String toString() {
        return "ConnectionPoint{" + "endPoint=" + endPoint + "," + session + '}';
    }

}
