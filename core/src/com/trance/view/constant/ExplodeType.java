package com.trance.view.constant;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public enum ExplodeType {

    Fire_Red(1),
    Fire_blue(2),
    ;

    int id;

    ExplodeType(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public static ExplodeType valueOf(int id){
        for(ExplodeType et : ExplodeType.values()){
            if(et.getId() == id){
                return et;
            }
        }
        return  null;
    }
}
