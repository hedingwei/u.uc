/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.uc.dpi.simulator.server.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.HexDisplay;

/**
 *
 * @author 定巍
 */
public class UcRawMessage {

    private long id;
    private byte[] header = new byte[16];
    private byte[] body = null;
    private byte messageType;
    private String sessionId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void normalize() {
        messageType = header[4];
    }
    
    

    public byte getVersion() {
        return header[0];
    }

    public String getProtoSignature() {
        byte[] d = new byte[3];
        d[0] = header[1];
        d[1] = header[2];
        d[2] = header[3];
        return new String(d);
    }

    public byte[] getHeader() {
        return header;
    }

    public long getMessageLength() {
        return (header[15] & 0xFF | (header[14] & 0xFF) << 8
                | (header[13] & 0xFF) << 16 | (header[12] & 0xFF) << 24);
    }

    public long getMessageSequenceNo() {
        return (header[11] & 0xFF | (header[10] & 0xFF) << 8
                | (header[9] & 0xFF) << 16 | (header[8] & 0xFF) << 24);
    }

    public byte getMessageType() {
        return header[4];
    }

    public int getMessageNo() {
        return (header[6] & 0xFF | (header[5] & 0xFF) << 8);
    }

    public long getLengthNeeded() {
        return getMessageLength() - 16;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(header);
            os.write(body);
        } catch (IOException ex) {
            Logger.getLogger(UcRawMessage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return os.toByteArray();
    }

    public ByteBuffer getByteBuffer() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(header);
            os.write(body);
        } catch (IOException ex) {
            Logger.getLogger(UcRawMessage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ByteBuffer.wrap(os.toByteArray());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(this.id).append("\n");
        sb.append("sessionId=").append(this.sessionId).append("\n");
        sb.append("messageLength=").append(this.getMessageLength()).append("\n");
        sb.append("messageType=").append(this.getMessageType()).append("\n");
        sb.append("messageNo=").append(this.getMessageNo()).append("\n");
        sb.append("messageSequenceNo=").append(this.getMessageSequenceNo()).append("\n");
        sb.append("protoSignature=").append(this.getProtoSignature()).append("\n");
        
        sb.append("header hex:\n").append(HexDisplay.getHex(header)).append("\n");
        sb.append("body hex:\n").append(HexDisplay.getHex(body)).append("\n");
        return sb.toString();
    }

    
    
}
