package com.trance.view.screens.type;

/**
 * Created by Administrator on 17/10/23.
 */

public enum ButtonType {
    WORLD(0),
    RENAME(1),
    TRAIN(2),
    RANKING(3),
    ATTACKINFO(6),

    ;
    private int id;

    ButtonType(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public static ButtonType valueOf(int id){
       for(ButtonType type : ButtonType.values()){
           if(type.getId() == id){
               return type;
           }
       }
       return null;
    }
}
