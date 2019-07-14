package com.trance.empire.modules.match.model;

import java.util.ArrayList;
import java.util.List;

import io.protostuff.Tag;

public class MatchDto {
	
	@Tag(1)
	private List<FightDto> team1 = new ArrayList<FightDto>(2);
	
	@Tag(2)
	private List<FightDto> team2 = new ArrayList<FightDto>(2);

	public List<FightDto> getTeam1() {
		return team1;
	}

	public void setTeam1(List<FightDto> team1) {
		this.team1 = team1;
	}

	public List<FightDto> getTeam2() {
		return team2;
	}

	public void setTeam2(List<FightDto> team2) {
		this.team2 = team2;
	}
}
