package com.trance.view.utils;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.audio.Sound;
import com.trance.empire.config.Module;
import com.trance.empire.model.CodeJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgUtil {
	private final static String common_json ="[{\"id\":0,\"msg\":\" 操作成功 \"},{\"id\":-1,\"msg\":\" 操作失败 \"},{\"id\":-2,\"msg\":\" 没操作权限 \"},{\"id\":-3,\"msg\":\" 基础数据不存在 \"},{\"id\":-4,\"msg\":\" 角色不存在 \"},{\"id\":-5,\"msg\":\" 持久化异常 \"},{\"id\":-6,\"msg\":\" 参数错误  \"},{\"id\":-7,\"msg\":\" 用户不在线上\"},{\"id\":-8,\"msg\":\" 金币不足 \"},{\"id\":-9,\"msg\":\" 银币不足 \"},{\"id\":-10,\"msg\":\" 粮食不足 \"},{\"id\":-11,\"msg\":\" 用户经验不够 \"},{\"id\":-12,\"msg\":\" 用户等级不够 \"},{\"id\":-99,\"msg\":\" 非法的服标识\"}]";
	private final static Map<Integer,Msg> common = new HashMap<Integer,Msg>();
	
	private final static String player_json ="[{\"id\":-10001,\"msg\":\"登录key错误\"},{\"id\":-10003,\"msg\":\"注册时用户名已存在\"},{\"id\":-10004,\"msg\":\"注册时角色名已存在\"},{\"id\":-10005,\"msg\":\"重连被禁止\"},{\"id\":-10006,\"msg\":\"账号被封禁止登陆\"},{\"id\":-10007,\"msg\":\"IP被封禁止登陆\"},{\"id\":-10008,\"msg\":\"购买体力次数上限\"},{\"id\":-10009,\"msg\":\"防沉迷状态错误\"},{\"id\":-10010,\"msg\":\"被防火墙加入黑名单\"}]";
	private final static Map<Integer,Msg> player = new HashMap<Integer,Msg>();
	
	private final static String world_json ="[{\"id\":-10001,\"msg\":\"暂时没有玩家\"},{\"id\":-10002,\"msg\":\"CD时间中\"},{\"id\":-10003,\"msg\":\"已分配\"}]";
	private final static Map<Integer,Msg> world = new HashMap<Integer,Msg>();
	
	private final static String building_json ="[{\"id\":-10001,\"msg\":\"建筑不存在\"},{\"id\":-10002,\"msg\":\"等级不足\"},{\"id\":-10003,\"msg\":\"主城等级不足\"},{\"id\":-10004,\"msg\":\"冷却队列已满\"},{\"id\":-10005,\"msg\":\"CD时间中\"}]";
	private final static Map<Integer,Msg> building = new HashMap<Integer,Msg>();
	
	private final static String dailyreward_json ="[{\"id\":-10001,\"msg\":\"当天已经领取过奖励\"}]";
	private final static Map<Integer,Msg> dailyreward = new HashMap<Integer,Msg>();
	
	private final static String battle_json ="[{\"id\":-10001,\"msg\":\"正在被攻击中(等侍4分钟)\"},{\"id\":-10002,\"msg\":\"攻击时间已过\"},{\"id\":-10003,\"msg\":\"没有可用部队\"}]";
	private final static Map<Integer,Msg> battle = new HashMap<Integer,Msg>();
	
	private final static String army_json ="[{\"id\":-10001,\"msg\":\"还在冷却中\"},{\"id\":-10002,\"msg\":\"部队不存在\"},{\"id\":-10003,\"msg\":\"部队未训练完成\"},{\"id\":-10004,\"msg\":\"已达到最大训练数量\"},{\"id\":-10005,\"msg\":\"等级未开放\"},{\"id\":-10006,\"msg\":\"冷却队列已满\"}]";
	private final static Map<Integer,Msg> army = new HashMap<Integer,Msg>();
	
	
	public static void init(){
		tomap(common_json,common);
		tomap(player_json,player);
		tomap(world_json,world);
		tomap(building_json,building);
		tomap(dailyreward_json,dailyreward);
		tomap(battle_json,battle);
		tomap(army_json,army);
	}
	
	public static void tomap(String jsonString, Map<Integer,Msg> map){
		map.clear();
		List<CodeJson> list = JSON.parseArray(jsonString,CodeJson.class);
		for(CodeJson e : list){
			Msg msg = new Msg();
			msg.msg = e.getMsg();
			msg.time = System.currentTimeMillis();
			map.put(e.getId(), msg);
		}
	}
	
	 public static class Msg{
		private long time;
		private String msg;
		
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
	
	public static void showMsg(int module, int code){
//		Log.e(LogTag.TAG, "module :" + module +"  code :"+ code);
		if(code < 0){
			Sound sound = ResUtil.getInstance().getSound(9);
			sound.play();
		}
		Msg msg = null;
		if(code <= 0 && code > -999){
			 msg = common.get(code);
		}else{
			if(module == Module.PLAYER){
				msg = player.get(code);	
			}
			else if(module == Module.WORLD){
				msg = world.get(code);
			}
			else if(module == Module.BUILDING){
				msg = building.get(code);
			}
			else if(module == Module.DAILY_REWARD){
				msg = dailyreward.get(code);
			}
			else if(module == Module.BATTLE){
				msg = battle.get(code);
			}
			else if(module == Module.ARMY){
				msg = army.get(code);
			}
		}
		
		if(msg == null){
			showMsg(code);
		}else{
			long now = System.currentTimeMillis();
			if(now - msg.getTime() < 2000){
				return;
			}
			msg.setTime(now);
			showMsg(msg.getMsg());
		}
		
	}
	
	public static void showMsg(Object obj){
//		Message msg = Message.obtain();
//		msg.what = -1000;
//		msg.obj = obj;
//		MainActivity.handler.sendMessage(msg);
	}
}
