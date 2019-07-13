package com.trance.common.socket.model;

/**
 * 响应消息状态
 * 
 * @author trance
 */
public enum ResponseStatus {
	
	/**
	 * 未知错误
	 */
	ERROR((byte)-1),
	
	/**
	 * 操作成功
	 */
	SUCCESS((byte)0),
	
	/**
	 * 协议解析错误
	 */
	RESOLVE_ERROR((byte)2),
	
	/**
	 * 没有权限
	 */
	NO_RIGHT((byte)3),
	
	/**
	 * 验证码错
	 */
	AUTH_CODE_ERROR((byte)4);
	
	/**
	 * 状态码
	 */
	final byte value;
	
	private ResponseStatus(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	/**
	 * 根据状态值取得 ResponseStatus
	 * @param value 状态值
	 * @return ResponseStatus
	 */
	public static ResponseStatus valueOf(byte value) {
		for (ResponseStatus status: ResponseStatus.values()) {
			if (status.getValue() == value) {
				return status;
			}
		}
		return null;
	}
}
