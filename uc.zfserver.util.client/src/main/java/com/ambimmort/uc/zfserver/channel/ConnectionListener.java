/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.uc.zfserver.channel;

import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public interface ConnectionListener {
    public void onConnected(IoSession session);
    public void onConnecting(IoSession session);
    public void onDisConnected(IoSession session);
}
