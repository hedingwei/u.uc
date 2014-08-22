/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.dpi;

import com.ambimmort.uc.dpi.simulator.server.core.UcRawMessage;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import java.util.Arrays;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

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
        System.out.println(session.getRemoteAddress());
        UcRawMessage msg = (UcRawMessage) message;
        System.out.println(msg);
        UcMsg.xCD ack = UcMsg.xCD.getSuccessACK(UcType.newUINT1(msg.getMessageType()), UcType.newUINT2(msg.getMessageNo()), UcType.newUINT4(msg.getMessageSequenceNo()));
        ack.setUcPacketErrorInfo(UcType.newUString_UINT2(""));
        ack.normalize();
        UcRawMessage ackm = new UcRawMessage();
        ackm.setBody(ack.getBodyBytes());
        ackm.setHeader(ack.getHeader().getBytes());
        ackm.normalize();
        session.write(ackm);
        System.out.println("ack sent");
        System.out.println(Arrays.toString(ackm.getBytes()));
        
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        session.close(true);
    }

}
