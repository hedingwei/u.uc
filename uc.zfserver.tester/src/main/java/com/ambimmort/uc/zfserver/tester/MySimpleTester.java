/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.tester;

import com.ambimmort.uc.zfserver.channel.client.ClientConnection;
import com.ambimmort.uc.zfserver.codec.UcRawMessage;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.util.HexDisplay;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class MySimpleTester {

    private ClientConnection cc = null;
    private List<String> queue = new ArrayList<String>();
    private Map<String,String> index = new HashMap<String, String>();
    private String host;
    private int port;

    public MySimpleTester(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void add(String messageType, String messagePath) {
        queue.add(messagePath);
        index.put(messagePath, messageType);
    }

    public void start() {
        if (cc == null) {
            cc = new ClientConnection(this.host, this.port, new IoHandlerAdapter() {
                @Override
                public void sessionOpened(IoSession session) throws Exception {
                    UcRawMessage msg = new UcRawMessage();
                    for (String path : queue) {
                        UcMsg m = UcMsg.buildUcMsg(MySimpleTester.this.index.get(path), FileUtils.readFileToString(new File(path), "utf-8"));
                        System.out.println("=["+MySimpleTester.this.index.get(path)+"]={"+path+"}=========");
                        HexDisplay.print(m.toBytes());
                        msg.setHeader(m.getHeader().getBytes());
                        msg.setBody(m.getBodyBytes());
                        session.write(msg);
                    }

                }

                @Override
                public void sessionClosed(IoSession session) throws Exception {

                }

                @Override
                public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
                    cause.printStackTrace();
                }

                @Override
                public void messageReceived(IoSession session, Object message) throws Exception {

                    UcRawMessage rmsg = (UcRawMessage) message;
                    UcMsg.xCD ack = new UcMsg.xCD();
                    ack.parse(rmsg.getHeader(), rmsg.getBody());
                    System.out.println(ack.toString());
                }
            });
            cc.start();
        }
    }

}
