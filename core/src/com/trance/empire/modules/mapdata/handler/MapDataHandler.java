package com.trance.empire.modules.mapdata.handler;

import com.trance.common.socket.SimpleSocketClient;
import com.trance.common.socket.handler.HandlerSupport;
import com.trance.common.socket.handler.ResponseProcessorAdapter;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.view.utils.MsgUtil;

import org.apache.mina.core.session.IoSession;

import java.util.HashMap;


/**
 * 地图模块
 * 
 * @author zyl
 *
 */
public class MapDataHandler extends HandlerSupport {
	
	public MapDataHandler(SimpleSocketClient socketClient) {
		super(socketClient);
	}

	@Override
	public void init() {
		this.registerProcessor(new ResponseProcessorAdapter(){
			
			@Override
			public int getModule() {
				return Module.MAP_DATA;
			}
			
			@Override
			public int getCmd() {
				return MapDataCmd.SAVE_PLAYER_MAP_DATA;
			}
			
			@Override
			public Object getType() {
				return HashMap.class;
			}
			
			@Override
			public void callback(IoSession session, Response response,
								 Object message) {
				ResponseStatus status = response.getStatus();
				if (status == ResponseStatus.SUCCESS) {
					HashMap<?, ?> result = (HashMap<?, ?>) response.getValue();
					int code = (Integer) result.get("result");
					if (code != Result.SUCCESS) {
						MsgUtil.getInstance().showMsg(Module.MAP_DATA, code);
					}
				}
			}
		});
		this.registerProcessor(new ResponseProcessorAdapter(){
			
			@Override
			public int getModule() {
				return Module.MAP_DATA;
			}
			
			@Override
			public int getCmd() {
				return MapDataCmd.GET_TARGET_PLAYER_MAP_DATA;
			}
			
			@Override
			public Object getType() {
				return HashMap.class;
			}
			
			@Override
			public void callback(IoSession session, Response response,
								 Object message) {
				
			}
		});
		
	}
}
