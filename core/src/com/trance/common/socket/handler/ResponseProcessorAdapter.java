package com.trance.common.socket.handler;

import org.apache.mina.core.session.IoSession;

import com.trance.common.socket.model.Response;

/**
 * 响应消息处理器适配器
 * 
 * @author trance
 * @param <T>
 */
public class ResponseProcessorAdapter<T> implements ResponseProcessor {
	
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
	
	public ResponseProcessorAdapter() {
		
	}
	
	public ResponseProcessorAdapter(byte module, byte cmd) {
		this(module, cmd, null);
	}
	
	public ResponseProcessorAdapter(byte module, byte cmd, Class<T> type) {
		super();
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
	public void callback(IoSession session, Response response) {
		
	}
}
