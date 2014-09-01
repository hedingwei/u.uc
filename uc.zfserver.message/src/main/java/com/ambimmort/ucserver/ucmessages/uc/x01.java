/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages.uc;

import com.ambimmort.msg0X01.Msg0X01Document;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import com.ambimmort.ucserver.util.HexDisplay;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author 定巍
 */
public class x01 extends UcMsg {

    private UcType.UINT1 PacketType;
    private UcType.UINT1 PacketSubType;
    private UcType.UINT4 R_StartTime;
    private UcType.UINT4 R_EndTime;
    private UcType.UINT1 R_Freq;
    private UcType.IPv4 IP;
    private UcType.UINT2 Port;
    private UcType.UINT1 R_Method;
    private UcType.UserName UserName;
    private UcType.Password Password;
    private UcType.UINT4 messageSerialNo = null;

    private Msg0X01Document document;

    public void setMessageSerialNo(UcType.UINT4 serialNo) {
        this.messageSerialNo = serialNo;
    }

    public UcType.UINT4 getMessageSerialNo() {
        return messageSerialNo;
    }

    @Override
    public void parseBytes(byte[] header, byte[] body) {

//        document = Msg0X01Document.Factory.newInstance();
//        Msg0X01 msg = document.addNewMsg0X01();
//        //process body
//        this.header.headerBytes = header;
//        msg.setMessageNo(this.header.getMessageNo().toInteger());
//        msg.setMessageSequenceNo(this.header.getMessageSequenceNo().toInteger());
//
//        //process
//        this.PacketType = new UcType.UINT1(body, 0, 1);
//        msg.setWebHitThreshold(this.Web_Hit_Threshold.toInteger());
//
//        this.KW_Threholds = new UcType.UINT2(body, 2, 2);
//        msg.setKWThreholds(this.KW_Threholds.toInteger());
//
//        byte searchEngineLength = new UcType.UINT1(body, 4, 1).toByte();
//        this.SearchEngines = new UcType.UArray_UINT1();
//        SearchEngines ses = msg.addNewSearchEngines();
//        int recordPtr = 5;
//        for (byte i = 0; i < searchEngineLength; i++) {
//
//            UcType.UINT1 SEName_Length = new UcType.UINT1(body, recordPtr, 1);
//            SearchEngines.push(SEName_Length);
//            recordPtr++;
//
//            UcType.UString_UINT1 SEName = new UcType.UString_UINT1(body, recordPtr - 1, SEName_Length.toByte()+1);
//            recordPtr = recordPtr + SEName_Length.toByte();
//
//            SearchEngines.push(SEName);
//            SearchEngine se = ses.addNewSearchEngine();
//            se.setSEName(SEName.toString());
//
//        }
//
//        byte Cookie_Host_Num = new UcType.UINT1(body, recordPtr, 1).toByte();
//        recordPtr++;
//        this.CookieHosts = new UcType.UArray_UINT1();
//        CookieHosts chs = msg.addNewCookieHosts();
//        for (byte i = 0; i < Cookie_Host_Num; i++) {
//
//            UcType.UINT1 Cookie_Host_Name_Length = new UcType.UINT1(body, recordPtr, 1);
//            recordPtr++;
//
//            UcType.UString_UINT1 Cookie_Host_Name = new UcType.UString_UINT1(body, recordPtr - 1, Cookie_Host_Name_Length.toByte()+1);
//            recordPtr = recordPtr + Cookie_Host_Name_Length.toByte();
//
//            UcType.UINT1 Cookie_Host_key_length = new UcType.UINT1(body, recordPtr, 1);
//            recordPtr++;
//            UcType.UString_UINT1 Cookie_Host_key_value = new UcType.UString_UINT1(body, recordPtr - 1, Cookie_Host_key_length.toByte()+1);
//            recordPtr = recordPtr + Cookie_Host_key_length.toByte();
//
//
//            CookieHost ch = chs.addNewCookieHost();
//            ch.setName(Cookie_Host_Name.toString());
//            ch.setKey(Cookie_Host_key_value.toString());
//        }
//
//        this.messageSerialNo = new UcType.UINT4(body, recordPtr, 4);
//        msg.setMessageSerialNo(this.messageSerialNo.toInteger());
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
            Logger.getLogger(x01.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(x01.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
    }

    @Override
    public void parseXML(String xmlForm) {
        try {

            getHeader().setMessageType(UcType.newUINT1(0x01));
            getHeader().setMessageSequenceNo(UcType.newUINT4(1l));
            Msg0X01Document document = null;
            try {
                document = Msg0X01Document.Factory.parse(xmlForm);
            } catch (XmlException ex) {
                Logger.getLogger(x01.class.getName()).log(Level.SEVERE, null, ex);
            }
            Msg0X01Document.Msg0X01 msg = document.getMsg0X01();
            this.PacketType = UcType.UINT1.newUINT1(msg.getPacketType());
            this.PacketSubType = UcType.UINT1.newUINT1(msg.getPacketSubtype());
            this.R_StartTime = UcType.UINT4.newUINT4(msg.getRStartTime());
            this.R_EndTime = UcType.UINT4.newUINT4(msg.getREndTime());
            this.R_Freq = UcType.newUINT1(msg.getRFreq());
            this.IP = new UcType.IPv4(msg.getRDestIP().getIPv4Address());
            this.Port = UcType.newUINT2(msg.getRDestPort());
            this.R_Method = UcType.newUINT1(msg.getRMethod());
            this.UserName = new UcType.UserName(msg.getUserName());
            this.Password = new UcType.Password(msg.getPassword());
            this.messageSerialNo = UcType.newUINT4(msg.getMessageSerialNo2());
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
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
            os.write(this.PacketType.getBytes());
            os.write(this.PacketSubType.getBytes());
            os.write(this.R_StartTime.getBytes());
            os.write(this.R_EndTime.getBytes());
            os.write(this.R_Freq.getBytes());
            os.write(this.IP.getBytes());
            os.write(this.Port.getBytes());
            os.write(this.R_Method.getBytes());
            os.write(this.UserName.getBytes());
            os.write(this.Password.getBytes());
            os.write(messageSerialNo.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(x01.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(x01.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
    }

    public static void main(String[] args) {
        try {
            Msg0X01Document document = Msg0X01Document.Factory.parse(new File("src/main/resources/xml/msg_0x01.xml"));
            UcMsg.x01 x01 = new x01();
            x01.parseXML(document.toString());
            System.out.println(HexDisplay.getHex(x01.toBytes()));
        } catch (XmlException ex) {
            Logger.getLogger(x01.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(x01.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public boolean isVersionManaged() {
        return true;
    }

  



}
