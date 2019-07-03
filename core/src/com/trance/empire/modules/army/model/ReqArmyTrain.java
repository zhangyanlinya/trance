package com.trance.empire.modules.army.model;

import io.protostuff.Tag;

public class ReqArmyTrain {
	
	@Tag(1)
	private int armyId;
	
	@Tag(2)
	private int amount;

	public int getArmyId() {
		return armyId;
	}

	public void setArmyId(int armyId) {
		this.armyId = armyId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
