package com.trance.empire.modules.player.model;

import java.util.List;
import java.util.Map;

import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.coolqueue.model.CoolQueueDto;

import io.protostuff.Tag;

public class ResLogin {
	
	@Tag(1)
	private PlayerDto playerDto;
	
	@Tag(2)
	private int[][] mapdata;
	
	@Tag(3)
	private Map<String,PlayerDto> worldPlayers;
	
	@Tag(4)
	private List<ArmyDto> armys;
	
	@Tag(5)
	private List<CoolQueueDto> coolQueues;
	
	@Tag(6)
	private List<BuildingDto> buildings;
	
	@Tag(7)
	private long serverTime;

	public PlayerDto getPlayerDto() {
		return playerDto;
	}

	public void setPlayerDto(PlayerDto playerDto) {
		this.playerDto = playerDto;
	}

	public int[][] getMapdata() {
		return mapdata;
	}

	public void setMapdata(int[][] mapdata) {
		this.mapdata = mapdata;
	}

	public Map<String, PlayerDto> getWorldPlayers() {
		return worldPlayers;
	}

	public void setWorldPlayers(Map<String, PlayerDto> worldPlayers) {
		this.worldPlayers = worldPlayers;
	}

	public List<ArmyDto> getArmys() {
		return armys;
	}

	public void setArmys(List<ArmyDto> armys) {
		this.armys = armys;
	}

	public List<CoolQueueDto> getCoolQueues() {
		return coolQueues;
	}

	public void setCoolQueues(List<CoolQueueDto> coolQueues) {
		this.coolQueues = coolQueues;
	}

	public List<BuildingDto> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<BuildingDto> buildings) {
		this.buildings = buildings;
	}

	public long getServerTime() {
		return serverTime;
	}

	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}
}
