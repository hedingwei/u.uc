/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages;

import com.ambimmort.msg0X00.Msg0X00Document;
import com.ambimmort.msg0X00.Msg0X00Document.Msg0X00.CookieHosts.CookieHost;
import com.ambimmort.msg0X00.Msg0X00Document.Msg0X00.SearchEngines.SearchEngine;
import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import com.ambimmort.xmlbeanutil.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;

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

    public abstract String toXML(String xmlForm);

    public void normalize() {
        byte[] bodys = getBodyBytes();
        try {
            getHeader().setMessageLength(UcType.newUINT4(bodys.length + 16));
        } catch (Exception e) {

        }
    }

    public static class Header {

        private byte[] headerBytes = new byte[16];

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

        public byte[] getBytes() {
            return headerBytes;
        }

    }

    public static class x00 extends UcMsg {

        private UcType.UINT2 Web_Hit_Threshold;
        private UcType.UINT2 KW_Threholds;
        private UcType.UArray_UINT1 SearchEngines;
        private UcType.UArray_UINT1 CookieHosts;
        private UcType.UString_UINT4 serialNo;

        public void setSerialNo(UcType.UString_UINT4 serialNo) {
            this.serialNo = serialNo;
        }

        public UcType.UString_UINT4 getSerialNo() {
            return serialNo;
        }

        @Override
        public void parseBytes(byte[] header, byte[] body) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public byte[] toBytes() {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                byte[] bodys = getBodyBytes();
                getHeader().setMessageLength(UcType.newUINT4(bodys.length + 16));
                
                os.write(getHeader().getBytes());
                os.write(bodys);
            } catch (IOException ex) {
                Logger.getLogger(x00.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UcTypeException ex) {
                Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(x00.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return os.toByteArray();
        }

        @Override
        public void parseXML(String xmlForm) {
            try {
              
                Msg0X00Document document = Msg0X00Document.Factory.parse(xmlForm);
                Msg0X00Document.Msg0X00 msg = document.getMsg0X00();
                Web_Hit_Threshold = UcType.UINT2.newUINT2(msg.getWebHitThreshold());
                KW_Threholds = UcType.UINT2.newUINT2(msg.getKWThreholds());
                SearchEngines = UcType.newUArray_UINT1();
                for (SearchEngine se : msg.getSearchEngines().getSearchEngineArray()) {
                    SearchEngines.push(UcType.newUString_UINT1(se.getSEName()));
                }
                CookieHosts = UcType.newUArray_UINT1();
                for (CookieHost host : msg.getCookieHosts().getCookieHostArray()) {
                    CookieHosts.push(UcType.newUString_UINT1(host.getName()));
                    CookieHosts.push(UcType.newUString_UINT1(host.getKey()));
                }

            } catch (XmlException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UcTypeException ex) {
                Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public String toXML(String xmlForm) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public byte[] getBodyBytes() {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                os.write(Web_Hit_Threshold.getBytes());
                os.write(KW_Threholds.getBytes());
                os.write(SearchEngines.getBytes());
                os.write(CookieHosts.getBytes());
                os.write(serialNo.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(x00.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(x00.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return os.toByteArray();
        }

    }

    public static class xCD extends UcMsg {

        private UcType.UINT1 MessageType;
        private UcType.UINT2 MessageNo;
        private UcType.UINT4 MessageSequenceNo;
        private UcType.UINT1 UcPacketErrorStatus;
        private UcType.UString_UINT2 UcPacketErrorInfo;

        public static xCD getSuccessACK(UcType.UINT1 messageType, UcType.UINT2 messageNo, UcType.UINT4 messageSequenceNo) {
            try {
                xCD msg = new xCD();
                msg.setMessageNo(messageNo);
                msg.setMessageSequenceNo(messageSequenceNo);
                msg.setMessageType(messageType);
                msg.getHeader().setMessageLength(UcType.newUINT4(16 + msg.getBodyBytes().length));
                msg.getHeader().setMessageNo(UcType.newUINT2(0));
                msg.getHeader().setMessageSequenceNo(UcType.newUINT4(0));
                return msg;
            } catch (UcTypeException ex) {
                Logger.getLogger(xCD.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        public xCD() {
            try {
                header.setMessageType(UcType.newUINT1(0xcd));
                MessageType = UcType.newUINT1(0x00);
                MessageNo = UcType.newUINT2(0);
                MessageSequenceNo = UcType.newUINT4(0);
                UcPacketErrorInfo = UcType.newUString_UINT2("");
                UcPacketErrorStatus = UcType.newUINT1(0x00);
            } catch (UcTypeException ex) {
                Logger.getLogger(xCD.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(xCD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public Header getHeader() {
            return header;
        }

        public UcType.UINT1 getMessageType() {
            return MessageType;
        }

        public void setMessageType(UcType.UINT1 MessageType) {
            this.MessageType = MessageType;
        }

        public UcType.UINT2 getMessageNo() {
            return MessageNo;
        }

        public void setMessageNo(UcType.UINT2 MessageNo) {
            this.MessageNo = MessageNo;
        }

        public UcType.UINT4 getMessageSequenceNo() {
            return MessageSequenceNo;
        }

        public void setMessageSequenceNo(UcType.UINT4 MessageSequenceNo) {
            this.MessageSequenceNo = MessageSequenceNo;
        }

        public UcType.UINT1 getUcPacketErrorStatus() {
            return UcPacketErrorStatus;
        }

        public void setUcPacketErrorStatus(UcType.UINT1 UcPacketErrorStatus) {
            this.UcPacketErrorStatus = UcPacketErrorStatus;
        }

        public UcType.UString_UINT2 getUcPacketErrorInfo() {
            return UcPacketErrorInfo;
        }

        public void setUcPacketErrorInfo(UcType.UString_UINT2 UcPacketErrorInfo) {
            this.UcPacketErrorInfo = UcPacketErrorInfo;
        }

        public byte[] getBodyBytes() {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                os.write(MessageType.getBytes());
                os.write(MessageNo.getBytes());
                os.write(MessageSequenceNo.getBytes());
                os.write(UcPacketErrorStatus.getBytes());
                os.write(UcPacketErrorInfo.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(xCD.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(xCD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return os.toByteArray();
        }

        public byte[] toBytes() {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                byte[] bodys = getBodyBytes();
                getHeader().setMessageLength(UcType.newUINT4(bodys.length + 16));
                os.write(getHeader().getBytes());
                os.write(bodys);
            } catch (IOException ex) {
                Logger.getLogger(xCD.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UcTypeException ex) {
                Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(xCD.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return os.toByteArray();
        }

        public void parse(byte[] header, byte[] body) {
            this.header.headerBytes = header;
            this.MessageType = new UcType.UINT1(body, 0, 1);
            this.MessageNo = new UcType.UINT2(body, 1, 2);
            this.MessageSequenceNo = new UcType.UINT4(body, 3, 4);
//            this.UcPacketErrorStatus = new UcType.UINT1(body, 7, 1);
            this.UcPacketErrorInfo = new UcType.UString_UINT2(body, 7, body.length - 7);
        }

        @Override
        public void parseBytes(byte[] header, byte[] body) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void parseXML(String xmlForm) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String toXML(String xmlForm) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ACK[").append(this.MessageType.toByte()).append("]{MessageNo=").append(this.MessageNo.toInteger()).append(",MessageSequenceNo=").append(this.MessageSequenceNo).append(",UcPacketErrorStatus=").append(this.UcPacketErrorStatus).append(",UcPacketErrorInfo='").append(this.UcPacketErrorInfo.toString()).append("'}");
            return sb.toString();
        }

    }

    public static void main(String[] args) {
        UcMsg.x00 x00 = new UcMsg.x00();

        UcMsg.xCD ack = new UcMsg.xCD();
        try {
            ack.setMessageType(UcType.newUINT1(0x00));
            ack.setMessageNo(UcType.newUINT2(1));
            ack.setMessageSequenceNo(UcType.newUINT4(1));
            ack.setUcPacketErrorStatus(UcType.newUINT1(0x00));
            ack.setUcPacketErrorInfo(UcType.newUString_UINT2("abc"));
            System.out.println(Arrays.toString(ack.toBytes()));

        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
