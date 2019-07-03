package com.trance.empire.modules.world.model;

import java.util.List;

import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.player.model.PlayerDto;

import io.protostuff.Tag;

public class ResChangePlayer {
	
	@Tag(1)
	private PlayerDto playerDto;
	
	@Tag(2)
	private int[][] map;
	
	@Tag(3)
	private List<BuildingDto> buildings;

	public PlayerDto getPlayerDto() {
		return playerDto;
	}

	public void setPlayerDto(PlayerDto playerDto) {
		this.playerDto = playerDto;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public List<BuildingDto> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<BuildingDto> buildings) {
		this.buildings = buildings;
	}
}
