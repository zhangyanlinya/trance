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
	 * 请求对象信息字节数组
	 */
	private byte[] valueBytes;
	
	/**
	 * 请求对象信息
	 */
	private Object value;

	/**
	 * 响应标识
	 */
	private ResponseStatus status = ResponseStatus.SUCCESS;
	
	public Response() {
		
	}

	public Response(int sn, int module, int cmd) {
		this.cmd = cmd;
		this.module = module;
		this.sn = sn;
	}
	
	public static Response valueOf(int sn, int module, int cmd, Object value) {
		Response p = new Response(sn, module, cmd);
		p.setValue(value);
		return p;
	}

	public static Response valueOf(int sn, int module, int cmd,
			boolean isCompressed, byte[] valueBytes, ResponseStatus status) {
		Response res = new Response();
		res.sn = sn;
		res.module = module;
		res.cmd = cmd;
//		res.format = format;
		res.isCompressed = isCompressed;
		res.valueBytes = valueBytes;
//		res.receiveTime = receiveTime;
//		res.responseTime = responseTime;
		res.status = status;
		return res;
	}
	
	/**
	 * 生成Response
	 * @param request Request
	 * @return Response
	 */
	public static Response wrap(Request request) {
		Response response = new Response();
		response.setSn(request.getSn());
		response.setModule(request.getModule());
		response.setCmd(request.getCmd());
//		response.setFormat(request.getFormat());
//		response.setReceiveTime(request.getReceiveTime());
		return response;
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

	public boolean isCompressed() {
		return isCompressed;
	}

	public void setCompressed(boolean isCompressed) {
		this.isCompressed = isCompressed;
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

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
}
