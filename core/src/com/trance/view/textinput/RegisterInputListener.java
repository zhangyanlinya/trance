package com.trance.view.textinput;

import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.Input.TextInputListener;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.player.handler.PlayerCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.view.screens.callback.LoginCallback;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.SocketUtil;

import java.util.HashMap;
import java.util.Map;
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
		if(text == null || text.trim().length() < 2 || text.trim().length() > 10){
			return;
		}

		if(stringFilter(text)){
			return;
		}
		
		text = removeEmoji(text);

		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("userName", Player.userName);
		parms.put("playerName",text);
		parms.put("server",1);
//		parms.put("loginKey","");

		Response response = SocketUtil.send(Request.valueOf(Module.PLAYER, PlayerCmd.CREATE_PLAYER, parms),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return;
		}

		byte[] bytes = response.getValueBytes();
		String str = new String(bytes);
		Result result = JSON.parseObject(str, Result.class);
		Object codeObject = result.get("result");
		int code = Integer.valueOf(String.valueOf(codeObject));
		if(code != Result.SUCCESS){
			MsgUtil.getInstance().showMsg(Module.PLAYER, code);
			return;
		}

		Player.player.setPlayerName(text);
		FontUtil.getFont().appendText(text);
		callback.handleMessage(result);

	}

	@Override
	public void canceled() {
		
	}
	
	// 过滤特殊字符
	public  boolean stringFilter(String str) throws PatternSyntaxException {
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}  
	
	public String removeEmoji(String str){
		str = str.replaceAll("[^\\u0000-\\uFFFF]", "");
		return str;
	}
}
