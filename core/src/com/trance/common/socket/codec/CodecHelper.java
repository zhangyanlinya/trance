package com.trance.common.socket.codec;

import com.trance.common.socket.converter.JsonConverter;
import com.trance.common.socket.converter.ProtostuffConverter;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.common.util.GZIPUtil;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 编解码帮助类
 * 
 * @author trance
 */
public class CodecHelper {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(CodecHelper.class);
	
	/**
	 * 请求消息体的最小长度
	 */
	private static final int REQUEST_LEAST_LENGTH = 17;
	
	/**
	 * 响应消息体的最小长度
	 */
	private static final int RESPONSE_LEAST_LENGTH = 17;
	
	/**
	 * 字节数组转换成请求消息
	 * 
	 * <p>
	 * 流水号[int]|d客户端请求时间[long]|d消息对象类型[int]|压缩标识[byte]|验证码[int]|模块ID[int]|命令ID[int]|数据对象[byte[]]|
	 * 20 + 8 + 1 = 29 ;
	 * 16 + 1 = 17 ;
	 * @param data byte[]
	 * @return {@link Request}
	 */
	public static Request toRequest(byte[] data) {
		if (data == null) {
			return null;
		}
		
		int dataLength = data.length;
		//消息最小长度
		int leastLength = REQUEST_LEAST_LENGTH;
		if (dataLength < leastLength) {			
			return null;
		}
		
		IoBuffer in = IoBuffer.wrap(data);
		try {
			int sn = in.getInt();
//			long requestTime = (long) in.getDouble();
//			int format = in.getInt();
			byte compress = in.get();
			int authCode = in.getInt();
			int module = in.getInt();
			int cmd = in.getInt();
			
			byte[] valueBytes = null;
			if (dataLength > leastLength) {
				valueBytes = new byte[dataLength - leastLength];
				in.get(valueBytes);
			}
			
			Request request = new Request(sn, module, cmd,
					compress == (byte) 1, authCode, valueBytes);
			
			return request;
		} catch (Exception ex) {
			logger.error("字节数组转换成请求消息异常！", ex);
			
		} finally {
			in.clear();
			in = null;
		}
		
		return null;
	}
	
	/**
	 * 请求消息转换成字节数组
	 * @param request Request
	 * @return byte[]
	 */
	public static byte[] toByteArray(Request request) {
		if (request == null) {
			return null;
		}
		
		int capacity = REQUEST_LEAST_LENGTH;
		if (request.getValueBytes() != null) {
			capacity += request.getValueBytes().length;
		}
	
		IoBuffer buf = IoBuffer.allocate(capacity);
		buf.setAutoExpand(true);		
		try {
			buf.putInt(request.getModule());
			buf.putInt(request.getCmd());
			if (request.getValueBytes() != null) {
				buf.put(request.getValueBytes());
			}
			byte[] bytes = new byte[buf.position()];
			buf.rewind();
			buf.get(bytes);						
//			int authCode = HashAlgorithms.fnvHash(bytes);
			
			buf.clear();
			buf.putInt(request.getSn());
			buf.put((byte) (request.isCompressed() ? 1 : 0));
			buf.putInt(0);
			buf.putInt(request.getModule());
			buf.putInt(request.getCmd());			
			if (request.getValueBytes() != null) {
				buf.put(request.getValueBytes());
			}
			
			byte[] data = new byte[buf.position()];
			buf.rewind();
			buf.get(data);			
			return data;
		} catch (Exception ex) {				
			logger.error("请求消息转换成字节数组异常！", ex);
			
		} finally {
			buf.clear();
			buf = null;
		}
		return null;
	}
	
