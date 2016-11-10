package com.trance.common.socket.filter;


import com.trance.view.utils.SocketUtil;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 断线重连回调拦截器 
 * @author Administrator
 *
 */
public class ReconnectionFilter extends IoFilterAdapter {
	
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ReconnectionFilter.class);
	
	 @Override  
     public void sessionClosed(NextFilter nextFilter, IoSession ioSession) throws Exception {
			for (;;) {
				Thread.sleep(3000);
				boolean success = SocketUtil.offlineReconnect();
				if (success) {
					break;
				}
				logger.error("reconnect after 3 socond...");
			}
     }
}
