package com.trance.common.socket.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 响应消息处理器集合
 * 
 * @author trance
 */
public class ResponseProcessors {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ResponseProcessors.class);
	
	/**
	 * 响应消息处理器集合 {module： {cmd：ResponseProcessor}}
	 */
	private final ConcurrentMap<Byte, ConcurrentMap<Byte, ResponseProcessor>> processorMap = new ConcurrentHashMap<Byte, ConcurrentMap<Byte, ResponseProcessor>>();
	
	/**
	 * 注册响应消息处理器
	 * @param processor ResponseProcessor
	 */
	public void registerProcessor(ResponseProcessor processor) {
		if (processor == null) {
			return;
		}
		
		byte module = processor.getModule();
		ConcurrentMap<Byte, ResponseProcessor> cmds = this.processorMap.get(module);
		if (cmds == null) {
			cmds = new ConcurrentHashMap<Byte, ResponseProcessor>();
			ConcurrentMap<Byte, ResponseProcessor> existsCmds = this.processorMap.putIfAbsent(module, cmds);
			if (existsCmds != null) {
				cmds = existsCmds;
			}
		}
		
		int cmd = processor.getCmd();
		ResponseProcessor existsProcess = cmds.put(processor.getCmd(), processor);
		if (existsProcess != null) {
			logger.error("响应消息处理器[module: {}, cmd: {}]被覆盖！", new Object[] {module, cmd});
		}		
	}
	
	/**
	 * 取得响应消息处理器
	 * @param module 模块ID
	 * @param cmd 命令ID
	 * @return ResponseProcessor
	 */
	public ResponseProcessor getProcessor(byte module, byte cmd) {
		ResponseProcessor processor = null;
		ConcurrentMap<Byte, ResponseProcessor> cmds = this.processorMap.get(module);
		if (cmds != null) {
			processor = cmds.get(cmd);
		}
		return processor;
	}
	
	/**
	 * 取得响应消息处理器集合
	 * @return List<ResponseProcessor>ResponseProcessors.java
	 */
	public List<ResponseProcessor> getResponseProcessorList() {
		int capacity = this.processorMap.size() * 5;
		List<ResponseProcessor> result = new ArrayList<ResponseProcessor> (capacity);
		
		for (ConcurrentMap<Byte, ResponseProcessor> cmdProcessor: this.processorMap.values()) {
			result.addAll(cmdProcessor.values());
		}
		
		return result;
	}
	
	/**
	 * 清空响应消息处理器
	 */
	public void clear() {
		this.processorMap.clear();
	}
}
