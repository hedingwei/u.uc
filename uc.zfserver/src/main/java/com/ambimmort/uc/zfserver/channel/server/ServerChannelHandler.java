/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.zfserver.channel.server;

import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPoint;
import com.ambimmort.uc.zfserver.component.zfserver.DPIEndPointManager;
import com.ambimmort.uc.zfserver.type.ConnectionState;
import com.ambimmort.uc.zfserver.codec.UcRawMessage;
import com.ambimmort.uc.zfserver.component.messageDriven.MDEComponent;
import com.ambimmort.ucserver.ucmessages.UcType;
import com.ambimmort.ucserver.util.HexDisplay;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class ServerChannelHandler extends IoHandlerAdapter {

    private DPIEndPoint endPoint;

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
        System.out.println("server got: " + addr.getAddress().getHostAddress());

        if (DPIEndPointManager.getInstance().getEndPoints().containsKey(addr.getAddress().getHostAddress())) {
            DPIEndPoint ep = null;
            System.out.println("server got: " + addr.getAddress().getHostAddress());
            if (addr.getAddress().getHostAddress().equals("127.0.0.1")) {
                ep = DPIEndPointManager.getInstance().getEndPoints().get("127.0.0.1");
            } else {
                ep = DPIEndPointManager.getInstance().getEndPoints().get(addr.getAddress().getHostAddress());
            }

            System.out.println("Address:" + addr.getAddress().getHostAddress());
            endPoint = ep;
            ep.getServerChannel().setState(ConnectionState.Connected);

            ep.persistence();
        } else {
            System.out.println("server has no configuration: " + addr.getAddress().getHostAddress());
            session.close(true);
        }
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        UcRawMessage rmsg = (UcRawMessage) message;
        if (rmsg.getMessageType() == (byte) 0xc1) {
            Map<String, Object> args = new HashMap<String, Object>();
            byte[] data = rmsg.getBody();
            UcType.UString_UINT1 name = new UcType.UString_UINT1(data);
            args.put("name", name.toString());
            args.put("time", System.currentTimeMillis());
            args.put("endPoint", endPoint);
            args.put("data", rmsg.getBytes());
            MDEComponent.getInstance().fire("u.0xc1", args);

        } else if (rmsg.getMessageType() == (byte) 0xc2) {

            byte[] data = rmsg.getBody();
            System.err.println(HexDisplay.getHex(rmsg.getHeader()));
            System.err.println("===========================");
            System.err.println(HexDisplay.getHex(data));
            UcType.UINT1 mtNum = new UcType.UINT1(data,0);
            int ptr = 1;
            UcType.UINT1 mt = null;
            UcType.UINT4 sn = null;
            Map<String, Object> args = new HashMap<String, Object>();
            JSONObject rst = new JSONObject();
            String tp = null;
            System.out.println("mtNum:"+mtNum.toInteger());
            for (int i = 0; i < mtNum.toInteger(); i++) {
//                System.out.println("ci:"+i+"\t"+mt.toByte());
                System.out.println("ci:"+i+"\tptr:"+ptr);
                mt = new UcType.UINT1(data, ptr);
                ptr++;
                System.out.println("ci:"+i+"\tptr:"+ptr);
                sn = new UcType.UINT4(data, ptr);
                ptr += 4;
                args = new HashMap<String, Object>();
                tp = Integer.toHexString(mt.toInteger());
                if (tp.length() == 1) {
                    tp = "0" + tp;
                }
                rst.put("0x" + tp, sn.toLong());

            }
            args.put("v", rst);
            args.put("endPoint", endPoint);
            MDEComponent.getInstance().fire("u.0xc2", args);
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        InetSocketAddress addr = (InetSocketAddress) session.getRemoteAddress();
        if (DPIEndPointManager.getInstance().getEndPoints().containsKey(addr.getAddress().getHostAddress())) {
            DPIEndPoint ep = DPIEndPointManager.getInstance().getEndPoints().get(addr.getAddress().getHostAddress());
            ep.getServerChannel().setState(ConnectionState.DisConnected);
            ep.persistence();
        }
    }

}
