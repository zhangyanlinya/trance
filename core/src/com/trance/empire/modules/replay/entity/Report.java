package com.trance.empire.modules.replay.entity;

import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.army.model.TechDto;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.replay.model.Click;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {

	private String id;
	
	private long time;
	
	private long aId;
	
	private String aName;
	
	private long bId;
	
	private String bName;
	
	private int[][] map;
	
	private Map<String, BuildingDto> buildings = new HashMap<String, BuildingDto>();
	
	private List<ArmyDto> armys = new ArrayList<ArmyDto>();

    private List<TechDto> techs = new ArrayList<TechDto>();

	private List<Click> clicks = new ArrayList<Click>();
	
	public String generateKey(long aId, long time){
		return aId + "_" + time;
	}

	public String getBuildingKey(int x, int y) {
		return x + "_" + y;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getaId() {
		return aId;
	}

	public void setaId(long aId) {
		this.aId = aId;
	}

	public String getaName() {
		return aName;
	}

	public void setaName(String aName) {
		this.aName = aName;
	}

	public long getbId() {
		return bId;
	}

	public void setbId(long bId) {
		this.bId = bId;
	}

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	public List<Click> getClicks() {
		return clicks;
	}

	public void setClicks(List<Click> clicks) {
		this.clicks = clicks;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public Map<String, BuildingDto> getBuildings() {
		return buildings;
	}

	public void setBuildings(Map<String, BuildingDto> buildings) {
		this.buildings = buildings;
	}

	public List<ArmyDto> getArmys() {
		return armys;
	}

	public void setArmys(List<ArmyDto> armys) {
		this.armys = armys;
	}

    public List<TechDto> getTechs() {
        return techs;
    }

    public void setTechs(List<TechDto> techs) {
        this.techs = techs;
    }
}
