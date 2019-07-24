package com.trance.view.textinput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.common.util.ProtostuffUtil;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.player.handler.PlayerCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.ReqCreatePlayer;
import com.trance.empire.modules.player.model.ResLogin;
import com.trance.view.screens.callback.LoginCallback;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.SocketUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegisterInputListener implements TextInputListener {

	private LoginCallback callback;

	public RegisterInputListener(LoginCallback callback){
		this.callback = callback;
	}

	@Override
	public void input(String text) {
		if(text == null || text.trim().length() < 2 || text.trim().length() > 22){
			MsgUtil.getInstance().showMsg("name is too long or too short");
			return;
		}

		if(stringFilter(text)){
			MsgUtil.getInstance().showMsg("name is illegal");
			return;
		}
		
		final String playerName = removeEmoji(text);

		ReqCreatePlayer req = new ReqCreatePlayer();
		req.setUserName(Player.userName);
		req.setPlayerName(text);
		req.setServer(1);
//		parms.put("loginKey","");

		Response response = SocketUtil.send(Request.valueOf(Module.PLAYER, PlayerCmd.CREATE_PLAYER, req),true);
		if(response == null || response.getStatus() != 0){
			return;
		}

		byte[] bytes = response.getValueBytes();
		Result<ResLogin> result = ProtostuffUtil.parseObject(bytes, Result.class);
		int code = result.getCode();
		if(code != Result.SUCCESS){
			MsgUtil.getInstance().showMsg(Module.PLAYER, code);
			return;
		}

		callback.handleMessage(result);

		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				FontUtil.getFont().appendText(playerName);
			}
		});
	}

	@Override
	public void canceled() {
		
	}
	
	// 过滤特殊字符
	private boolean stringFilter(String str) throws PatternSyntaxException {
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}  
	
	private String removeEmoji(String str){
		str = str.replaceAll("[^\\u0000-\\uFFFF]", "");
		return str;
	}
}
