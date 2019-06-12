package com.trance.common.socket.converter;

import com.alibaba.fastjson.JSON;
import com.trance.common.socket.codec.CodecFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;


/**
 * json转换器
 * 
 * @author zhangyl
 */
public class JsonConverter implements ObjectConverter {


	private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);

	@Override
	public CodecFormat getFormat() {
		return CodecFormat.JSON;
	}

	@Override
	public byte[] encode(Object obj) {
		if (obj == null) {
			return null;
		}

		return JSON.toJSONBytes(obj);
	}

	@Override
	public Object decode(byte[] data, Object type) {
		if (data == null) {
			return null;
		}
		if(type.equals(void.class)) {
			return null;
		}

		if (type instanceof Type) {
			return JSON.parseObject(data, (Type)type);
		} else {
			logger.error("不支持的类型参数[{}]", type);
		}

		return null;
	}

}
