package com.trance.empire.modules.reward.result;


import com.trance.empire.constant.RewardType;

import io.protostuff.Tag;

/**
 * &#x5956;&#x52b1;&#x7ed3;&#x679c;
 *
 * @author trance
 */
public class RewardResult<T> {
	
	/**
	 * 序列号, 用来区分顺序
	 */
	@Tag(1)
	private int sn = 0;
	
	/**
	 * 奖励类型值
	 */
	@Tag(2)
	private int type = RewardType.NONE.getValue();
	
	/**
	 * 防沉迷影响值
	 */
	@Tag(3)
	private int fcmExtra = 0;
	
	/**
	 * 实际变化数值
	 */
	@Tag(4)
	private int actualCount = 0;
	
	/**
	 * 通过邮件发放的数量
	 */
	@Tag(5)
	private int mailRewardCount = 0;
	
	/**
	 * 执行操作后的当前值
	 */
	@Tag(6)
	private T current;
	
	public RewardResult() {
		
	}
	
	public RewardResult(RewardType type) {
		this.type = type.getValue();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFcmExtra() {
		return fcmExtra;
	}

	public void setFcmExtra(int fcmExtra) {
		this.fcmExtra = fcmExtra;
	}

	public int getActualCount() {
		return actualCount;
	}

	public void setActualCount(int actualCount) {
		this.actualCount = actualCount;
	}

	public T getCurrent() {
		return current;
	}

	public void setCurrent(T current) {
		this.current = current;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}

	public int getMailRewardCount() {
		return mailRewardCount;
	}

	public void setMailRewardCount(int mailRewardCount) {
		this.mailRewardCount = mailRewardCount;
	}

}
