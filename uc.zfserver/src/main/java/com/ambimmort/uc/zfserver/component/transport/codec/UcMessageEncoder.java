/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.component.transport.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 *
 * @author 定巍
 */
public class UcMessageEncoder extends ProtocolEncoderAdapter{

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        if(message instanceof UcRawMessage){
            IoBuffer buffer = IoBuffer.wrap(((UcRawMessage)message).getByteBuffer());
            out.write(buffer);
            out.flush();
        }
    }
    
}
