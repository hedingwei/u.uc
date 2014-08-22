/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.dpi.simulator.server;

import com.ambimmort.uc.dpi.simulator.server.core.LocalUcMessageHandler;
import com.ambimmort.uc.dpi.simulator.server.core.UcProtocolCodecFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author 定巍
 */
public class UcServer {

    private IoAcceptor acceptor = null;

    public UcServer() {
        acceptor = new NioSocketAcceptor();
      
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new UcProtocolCodecFactory()));
        acceptor.setHandler(new LocalUcMessageHandler());
    }

    public void bind() {
        try {
            acceptor.bind(new InetSocketAddress(50000));
        } catch (IOException ex) {
            Logger.getLogger(UcServer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("%%%%%%%%%%");
        }
    }

    public void unbind() {
        acceptor.unbind();
    }

    public static void main(String[] args) {
        UcServer server = new UcServer();
        server.bind();
    }
}
