package com.trance.common.socket.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trance.common.socket.model.Response;


/**
 * 响应协议编码器
 * 
 * @author trance
 */
public class ResponseEncoder extends ProtocolEncoderAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(ResponseEncoder.class);
	
	public ResponseEncoder() {
		
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
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
			
		} else if (message instanceof Response) {			
			Response response = (Response) message;
			buffer = toIoBuffer(response);
			
		} else {
			logger.error("未能识别的响应消息！");
		}
		
		if (buffer != null) {
			out.write(buffer);
			out.flush();
		}
	}
	
	/**
	 * 响应消息转换成IoBuffer 
	 * @param response Response
	 * @return IoBuffer
	 */
	private IoBuffer toIoBuffer(Response response) {
		if (response == null) {
			return null;
		}
		
		byte[] resData = CodecHelper.encodeAndToByteArray(response);
		if (resData == null) {
			return null;
		}
		
		IoBuffer buffer = CodecHelper.body2IoBuffer(resData);
		return buffer;
	}

}
