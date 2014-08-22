/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dpi;

import com.ambimmort.uc.dpi.simulator.server.core.UcProtocolCodecFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author 定巍
 */
public class UcServerDPI {

    private IoAcceptor acceptor = null;

    public UcServerDPI() {

        acceptor = new NioSocketAcceptor();
        if (true) {
            acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        }
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new UcProtocolCodecFactory()));
        acceptor.setHandler(new DPIUcMessageHandler());
        acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 60);
    }

    public void bind() {
        try {
            acceptor.bind(new InetSocketAddress(50000));
        } catch (IOException ex) {
            Logger.getLogger(UcServerDPI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("%%%%%%%%%%");
        }
    }

    public void unbind() {
        acceptor.unbind();
    }

    public static void main(String[] args) {
        UcServerDPI server = new UcServerDPI();
        server.bind();
    }
}
