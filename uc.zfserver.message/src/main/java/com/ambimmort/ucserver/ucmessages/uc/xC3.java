/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages.uc;

import com.ambimmort.msg0X00.Msg0X00Document;
import com.ambimmort.msg0X00.Msg0X00Document.Msg0X00;
import com.ambimmort.msg0X00.Msg0X00Document.Msg0X00.CookieHosts;
import com.ambimmort.msg0X00.Msg0X00Document.Msg0X00.CookieHosts.CookieHost;
import com.ambimmort.msg0X00.Msg0X00Document.Msg0X00.SearchEngines;
import com.ambimmort.msg0X00.Msg0X00Document.Msg0X00.SearchEngines.SearchEngine;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import com.ambimmort.ucserver.util.HexDisplay;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author 定巍
 */
public class xC3 extends UcMsg {

    private UcType.UINT1 MessageType;
    private UcType.UINT4 MessageSerialNo;
    private UcType.UArray_UINT2 Messages;

    public xC3(int MessageType, long MessageSerialNo) {
        try {
            this.MessageType = UcType.newUINT1(MessageType);
            this.MessageSerialNo = UcType.newUINT4(MessageSerialNo);
            Messages = new UcType.UArray_UINT2();
        } catch (UcTypeException ex) {
            Logger.getLogger(xC3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean isVersionManaged() {
        return false;
    }

    @Override
    public void setMessageSerialNo(UcType.UINT4 messageSerialNo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UcType.UINT4 getMessageSerialNo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void parseBytes(byte[] header, byte[] body) {
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
            Logger.getLogger(xC3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(xC3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
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
    public byte[] getBodyBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(this.MessageType.getBytes());
            os.write(MessageSerialNo.getBytes());
            os.write(Messages.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(xC3.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(xC3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
    }

    public void addMessage(int changeType, UcMsg msg) {
        UcType.UObject obj = new UcType.UObject();
        obj.push("messageNo", msg.getHeader().getMessageNo());
        try {
            if(changeType==0){
                obj.push("action", UcType.newUINT1(3));
            }else if(changeType==1){
                obj.push("action", UcType.newUINT1(2));
            }else if(changeType==2){
                obj.push("action", UcType.newUINT1(1));
            }
            
        } catch (UcTypeException ex) {
            Logger.getLogger(xC3.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] body = msg.getBodyBytes();
        try {
            obj.push("length", UcType.newUINT4(body.length));
        } catch (UcTypeException ex) {
            Logger.getLogger(xC3.class.getName()).log(Level.SEVERE, null, ex);
        }
        obj.push("mc", new UcType.UObject(body));
        Messages.push(obj);
    }

    public static void main(String[] args) {

    }

}
