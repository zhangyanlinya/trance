package com.trance.common.socket.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.trance.common.socket.model.Response;


/**
 * 响应消息解码器
 * 
 * @author trance
 */
public class ResponseDecoder extends CumulativeProtocolDecoder {
	
	/**
	 * <pre>
	 * 请求消息通讯协议：消息头 + 消息体
	 * 消息头:	消息头标识(int)+消息体长度(int)
	 * 消息体：	{@link Response}
	 * </pre>
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
//		if (!session.isConnected() || session.isClosing()) {
//			return false;
//		}
		
		if (in.remaining() < 2) {
			return false;// 继续接收数据，以待数据完整
		}

		in.mark();// 标记当前位置，以便reset
		int size = in.getShort();// 读取4字节判断消息长度

		int remain = in.remaining();
		if (remain < size) {// 如果消息内容不够，则重置，相当于不读取size
			in.reset();
			return false;// 接收新数据，以拼凑成完整数据
		} 
		
		//====从方法中移过来===
		short sn = in.getShort();
		byte compress = in.get();
		byte module = in.get();
		byte cmd = in.get();
		byte status = in.get();
		int datalen = size - 6;
		byte[] valueBytes = null;
		if (datalen > 0) {
			valueBytes = new byte[datalen];
			in.get(valueBytes);
		}
		Response response = Response.valueOf(sn, module, cmd, compress, valueBytes, status);
		out.write(response);
		//====从方法中移过来===
		
//		
//		// 读取指定长度的字节数
//		byte[] bytes = new byte[size];
//		in.get(bytes);
//
//		Response response = CodecHelper.toResponse(bytes);
//		if (response != null) {
//			out.write(response);
//		}

		if (in.remaining() > 0) {
			return true;// 如果读取内容后还粘了包，就让父类再给俺 一次，进行下一次解析
		} 
		
		return false;
		
		
		
//		
//		
//		CodecContext ctx = getContext(session);
//		
//		//新包开始
//		if (ctx.getState() == CodecState.READY) {
//			while (true) {
//				if (in.remaining() < PACKAGE_HEADER_LENGTH) {
//					return false;
//				}
//				
//				in.mark();				
//				if(in.getInt() == PACKAGE_HEADER_ID) {
//					// 已检测到数据头
//					break;
//				} else {
//					// 只略过一个字节
//					in.reset();
//					in.get();
//				}
//			}
//			
//			// 消息体长度
//			int length = in.getInt();
//			if (length <= 0) {
//				logger.error("无效的消息体长度：{}", length);
//				return true;
//			}
//			
//			ctx.setBytesNeeded(length);
//			ctx.setState(CodecState.WAITTING_FOR_DATA);
//		}
//		
//		if (in.remaining() < ctx.getBytesNeeded()) {
//			return false;
//		}
//		
//		byte[] data = new byte[ctx.getBytesNeeded()];
//		in.get(data);
//		
//		Response response = CodecHelper.toResponse(data);
//		if (response != null) {
//			out.write(response);				
//		}  
//		
//		ctx.setBytesNeeded(0);
//		ctx.setState(CodecState.READY);
//		return true;
	}
	
//	/**
//	 * 响应消息解码上下文
//	 */
//	private final AttributeKey RESPONSE_DECODER_CONTEXT = new AttributeKey(ResponseDecoder.class, "context");
//	
//	/**
//	 * 获取上下文
//	 * @param session IoSession
//	 * @return Context
//	 */
//	private CodecContext getContext(IoSession session) {
//		CodecContext ctx = (CodecContext) session.getAttribute(RESPONSE_DECODER_CONTEXT);
//		if (ctx == null) {
//			ctx = new CodecContext();
//			session.setAttribute(RESPONSE_DECODER_CONTEXT, ctx);
//		}
//		
//		return ctx;
//	}

}
