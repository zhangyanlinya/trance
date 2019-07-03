package com.trance.empire.modules.tech.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class TechDto {
    private int id;
    private int level;
    private int amout;
    private int useAmount;
    private transient TextureRegion region;
    private transient Rectangle rect;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAmout() {
        return amout;
    }

    public void setAmout(int amout) {
        this.amout = amout;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public int getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(int useAmount) {
        this.useAmount = useAmount;
    }

    public void resetAmount(){
        this.useAmount = amout;
    }
}
