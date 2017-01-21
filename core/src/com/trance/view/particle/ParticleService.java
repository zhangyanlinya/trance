package com.trance.view.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.trance.view.particle.actor.ParticleEffectActor;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class ParticleService {

    private static ParticleService particleService;
    private ParticleEffect particle;  //粒子实例
    private ParticleEffect effect;  //临时变量
    private ParticleEffectPool particlepool;   //一个粒子系统的统一管理的类，负责管理粒子系统的产生回收，可以用它的obtain()方法得到一个ParticleEffect实例

    public static ParticleService getInstance(){
        if(particleService == null){
            particleService = new ParticleService();
        }
        return particleService;
    }

    public ParticleService(){
        particle = new ParticleEffect();
        //Gdx.files.internal()的位置是在项目的asset根目录下，同理在asset下的data文件夹的话，格式应该为Gdx.files.internal(“data/particle.p”).
        //第一个参数是Particle Editor生成的编辑文件（注意后缀名也是自己取的，读取的时候记得对应），第二个参数是形成单个粒子的图片文件。
        particle.load(Gdx.files.internal("particle/fire.p"), Gdx.files.internal("particle/"));  //还可以换成particle_fire.p
        particlepool = new ParticleEffectPool(particle, 5, 10);
    }

    public Actor addEffectActor(float x, float y){
        effect = particlepool.obtain();
        final ParticleEffectActor actor = new ParticleEffectActor(effect);
        actor.setPosition(x, y);
        effect.setPosition(x, y);

        ScaleByAction action = Actions.scaleBy(x, y, 1f);
        RunnableAction run = Actions.run(new Runnable() {
                 @Override
                 public void run() {
                         actor.remove();
                     }
        });
        actor.addAction(Actions.sequence(action,run));
        actor.start();
        return actor;
    }

    public void disponse(){
        particle.dispose();
        if (effect != null)
            effect.dispose();
        particlepool.clear();
    }
}
