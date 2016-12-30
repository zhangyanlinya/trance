package com.trance.empire.modules.player.model;

import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.army.model.TechDto;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.coolqueue.model.CoolQueueDto;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * PlayerDto对象
 * 
 * @author Along
 *
 */
public class PlayerDto{
	

	/**
	 * 玩家id
	 */
	private long id;

	/**
	 * 主公名称
	 */
	private String playerName;
	
	/**
	 * 等级
	 */
	private int level = 1;
	
	/**
	 * 金币
	 */
	private long gold;
	
	/**
	 * 银元
	 */
	private long silver;
	
	/**
	 * 粮草
	 */
	private long foods;
	
	/**
	 * UP
	 */
	private long up;
	
	/**
	 * 经验
	 */
	private long experience;
	
	/**
	 * 注册时间
	 */
	public long registerTime;
	
	/**
	 * 最后一次登录时间
	 */
	public long loginTime;

	/**
	 * 服标识
	 */
	private int server;
	
	/**
	 * myself
	 */
	private boolean myself;
	
	private int[][] map;
	
	private final ConcurrentMap<Integer,ArmyDto> armys = new ConcurrentHashMap<Integer,ArmyDto>();
	
	private final ConcurrentMap<Integer,BuildingDto> buildings = new ConcurrentHashMap<Integer,BuildingDto>();
	
	private final ConcurrentMap<Integer,CoolQueueDto> coolQueues = new ConcurrentHashMap<Integer,CoolQueueDto>();

	private final ConcurrentMap<Integer,TechDto> techs = new ConcurrentHashMap<Integer,TechDto>();

	private int x;
	
	private int y;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlayerName() {
		int len = playerName.length();
		playerName = len > 8 ? playerName.substring(0,8) : playerName;
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getGold() {
		return gold;
	}

	public void setGold(long gold) {
		this.gold = gold;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	public int getServer() {
		return server;
	}

	public void setServer(int server) {
		this.server = server;
	}

	public long getUp() {
		return up;
	}

	public void setUp(long up) {
		this.up = up;
	}

	public long getExperience() {
		return experience;
	}

	public void setExperience(long experience) {
		this.experience = experience;
	}

	public boolean isMyself() {
		return myself;
	}

	public void setMyself(boolean myself) {
		this.myself = myself;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}
	
	public ConcurrentMap<Integer, ArmyDto> getArmys() {
		return armys;
	}

	public void addAmry(ArmyDto dto) {
		armys.put(dto.getId(), dto);
	}

	public ConcurrentMap<Integer, BuildingDto> getBuildings() {
		return buildings;
	}

	public void addBuilding(BuildingDto dto) {
		buildings.put(dto.getId(), dto);
	}


	public ConcurrentMap<Integer, CoolQueueDto> getCoolQueues() {
		return coolQueues;
	}
	
	/**
	 * 根据类型获得一个冷却队列
	 * @param type
	 * @return
	 */
	public CoolQueueDto getCoolQueueByType(int type){
		for(CoolQueueDto cool : coolQueues.values()){
			if(cool.getType() == type){
				return cool;
			}
		}
		return null;
	}
	
	public void addCoolQueue(CoolQueueDto dto) {
		coolQueues.put(dto.getId(), dto);
	}

	public long getSilver() {
		return silver;
	}

	public void setSilver(long silver) {
		this.silver = silver;
	}

	public long getFoods() {
		return foods;
	}

	public void setFoods(long foods) {
		this.foods = foods;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public ConcurrentMap<Integer, TechDto> getTechs() {
		return techs;
	}

	public void addTech(TechDto dto) {
		techs.put(dto.getId(), dto);
	}
}
