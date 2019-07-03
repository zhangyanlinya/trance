package com.trance.empire.modules.world.model;

import java.util.List;

import com.trance.empire.modules.building.model.BuildingDto;

import io.protostuff.Tag;

public class ResSpyAnyOne {
	
	@Tag(1)
	private int[][] map;
	
	@Tag(2)
	private List<BuildingDto> buildings;

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
