package com.trance.empire.modules.building.handler;


/**
 * 主城建筑模块
 *
 * @author Along
 *
 */
public interface BuildingCmd {

	/**
	 * 返回主城开放的建筑
 	 * @return {@link List<PlayerBuildingDto>}
	 */
	byte GET_BUILDINGS = 1;

	/**
	 * 建筑升级
	 * @param buildingId 建筑id
	 * @return Map {
	 * 				"result" : {@link BuildingResult},
	 * 				"content" : {@link BuildingDto}, 玩家建筑DTO
	 * 				"coolQueueDto" : {@link CoolQueueDto}, 冷却队列DTO
	 * 				"valueResultSet" : {@link ValueResultSet} 更新的属性集
	 * 				"newPlayerBuildingDtos" : {@link List<PlayerBuildingDto>} 新建筑DTO列表
	 * 				}
	 */
	byte UPGRADE_BUILDING_LEVEL = 2;

	/**
	 * 收割
	 */
	byte HARVIST = 3;

    /**
     * 使用金币建筑升级
     */
    byte UPGRADE_BUILDING_LEVEL_BY_GOLD = 4;

}
