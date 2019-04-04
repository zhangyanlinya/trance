package com.trance.view.model;


public class Gird {

    public int id;

    public int i;

    public int j;

    public float x;

    public float y;

    public Gird(){
    }

    public Gird(int id, int i, int j, float x , float y){
        this.id = id;
        this.i = i;
        this.j = j;
        this.x = x;
        this.y = y;
    }

    public Gird(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public String toString() {
        return "Gird{" +
                "id=" + id +
                ", i=" + i +
                ", j=" + j +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
