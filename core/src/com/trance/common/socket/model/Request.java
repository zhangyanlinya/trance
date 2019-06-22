package com.trance.common.socket.model;

import java.io.Serializable;

import com.trance.common.socket.codec.CodecFormat;


/**
 * 请求消息类
 * 
 * @author trance
 */
public class Request implements Serializable {
	
	private static final long serialVersionUID = 4976371020835423388L;

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
	 * 编解码格式
	 */
	private CodecFormat format = CodecFormat.JSON;
	
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

	public CodecFormat getFormat() {
		return format;
	}

	public void setFormat(CodecFormat format) {
		this.format = format;
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

//	public long getRequestTime() {
//		return requestTime;
//	}
//
//	public void setRequestTime(long requestTime) {
//		this.requestTime = requestTime;
//	}

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
