package com.trance.empire.modules.fitting.model;

import com.trance.empire.modules.army.model.ArmyType;

/**
 * Created by Administrator on 2017/2/4 0004.
 */

public enum FittingType {
    ONE(1),

    TWO(2),

    THREE(3),

    FOUR(4),

    FIVE(5),

    SIX(6),

    SEVEN(7),

    EIGHT(8),

    NINE(9);

    public final int id;

    FittingType(int id) {
        this.id = id;
    }

    public static FittingType valueOf(int id){
        for(FittingType type : FittingType.values()){
            if(type.id == id){
                return type;
            }
        }
        return null;
    }
}
