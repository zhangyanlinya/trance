package com.trance.empire.modules.battle.model;

import java.util.List;

import com.trance.empire.modules.army.model.ArmyVo;
import com.trance.empire.modules.replay.model.Click;

import io.protostuff.Tag;

public class ReqFinishBattle {
	
	@Tag(1)
	private int x;
	
	@Tag(2)
	private int y;
	
	@Tag(3)
	private int state;
	
	@Tag(4)
	private List<ArmyVo> armys;
	
	@Tag(5)
	private List<Click> clicks;
	
	@Tag(6)
	private String sign;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public List<ArmyVo> getArmys() {
		return armys;
	}

	public void setArmys(List<ArmyVo> armys) {
		this.armys = armys;
	}

	public List<Click> getClicks() {
		return clicks;
	}

	public void setClicks(List<Click> clicks) {
		this.clicks = clicks;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
