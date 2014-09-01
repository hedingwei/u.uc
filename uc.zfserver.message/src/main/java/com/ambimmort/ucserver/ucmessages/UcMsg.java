/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages;

import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import com.ambimmort.ucserver.ucmessages.uc.x45;
import com.ambimmort.ucserver.util.HexDisplay;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
//import com.ambimmort.xmlbeanutil.Test;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public abstract class UcMsg {

    protected Header header = new Header();

    public abstract boolean isVersionManaged();

    public abstract void setMessageSerialNo(UcType.UINT4 messageSerialNo);

    public abstract UcType.UINT4 getMessageSerialNo();

    public Header getHeader() {
        return header;
    }

    public abstract byte[] getBodyBytes();

    public abstract void parseBytes(byte[] header, byte[] body);

    public byte[] toBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            byte[] bodys = getBodyBytes();
            getHeader().setMessageLength(UcType.newUINT4(bodys.length + 16));
            os.write(getHeader().getBytes());
            os.write(bodys);
        } catch (IOException ex) {
            Logger.getLogger(x45.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(x45.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
    }

    public abstract void parseXML(String xmlForm);

    public abstract String toXML();

    public void normalize() {
        byte[] bodys = getBodyBytes();
        try {
            getHeader().setMessageLength(UcType.newUINT4(bodys.length + 16));
        } catch (Exception e) {

        }
    }

    public static class Header {

        public byte[] headerBytes = new byte[16];

        public Header() {
            headerBytes[0] = 16;
            headerBytes[1] = 'C';
            headerBytes[2] = 'U';
            headerBytes[3] = 'C';
            headerBytes[7] = 0;
        }

        public Header(UcType.UINT1 messageType, UcType.UINT2 messageNo, UcType.UINT4 messageSequenceNo, UcType.UINT4 messageLength) {
            headerBytes[0] = 16;
            headerBytes[1] = 'C';
            headerBytes[2] = 'U';
            headerBytes[3] = 'C';

            headerBytes[4] = messageType.toByte();
            headerBytes[5] = messageNo.getBytes()[0];
            headerBytes[6] = messageNo.getBytes()[1];
            headerBytes[7] = 0;

            headerBytes[8] = messageSequenceNo.getBytes()[0];
            headerBytes[9] = messageSequenceNo.getBytes()[1];
            headerBytes[10] = messageSequenceNo.getBytes()[2];
            headerBytes[11] = messageSequenceNo.getBytes()[3];

            headerBytes[12] = messageLength.getBytes()[0];
            headerBytes[13] = messageLength.getBytes()[1];
            headerBytes[14] = messageLength.getBytes()[2];
            headerBytes[15] = messageLength.getBytes()[3];
        }

        public void setMessageLength(UcType.UINT4 messageLength) {
            headerBytes[12] = messageLength.getBytes()[0];
            headerBytes[13] = messageLength.getBytes()[1];
            headerBytes[14] = messageLength.getBytes()[2];
            headerBytes[15] = messageLength.getBytes()[3];
        }

        public void setMessageSequenceNo(UcType.UINT4 messageSequenceNo) {
            headerBytes[8] = messageSequenceNo.getBytes()[0];
            headerBytes[9] = messageSequenceNo.getBytes()[1];
            headerBytes[10] = messageSequenceNo.getBytes()[2];
            headerBytes[11] = messageSequenceNo.getBytes()[3];
        }

        public void setMessageNo(UcType.UINT2 messageNo) {
            headerBytes[5] = messageNo.getBytes()[0];
            headerBytes[6] = messageNo.getBytes()[1];
        }

        public void setMessageType(UcType.UINT1 messageType) {
            headerBytes[4] = messageType.toByte();
        }

        public void setProtoSignature(byte[] protoSignature) {
            headerBytes[1] = protoSignature[0];
            headerBytes[2] = protoSignature[1];
            headerBytes[3] = protoSignature[2];
        }

        public UcType.UINT1 getMessageType() {
            return new UcType.UINT1(new byte[]{headerBytes[4]});
        }

        public UcType.UINT2 getMessageNo() {
            return new UcType.UINT2(new byte[]{headerBytes[5], headerBytes[6]});
        }

        public UcType.UINT4 getMessageSequenceNo() {
            return new UcType.UINT4(new byte[]{headerBytes[8], headerBytes[9], headerBytes[10], headerBytes[11]});
        }

        public byte[] getBytes() {
            return headerBytes;
        }

    }

    public static class x00 extends com.ambimmort.ucserver.ucmessages.uc.x00 {
    }

    public static class x01 extends com.ambimmort.ucserver.ucmessages.uc.x01 {
    }

    public static class x85 extends com.ambimmort.ucserver.ucmessages.uc.x85 {
    }

    public static class xCD extends com.ambimmort.ucserver.ucmessages.uc.xCD {
    }

    public static xCD buildAck_Ok(int messageType, int messageNo, long messageSequenceNo) {
        UcMsg.xCD ack = new UcMsg.xCD();
        try {
            ack.setMessageType(UcType.newUINT1(messageType));
            ack.setMessageNo(UcType.newUINT2(messageNo));
            ack.setMessageSequenceNo(UcType.newUINT4(messageSequenceNo));
            ack.setUcPacketErrorStatus(UcType.newUINT1(0x00));
            ack.setUcPacketErrorInfo(UcType.newUString_UINT2(""));
            System.out.println(HexDisplay.getHex(ack.toBytes()));
            return ack;
        } catch (Exception ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static UcMsg buildUcMsg(String type, String xmlForm) {
        UcMsg msg = null;
        String className = "com.ambimmort.ucserver.ucmessages.uc." + type.toUpperCase().replace("0X", "x");
        try {
            msg = (UcMsg) Class.forName(className).newInstance();
            msg.parseXML(xmlForm);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        }

        return msg;
    }

    public static void main(String[] args) {

        UcMsg.buildAck_Ok(1, 1, 1);

//        UcMsg.x00 x00 = new UcMsg.x00();
//
//        UcMsg.xCD ack = new UcMsg.xCD();
//        try {
//            ack.setMessageType(UcType.newUINT1(0x00));
//            ack.setMessageNo(UcType.newUINT2(1));
//            ack.setMessageSequenceNo(UcType.newUINT4(1));
//            ack.setUcPacketErrorStatus(UcType.newUINT1(0x00));
//            ack.setUcPacketErrorInfo(UcType.newUString_UINT2("abc"));
//            System.out.println(HexDisplay.getHex(ack.toBytes()));
//
//        } catch (UcTypeException ex) {
//            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
