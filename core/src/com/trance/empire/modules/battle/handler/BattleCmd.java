package com.trance.empire.modules.battle.handler;


public interface BattleCmd {
	
	/**
	 * @param targetId Long
	 * @return Map {content: ValueResultSet}	
	 */
	byte START_BATTLE = 1;
	
	/**
	 * @param armys : List<ArmyDto> armys
	 * @param destLv: @link Integer 
	 * @param result: 0- win 1- fail
	 * @param sign  : @link String
	 * @return Map {content: ValueResultSet}	
	 */
	byte FINISH_BATTLE = 2;

	byte GET_ATTACK_INFO =3;
}
