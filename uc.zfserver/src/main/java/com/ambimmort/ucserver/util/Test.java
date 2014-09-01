/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.util;

import com.ambimmort.msg0X00.Msg0X00Document;
import com.ambimmort.msg0X01.Msg0X01Document;
import com.ambimmort.uc.zfserver.channel.client.ClientConnection;
import com.ambimmort.uc.zfserver.codec.UcRawMessage;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author 定巍
 */
public class Test {

    public static void main(String[] args) throws InterruptedException, IOException {
        ClientConnection cc = new ClientConnection("111.228.250.11", 50000, new IoHandlerAdapter() {

            @Override
            public void sessionOpened(IoSession session) throws Exception {
                System.out.println("session opened:"+session);
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

            @Override
            public void messageSent(IoSession session, Object message) throws Exception {
                System.out.println("msg sent");
            }
            
            

        });
        cc.start();
        try {
            
            Msg0X01Document document = Msg0X01Document.Factory.parse(new File("C:\\Users\\定巍\\Documents\\GitHub\\u.uc\\uc.zfserver.message\\src\\main\\resources\\xml\\msg_0x01.xml"));
            UcMsg msg = UcMsg.buildUcMsg("0x01", document.toString());
            
//            System.out.println(HexDisplay.getHex(msg.toBytes()));
//                        Thread.sleep(5000);
            UcRawMessage send = new UcRawMessage();
            msg.normalize();
            send.setHeader(msg.getHeader().headerBytes);
            
            send.setBody(msg.getBodyBytes());
            
            System.out.println(HexDisplay.getHex(send.getBytes()));

            cc.send(send);
            System.out.println("done");

        } catch (XmlException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
