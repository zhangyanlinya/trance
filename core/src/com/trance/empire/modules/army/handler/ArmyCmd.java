package com.trance.empire.modules.army.handler;

public interface ArmyCmd {
	
	byte GET_ARMYS = 1;
	
	
	byte UPGRADE_LEVEL = 2;
	
	/**
	 * type
	 * amount
	 * 
	 */
	byte TRAIN_ARMY = 3;
	
	/**
	 * obtain amry
	 */
	byte OBTAIN_ARMY = 4;
}

