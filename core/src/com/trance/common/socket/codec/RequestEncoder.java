package com.trance.common.socket.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trance.common.socket.converter.JsonConverter;
import com.trance.common.socket.converter.ProtostuffConverter;
import com.trance.common.socket.model.Request;


/**
 * 请求消息编码器
 * 
 * @author trance
 */
public class RequestEncoder extends ProtocolEncoderAdapter {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(RequestEncoder.class);
	
	public RequestEncoder() {
		
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out){
		if (message == null) {
			return;
		}
		
		if(!session.isConnected() || session.isClosing()) {
			return;
		}
		
		IoBuffer buffer = null;
		if (message instanceof IoBuffer) {
			buffer = (IoBuffer) message;
			
		} else if (message instanceof byte[]) {
			byte[] data = (byte[]) message;			
			buffer = CodecHelper.toIoBuffer(data);
			
		} else if (message instanceof Request) {			
			Request request = (Request) message;
			buffer = toIoBuffer(request);
			
		} else {
			logger.error("未能识别的请求消息！");
		}
		
		if (buffer != null) {
			out.write(buffer);
			out.flush();
		}

	}
	
	/**
	 * 请求消息转换成IoBuffer 
	 * @param request Request
	 * @return IoBuffer
	 */
	private IoBuffer toIoBuffer(Request request) {
		if (request == null) {
			return null;
		}
		
		//对象转换
		byte[] data = ProtostuffConverter.encode(request.getValue());
		request.setValueBytes(data);
				
//		//需要压缩 
//		if (data != null && data.length > 128) {
//			logger.error("压缩前 " + data.length);
//			request.setValueBytes(GZIPUtil.compress(data));
//			logger.error("压缩后 " + request.getValueBytes().length);
//			request.setCompressed(true);
//		}
		
		byte[] reqData = CodecHelper.toByteArray(request);
		if (reqData == null) {
			return null;
		}
		
		IoBuffer buffer = CodecHelper.body2IoBuffer(reqData);
		return buffer;
	}
}
