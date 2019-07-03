package com.trance.empire.modules.coolqueue.model;


import io.protostuff.Tag;

/**
 * 冷却队列DTO
 * 
 * @author Along
 *
 */
public class CoolQueueDto {

	/**
	 * 冷却队列的id
	 */
	@Tag(1)
	private int id;

	/**
	 * 冷却队列类型
	 */
	@Tag(2)
	private int type;

	/**
	 * 冷却到期时间
	 */
	@Tag(3)
	private long expireTime;

	/**
	 * 冷却到期时间
	 */
	@Tag(4)
	private int coolTime;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

    public int getCoolTime() {
        return coolTime;
    }

    public void setCoolTime(int coolTime) {
        this.coolTime = coolTime;
    }
}
