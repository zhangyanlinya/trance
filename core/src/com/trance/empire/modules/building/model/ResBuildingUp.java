package com.trance.empire.modules.building.model;

import java.util.List;

import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.reward.result.ValueResultSet;

import io.protostuff.Tag;

public class ResBuildingUp {
	
	@Tag(1)
	private BuildingDto buildingDto;
	
	@Tag(2)
	private ValueResultSet valueResultSet;
	
	@Tag(3)
	private List<ArmyDto> armyDtos;

	public BuildingDto getBuildingDto() {
		return buildingDto;
	}

	public void setBuildingDto(BuildingDto buildingDto) {
		this.buildingDto = buildingDto;
	}

	public ValueResultSet getValueResultSet() {
		return valueResultSet;
	}

	public void setValueResultSet(ValueResultSet valueResultSet) {
		this.valueResultSet = valueResultSet;
	}

	public List<ArmyDto> getArmyDtos() {
		return armyDtos;
	}

	public void setArmyDtos(List<ArmyDto> armyDtos) {
		this.armyDtos = armyDtos;
	}
}
