package com.jerryio.spoon.desktop.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static final String OsName = System.getProperty("os.name");
    public static final boolean IsWindows = OsName.startsWith("Windows");
    public static final boolean IsMac = OsName.startsWith("Mac") || OsName.startsWith("Darwin");

    public static byte[] getMD5Checksum(byte[] a, byte[] b) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(a);
        md.update(b);
        return md.digest();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static String getMyIP() {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("google.com", 80));
            String rtn = socket.getLocalAddress().getHostAddress();
            socket.close();
            return rtn;
        } catch (IOException e) {
            return "(error)";
        }
    }
}
