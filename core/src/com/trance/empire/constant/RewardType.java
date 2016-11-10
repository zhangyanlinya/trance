package com.trance.empire.constant;

/**
 * 奖励类型
 * 
 * @author trance
 */
public enum RewardType {
	
	/**
	 * 空奖励
	 */
	NONE(0),
	
	/**
	 * 金币
	 */
	GOLD(1),
	
	/**
	 * 返还金币, 即礼券
	 */
	FUND_GOLD(2),
	
	/**
	 * 内币
	 */
	RMB(3),
	
	/**
	 * 银两
	 */
	SILVER(4),
	
	/**
	 * 粮食
	 */
	FOODS(5),
	
	/**
	 * 道具
	 */
	ITEM(6),

	/**
	 * 水晶
	 */
	CRYSTAL(7),
	
	/**
	 * 主公经验
	 */
	PLAYER_EXP(8),

	/**
	 * 金钱混合(主要用在混合扣的情况, 顺序：返还金币-内币-金币)
	 */
	MONEY_MIX(1001),
	
	/**
	 * 内币金币混合(只能用内币金币消费, 顺序：内币-金币)
	 */
	MONEY_RMB_GOLD(1002),
	
	/**
	 * 体力混合(主要用在混合扣的情况, 顺序：体力buff-体力)
	 */
	ENERGY_MIX(1003);
	
	
	/**
	 * 类型值
	 */
	private final int value;
	
	
	private RewardType(int value) {
		this.value = value;
	}

	/**
	 * 类型值
	 * @return int
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * 生成 RewardType
	 * @param value 类型值
	 * @return RewardType
	 */
	public static RewardType valueOf(int value) {
		for (RewardType type: RewardType.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return null;
	}
	
}
