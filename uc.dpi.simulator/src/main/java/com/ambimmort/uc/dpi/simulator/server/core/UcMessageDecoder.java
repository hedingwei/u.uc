/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.dpi.simulator.server.core;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 *
 * @author 定巍
 */
public class UcMessageDecoder extends CumulativeProtocolDecoder {

    @Override
    protected boolean doDecode(IoSession is, IoBuffer ib, ProtocolDecoderOutput pdo) throws Exception {
        UcRawMessage msg = null;
        if (is.containsAttribute("message")) {
            msg = (UcRawMessage) is.getAttribute("message");
            if (ib.remaining() >= msg.getLengthNeeded()) {
                msg.setBody(new byte[(int) msg.getLengthNeeded()]);
                ib.get(msg.getBody());
                pdo.write(msg);
                is.setAttribute("message", null);
                if (!ib.hasRemaining()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            while (ib.remaining() >= 16) {
                msg = new UcRawMessage();
                is.setAttribute("message", msg);
                ib.get(msg.getHeader());
                if (ib.remaining() >= msg.getLengthNeeded()) {
                    msg.setBody(new byte[(int) msg.getLengthNeeded()]);
                    ib.get(msg.getBody());
                    pdo.write(msg);
                    is.setAttribute("message", null);
                    if (!ib.hasRemaining()) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
            {
                return false;
            }
        }

    }

}
