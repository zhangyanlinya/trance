package com.trance.empire.modules.army.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import io.protostuff.Tag;

public class ArmyDto {

	@Tag(1)
	private int id;

	@Tag(2)
	private int level;

	@Tag(3)
	private int amout;

	@Tag(4)
	private int addAmount;

	@Tag(5)
	private long expireTime;

	private transient TextureRegion region;
	private transient boolean go;
	private transient Rectangle rect;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public int getAddAmount() {
		return addAmount;
	}
	public void setAddAmount(int addAmount) {
		this.addAmount = addAmount;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

}
