package com.trance.common.socket.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.trance.common.socket.model.Request;



/**
 * 请求消息解码器
 * 
 * @author trance
 */
public class RequestDecoder extends CumulativeProtocolDecoder {

//	/**
//	 * logger
//	 */
//	private static final Logger logger = LoggerFactory.getLogger(RequestDecoder.class);
	
//	
//	/**
//	 * 上下文
//	 */
//	private final AttributeKey DECODER_CONTEXT  = new AttributeKey(RequestDecoder.class, "context");
	
	
	/**
	 * <pre>
	 * 请求消息通讯协议：消息头 + 消息体
	 * 消息头:	消息头标识(int)+消息体长度(int)
	 * 消息体：	{@link Request}
	 * </pre>
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
//		if (!session.isConnected() || session.isClosing()) {
//			return false;
//		}
//		System.out.println("收到客户端发来的字节数组了");

		if (in.remaining() < 2) {
			return false;// 继续接收数据，以待数据完整
		}

		in.mark();// 标记当前位置，以便reset
		int size = in.getShort();// 读取2字节判断消息长度

		int remain = in.remaining();
		if (remain < size) {// 如果消息内容不够，则重置，相当于不读取size
			in.reset();
			return false;// 接收新数据，以拼凑成完整数据
		} 
		
		//====从方法中移过来===
		short sn = in.getShort();
		byte authCode = in.get();
		byte module = in.get();
		byte cmd = in.get();
		
		int datalen = size - 5;
		byte[] valueBytes = null;
		if (datalen > 0) {
			valueBytes = new byte[datalen];
			in.get(valueBytes);
		}

		Request request = Request.valueOf(sn, module, cmd, authCode, valueBytes);
		out.write(request);
		//====从方法中移过来===
		
//		// 读取指定长度的字节数
//		byte[] bytes = new byte[size];
//		in.get(bytes);
//
//		Request request = CodecHelper.toRequest(bytes);
//		if (request != null) {
//			out.write(request);
//			System.out.println("解析成功");
//		}

		if (in.remaining() > 0) {
			return true;// 如果读取内容后还粘了包，就让父类再给俺 一次，进行下一次解析
		}
		
		return false;
		
		
//		
//		CodecContext ctx = getContext(session);
//		
//		//新包开始
//		if (ctx.getState() == CodecState.READY) {
//			//flash 策略请求
////			boolean continued = doIfFlashPolicyRequest(session, in, out);
////			if (!continued) {
////				return false;
////			}
//			
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
//			if (length <= 0 || length > PACKAGE_BODY_MAX_LENGTH) {
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
//		Request request = CodecHelper.toRequest(data);
//		if (request != null) {
//			out.write(request);				
//		}  
//		
//		ctx.setBytesNeeded(0);
//		ctx.setState(CodecState.READY);
//		return true;
	}
	
//	/**
//	 * 获取上下文
//	 * @param session IoSession
//	 * @return Context
//	 */
//	@SuppressWarnings("unused")
//	private CodecContext getContext(IoSession session) {
//		CodecContext ctx = (CodecContext) session.getAttribute(DECODER_CONTEXT);
//		if (ctx == null) {
//			ctx = new CodecContext();
//			session.setAttribute(DECODER_CONTEXT, ctx);
//		}
//		
//		return ctx;
//	}
	
}
