package com.trance.view.screens.type;

/**
 * Created by Administrator on 17/10/23.
 */

public enum ButtonType {
    WORLD(0),
    RENAME(1),
    TRAIN(2),
    RANKING(3),
    UPGRADE(4),

    ;
    private int id;

    ButtonType(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public static ButtonType valueOf(int id){
        return ButtonType.values()[id];
    }
}
