package com.trance.common.util;

import java.nio.charset.Charset;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffUtil {

	private final static Charset charset = Charset.forName("utf8");
	
	 /**
     * 线程局部变量
     */
    private static final ThreadLocal<LinkedBuffer> BUFFERS = new ThreadLocal<LinkedBuffer>();


	/**
	 * 序列化
	 *
	 * @param obj
	 *            序列化对象
	 * @return 序列化后的byte[]值
	 */
	public static <T> byte[] toPbBytes(T obj) {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) obj.getClass();
		LinkedBuffer buffer = BUFFERS.get();
        if (buffer == null) {//存储buffer到线程局部变量中，避免每次序列化操作都分配内存提高序列化性能
            buffer = LinkedBuffer.allocate(512);
            BUFFERS.set(buffer);
        }
		try {
			Schema<T> schema = RuntimeSchema.getSchema(clazz);
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			buffer.clear();
		}
	}

	public static String toPBString(Object obj) {
		byte[] bytes = toPbBytes(obj);
		return new String(bytes, charset);
	}

	/**
	 * 反序列化
	 *
	 * @param data
	 *            序列化后的byte[]值
	 * @param clazz
	 *            反序列化后的对象
	 * @return 返回的对象
	 */
	public static <T> T parseObject(byte[] data, Class<T> clazz) {
		try {
			Schema<T> schema = RuntimeSchema.getSchema(clazz);
			T obj = schema.newMessage();
			ProtostuffIOUtil.mergeFrom(data, obj, schema);
			return obj;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
}