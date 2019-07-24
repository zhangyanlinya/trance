package com.trance.common.socket.model;

/**
 * 响应消息类
 * 
 * @author trance
 */
public class Response {

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
	private byte compressed;

	/**
	 * 请求对象信息字节数组
	 */
	private byte[] valueBytes;

	/**
	 * 请求对象信息
	 */
	private Object value;

	/**
	 * 响应标识{@link ResponseStatus}
	 */
	private byte status;
	

	public static Response valueOf(short sn, byte module, byte cmd) {
		Response p = new Response();
		p.cmd = cmd;
		p.module = module;
		p.sn = sn;
		return p;
	}
	
	public static Response valueOf(short sn, byte module, byte cmd, Object value) {
		Response p = new Response();
		p.setValue(value);
		return p;
	}

	public static Response valueOf(short sn, byte module, byte cmd,
			byte compressed, byte[] valueBytes, byte status) {
		Response res = new Response();
		res.sn = sn;
		res.module = module;
		res.cmd = cmd;
		res.compressed = compressed;
		res.valueBytes = valueBytes;
		res.status = status;
		return res;
	}
	
	public void free() {
		sn = -1;
		module = 0;
		cmd = 0;
		compressed = 0;
		valueBytes = null;
		status = 0;
		value = 0;
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

	public byte getCompressed() {
		return compressed;
	}

	public void setCompressed(byte compressed) {
		this.compressed = compressed;
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

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
}
