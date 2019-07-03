package com.trance.empire.modules.reward.result.vo;


/**
 * 主公经验VO
 * 
 * @author trance
 */
public class PlayerExp {

	/**
	 * 当前等级
	 */
	private long currLevel = 0;
	
	/**
	 * 当前经验
	 */
	private long currExp = 0;
	
	
	public static PlayerExp valueOf(long currLevel, long currExp ) {
		PlayerExp e = new PlayerExp();
		e.currLevel = currLevel;
		e.currExp = currExp;
		return e;
	}

	public long getCurrLevel() {
		return currLevel;
	}

	public void setCurrLevel(long currLevel) {
		this.currLevel = currLevel;
	}

	public long getCurrExp() {
		return currExp;
	}

	public void setCurrExp(long currExp) {
		this.currExp = currExp;
	}

}
