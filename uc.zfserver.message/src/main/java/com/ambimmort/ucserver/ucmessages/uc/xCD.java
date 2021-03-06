/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.ucserver.ucmessages.uc;

import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class xCD extends UcMsg {

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

        public UcMsg.Header getHeader() {
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
        public String toXML() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ACK[").append(this.MessageType.toByte()).append("]{MessageNo=").append(this.MessageNo.toInteger()).append(",MessageSequenceNo=").append(this.MessageSequenceNo).append(",UcPacketErrorStatus=").append(this.UcPacketErrorStatus).append(",UcPacketErrorInfo='").append(this.UcPacketErrorInfo.toString()).append("'}");
            return sb.toString();
        }

    @Override
    public boolean isVersionManaged() {
        return false;
    }

    @Override
    public void setMessageSerialNo(UcType.UINT4 messageSerialNo) {
        
    }

    @Override
    public UcType.UINT4 getMessageSerialNo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    }
