package com.trance.view.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.trance.view.utils.TimeUtil;

/**
 * 进度
 */

public class Progressbar extends Actor {

    private long expireTime;
    private long coolTime;
    //画笔
    private ShapeRenderer renderer;

    public Progressbar(long expireTime, long coolTime) {
        this.expireTime = expireTime;
        this.coolTime = coolTime;
        renderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        long now = TimeUtil.getServerTime();
        long leftTime = expireTime - now;
        if(leftTime <= 0){
            return;
        }

        //draw progress
        float percent = (coolTime - leftTime)/ (float)coolTime;
        renderer.setColor(Color.RED);
        renderer.begin(ShapeRenderer.ShapeType.Line);
       // renderer.rect(Gdx.graphics.getWidth() / 4 , 100, Gdx.graphics.getWidth() / 2, 40);
        renderer.rect(this.getX() + getWidth() + 10, this.getY() +2, Gdx.graphics.getWidth() / 4, 40);
        renderer.end();
        if(percent < 0.2){
            renderer.setColor(Color.RED);
        }else if(percent < 0.5){
            renderer.setColor(Color.YELLOW);
        }else{
            renderer.setColor(Color.GREEN);
        }
        renderer.begin(ShapeRenderer.ShapeType.Filled);
       // renderer.rect(Gdx.graphics.getWidth() / 4 + 2, 104, percent * Gdx.graphics.getWidth()/2 - 6, 34);
        renderer.rect(2 + this.getX() + getWidth() + 10, this.getY() + 6, percent * Gdx.graphics.getWidth()/4 -6, 34);
        renderer.end();
    }

    public boolean isFinish(){
        long now = TimeUtil.getServerTime();
        long leftTime = expireTime - now;
        return leftTime <= 0;
    }
}
