/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages.uc;

import com.ambimmort.msg0X00.Msg0X00Document;
import com.ambimmort.msg0X85.Type0X85Document;
import com.ambimmort.msg0X85.Type0X85Document.Type0X85.Messages.Message;
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
public class x85 extends UcMsg {

    private UcType.UINT1 Bind_Action;
    private UcType.UINT1 UserType;
    private UcType.UString_UINT1 UserName;
    private UcType.UArray_UINT2 Messages;

    private Type0X85Document document;

    

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
            Logger.getLogger(x85.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(x85.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
    }

    @Override
    public void parseXML(String xmlForm) {
        try {

            getHeader().setMessageType(UcType.newUINT1(0x85));
            getHeader().setMessageSequenceNo(UcType.newUINT4(1l));
            Type0X85Document document = null;
            try {
                document = Type0X85Document.Factory.parse(xmlForm);
            } catch (XmlException ex) {
                Logger.getLogger(x85.class.getName()).log(Level.SEVERE, null, ex);
            }
            Type0X85Document.Type0X85 msg = document.getType0X85();
            
            this.Bind_Action = UcType.newUINT1(msg.getBindAction());
            this.UserType = UcType.newUINT1(msg.getUserType());
            this.UserName = UcType.newUString_UINT1(msg.getUserName());
            this.Messages = UcType.newUArray_UINT2();
            for(Message m: msg.getMessages().getMessageArray()){
                UcType.UObject obj = UcType.newUObject();
                obj.push("messageType", UcType.newUINT1(m.getMessageType()));
                obj.push("messageNo",UcType.newUINT2(m.getMessageNo()));
                this.Messages.push(obj);
            }
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(x85.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toXML() {
        if (this.document != null) {
            return this.document.toString();
        }
        return null;
    }

    @Override
    public byte[] getBodyBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(this.Bind_Action.getBytes());
            os.write(this.UserType.getBytes());
            os.write(this.UserName.getBytes());
            os.write(this.Messages.getBytes());
            
        } catch (IOException ex) {
            Logger.getLogger(x85.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(x85.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
    }

    public static void main(String[] args)  {
        try {
            Msg0X00Document document = Msg0X00Document.Factory.parse(new File("src/main/resources/xml/msg_0x00.xml"));
            System.out.println(HexDisplay.getHex(UcMsg.buildUcMsg("0x00", document.toString()).toBytes()));

        } catch (XmlException ex) {
            Logger.getLogger(x85.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(x85.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        try {
            return UcType.UINT4.newUINT4(0l);
        } catch (UcTypeException ex) {
            Logger.getLogger(x85.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    

}
