package com.trance.common.socket.converter;

import java.nio.charset.Charset;

import com.trance.common.util.ByteUtil;
import com.trance.common.util.ProtostuffUtil;

public class ProtostuffConverter {

	private final static Charset charset = Charset.forName("UTF-8");

	public static byte[] encode(Object obj) {
		if (obj == null) {
			return null;
		}
		
		if (obj instanceof Integer) {
			return ByteUtil.intToByteArr((Integer)obj);
		} else if (obj instanceof Long) {
			return ByteUtil.longToByteArr((Long)obj);
		} else if(obj instanceof String) {
			return ((String) obj).getBytes(charset);
		}

		return ProtostuffUtil.toPbBytes(obj);
	}

	public static Object decode(byte[] bytes, Class<?> clazz) {
		if (bytes == null) {
			return null;
		}

		if (clazz == Integer.class) {
			return ByteUtil.byteArrToInt(bytes);
		} else if (clazz == Long.class) {
			return ByteUtil.byteArrToLong(bytes);
		} else if(clazz == String.class) {
			return new String(bytes, charset);
		}
		return ProtostuffUtil.parseObject(bytes, clazz);
	}
}
