package com.trance.empire.modules.mapdata.handler;

public interface MapDataCmd {
	

	/**
	 * @return Map{ result:
	 * 				content:PlayerMapDataDto}
	 */
	byte GET_PLAYER_MAP_DATA = 1;
	
	/**
	 * @param int[][] map
	 * 
	 */
	byte SAVE_PLAYER_MAP_DATA = 2;

	byte GET_TARGET_PLAYER_MAP_DATA = 3;
}
