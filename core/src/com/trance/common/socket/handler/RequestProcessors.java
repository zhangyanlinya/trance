package com.trance.common.socket.handler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 请求处理器集合
 * 
 * @author trance
 */
public class RequestProcessors {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(RequestProcessors.class);
	
	/**
	 * 请求处理器集合 {module： {cmd：RequestProcessor}}
	 */
	private final ConcurrentMap<Byte, ConcurrentMap<Byte, RequestProcessor>> processorMap = new ConcurrentHashMap<Byte, ConcurrentMap<Byte, RequestProcessor>>();
	
	
	public RequestProcessors() {
		
	}
	
	/**
	 * 注册请求处理器
	 * @param processor RequestProcessor
	 */
	public void registerProcessor(RequestProcessor processor) {
		if (processor == null) {
			return;
		}
		
		byte module = processor.getModule();
		ConcurrentMap<Byte, RequestProcessor> cmds = this.processorMap.get(module);
		if (cmds == null) {
			cmds = new ConcurrentHashMap<Byte, RequestProcessor>();
			ConcurrentMap<Byte, RequestProcessor> existsCmds = this.processorMap.putIfAbsent(module, cmds);
			if (existsCmds != null) {
				cmds = existsCmds;
			}
		}
		
		byte cmd = processor.getCmd();
		RequestProcessor existsProcess = cmds.put(processor.getCmd(), processor);
		if (existsProcess != null) {
			logger.error("请求处理器[module: {}, cmd: {}]被覆盖！", new Object[] {module, cmd});
		}		
	}
	
	/**
	 * 取得请求处理器
	 * @param module 模块ID
	 * @param cmd 命令ID
	 * @return RequestProcessor
	 */
	public RequestProcessor getProcessor(byte module, byte cmd) {
		RequestProcessor processor = null;
		ConcurrentMap<Byte, RequestProcessor> cmds = this.processorMap.get(module);
		if (cmds != null) {
			processor = cmds.get(cmd);
		}
		return processor;
	}
	
	
}
