package com.trance.empire.modules.player.handler;

import com.trance.common.socket.SimpleSocketClient;
import com.trance.common.socket.handler.HandlerSupport;
import com.trance.common.socket.handler.ResponseProcessorAdapter;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.modules.player.model.Player;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.SocketUtil;

import org.apache.mina.core.session.IoSession;


/**
 * 主公模块
 * 
 * @author zyl
 *
 */
public class PlayerHandler extends HandlerSupport {

	public PlayerHandler(SimpleSocketClient socketClient) {
		super(socketClient);
	}

	@Override
	public void init() {
		this.registerProcessor(new ResponseProcessorAdapter(){

			@Override
			public int getModule() {
				return Module.PLAYER;
			}

			@Override
			public int getCmd() {
				return PlayerCmd.HEART_BEAT;
			}

			@Override
			public Object getType() {
				return null;
			}

			@Override
			public void callback(IoSession session, Response response,
								 Object message) {
				if(response != null){ 
					if(response.getStatus() == ResponseStatus.SUCCESS){
//						System.out.println("连接还活着...");
						return;//还活着
					}
				}
				// 死了 则关闭连接
//				System.out.println("连接死掉了! 准备重连...");
				session.close(true);
			}
		});
		
		this.registerProcessor(new ResponseProcessorAdapter(){

			@Override
			public int getModule() {
				return Module.PLAYER;
			}

			@Override
			public int getCmd() {
				return PlayerCmd.PUSH_LEVEL_UPGRADE;
			}

			@Override
			public Object getType() {
				return Integer.class;
			}

			@Override
			public void callback(IoSession session, Response response,
								 Object message) {
				if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
					return;
				}
				int newLevel = (Integer) response.getValue();
				MsgUtil.getInstance().showMsg("Congratulations on the upgrade");
				Player.player.setLevel(newLevel);
			}
		});
		this.registerProcessor(new ResponseProcessorAdapter(){
			
			@Override
			public int getModule() {
				return Module.PLAYER;
			}
			
			@Override
			public int getCmd() {
				return PlayerCmd.PUSH_OFF_LINE;
			}
			
			@Override
			public Object getType() {
				return Integer.class;
			}
			
			@Override
			public void callback(IoSession session, Response response,
								 Object message) {
				if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
					return;
				}
				
				SocketUtil.heartbeat = false;
				int reason = (Integer) response.getValue();
				String msg ="";
				switch(reason){
				case 1:
					msg ="Account in other places login";
					break;
				case 2:
					msg ="Account has kicked out by admin";
					break;
				case 3:
					msg ="IP is blocked";
					break;
				case 4:
					msg ="Account is blocked";
					break;
				case 5:
					msg ="server is colosed please wait";
					break;
				case 6:
					msg ="Being attacked, wait 4 minutes";
					break;
				}
				MsgUtil.getInstance().showMsg(msg);
			}
		});

		this.registerProcessor(new ResponseProcessorAdapter(){

			@Override
			public int getModule() {
				return Module.PLAYER;
			}

			@Override
			public int getCmd() {
				return PlayerCmd.CLEAR_ANONYMOUS_SESSION;
			}

			@Override
			public Object getType() {
				return Integer.class;
			}

			@Override
			public void callback(IoSession session, Response response,
								 Object message) {
				if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
					return;
				}

				SocketUtil.heartbeat = false;
			}
		});
	}
}