	/**
	 * 响应消息转换成字节数组
	 * 
	 * <p>
	 * 流水号[int]| d接收请求时间[long]|d响应时间[long]|d消息对象类型[int]|压缩标识[byte]|模块ID[int]|命令ID[int]|响应状态[int]|数据对象[byte[]]|
	 * 以前是 20 + 16 + 1 = 37  
	 * 现在是 16 + 1 = 17
	 * 
	 * @param response Response
	 * @return byte[]
	 */
	public static byte[] toByteArray(Response response) {
		if (response == null) {
			return null;
		}
		
		int capacity = RESPONSE_LEAST_LENGTH;
		if (response.getValueBytes() != null) {
			capacity += response.getValueBytes().length;
		}
		
		IoBuffer buf = IoBuffer.allocate(capacity);
		buf.setAutoExpand(true);		
		try {
			buf.putInt(response.getSn());
			buf.put((byte) (response.isCompressed() ? 1 : 0));
			buf.putInt(response.getModule());
			buf.putInt(response.getCmd());
			buf.putInt(response.getStatus().getValue());
			
			if (response.getValueBytes() != null) {
				buf.put(response.getValueBytes());
			}
			
			byte[] data = new byte[buf.position()];
			buf.rewind();
			buf.get(data);			
			return data;
		} catch (Exception ex) {				
			logger.error("响应消息转换成字节数组异常！", ex);
			
		} finally {
			buf.clear();
			buf = null;
		}
		return null;
	}
	
	/**
	 * 响应消息编码和转换成字节数组
	 * @param response Response
	 * @return byte[]
	 */
	public static byte[] encodeAndToByteArray(Response response) {
		if (response == null) {
			return null;
		}

		//对象转换
		byte[] data = ProtostuffConverter.encode(response.getValue());
		response.setValueBytes(data);
						
//		//需要压缩 
//		if (response.isCompressed()) {
//			//TODO
//		}
		
		//需要压缩 
		if (data != null && data.length > 512) {
//			logger.error("压缩前 " + data.length);
			response.setValueBytes(GZIPUtil.compress(data));
//			logger.error("压缩后 " + response.getValueBytes().length);
			response.setCompressed(true);
		}
				
//		response.setResponseTime(System.currentTimeMillis());
		byte[] resData = toByteArray(response);
		return resData;
	}
	
	/**
	 * 字节数组转换成响应消息
	 * @param data 字节数组
	 * @return Response
	 */
	public static Response toResponse(byte[] data) {
		if (data == null) {
			return null;
		}
		
		int dataLength = data.length;
		//消息最小长度
		int leastLength = RESPONSE_LEAST_LENGTH;
		if (dataLength < leastLength) {			
			return null;
		}
		
		IoBuffer in = IoBuffer.wrap(data);
		try {
			int sn = in.getInt();
			byte compress = in.get();
			int module = in.getInt();
			int cmd = in.getInt();
			int status = in.getInt();
			
			byte[] valueBytes = null;
			if (dataLength > leastLength) {
				valueBytes = new byte[dataLength - leastLength];
				in.get(valueBytes);
			}
			
			Response response = Response.valueOf(sn, module, cmd, compress == (byte) 1, 
													valueBytes, ResponseStatus.valueOf(status));
			
			return response;
		} catch (Exception ex) {
			logger.error("字节数组转换成响应消息异常！", ex);
			
		} finally {
			in.clear();
			in = null;
		}
		
		return null;
	}
	
	/**
	 * 消息体字节数组封装成IoBuffer
	 * @return IoBuffer
	 */
	public static IoBuffer body2IoBuffer(byte[] bodyBytes) {
		if (bodyBytes == null) {
			return null;
		}
		
//		int capacity = bodyBytes.length + PACKAGE_HEADER_LENGTH;
		int capacity = bodyBytes.length + 4;
		IoBuffer buffer = IoBuffer.allocate(capacity);
		buffer.setAutoExpand(true);
//		buffer.putInt(PACKAGE_HEADER_ID);
		buffer.putInt(bodyBytes.length);
		buffer.put(bodyBytes);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * 字节数组转换成IoBuffer
	 * @param data 字节数组
	 * @return IoBuffer
	 */
	public static IoBuffer toIoBuffer(byte[] data) {
		if (data == null) {
			return null;
		}
		
		IoBuffer buffer = IoBuffer.allocate(data.length);
		buffer.setAutoExpand(true); 
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
