/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages;

import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import com.ambimmort.ucserver.util.HexDisplay;
//import com.ambimmort.xmlbeanutil.Test;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public abstract class UcMsg {

    protected Header header = new Header();

    public Header getHeader() {
        return header;
    }

    public abstract byte[] getBodyBytes();

    public abstract void parseBytes(byte[] header, byte[] body);

    public abstract byte[] toBytes();

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
        
        public UcType.UINT1 getMessageType(){
            return new UcType.UINT1(new byte[]{headerBytes[4]});
        }
        
        public UcType.UINT2 getMessageNo(){
            return new UcType.UINT2(new byte[]{headerBytes[5],headerBytes[6]});
        }
        
        public UcType.UINT4 getMessageSequenceNo(){
            return new UcType.UINT4(new byte[]{headerBytes[8],headerBytes[9],headerBytes[10],headerBytes[11]});
        }

        public byte[] getBytes() {
            return headerBytes;
        }

    }

    public static class x00 extends com.ambimmort.ucserver.ucmessages.uc.x00 {}

    public static class xCD extends com.ambimmort.ucserver.ucmessages.uc.xCD {}

    public static void main(String[] args) {
        UcMsg.x00 x00 = new UcMsg.x00();

        UcMsg.xCD ack = new UcMsg.xCD();
        try {
            ack.setMessageType(UcType.newUINT1(0x00));
            ack.setMessageNo(UcType.newUINT2(1));
            ack.setMessageSequenceNo(UcType.newUINT4(1));
            ack.setUcPacketErrorStatus(UcType.newUINT1(0x00));
            ack.setUcPacketErrorInfo(UcType.newUString_UINT2("abc"));
            System.out.println(HexDisplay.getHex(ack.toBytes()));

        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
