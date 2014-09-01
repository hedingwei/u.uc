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
import com.ambimmort.msg0X43.Type0X45Document;
import com.ambimmort.msg0X43.Type0X45Document.Type0X45.DestAreaGroups.DestAreaGroup;
import com.ambimmort.msg0X43.Type0X45Document.Type0X45.SrcAreaGroups.SrcAreaGroup;
import com.ambimmort.msg0X43.Type0X45Document.Type0X45.SrcAreaGroups.SrcAreaGroup.Areas;
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
public class x45 extends UcMsg {

    private UcType.UArray_UINT2 Src_AreaGroup = new UcType.UArray_UINT2();
    private UcType.UArray_UINT2 Dest_AreaGroup = new UcType.UArray_UINT2();
    private UcType.UINT4 messageSerialNo;

    @Override
    public boolean isVersionManaged() {
        return true;
    }

    public UcType.UINT4 getMessageSerialNo() {
        return messageSerialNo;
    }

    public void setMessageSerialNo(UcType.UINT4 serialNo) {
        this.messageSerialNo = serialNo;
    }

    @Override
    public void parseBytes(byte[] header, byte[] body) {
    }

    @Override
    public void parseXML(String xmlForm) {
        try {

            getHeader().setMessageType(UcType.newUINT1(0x45));
            getHeader().setMessageSequenceNo(UcType.newUINT4(1l));

            Type0X45Document document = Type0X45Document.Factory.parse(xmlForm);
            Type0X45Document.Type0X45 msg = document.getType0X45();
            for (SrcAreaGroup g : msg.getSrcAreaGroups().getSrcAreaGroupArray()) {
                UcType.UObject obj = new UcType.UObject();
                obj.push("src_AreaGroup_ID", UcType.newUINT2(g.getSrcAreaGroupID()));
                obj.push("AS_Num", UcType.newUINT1(g.getASNum()));
                obj.push("AS_ID", UcType.newUINT4(g.getASID()));
                UcType.UArray_UINT1 areas = new UcType.UArray_UINT1();
                for (Areas a : g.getAreasArray()) {
                    areas.push(UcType.newUString_UINT1(a.getArea().getName()));
                }
                obj.push("Area", areas);
                this.Src_AreaGroup.push(obj);
            }

            for (DestAreaGroup g : msg.getDestAreaGroups().getDestAreaGroupArray()) {
                UcType.UObject obj = new UcType.UObject();
                obj.push("dst_AreaGroup_ID", UcType.newUINT2(g.getDestAreaGroupID()));
                obj.push("AS_Num", UcType.newUINT1(g.getASNum()));
                obj.push("AS_ID", UcType.newUINT4(g.getASID()));
                UcType.UArray_UINT1 areas = new UcType.UArray_UINT1();
                for (DestAreaGroup.Areas a : g.getAreasArray()) {
                    areas.push(UcType.newUString_UINT1(a.getArea().getName()));
                }
                obj.push("Area", areas);
                this.Dest_AreaGroup.push(obj);
            }
            this.setMessageSerialNo(UcType.newUINT4(msg.getMessageSerialNo()));
        } catch (XmlException ex) {
            Logger.getLogger(x45.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toXML() {
        return null;
    }

    @Override
    public byte[] getBodyBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(this.Src_AreaGroup.getBytes());
            os.write(this.Dest_AreaGroup.getBytes());
            os.write(this.messageSerialNo.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(x45.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(x45.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
    }

    public static void main(String[] args) {
        UcMsg msg = UcMsg.buildUcMsg("0x45", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"\n" +
"<Type_0x45\n" +
"    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n" +
"    xmlns='http://www.ambimmort.com/msg_0x43'\n" +
"    xsi:schemaLocation='http://www.ambimmort.com/msg_0x43 ../schemas/msg_0x45.xsd' messageNo=\"1\" messageSerialNo=\"1\" messageSequenceNo=\"1\">\n" +
"    <Src_AreaGroups>\n" +
"        <Src_AreaGroup>\n" +
"            <Src_AreaGroup_ID>1</Src_AreaGroup_ID>\n" +
"            <AS_Num>0</AS_Num>\n" +
"            <AS_ID>0</AS_ID>\n" +
"            <Areas>\n" +
"                <Area>\n" +
"                    <Name>a1</Name>\n" +
"                </Area>\n" +
"            </Areas>\n" +
"        </Src_AreaGroup>\n" +
"    </Src_AreaGroups>\n" +
"    <Dest_AreaGroups>\n" +
"        <Dest_AreaGroup>\n" +
"            <Dest_AreaGroup_ID>1</Dest_AreaGroup_ID>\n" +
"            <AS_Num>0</AS_Num>\n" +
"            <AS_ID>0</AS_ID>\n" +
"            <Areas>\n" +
"                <Area>\n" +
"                    <Name>abcde</Name>\n" +
"                </Area>\n" +
"            </Areas>\n" +
"        </Dest_AreaGroup>\n" +
"    </Dest_AreaGroups>\n" +
"    <MessageSerialNo>1</MessageSerialNo>\n" +
"\n" +
"</Type_0x45>\n" +
"");
        HexDisplay.print(msg.toBytes());


    }

}
