package com.trance.common.socket.converter;

import com.trance.common.util.ProtostuffUtil;

public class ProtostuffConverter{
	
	public static byte[] encode(Object obj) {
		if (obj == null) {
			return null;
		}

		return ProtostuffUtil.toPbBytes(obj);
	}

	public static Object decode(byte[] data, Object type) {
		if (data == null || type == null) {
			return null;
		}
		
		return ProtostuffUtil.parseObject(data, (Class<?>)type);
	}
}
