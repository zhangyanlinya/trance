package com.trance.empire.modules.player.model;

import java.util.List;

import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.building.model.BuildingDto;

import io.protostuff.Tag;

public class ResCreatePlayer {
	
	@Tag(1)
	private PlayerDto playerDto;
	
	@Tag(2)
	private long serverTime;
	
	@Tag(3)
	private List<BuildingDto> buildings;
	
	@Tag(4)
	private List<ArmyDto> armys;

	public PlayerDto getPlayerDto() {
		return playerDto;
	}

	public void setPlayerDto(PlayerDto playerDto) {
		this.playerDto = playerDto;
	}

	public long getServerTime() {
		return serverTime;
	}

	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}

	public List<BuildingDto> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<BuildingDto> buildings) {
		this.buildings = buildings;
	}

	public List<ArmyDto> getArmys() {
		return armys;
	}

	public void setArmys(List<ArmyDto> armys) {
		this.armys = armys;
	}
}
