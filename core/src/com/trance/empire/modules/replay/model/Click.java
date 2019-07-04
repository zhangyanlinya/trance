package com.trance.empire.modules.replay.model;

import io.protostuff.Tag;

public class Click {
	
	/**
	 * 倒计时
	 */
	@Tag(1)
	private int t;
	
	@Tag(2)
	private int x;
	
	@Tag(3)
	private int y;

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

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

