package com.trance.view.particle.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Administrator on 2017/1/20 0020.
 */

public class ParticleEffectActor extends Actor {
    private ParticleEffect particleEffect;
    private Vector2 acc = new Vector2();
    public ParticleEffectActor(ParticleEffect particleEffect) {
        super();
        this.particleEffect = particleEffect;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        particleEffect.draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        acc.set(getWidth() / 2, getHeight() / 2);
        localToStageCoordinates(acc);
        particleEffect.setPosition(acc.x, acc.y);
        particleEffect.update(delta);

    }

    public void start(){
        particleEffect.start();
    }
}