package com.trance.view.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.trance.view.constant.ExplodeType;
import com.trance.view.pools.ExplodePool;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.WorldUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public class Explode extends Actor {

    public final static Pool<Explode> pool = new ExplodePool();

    private ExplodeType type;

    private float x;

    private float y;

    private List<Body> bodies;

    private float dalay;

    private TextureRegion[] regions;
    private float stateTime;

    //当前帧
    private TextureRegion currentFrame;
    private Animation animation;

    @Override
    public void draw(Batch batch, float parentAlpha){
        stateTime += Gdx.graphics.getDeltaTime();
        //下一帧
        currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame,x,y);


    }

    public void init(World world, ExplodeType type, float x , float y){
        this.type = type;
        this.x = x;
        this.y = y;

        intAnimation(type);

        bodies = WorldUtils.createExplode(world, 10, x, y, 5);
    }

    public void intAnimation(ExplodeType type){
        regions = ResUtil.getInstance().getExplodeAnimation(type);
        //0.06*11=0.66 大概就是1秒钟播放完这个动画。
        animation = new Animation(0.3f, regions);
    }

}
