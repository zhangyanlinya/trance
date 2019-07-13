package com.trance.common.socket.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 请求消息类
 * 
 * @author trance
 */
public class Request {
	
	/**
	 * 流水号
	 */
	private short sn = -1;

	/**
	 * 模块ID
	 */
	private byte module;

	/**
	 * 命令ID
	 */
	private byte cmd;
	
	/**
	 * 是否压缩
	 */
	private boolean isCompressed = false;
	
	/**
	 * 验证码
	 */
	private int authCode;
	
	/**
	 * 请求对象信息字节数组
	 */
	private byte[] valueBytes;
	
	/**
	 * 请求对象信息
	 */
	private Object value;
	
	/**
	 * CountDownLatch
	 */
	private CountDownLatch latch = new CountDownLatch(1);
	
	/**
	 * 响应消息 {@link Response}
	 */
	private Response response = null;
	
	
	public Request() {
		
	}

	public static Request valueOf(short sn, byte module, byte cmd, boolean isCompressed, int authCode, byte[] valueBytes) {
		Request q = new Request();
		q.sn = sn;
		q.module = module;
		q.cmd = cmd;
		q.isCompressed = isCompressed;
		q.authCode = authCode;
		q.valueBytes = valueBytes;
		return q;
	}

	public static Request valueOf(short sn, byte module, byte cmd, Object value) {
		Request q = new Request();
		q.sn = sn;
		q.module = module;
		q.cmd = cmd;
		q.value = value;
		return q;
	}
	
	public static Request valueOf(byte module, byte cmd, Object value) {
		return Request.valueOf((short)-1, module, cmd, value);
	}
	
	public void free() {
		sn = -1;
		module = 0;
		cmd = 0;
		value = 0;
		isCompressed = false;
		valueBytes = null;
		authCode = 0;
		latch = new CountDownLatch(1);
		response = null;
	}
	
	public void await(long timeout, TimeUnit unit) throws InterruptedException {
		this.latch.await(timeout, unit);
	}
	
	public void release(){
		this.latch.countDown();
	}

	public short getSn() {
		return sn;
	}

	public void setSn(short sn) {
		this.sn = sn;
	}

	public byte getModule() {
		return module;
	}

	public void setModule(byte module) {
		this.module = module;
	}

	public byte getCmd() {
		return cmd;
	}

	public void setCmd(byte cmd) {
		this.cmd = cmd;
	}

	public byte[] getValueBytes() {
		return valueBytes;
	}

	public void setValueBytes(byte[] valueBytes) {
		this.valueBytes = valueBytes;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isCompressed() {
		return isCompressed;
	}

	public void setCompressed(boolean isCompressed) {
		this.isCompressed = isCompressed;
	}

	public int getAuthCode() {
		return authCode;
	}

	public void setAuthCode(int authCode) {
		this.authCode = authCode;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("sn[").append(sn).append("] ");
		sb.append("module[").append(module).append("] ");
		sb.append("cmd[").append(cmd).append("] ");
		return sb.toString();
	}
}
