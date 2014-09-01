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
public class xC2 extends UcMsg {
    private String dir;
    
    public xC2(String dir){
        try {
            getHeader().setMessageType(UcType.newUINT1(0xc2));
            getHeader().setMessageNo(UcType.newUINT2(1));
            getHeader().setMessageSequenceNo(UcType.newUINT4(1));
            this.dir = dir;
        } catch (UcTypeException ex) {
            Logger.getLogger(xC2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public byte[] getBodyBytes() {
        File path;
        UcType.UArray_UINT1 array = new UcType.UArray_UINT1();
        try {
            for(int i = 0 ; i < 255 ; i++){
                UcType.UObject obj = new UcType.UObject();
                long sNO = 0;
                path = new File(dir+i);
                if(path.exists()){
                    File[] l = path.listFiles();
                    for(File f : l){
                        sNO = Math.max(sNO , Long.parseLong(f.getName()));
                    }
                }
//                else{
//                    path.mkdirs();
//                }
                obj.push("mt", UcType.newUINT1(i));
                obj.push("ms", UcType.newUINT4(sNO));
                array.push(obj);
            }
        } catch (UcTypeException ex) {
            Logger.getLogger(xC2.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return array.getBytes();
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
            Logger.getLogger(xC2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UcTypeException ex) {
            Logger.getLogger(UcMsg.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(xC2.class.getName()).log(Level.SEVERE, null, ex);
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
