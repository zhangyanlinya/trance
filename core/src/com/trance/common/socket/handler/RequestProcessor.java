package com.trance.common.socket.handler;

import org.apache.mina.core.session.IoSession;

import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;



/**
 * 请求处理器
 * 
 * @author trance
 */
public interface RequestProcessor {
	
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
	 * 对象类型, null-业务自己做转换
	 * @param <T>
	 * @param <T>
	 * @return Object
	 */
	 Class<?> getType();
	
	/**
	 * 调用请求具体处理方法
	 * @param session IoSession
	 * @param request 请求消息
	 * @param response 响应消息
	 */
	void process(IoSession session, Request request, Response response);

}
