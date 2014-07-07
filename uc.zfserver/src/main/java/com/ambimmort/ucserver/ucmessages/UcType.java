/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.ucserver.ucmessages;

import com.ambimmort.ucserver.util.ByteConvert;
import com.ambimmort.ucserver.ucmessages.exceptions.UcTypeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public abstract class UcType {

    private byte[] data = null;

    protected UcType(byte[] data) {
        this.data = data;
    }

    protected UcType(byte[] data, int offset, int length) {
        this.data = new byte[length];
        System.arraycopy(data, offset, this.data, 0, length);
    }

    public byte[] getBytes() {
        return data;
    }

    public int getSize() {
        return data.length * 8;
    }

    public static UObject newUObject() {
        return new UObject();
    }

    public static UArray newUArray() {
        return new UArray();
    }

    public static UINT1 newUINT1(int uByteValue) throws UcTypeException {
        if (uByteValue >= 0 && uByteValue <= 255) {
            return new UINT1(ByteConvert.ubyteToBytes(uByteValue));
        } else {
            throw new UcTypeException("UByte range: [0,255], value=" + uByteValue);
        }
    }

    public static UINT2 newUINT2(int uShortValue) throws UcTypeException {
        if (uShortValue >= 0 && uShortValue <= 65535) {
            return new UINT2(ByteConvert.ushortToBytes(uShortValue));
        } else {
            throw new UcTypeException("UByte range: [0,65535], value=" + uShortValue);
        }
    }

    public static UINT4 newUINT4(long uIntValue) throws UcTypeException {
        if (uIntValue >= 0 && uIntValue <= 2147483647) {
            return new UINT4(ByteConvert.uintToBytes(uIntValue));
        } else {
            throw new UcTypeException("UByte range: [0,2147483647], value=" + uIntValue);
        }
    }

    public static UcType.UString_UINT1 newUString_UINT1(String uStringValue) throws UcTypeException, UnsupportedEncodingException {
        byte[] uStringValueBytes = uStringValue.getBytes("utf-8");
        int uStringValueLength = uStringValueBytes.length;
        if (uStringValueLength <= 255) {
            byte[] data = new byte[uStringValueLength + 1];
            data[0] = (byte) (uStringValueLength & 0xFF);
            System.arraycopy(uStringValueBytes, 0, data, 1, uStringValueLength);
            return new UString_UINT1(data);
        } else {
            throw new UcTypeException("UString1Byte string length: [0,255], length=" + uStringValueLength + ", string :\n" + uStringValue);
        }
    }

    public static UcType.UString_UINT2 newUString_UINT2(String uStringValue) throws UcTypeException, UnsupportedEncodingException {
        byte[] uStringValueBytes = uStringValue.getBytes("utf-8");
        int uStringValueLength = uStringValueBytes.length;
        if (uStringValueLength <= 65535) {
            byte[] data = new byte[uStringValueLength + 2];
            data[0] = (byte) (uStringValueLength >> 8 & 0xFF);
            data[1] = (byte) (uStringValueLength & 0xFF);
            System.arraycopy(uStringValueBytes, 0, data, 2, uStringValueLength);
            return new UString_UINT2(data);
        } else {
            throw new UcTypeException("UString1Byte string length: [0,65535], length=" + uStringValueLength + ", string :\n" + uStringValue);
        }
    }

    public static UcType.UString_UINT4 newUString_UINT4(String uStringValue) throws UcTypeException, UnsupportedEncodingException {
        byte[] uStringValueBytes = uStringValue.getBytes("utf-8");
        int uStringValueLength = uStringValueBytes.length;
        if (uStringValueLength <= 2147483647) {
            byte[] data = new byte[uStringValueLength + 4];

            data[3] = (byte) (int) (uStringValueLength & 0xFF);
            data[2] = (byte) (int) (uStringValueLength >> 8 & 0xFF);
            data[1] = (byte) (int) (uStringValueLength >> 16 & 0xFF);
            data[0] = (byte) (int) (uStringValueLength >> 24 & 0xFF);
            System.arraycopy(uStringValueBytes, 0, data, 4, uStringValueLength);

            return new UString_UINT4(data);
        } else {
            throw new UcTypeException("UString1Byte string length: [0,65535], length=" + uStringValueLength + ", string :\n" + uStringValue);
        }
    }

    public static class UINT1 extends UcType {

        public UINT1(byte[] data) {
            super(data);
        }

        public UINT1(byte[] data, int offset, int length) {
            super(data, offset, length);
        }

        @Override
        public String toString() {
            return "" + ByteConvert.bytesToUbyte(super.data);
        }

        public byte toByte() {
            return super.data[0];
        }

    }

    public static class UINT2 extends UcType {

        public UINT2(byte[] data) {
            super(data);
        }

        public UINT2(byte[] data, int offset, int length) {
            super(data, offset, length);
        }

        @Override
        public String toString() {
            return "" + ByteConvert.bytesToUshort(super.data);
        }

        public int toInteger() {
            return ByteConvert.bytesToUshort(super.data);
        }
    }

    public static class UINT4 extends UcType {

        public UINT4(byte[] data) {
            super(data);
        }

        public UINT4(byte[] data, int offset, int length) {
            super(data, offset, length);
        }

        @Override
        public String toString() {
            return "" + ByteConvert.bytesToUint(super.data);
        }

        public long toLong() {
            return ByteConvert.bytesToUint(super.data);
        }
    }

    public static class UString_UINT1 extends UcType {

        public UString_UINT1(byte[] data) {
            super(data);
        }

        public UString_UINT1(byte[] data, int offset, int length) {
            super(data, offset, length);
        }

        @Override
        public String toString() {
            return new String(super.getBytes(), 1, super.data.length - 1, Charset.forName("utf-8"));
        }

        public long length() {
            return ByteConvert.bytesToUbyte(new byte[]{super.data[0]});
        }
    }

    public static class UString_UINT2 extends UcType {

        public UString_UINT2(byte[] data) {
            super(data);
        }

        public UString_UINT2(byte[] data, int offset, int length) {
            super(data, offset, length);
        }

        @Override
        public String toString() {
            return new String(super.getBytes(), 2, super.data.length - 2, Charset.forName("utf-8"));
        }

        public long length() {
            return ByteConvert.bytesToUshort(new byte[]{super.data[0], super.data[1]});
        }
    }

    public static class UString_UINT4 extends UcType {

        public UString_UINT4(byte[] data) {
            super(data);
        }

        public UString_UINT4(byte[] data, int offset, int length) {
            super(data, offset, length);
        }

        @Override
        public String toString() {
            return new String(super.getBytes(), 4, super.data.length - 4, Charset.forName("utf-8"));
        }

        public long length() {
            return ByteConvert.bytesToUint(new byte[]{super.data[0], super.data[1], super.data[2], super.data[3]});
        }
    }

    public static class UObject extends UcType {

        private ArrayList<String> fields = new ArrayList<String>();
        private final HashMap<String, UcType> value = new HashMap();

        public UObject(byte[] data) {
            super(data);
        }
        
        public UObject(byte[] data, int offset, int length) {
            super(data, offset, length);
        }

        public UObject() {
            super(null);
        }

        public void push(String field, UcType aUcTypeValue) {
            fields.add(field);
            value.put(field, aUcTypeValue);
        }

        @Override
        public byte[] getBytes() {
            if (super.data == null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                for (String field : fields) {
                    try {
                        os.write(value.get(field).getBytes());
                    } catch (IOException ex) {
                        Logger.getLogger(UcType.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    os.close();
                } catch (Exception e) {

                }
                super.data = os.toByteArray();

            }
            return super.data;

        }

    }

    public static class UArray extends UcType {

        private ArrayList<UcType> list = new ArrayList<UcType>();

        public UArray(byte[] data) {
            super(data);
        }
        
        public UArray(byte[] data, int offset, int length) {
            super(data, offset, length);
        }

        public UArray() {
            super(null);
        }

        public void push(UcType aUcTypeValue) {
            list.add(aUcTypeValue);
        }

        @Override
        public byte[] getBytes() {
            if (super.data == null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                for (UcType field : list) {
                    try {
                        os.write(field.getBytes());
                    } catch (IOException ex) {
                        Logger.getLogger(UcType.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    os.close();
                } catch (Exception e) {

                }
                super.data = os.toByteArray();

            }
            return super.data;

        }

    }

    public static void main(String[] args) {
        UArray obj = UcType.newUArray();
        try {
            obj.push(UcType.newUINT1(0));
        } catch (UcTypeException ex) {
            Logger.getLogger(UcType.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
