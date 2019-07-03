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
	private int sn = -1;

	/**
	 * 模块ID
	 */
	private int module;

	/**
	 * 命令ID
	 */
	private int cmd;
	
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
	 * 接收请求时间(ms)
	 */
	private long receiveTime = System.currentTimeMillis();

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

	public Request(int sn, int module, int cmd, boolean isCompressed, int authCode, byte[] valueBytes) {
		this.sn = sn;
		this.module = module;
		this.cmd = cmd;
		this.isCompressed = isCompressed;
		this.authCode = authCode;
		this.valueBytes = valueBytes;
	}

	public static Request valueOf(int sn, int module, int cmd, Object value) {
		Request q = new Request();
		q.sn = sn;
		q.module = module;
		q.cmd = cmd;
		q.value = value;
		return q;
	}
	
	public static Request valueOf(int module, int cmd, Object value) {
		return Request.valueOf(-1, module, cmd, value);
	}

	public void await(long timeout, TimeUnit unit) throws InterruptedException {
		this.latch.await(timeout, unit);
	}

	public void release(){
		this.latch.countDown();
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public int getModule() {
		return module;
	}

	public void setModule(int module) {
		this.module = module;
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
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

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
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
//		sb.append("[").append(DateUtil.date2String(new Date(requestTime), DatePattern.PATTERN_NORMAL)).append("] ");
//		sb.append("receiveTime[").append(DateUtil.date2String(new Date(receiveTime), DatePattern.PATTERN_NORMAL)).append("] ");
		return sb.toString();
	}
	
	
}
