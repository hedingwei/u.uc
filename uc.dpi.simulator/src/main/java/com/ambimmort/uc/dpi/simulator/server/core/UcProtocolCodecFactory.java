/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.dpi.simulator.server.core;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 *
 * @author 定巍
 */
public class UcProtocolCodecFactory implements ProtocolCodecFactory {

    ProtocolDecoder decoder = null;
    ProtocolEncoder encoder = null;

    public UcProtocolCodecFactory() {
        decoder = new UcMessageDecoder();
        encoder = new UcMessageEncoder();
    }

    public ProtocolEncoder getEncoder(IoSession is) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession is) throws Exception {
        return decoder;
    }

}
