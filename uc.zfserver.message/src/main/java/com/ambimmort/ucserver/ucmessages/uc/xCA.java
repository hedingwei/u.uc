/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages.uc;

import com.ambimmort.msg0Xca.Type0XcaDocument;
import com.ambimmort.ucserver.ucmessages.UcMsg;
import com.ambimmort.ucserver.ucmessages.UcType;
import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;
import com.ambimmort.ucserver.util.HexDisplay;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author 定巍
 */
public class xCA extends UcMsg {

    private UcType.IPv4 IP;
    private UcType.UINT2 Port;
    private UcType.UserName UserName;
    private UcType.Password Password;
    private UcType.UString_UINT2 FileName;
    private UcType.UINT4 IPAT_SerialNo;

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
    public void parseXML(String xmlForm) {
        try {
            getHeader().setMessageType(UcType.newUINT1(0xca));
            getHeader().setMessageSequenceNo(UcType.newUINT4(1l));

            Type0XcaDocument document = Type0XcaDocument.Factory.parse(xmlForm);
            Type0XcaDocument.Type0Xca msg = document.getType0Xca();
            this.IP = new UcType.IPv4(msg.getServerIP().getIPv4Address());
            this.Port = UcType.newUINT2(msg.getServerPort());
            this.UserName = new UcType.UserName(msg.getUserName());
            this.Password = new UcType.Password(msg.getPassword());
            this.FileName = UcType.newUString_UINT2(msg.getIPATFileName());
            this.IPAT_SerialNo = UcType.newUINT4(msg.getIPATSerialNo());
        } catch (XmlException ex) {
            Logger.getLogger(xCA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(xCA.class.getName()).log(Level.SEVERE, null, ex);
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
            os.write(this.IP.getBytes());
            os.write(this.Port.getBytes());
            os.write(this.UserName.getBytes());
            os.write(this.Password.getBytes());
            os.write(this.FileName.getBytes());
            os.write(this.IPAT_SerialNo.getBytes());

        } catch (IOException ex) {
            Logger.getLogger(xCA.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(xCA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
    }

    public static void main(String[] args) {
        UcMsg msg = UcMsg.buildUcMsg("0xCA", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<Type_0xca messageNo=\"0\" messageSequenceNo=\"0\"\n"
                + "	messageSerialNo=\"0\" xmlns=\"http://www.ambimmort.com/msg_0xca\" xmlns:u=\"http://www.ambimmort.com/UType\"\n"
                + "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + "	xsi:schemaLocation=\"http://www.ambimmort.com/msg_0xca sg_0xca.xsd \">\n"
                + "	<Server_IP>\n"
                + "		<u:IPType>IPv4</u:IPType>\n"
                + "		<u:IPv4Address>1.1.1.1</u:IPv4Address>\n"
                + "	</Server_IP>\n"
                + "	<Server_Port>22</Server_Port>\n"
                + "	<UserName>UserName</UserName>\n"
                + "	<Password>Password</Password>\n"
                + "	<IPAT_FileName>IPAT_FileName</IPAT_FileName>\n"
                + "	<IPAT_SerialNo>1</IPAT_SerialNo>\n"
                + "</Type_0xca>\n"
                + "");
        HexDisplay.print(msg.toBytes());

    }

}
