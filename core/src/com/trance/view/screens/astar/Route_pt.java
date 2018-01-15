package com.trance.view.screens.astar;


public class Route_pt {

    private int x;

    private int y;

    public Route_pt(){

    }
    public Route_pt(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Route_pt(Route_pt pt){
        this.x = pt.x;
        this.y = pt.y;
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
