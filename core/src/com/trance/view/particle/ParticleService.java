package com.trance.view.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/19 0019.
 */

public class ParticleService {

    private static ParticleService particleService;
    private ParticleEffect particle;  //粒子实例
    private ParticleEffect tem;  //临时变量
    private ParticleEffectPool particlepool;   //一个粒子系统的统一管理的类，负责管理粒子系统的产生回收，可以用它的obtain()方法得到一个ParticleEffect实例
    private ArrayList<ParticleEffect> particlelist;

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
        particle.load(Gdx.files.internal("fire2.p"), Gdx.files.internal(""));  //还可以换成particle_fire.p
        particlepool = new ParticleEffectPool(particle, 1, 2);
        particlelist = new ArrayList<ParticleEffect>();  //实例化装粒子效应的集合
    }

    public void draw(Batch batch, float x, float y){
        tem = particlepool.obtain();
        tem.setPosition(x, y);
        particlelist.add(tem);
//        batch.end();
        for(int i=0;i<particlelist.size();i++){
            particlelist.get(i).draw(batch, Gdx.graphics.getDeltaTime());
        }
//        batch.begin();

        //清除已经播放完成的粒子系统
        ParticleEffect temparticle;
        for(int i=0;i<particlelist.size();i++){
            temparticle=particlelist.get(i);
            if(temparticle.isComplete()){
                particlelist.remove(i);
            }
        }
    }

    public void disponse(){
        particle.dispose();
        if(tem!=null)
            tem.dispose();
        particlepool.clear();
    }
}
