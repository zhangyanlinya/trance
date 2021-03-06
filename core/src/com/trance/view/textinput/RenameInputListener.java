package com.trance.view.textinput;

import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.Input.TextInputListener;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.common.util.ProtostuffUtil;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.player.handler.PlayerCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.ReqRename;
import com.trance.view.config.Config;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.SocketUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RenameInputListener implements TextInputListener {

	@Override
	public void input(String text) {
		if(text == null || text.trim().length() < 2 || text.trim().length() > 22){
			MsgUtil.getInstance().showMsg("name is too long or too short");
			return;
		}
		if(text.equals(Player.player.getPlayerName())){
			return;
		}
		
		if(stringFilter(text)){
			MsgUtil.getInstance().showMsg("name is illegal");
			return;
		}
		
		text = removeEmoji(text);

		if("test123".equals(text)){
			Config.debug = true;
			return;
		}
		
		ReqRename req = new ReqRename();
		req.setName(text);
		Response response = SocketUtil.send(Request.valueOf(Module.PLAYER, PlayerCmd.RENAME, req),true);
		if(response == null || response.getStatus() != 0){
			return;
		}
		
		Result<String> result = ProtostuffUtil.parseObject(response.getValueBytes(), Result.class);
		int code = result.getCode();
		if(code == 0){
			Player.player.setPlayerName(text);
			FontUtil.getFont().appendText(text);
		}else{
			MsgUtil.getInstance().showMsg(Module.PLAYER, code);
		}
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
