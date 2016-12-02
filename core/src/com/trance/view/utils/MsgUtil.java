package com.trance.view.utils;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.trance.empire.config.Module;
import com.trance.empire.model.CodeJson;
import com.trance.view.TranceGame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgUtil {

	private static MsgUtil msgUtil;

	private TranceGame tranceGame;
	private String lang;

	private final  String zh_common ="[{\"id\":0,\"msg\":\" 操作成功 \"},{\"id\":-1,\"msg\":\" 操作失败 \"},{\"id\":-2,\"msg\":\" 没操作权限 \"},{\"id\":-3,\"msg\":\" 基础数据不存在 \"},{\"id\":-4,\"msg\":\" 角色不存在 \"},{\"id\":-5,\"msg\":\" 持久化异常 \"},{\"id\":-6,\"msg\":\" 参数错误  \"},{\"id\":-7,\"msg\":\" 用户不在线上\"},{\"id\":-8,\"msg\":\" 金币不足 \"},{\"id\":-9,\"msg\":\" 银币不足 \"},{\"id\":-10,\"msg\":\" 粮食不足 \"},{\"id\":-11,\"msg\":\" 用户经验不够 \"},{\"id\":-12,\"msg\":\" 用户等级不够 \"},{\"id\":-99,\"msg\":\" 非法的服标识\"}]";
	private final  String zh_player ="[{\"id\":-10001,\"msg\":\"登录key错误\"},{\"id\":-10003,\"msg\":\"注册时用户名已存在\"},{\"id\":-10004,\"msg\":\"注册时角色名已存在\"},{\"id\":-10005,\"msg\":\"重连被禁止\"},{\"id\":-10006,\"msg\":\"账号被封禁止登陆\"},{\"id\":-10007,\"msg\":\"IP被封禁止登陆\"},{\"id\":-10008,\"msg\":\"购买体力次数上限\"},{\"id\":-10009,\"msg\":\"防沉迷状态错误\"},{\"id\":-10010,\"msg\":\"被防火墙加入黑名单\"}]";
	private final  String zh_world ="[{\"id\":-10001,\"msg\":\"暂时没有玩家\"},{\"id\":-10002,\"msg\":\"CD时间中\"},{\"id\":-10003,\"msg\":\"已分配\"}]";
	private final  String zh_building ="[{\"id\":-10001,\"msg\":\"建筑不存在\"},{\"id\":-10002,\"msg\":\"等级不足\"},{\"id\":-10003,\"msg\":\"主城等级不足\"},{\"id\":-10004,\"msg\":\"冷却队列已满\"},{\"id\":-10005,\"msg\":\"CD时间中\"}]";
	private final  String zh_dailyreward ="[{\"id\":-10001,\"msg\":\"当天已经领取过奖励\"}]";
	private final  String zh_battle ="[{\"id\":-10001,\"msg\":\"正在被攻击中(等侍4分钟)\"},{\"id\":-10002,\"msg\":\"攻击时间已过\"},{\"id\":-10003,\"msg\":\"没有可用部队\"}]";
	private final  String zh_army ="[{\"id\":-10001,\"msg\":\"还在冷却中\"},{\"id\":-10002,\"msg\":\"部队不存在\"},{\"id\":-10003,\"msg\":\"部队未训练完成\"},{\"id\":-10004,\"msg\":\"已达到最大训练数量\"},{\"id\":-10005,\"msg\":\"等级未开放\"},{\"id\":-10006,\"msg\":\"冷却队列已满\"}]";

	private final String en_common = "[{\"id\": 0,\"msg\":\" operation succeeded \"}, {\"id\": -1,\"msg\":\" operation failed \"},{\"id\":-2,\"msg\": \"No action permission\"}, { \"id\": -3, \"msg\": \"Base data does not exist\"}, {\"id\":-4,\"msg\":\" Role does not exist \"}, { \"id\": -5, \"msg\": \"persistence exception\"}, { \"id\": -6, \"msg\": \"parameter error\"},{\"id\":-7,\"msg\":\" User is not online \"}, {\"id\": -8,\"msg\":\" Insufficient gold \"}, {\"id\": -9,\"msg\":\" Insufficient silver \"},{\"id\":-10, \"msg\": \"Food shortage\"}, { \"id\": -11, \"msg\": \"Insufficient user experience\"}, { \"id\": -12, \"msg\":\" Insufficient user level\"},{\"id\": -99, \"msg\": \"illegal service logo\"}]";
	private final String en_player = "[{\"id\": -10001,\"msg\":\" login key error \"}, {\"id\": -10003,\"msg\":\"The user name already exists when registering \"}, {\"id\": -10004,\"msg\":\" the role name is already registered at the time of registration \"}, {\"id\": -10005,\"msg\":\" reconnection disabled \"},{ \"id\":-10006, \"msg\":\" The account is blocked from landing \"}, {\"id\": -10007,\"msg\":\" IP is forbidden to land \"}, {\"id\": -10008,\"msg\":\" }, \",\"id\": -10009,\"msg\":\" anti-obfuscation status error \"}, {\"id\": -10010,\"msg\":\" blacklisted by the firewall \"}]";
	private final String en_world = "[{\"id\": -10001,\"msg\":\" No Players Now \"}, {\"id\": -10002,\"msg\":\" CD Time \" -10003, \"msg\": \"Assigned\"}]";
	private final String en_building = "[{\"id\": -10001,\"msg\":\" building does not exist \"}, {\"id\": -10002,\"msg\": \"Insufficient level\"},{\"id\": -10003, \"msg\": \"Main city level insufficient\"}, { \"id\": -10004, \"msg\": \"Cooling queue full\"}, { \"id\": -10005, \"msg\": \"CD time \"}]";
	private final String en_dailyreward = "[{\"id\": -10001,\"msg\":\" The award has been received that day \"}]";
	private final String en_battle = "[{\"id\": -10001,\"msg\":\" Being attacked (wait 4 minutes) \"}, {\"id\": -10002,\"msg\":\" attack time has passed \"}, {\"id\": -10003,\"msg\":\" No troops available \"}]";
	private final String en_army = "[{\"id\": -10001,\"msg\":\" is still cooling \"}, {\"id\": -10002,\"msg\":\"Troops do not exist\"}, {\"id\": -10003, \"msg\": \"Troop not trained completed\"}, { \"id\": -10004, \"msg\": \"Max training reached\"}, {\"id\":-10005,\"msg\":\" Level not open \"}, {\"id\": -10006,\"msg\":\" The cooling queue is full \"}]";

	private final  Map<Integer,Msg> common = new HashMap<Integer,Msg>();
	private final  Map<Integer,Msg> player = new HashMap<Integer,Msg>();
	private final  Map<Integer,Msg> world = new HashMap<Integer,Msg>();
	private final  Map<Integer,Msg> building = new HashMap<Integer,Msg>();
	private final  Map<Integer,Msg> dailyreward = new HashMap<Integer,Msg>();
	private final  Map<Integer,Msg> battle = new HashMap<Integer,Msg>();
	private final  Map<Integer,Msg> army = new HashMap<Integer,Msg>();

	private final Map<String,String> zh_local = new HashMap<String,String>();


	private MsgUtil(){}
	public static MsgUtil getInstance(){
		if(msgUtil == null){
			msgUtil = new MsgUtil();
		}
		return msgUtil;
	}
	
	public void init(TranceGame tranceGame,String lang){
		this.tranceGame = tranceGame;
		this.lang = lang;
		if(lang == null || lang.equals("")){
			lang = "en";
		}
		if("zh".equals(lang)) {
			tomap(zh_common, common);
			tomap(zh_player, player);
			tomap(zh_world, world);
			tomap(zh_building, building);
			tomap(zh_dailyreward, dailyreward);
			tomap(zh_battle, battle);
			tomap(zh_army, army);
		}else if("en".equals(lang)){
			tomap(en_common, common);
			tomap(en_player, player);
			tomap(en_world, world);
			tomap(en_building, building);
			tomap(en_dailyreward, dailyreward);
			tomap(en_battle, battle);
			tomap(en_army, army);
		}else{//default en
			tomap(en_common, common);
			tomap(en_player, player);
			tomap(en_world, world);
			tomap(en_building, building);
			tomap(en_dailyreward, dailyreward);
			tomap(en_battle, battle);
			tomap(en_army, army);
		}

		//local
		initLocal(lang);
	}

	private void initLocal(String lang){
		if("zh".equals(lang)) {
			zh_local.put("Congratulations on the upgrade", "恭喜升级!");
			zh_local.put("Account in other places login", "账号在其他地方登录");
			zh_local.put("Account has kicked out by admin", "账号被管理后台踢下线");
			zh_local.put("IP is blocked", "IP被封");
			zh_local.put("Account is blocked", "账号被封");
			zh_local.put("server is colosed please wait", "服务器已关闭 请等待");
			zh_local.put("Being attacked, wait 4 minutes", "正在遭到攻击 等待4分钟");
			zh_local.put("The maximum number of trains has been reached", "已达到最大训练数量");
			zh_local.put("Reconnect the server successfully", "重新连接服务器成功");
			zh_local.put("Connection to server failed", "连接服务器失败！");
			zh_local.put("please login again", "请重新登录");

			//
			zh_local.put("gold","金币");
			zh_local.put("silver","银币");
			zh_local.put("foods","粮食");
			zh_local.put("experience","经验");
			zh_local.put("input new name","请输入新名字");
			zh_local.put("Drag building placement","可拖动建筑放置");
			zh_local.put("Click the picture to start the game","点击图片开始游戏");
			zh_local.put("laud","点赞");
			zh_local.put("Click Build Image to upgrade","点击建筑图片升级");
		}
	}

	public String getLocalMsg(String key){
		if(lang == null || lang.equals("") || "en".equals(lang)){
			return key;
		}
		String msg = null;
		if(lang.equals("zh")) {
			 msg = zh_local.get(key);
		}
		if(msg == null){
			return key;
		}
		return msg;
	}

	
	public  void tomap(String jsonString, Map<Integer,Msg> map){
		map.clear();
		List<CodeJson> list = JSON.parseArray(jsonString,CodeJson.class);
		for(CodeJson e : list){
			Msg msg = new Msg();
			msg.msg = e.getMsg();
			msg.time = System.currentTimeMillis();
			map.put(e.getId(), msg);
		}
	}
	
	 public class Msg{
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
	
	public void showMsg(int module, int code){
		Msg msg = getMsg(module, code);
		if(msg == null){
			showMsg(code);
		}else{
			long now = System.currentTimeMillis();
			if(now -msg.getTime() < 2000){
				return;
			}
			msg.setTime(now);
			showMsg(msg.getMsg());
		}
		
	}

	private Msg getMsg(int module, int code){
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
		return  msg;
	}
	
	public void showMsg(final Object  obj){
		final String msg = getLocalMsg(obj + "");
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				tranceGame.showMsg(msg);
			}
		});
	}

	public void showLoading(){
		tranceGame.getEvent().notify(1,null);
//		Gdx.app.postRunnable(new Runnable() {
//			@Override
//			public void run() {
//				tranceGame.showLoading();
//			}
//		});

	}
	public void hideLoading(){
		tranceGame.getEvent().notify(2,null);
//		Gdx.app.postRunnable(new Runnable() {
//			@Override
//			public void run() {
//				tranceGame.hideLoading();
//			}
//		});

	}
}
