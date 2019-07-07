package com.trance.empire.modules.replay.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trance.empire.modules.army.model.ArmyVo;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.tech.model.TechDto;

import io.protostuff.Tag;

public class ReportDto implements Cloneable {
	
	@Tag(1)
	private String id;

	@Tag(2)
	private long time;

	@Tag(3)
	private long aId;

	@Tag(4)
	private String aName;

	@Tag(5)
	private long bId;

	@Tag(6)
	private String bName;

	@Tag(7)
	private int[][] map;
	
	@Tag(8)
	private Map<String, BuildingDto> buildings = new HashMap<String, BuildingDto>();
	
	@Tag(9)
	private List<ArmyVo> armys = new ArrayList<ArmyVo>();
	
	@Tag(10)
    private List<TechDto> techs = new ArrayList<TechDto>();
	
	@Tag(11)
	private List<Click> clicks = new ArrayList<Click>();


    public ReportDto deepClone() {
        try {
            ReportDto dto = (ReportDto) super.clone();


            return dto;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return  null;
    }
	
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

    public List<ArmyVo> getArmys() {
		return armys;
	}

	public void setArmys(List<ArmyVo> armys) {
		this.armys = armys;
	}

	public List<TechDto> getTechs() {
        return techs;
    }

    public void setTechs(List<TechDto> techs) {
        this.techs = techs;
    }


}
