package com.trance.empire.modules.army.model;

import com.trance.empire.modules.coolqueue.model.CoolQueueDto;
import com.trance.empire.modules.reward.result.ValueResultSet;

import io.protostuff.Tag;

public class ResArmyUp {
	
	@Tag(1)
	private CoolQueueDto coolDto;
	
	@Tag(2)
	private ValueResultSet valueSet;
	
	public CoolQueueDto getCoolDto() {
		return coolDto;
	}

	public void setCoolDto(CoolQueueDto coolDto) {
		this.coolDto = coolDto;
	}

	public ValueResultSet getValueSet() {
		return valueSet;
	}

	public void setValueSet(ValueResultSet valueSet) {
		this.valueSet = valueSet;
	}
}
