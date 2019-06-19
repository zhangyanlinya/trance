package com.trance.common.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
 
 
/**
 * 
 * @author trance
 * 
 */
public class GZIPUtil {
	

    /**
     * 字符串压缩为GZIP字节数组
     */
    public static byte[] compress(byte[] bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
 
    /**
     * GZIP解压缩
     * 
     * @param bytes
     * @return
     */
    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return out.toByteArray();
    }
}

