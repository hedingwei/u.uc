package com.ambimmort.ucserver.util;

public class ByteConvert {

	public static byte[] longToBytes(long n) {
		byte[] b = new byte[8];
		b[7] = (byte) (int) (n & 0xFF);
		b[6] = (byte) (int) (n >> 8 & 0xFF);
		b[5] = (byte) (int) (n >> 16 & 0xFF);
		b[4] = (byte) (int) (n >> 24 & 0xFF);
		b[3] = (byte) (int) (n >> 32 & 0xFF);
		b[2] = (byte) (int) (n >> 40 & 0xFF);
		b[1] = (byte) (int) (n >> 48 & 0xFF);
		b[0] = (byte) (int) (n >> 56 & 0xFF);
		return b;
	}

	public static void longToBytes(long n, byte[] array, int offset) {
		array[(7 + offset)] = (byte) (int) (n & 0xFF);
		array[(6 + offset)] = (byte) (int) (n >> 8 & 0xFF);
		array[(5 + offset)] = (byte) (int) (n >> 16 & 0xFF);
		array[(4 + offset)] = (byte) (int) (n >> 24 & 0xFF);
		array[(3 + offset)] = (byte) (int) (n >> 32 & 0xFF);
		array[(2 + offset)] = (byte) (int) (n >> 40 & 0xFF);
		array[(1 + offset)] = (byte) (int) (n >> 48 & 0xFF);
		array[(0 + offset)] = (byte) (int) (n >> 56 & 0xFF);
	}

	public static long bytesToLong(byte[] array) {
		return ((array[0] & 0xFF) << 56 | (array[1] & 0xFF) << 48
				| (array[2] & 0xFF) << 40 | (array[3] & 0xFF) << 32
				| (array[4] & 0xFF) << 24 | (array[5] & 0xFF) << 16
				| (array[6] & 0xFF) << 8 | (array[7] & 0xFF) << 0);
	}

	public static long bytesToLong(byte[] array, int offset) {
		return ((array[(offset + 0)] & 0xFF) << 56
				| (array[(offset + 1)] & 0xFF) << 48
				| (array[(offset + 2)] & 0xFF) << 40
				| (array[(offset + 3)] & 0xFF) << 32
				| (array[(offset + 4)] & 0xFF) << 24
				| (array[(offset + 5)] & 0xFF) << 16
				| (array[(offset + 6)] & 0xFF) << 8 | (array[(offset + 7)] & 0xFF) << 0);
	}

	public static byte[] intToBytes(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xFF);
		b[2] = (byte) (n >> 8 & 0xFF);
		b[1] = (byte) (n >> 16 & 0xFF);
		b[0] = (byte) (n >> 24 & 0xFF);
		return b;
	}

	public static void intToBytes(int n, byte[] array, int offset) {
		array[(3 + offset)] = (byte) (n & 0xFF);
		array[(2 + offset)] = (byte) (n >> 8 & 0xFF);
		array[(1 + offset)] = (byte) (n >> 16 & 0xFF);
		array[offset] = (byte) (n >> 24 & 0xFF);
	}

	public static int bytesToInt(byte[] b) {
		return (b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24);
	}

	public static int bytesToInt(byte[] b, int offset) {
		return (b[(offset + 3)] & 0xFF | (b[(offset + 2)] & 0xFF) << 8
				| (b[(offset + 1)] & 0xFF) << 16 | (b[offset] & 0xFF) << 24);
	}

	public static byte[] uintToBytes(long n) {
		byte[] b = new byte[4];
		b[3] = (byte) (int) (n & 0xFF);
		b[2] = (byte) (int) (n >> 8 & 0xFF);
		b[1] = (byte) (int) (n >> 16 & 0xFF);
		b[0] = (byte) (int) (n >> 24 & 0xFF);

		return b;
	}

	public static void uintToBytes(long n, byte[] array, int offset) {
		array[(3 + offset)] = (byte) (int) n;
		array[(2 + offset)] = (byte) (int) (n >> 8 & 0xFF);
		array[(1 + offset)] = (byte) (int) (n >> 16 & 0xFF);
		array[offset] = (byte) (int) (n >> 24 & 0xFF);
	}

	public static long bytesToUint(byte[] array) {
		return (array[3] & 0xFF | (array[2] & 0xFF) << 8
				| (array[1] & 0xFF) << 16 | (array[0] & 0xFF) << 24);
	}

	public static long bytesToUint(byte[] array, int offset) {
		return (array[(offset + 3)] & 0xFF | (array[(offset + 2)] & 0xFF) << 8
				| (array[(offset + 1)] & 0xFF) << 16 | (array[offset] & 0xFF) << 24);
	}

	public static byte[] shortToBytes(short n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xFF);
		b[0] = (byte) (n >> 8 & 0xFF);
		return b;
	}

	public static void shortToBytes(short n, byte[] array, int offset) {
		array[(offset + 1)] = (byte) (n & 0xFF);
		array[offset] = (byte) (n >> 8 & 0xFF);
	}

	public static short bytesToShort(byte[] b) {
		return (short) (b[1] & 0xFF | (b[0] & 0xFF) << 8);
	}

	public static short bytesToShort(byte[] b, int offset) {
		return (short) (b[(offset + 1)] & 0xFF | (b[offset] & 0xFF) << 8);
	}

	public static byte[] ushortToBytes(int n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xFF);
		b[0] = (byte) (n >> 8 & 0xFF);
		return b;
	}

	public static void ushortToBytes(int n, byte[] array, int offset) {
		array[(offset + 1)] = (byte) (n & 0xFF);
		array[offset] = (byte) (n >> 8 & 0xFF);
	}

	public static int bytesToUshort(byte[] b) {
		return (b[1] & 0xFF | (b[0] & 0xFF) << 8);
	}

	public static int bytesToUshort(byte[] b, int offset) {
		return (b[(offset + 1)] & 0xFF | (b[offset] & 0xFF) << 8);
	}

	public static byte[] ubyteToBytes(int n) {
		byte[] b = new byte[1];
		b[0] = (byte) (n & 0xFF);
		return b;
	}

	public static void ubyteToBytes(int n, byte[] array, int offset) {
		array[0] = (byte) (n & 0xFF);
	}

	public static int bytesToUbyte(byte[] array) {
		return (array[0] & 0xFF);
	}

	public static int bytesToUbyte(byte[] array, int offset) {
		return (array[offset] & 0xFF);
	}
}
