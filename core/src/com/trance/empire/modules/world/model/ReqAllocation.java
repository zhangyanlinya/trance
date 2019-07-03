package com.trance.empire.modules.world.model;

import io.protostuff.Tag;

public class ReqAllocation {
	
	@Tag(1)
	private int x;
	
	@Tag(2)
	private int y;
	
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
}
