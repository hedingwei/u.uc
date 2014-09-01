/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages.uc;

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
public class xC1 extends UcMsg {

    UcType.UString_UINT1 Dev_Name;


    public xC1(String dev_name) {
        try {
            getHeader().setMessageType(UcType.newUINT1(0xc1));
            getHeader().setMessageNo(UcType.newUINT2(1));
            getHeader().setMessageSequenceNo(UcType.newUINT4(1));
            
            Dev_Name = UcType.newUString_UINT1(dev_name);
        } catch (UcTypeException ex) {
            Logger.getLogger(xC1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(xC1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    @Override
    public byte[] getBodyBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(Dev_Name.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(xC1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(xC1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
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
            os.write(getHeader().getBytes());
            os.write(bodys);
        } catch (IOException ex) {
            Logger.getLogger(xC1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(xC1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return os.toByteArray();
    }

    @Override
    public void parseXML(String xmlForm) {
        
    }

    @Override
    public String toXML() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
