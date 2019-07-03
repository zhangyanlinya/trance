package com.trance.view.net;


import com.trance.common.socket.SimpleSocketClient;
import com.trance.common.socket.handler.ResponseProcessor;
import com.trance.common.socket.handler.ResponseProcessors;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.view.utils.MsgUtil;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientServiceImpl implements ClientService{
	

	/**
	 * logger
	 */
//	private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
	
	
	/**
	 * SocketClient
	 */
	private SimpleSocketClient socketClient = null;
	
	/**
	 * 远程主机ip
	 */
//	private String ip = "47.91.208.119";
//	private String ip = "192.168.0.101";
	private String ip = "192.168.1.108";
	
	/**
	 * 远程主机端口号
	 */
	private int port = 10001;
	
//	/**
//	 * 上次重连时间
//	 */
//	private long lastReconnectTime;
//	
//	/**
//	 * 断线重连时间间隔
//	 */
//	private static final long RECONNECT_INTERVAL = 20 * 1000L;
	
	/**
	 * 响应处理器集合
	 */
	private final ResponseProcessors responseProcessors = new ResponseProcessors();


	private int threadCount = 5;
	

	@Override
	public void registerProcessor(ResponseProcessor processor) {
		this.responseProcessors.registerProcessor(processor);
		
	}


	/**
	 * @param request    Request
	 * @param showDialog
	 * @return
	 */
	@Override
	public Response send(Request request, boolean showDialog) {
		if(showDialog){
			MsgUtil.getInstance().showLoading();
		}
		try {
			Response response = this.socketClient.send(request);
			return response;
		} catch (Exception ex) {
//			logger.error("发送信息到远程服务器错误：{}", ex.getMessage());
		}finally{
			if(showDialog){
				MsgUtil.getInstance().hideLoading();
			}
		}
		return null;
	
	}

	/**
	 * @param request Request
	 */
	@Override
	public void sendAsync(Request request) {
		try {
			this.socketClient.sendAsync(request);
		} catch (Exception ex) {
//			logger.error("发送信息到远程服务器错误：{}", ex.getMessage());
		}
		
	}
	
//	/**
//	 * 检查SocketClient状态
//	 */
//	private void checkSocketClient() {
//		if (this.socketClient == null || !this.socketClient.isConnected()) {
//			synchronized(this) {
//				if (this.socketClient == null || !this.socketClient.isConnected()) {
//					if (System.currentTimeMillis() - this.lastReconnectTime >= RECONNECT_INTERVAL) {
//						this.lastReconnectTime = System.currentTimeMillis();
//						this.initSocket();
//						
//						this.lastReconnectTime = System.currentTimeMillis();
//					} else {
//						return;
//					}
//				}
//			}
//		}
//	}

	private boolean initSocket() {
		try {
			if(this.socketClient == null){
				this.socketClient = new SimpleSocketClient(this.ip, this.port, this.threadCount );
			}
			IoSession session = socketClient.getSession();
			if(session == null){
				return false;
			}
//			logger.info("连接远程服务器[ip: {}, port: {}]成功！", this.ip, this.port);
			
		} catch (Exception ex) {
//			logger.error("初始化远程服务器[ip: {}, port: {}] 连接错误：{}", this.ip, this.port, ex.getMessage());
			return false;
		}
		
		return true;
	}
	
	public void destroy() {
		if (this.socketClient != null) {
			this.socketClient.close();
			this.socketClient = null;
		}
	}
	
	public void init() {
//		初始化服务机地址
		initServerAddress();
		initSocket();
	}

	private void initServerAddress() {
		//TODO  
	}
	
	@Override
	public ResponseProcessors getResponseProcessors() {
		return responseProcessors;
	}
}
