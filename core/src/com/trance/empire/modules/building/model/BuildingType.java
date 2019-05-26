package com.trance.empire.modules.building.model;

/**
 * 主城建筑类型
         */
public enum BuildingType {
    NONE(0, 0),  //空地

    OFFICE(1, 3), // 官府

    HOUSE(2, 2), // 民居

    BARRACKS(3, 2), // 兵营

    /**
     * 加农
     */
    CANNON(4, 1),

    /**
     * 火箭
     */
    ROCKET(5, 1),

    /**
     * 火焰
     */
    FLAME(6, 1),

    /**
     * 机枪
     */
    GUN(7, 1),

    /**
     * 箭塔
     */
    TOWER(8, 1),

    /**
     * 迫击炮
     */
    MORTAR(9, 1),

    /**
     *
     */
    TREE(10, 1),

    /**
     *
     */
    GRASS(11, 1), ;

    final int id;

    final int occupy;

    BuildingType(int id, int occupy) {
        this.id = id;
        this.occupy = occupy;
    }

    public int getId() {
        return id;
    }

    public int getOccupy() {
        return occupy;
    }

    public static BuildingType valueOf(int id){
        return BuildingType.values()[id];
    }
}
