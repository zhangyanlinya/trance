package com.trance.view.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.trance.view.constant.ExplodeType;
import com.trance.view.pools.ExplodePool;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.WorldUtils;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public class Explode extends GameActor {

    public final static Pool<Explode> pool = new ExplodePool();

    private ExplodeType type;

    private int num = 8;
    private float power = 7;

    private float stateTime;
    private TextureRegion currentFrame;
    private Animation<TextureRegion> animation;

    @Override
    public void draw(Batch batch, float parentAlpha){
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, false);
        batch.draw(currentFrame,this.getX(),this.getY());
        if(animation.isAnimationFinished(stateTime)){
            stateTime = 0;
            destory();
        }
    }

    public void init(World world, ExplodeType type, float x , float y){
        this.type = type;
        this.setX(x - 100);
        this.setY(y - 100);
        this.camp = 2;
        this.role = 1;
        this.atk = 5;
        animation = ResUtil.getInstance().getExplodeAnimation(type);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        WorldUtils.createExplode(world, this, num, x , y, power);
        //sound explode
        Music music = ResUtil.getInstance().get("audio/explode/explode.mp3", Music.class);
        music.play();
    }

    private void destory(){
        this.alive = false;
        this.remove();
        pool.free(this);
    }

    @Override
    public void dead() {
        //do nothing
    }

    @Override
    protected void fire() {

    }

    @Override
    protected void move() {

    }
}
