package com.trance.empire.modules.army.model;

import com.trance.empire.modules.reward.result.ValueResultSet;

import io.protostuff.Tag;

public class ResArmyTrain {
	
	@Tag(1)
	private ValueResultSet valueSet;
	
	@Tag(2)
	private long expireTime;

	public ValueResultSet getValueSet() {
		return valueSet;
	}

	public void setValueSet(ValueResultSet valueSet) {
		this.valueSet = valueSet;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
}
