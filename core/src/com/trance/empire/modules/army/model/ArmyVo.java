package com.trance.empire.modules.army.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import io.protostuff.Tag;

public class ArmyVo implements Cloneable{
	
	@Tag(1)
	private int id;
	
	@Tag(2)
	private int lvl;
	
	@Tag(3)
	private int amout;
	
	private transient TextureRegion region;
	private transient boolean go;
	private transient Rectangle rect;
	
	public static ArmyVo valueOf(ArmyDto armyDto){
		ArmyVo vo = new ArmyVo();
		vo.amout = armyDto.getAmout();
		vo.lvl = armyDto.getLevel();
		vo.id = armyDto.getId();
		return vo;
	}
	
	@Override
	public ArmyVo clone(){
		try {
			return (ArmyVo) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public int getAmout() {
		return amout;
	}
	public void setAmout(int amout) {
		this.amout = amout;
	}
	
	public TextureRegion getRegion() {
		return region;
	}
	public void setRegion(TextureRegion region) {
		this.region = region;
	}
	public boolean isGo() {
		return go;
	}
	public void setGo(boolean go) {
		this.go = go;
	}
	public Rectangle getRect() {
		return rect;
	}
	public void setRect(Rectangle rect) {
		this.rect = rect;
	}	
}
