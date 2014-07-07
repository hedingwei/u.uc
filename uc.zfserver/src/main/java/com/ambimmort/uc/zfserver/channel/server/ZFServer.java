/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.channel.server;

import com.ambimmort.uc.zfserver.codec.UcProtocolCodecFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.firewall.ConnectionThrottleFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author 定巍
 */
public class ZFServer {

    private IoAcceptor acceptor = null;
    private WhitelistFilter whiteListFilter = new WhitelistFilter();

    public ZFServer() {
        acceptor = new NioSocketAcceptor();
        InetAddress[] address = new InetAddress[1];
        try {
            address[0] = InetAddress.getByName("localhost");
        } catch (UnknownHostException ex) {
            Logger.getLogger(ZFServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        whiteListFilter.setWhitelist(address);
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("whiteList", whiteListFilter);
        acceptor.getFilterChain().addLast("ConnectionThrottleFilter", new ConnectionThrottleFilter(1000));
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new UcProtocolCodecFactory()));

        acceptor.setHandler(new ServerChannelHandler());
    }

    public void bind() {
        try {
            acceptor.bind(new InetSocketAddress(50000));
        } catch (IOException ex) {
            Logger.getLogger(ZFServer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("%%%%%%%%%%");
        }
    }

    public void unbind() {
        acceptor.unbind();
    }

    public static void main(String[] args) {
        ZFServer server = new ZFServer();
        server.bind();
//        new LocalUcMessageHandler();
    }
}
