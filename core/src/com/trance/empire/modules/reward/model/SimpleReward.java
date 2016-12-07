package com.trance.empire.modules.reward.model;

import com.trance.empire.constant.RewardType;

/**
 * 基本奖励类型
 * 
 * @author trance
 */
public class SimpleReward{
	
	/**
	 * 奖励类型   {@link RewardType}
	 */
	protected RewardType type = RewardType.NONE;
	
	/**
	 * 数量
	 */
	protected int count = 0;


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public RewardType getType() {
		return type;
	}

	public void setType(RewardType type) {
		this.type = type;
	}
}
