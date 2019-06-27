package com.trance.common.socket;

import com.trance.common.socket.model.Response;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 客户机请求上下文
 * 
 * @author zhangyl
 */
public class ClientContext {
	
	/**
	 * 内部序列号
	 */
	private int sn;
	
	/**
	 * 响应消息 {@link Response}
	 */
	private Response response = null;
	
	/**
	 * CountDownLatch
	 */
	private CountDownLatch latch = new CountDownLatch(1);
	
	/**
	 * 是否同步返回
	 */
	private boolean sync = true;
	
	public ClientContext() {
		
	}

	public static ClientContext valueOf(int sn, boolean sync) {
		ClientContext ctx = new ClientContext();
		ctx.sn = sn;
		ctx.sync = sync;
		return ctx;
	}
	
	public void await(long timeout, TimeUnit unit) throws InterruptedException {
		this.latch.await(timeout, unit);
	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

	public void release(){
		this.latch.countDown();
	}

	public long getCount(){
		return this.latch.getCount();
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public int getSn() {
		return sn;
	}
	
	public void setSn(int sn) {
		this.sn = sn;
	}

}
