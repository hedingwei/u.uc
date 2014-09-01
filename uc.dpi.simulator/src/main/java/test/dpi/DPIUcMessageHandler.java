/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dpi;

import com.ambimmort.uc.dpi.simulator.server.core.UcRawMessage;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import test.HexDisplay;

/**
 *
 * @author 定巍
 */
public class DPIUcMessageHandler extends IoHandlerAdapter {

    @Override
    public void sessionCreated(IoSession session) throws Exception {

    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        boolean flag = true;
        System.out.println(session.getRemoteAddress());
        UcRawMessage msg = (UcRawMessage) message;
        System.out.println(msg);
        byte[] MessageSerialNo = new byte[4];
        byte MessageType = 0;
        if(UcType.newUINT1(msg.getMessageType()).toInteger()>=0x00&&UcType.newUINT1(msg.getMessageType()).toInteger()<=0x0a){
            
            MessageSerialNo[0] = msg.getBytes()[msg.getBytes().length-4];
            MessageSerialNo[1] = msg.getBytes()[msg.getBytes().length-3];
            MessageSerialNo[2] = msg.getBytes()[msg.getBytes().length-2];
            MessageSerialNo[3] = msg.getBytes()[msg.getBytes().length-1];
            MessageType = msg.getMessageType();
        }else if(msg.getMessageType()==(byte)0xc3){
            MessageSerialNo[0] = msg.getBody()[1];
            MessageSerialNo[1] = msg.getBody()[2];
            MessageSerialNo[2] = msg.getBody()[3];
            MessageSerialNo[3] = msg.getBody()[4];
            MessageType = msg.getBody()[0];
        }else{
            flag = false;
        }
        if(flag){
            String dir = "c:/program1/UCPolicy/"+UcType.newUINT1(MessageType).toString();
            File path  = new File(dir);
            if(!path.exists())path.mkdirs();
            File file = new File(dir+"/"+new UcType.UINT4(MessageSerialNo).toString());
            FileWriter fw = new FileWriter(file);
            fw.write(HexDisplay.getHex(msg.getBytes()));
            fw.close();
        }
        UcMsg.xCD ack = UcMsg.xCD.getSuccessACK(UcType.newUINT1(msg.getMessageType()), UcType.newUINT2(msg.getMessageNo()), UcType.newUINT4(msg.getMessageSequenceNo()));
        ack.setUcPacketErrorInfo(UcType.newUString_UINT2(""));
        ack.normalize();
        UcRawMessage ackm = new UcRawMessage();
        ackm.setBody(ack.getBodyBytes());
        ackm.setHeader(ack.getHeader().getBytes());
        ackm.normalize();
        session.write(ackm);
        System.out.println("ack sent");
//        System.out.println(Arrays.toString(ackm.getBytes()));
        
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        session.close(true);
    }

}
