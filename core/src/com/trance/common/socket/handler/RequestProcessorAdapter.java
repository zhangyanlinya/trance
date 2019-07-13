package com.trance.common.socket.handler;

import org.apache.mina.core.session.IoSession;

import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;


/**
 * 请求处理器适配器
 * 
 * @author trance
 */
public class RequestProcessorAdapter implements RequestProcessor {
	
	/**
	 * 模块ID
	 */
	private byte module = 0;
	
	/**
	 * 命令ID
	 */
	private byte cmd = 0;
	
	/**
	 * 请求参数类型
	 */
	private Class<?> type = null;
	
	public RequestProcessorAdapter() {
		
	}
	
	public RequestProcessorAdapter(byte module, byte cmd) {
		this(module, cmd, null);
	}
	
	public RequestProcessorAdapter(byte module, byte cmd, Class<?> type) {
		this.module = module;
		this.cmd = cmd;
		this.type = type;
	}


	@Override
	public byte getModule() {
		return this.module;
	}

	@Override
	public byte getCmd() {
		return this.cmd;
	}

	@Override
	public Class<?> getType() {
		return this.type;
	}

	@Override
	public void process(IoSession session, Request request, Response response) {
		
	}
}
