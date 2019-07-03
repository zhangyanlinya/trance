package com.trance.empire.model;

import io.protostuff.Tag;

/**
 * 返回给客户端的结果对象
 * 
 * @author Along
 * 
 */
public class Result<T> {

	public static final int SUCCESS = 0 ;

	@Tag(1)
	private int code;
	
	@Tag(2)
	private T content;
	
	/**
	 * 返回成功结果
	 * 
	 * @param content
	 *            内容对象
	 * @return
	 */
	public static <T> Result<T> Success(T content) {
		Result<T> result = new Result<T>();
		result.setContent(content);
		return result;
	}

	/**
	 * 返回失败结果
	 * 
	 * @param code
	 *            错误码
	 * @return
	 */
	public static <T> Result<T> Error(int code) {
		Result<T> result = new Result<T>();
		result.code = code;
		return result;
	}

	/**
	 * 返回自定义结果
	 * 
	 * @param code
	 *            错误码
	 * @param content
	 *            内容对象
	 * @return
	 */
	public static <T> Result<T> ValueOf(int code, T content) {
		Result<T> result = new Result<T>();
		result.code = code;
		result.content = content;
		return result;
	}

	/**
	 * 添加返回的内容对象（用在有多个内容对象要返回的时候）
	 * 
	 * @param key
	 *            内容对象对应的key
	 * @param value
	 *            内容对象
	 * @return
	 */
	public boolean addContent(String key, Object value) {
//		if (CODE.equalsIgnoreCase(key)
//				|| CONTENT.equalsIgnoreCase(key)) {
//			return false;
//		}
//		this.values.put(key, value);
		return true;
	}

	/**
	 * 设置错误码
	 * 
	 * @param code
	 *            错误码
	 */
	private void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * 返回错误码
	 * @return
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 设置内容对象
	 * 
	 * @param content
	 *            内容对象
	 */
	private void setContent(T content) {
		this.content = content;
	}
	
	/**
	 * 返回内容对象
	 * @return
	 */
	public T getContent() {
		return content;
	}

    public Object get(String content) {
	    return null;
    }
}
