package com.trance.view.model;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

public class RangeInfo {

    private float x;
    private float y;
    private float range;
    private float showTime;

    public RangeInfo(){}

    public RangeInfo(float x, float y, float range){
        this.x = x;
        this.y = y;
        this.range = range;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getShowTime() {
        return showTime;
    }

    public void setShowTime(float showTime) {
        this.showTime = showTime;
    }
}
