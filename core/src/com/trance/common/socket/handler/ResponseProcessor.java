package com.trance.common.socket.handler;

import org.apache.mina.core.session.IoSession;

import com.trance.common.socket.model.Response;


/**
 * 响应消息处理器
 * 
 * @author trance
 */
public interface ResponseProcessor {
	
	/**
	 * 获取模块号
	 * @return
	 */
	byte getModule();
	
	/**
	 * 获取命令号
	 * @return
	 */
	byte getCmd();
	
	/**
	 * 响应消息的对象类型, null-业务自己做转换
	 * @param <T>
	 * @return Class<?> 基本类型暂时只技能 Integer, Long , String 
	 */
	Class<?> getType();
	
	/**
	 * 响应消息回调
	 * @param session {@link IoSession}
	 * @param response {@link Response}
	 */
	void callback(IoSession session, Response response);

}
