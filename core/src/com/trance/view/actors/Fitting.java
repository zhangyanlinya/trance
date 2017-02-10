package com.trance.view.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.fitting.model.FittingType;
import com.trance.empire.modules.player.model.Player;
import com.trance.view.constant.RangeType;
import com.trance.view.pools.FittingPool;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.WorldUtils;

/**
 * Created by Administrator on 2017/2/4 0004.
 */

public class Fitting extends GameActor {

    public final static Pool<Fitting> fittingPool = new FittingPool();
    private FittingType fittingType;
    private Body body;
    private TextureRegion textureRegion;
    private float fireDelay = 2000;
    private float speed = 2;
    private GameActor parent;

    public void init(World world, GameActor parent, FittingType fittingType, float x , float y, float width, float height) {
        super.init(x, y, width, height);
        this.parent = parent;
        this.fittingType = fittingType;
        this.alive = true;
        this.move = true;
        this.camp = 2;
        this.hp = maxhp;
        this.degrees = 0;
        textureRegion = ResUtil.getInstance().getArmyTextureRegion(fittingType.id);
        if(this.getWidth() == 0 && this.getHeight() == 0){
            this.setWidth(textureRegion.getRegionWidth());
            this.setHeight(textureRegion.getRegionHeight());
        }
        switch(fittingType){
            case ONE:
                range = RangeType.SHORT;
                hp = 50;
                break;
            case TWO:
                atk = 20;
                fireDelay = 1000;
                break;
            case THREE:
                range = RangeType.SHORT;
                hp = 30;
                speed = 1.5f;
                break;
            case FOUR:

                break;
            case FIVE:
                fireDelay = 4000;
                atk = 200;
                speed = 1.5f;
                break;
            case SIX:
                atk = 50;
                range = RangeType.SHORT;
                speed = 1;
                break;
            case SEVEN:
                hp = 40;
                break;
            case EIGHT:
                speed = 4;
                range = 20;
                break;
            case NINE:
                atk = 100;
                break;
            default:
                break;
        }

        ArmyDto dto = Player.player.getArmys().get(fittingType.id);
        if(dto != null && dto.getLevel() > 0){
            atk *= dto.getLevel();
            hp *= dto.getLevel();
            maxhp = hp;
        }


        if(world == null){
            body = null;
            return;
        }
        body = WorldUtils.createFitting(world,fittingType.id, x, y, width, height);
        body.setUserData(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, getX(), getY(), hw,
                hh, getWidth(), getHeight(), getScaleX(),
                getScaleY(), getRotation());

    }

    @Override
    public void dead() {
        alive = false;
        remove();
        fittingPool.free(this);
    }

    @Override
    protected void fire() {

    }

    @Override
    protected void move() {

    }
}
