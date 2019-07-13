/**
 * @file:Module.java
 * @author:David
 **/
package com.trance.empire.config;

/**
 * @class:Module
 * @description:服务端 游戏模块 管理
 * @author:David
 * @version:v1.0
 * @date:2013-4-18
 **/
public interface Module {

	/**
	 * 管理后台
	 */
	byte MIS = 0;

	/**
	 * 主公
	 */
	byte PLAYER = 1;

	/**
	 * 地图
	 */
	byte MAP_DATA = 2;
	
	/**
	 * 地图
	 */
	byte WORLD = 3;
	
	/**
	 * RANKING
	 */
	byte RANKING = 4;
	
	/**
	 * army
	 */
	byte ARMY = 5;
	
	/**
	 * coolqueue
	 */
	byte COOLQUEUE = 6;
	
	/**
	 * building
	 */
	byte BUILDING = 7;
	
	/**
	 * battle
	 */
	byte BATTLE = 8;
	
	/**
	 * daily reward
	 */
	byte DAILY_REWARD = 9;

    /**
     * replay
     */
    byte REPLAY = 10;

	/**
	 * 通用模块
	 */
	byte COMMON_MODULE = 100;

	/**
	 * 充值模块
	 */
	byte CHARGE = 101;

	/**
	 * 补丁模块
	 */
	byte PATCH = 102;

}
