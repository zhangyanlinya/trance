package com.trance.common.socket.handler;

import com.trance.common.socket.model.Response;

import org.apache.mina.core.session.IoSession;

/**
 * 响应消息处理器适配器
 * 
 * @author zhangyl
 */
public class ResponseProcessorAdapter implements ResponseProcessor {
	
	/**
	 * 模块ID
	 */
	private int module = 0;
	
	/**
	 * 命令ID
	 */
	private int cmd = 0;
	
	/**
	 * 请求参数类型
	 */
	private Class<?> type;
	
	public ResponseProcessorAdapter() {
		
	}
	
	public ResponseProcessorAdapter(int module, int cmd) {
		this(module, cmd, null);
	}
	
	public ResponseProcessorAdapter(int module, int cmd, Class<?>  type) {
		super();
		this.module = module;
		this.cmd = cmd;
		this.type = type;
	}

	@Override
	public int getModule() {
		return this.module;
	}

	@Override
	public int getCmd() {
		return this.cmd;
	}

	@Override
	public Class<?>  getType() {
		return this.type;
	}

	@Override
	public void callback(IoSession session, Response response) {
		
	}

}
