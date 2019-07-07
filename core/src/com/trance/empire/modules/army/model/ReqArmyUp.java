package com.trance.empire.modules.army.model;

import io.protostuff.Tag;

public class ReqArmyUp {
	
	@Tag(1)
	private int armyId;

	public int getArmyId() {
		return armyId;
	}

	public void setArmyId(int armyId) {
		this.armyId = armyId;
	}
}
