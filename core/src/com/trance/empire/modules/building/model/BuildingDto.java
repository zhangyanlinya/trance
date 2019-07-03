package com.trance.empire.modules.building.model;


import io.protostuff.Tag;

/**
 * 主城建筑DTO
 * 
 * @author zyl
 *
 */
public class BuildingDto{

    @Tag(1)
    private int mid;

    @Tag(2)
    private int x;

    @Tag(3)
    private int y;

    @Tag(4)
    private int lvl;

    @Tag(5)
    private long htime;

    @Tag(6)
    private long cdtime;

    @Tag(7)
    private long etime;

    public String getKey(){
        return x + "_" + y;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
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

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public long getCdtime() {
        return cdtime;
    }

    public void setCdtime(long cdtime) {
        this.cdtime = cdtime;
    }

    public long getEtime() {
        return etime;
    }

    public void setEtime(long etime) {
        this.etime = etime;
    }

    public long getHtime() {
        return htime;
    }

    public void setHtime(long htime) {
        this.htime = htime;
    }

    @Override
    public String toString() {
        return "[mid=" + mid + ", x=" + x + ", y=" + y + "]";
    }
}
