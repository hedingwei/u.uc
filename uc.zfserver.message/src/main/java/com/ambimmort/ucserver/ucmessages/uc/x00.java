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
public class x00 extends UcMsg {

    private UcType.UINT2 Web_Hit_Threshold;
    private UcType.UINT2 KW_Threholds;
    private UcType.UArray_UINT1 SearchEngines;
    private UcType.UArray_UINT1 CookieHosts;
    private UcType.UINT4 messageSerialNo;

    private Msg0X00Document document;

    public void setMessageSerialNo(UcType.UINT4 serialNo) {
        this.messageSerialNo = serialNo;
    }

    public UcType.UINT4 getMessageSerialNo() {
        return messageSerialNo;
    }

    @Override
    public void parseBytes(byte[] header, byte[] body) {

        document = Msg0X00Document.Factory.newInstance();
        Msg0X00 msg = document.addNewMsg0X00();
        //process body
        this.header.headerBytes = header;
        msg.setMessageNo(this.header.getMessageNo().toInteger());
        msg.setMessageSequenceNo(this.header.getMessageSequenceNo().toInteger());

        //process
        this.Web_Hit_Threshold = new UcType.UINT2(body, 0, 2);
        msg.setWebHitThreshold(this.Web_Hit_Threshold.toInteger());

        this.KW_Threholds = new UcType.UINT2(body, 2, 2);
        msg.setKWThreholds(this.KW_Threholds.toInteger());

        byte searchEngineLength = new UcType.UINT1(body, 4, 1).toByte();
        this.SearchEngines = new UcType.UArray_UINT1();
        SearchEngines ses = msg.addNewSearchEngines();
        int recordPtr = 5;
        for (byte i = 0; i < searchEngineLength; i++) {

            UcType.UINT1 SEName_Length = new UcType.UINT1(body, recordPtr, 1);
            SearchEngines.push(SEName_Length);
            recordPtr++;

            UcType.UString_UINT1 SEName = new UcType.UString_UINT1(body, recordPtr - 1, SEName_Length.toByte()+1);
            recordPtr = recordPtr + SEName_Length.toByte();

            SearchEngines.push(SEName);
            SearchEngine se = ses.addNewSearchEngine();
            se.setSEName(SEName.toString());

        }

        byte Cookie_Host_Num = new UcType.UINT1(body, recordPtr, 1).toByte();
        recordPtr++;
        this.CookieHosts = new UcType.UArray_UINT1();
        CookieHosts chs = msg.addNewCookieHosts();
        for (byte i = 0; i < Cookie_Host_Num; i++) {

            UcType.UINT1 Cookie_Host_Name_Length = new UcType.UINT1(body, recordPtr, 1);
            recordPtr++;

            UcType.UString_UINT1 Cookie_Host_Name = new UcType.UString_UINT1(body, recordPtr - 1, Cookie_Host_Name_Length.toByte()+1);
            recordPtr = recordPtr + Cookie_Host_Name_Length.toByte();

            UcType.UINT1 Cookie_Host_key_length = new UcType.UINT1(body, recordPtr, 1);
            recordPtr++;
            UcType.UString_UINT1 Cookie_Host_key_value = new UcType.UString_UINT1(body, recordPtr - 1, Cookie_Host_key_length.toByte()+1);
            recordPtr = recordPtr + Cookie_Host_key_length.toByte();


            CookieHost ch = chs.addNewCookieHost();
            ch.setName(Cookie_Host_Name.toString());
            ch.setKey(Cookie_Host_key_value.toString());
        }

        this.messageSerialNo = new UcType.UINT4(body, recordPtr, 4);
        msg.setMessageSerialNo(this.messageSerialNo.toInteger());

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

            getHeader().setMessageType(UcType.newUINT1(0x00));
            getHeader().setMessageSequenceNo(UcType.newUINT4(1l));
            Msg0X00Document document = Msg0X00Document.Factory.parse(xmlForm);
            Msg0X00Document.Msg0X00 msg = document.getMsg0X00();
            Web_Hit_Threshold = UcType.UINT2.newUINT2(msg.getWebHitThreshold());
            KW_Threholds = UcType.UINT2.newUINT2(msg.getKWThreholds());
            SearchEngines = UcType.newUArray_UINT1();
            for (Msg0X00Document.Msg0X00.SearchEngines.SearchEngine se : msg.getSearchEngines().getSearchEngineArray()) {
                SearchEngines.push(UcType.newUString_UINT1(se.getSEName()));
            }
            CookieHosts = UcType.newUArray_UINT1();
            for (Msg0X00Document.Msg0X00.CookieHosts.CookieHost host : msg.getCookieHosts().getCookieHostArray()) {
                UcType.UObject obj = new UcType.UObject();
                obj.push("host_name", UcType.newUString_UINT1(host.getName()));
                obj.push("host_key", UcType.newUString_UINT1(host.getKey()));
                CookieHosts.push(obj);
            }
            this.setMessageSerialNo(UcType.newUINT4(msg.getMessageSerialNo()));
        } catch (XmlException ex) {
            Logger.getLogger(x00.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
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
            os.write(Web_Hit_Threshold.getBytes());
            os.write(KW_Threholds.getBytes());
            os.write(SearchEngines.getBytes());
            os.write(CookieHosts.getBytes());
            os.write(messageSerialNo.getBytes());
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

    public static void main(String[] args) {
        try {
            Msg0X00Document document = Msg0X00Document.Factory.parse(new File("src/main/resources/xml/msg_0x00.xml"));
            Msg0X00 _0x00 = document.getMsg0X00();

            
            
            
            UcMsg.x00 x00 = new x00();
            x00.parseXML(document.toString());
            System.out.println(HexDisplay.getHex(x00.toBytes()));
            
            
            

            UcMsg msg = new x00();
            msg.parseBytes(x00.getHeader().headerBytes, x00.getBodyBytes());
            
            System.out.println(msg.toXML());

        } catch (XmlException ex) {
            Logger.getLogger(x00.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(x00.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
