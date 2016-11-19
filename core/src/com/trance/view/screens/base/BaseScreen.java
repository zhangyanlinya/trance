package com.trance.view.screens.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.trance.view.TranceGame;
import com.trance.view.utils.ResUtil;

import var3d.net.freefont.FreeBitmapFont;
import var3d.net.freefont.FreeFont;

/**
 * Created by Administrator on 2016/11/16 0016.
 */

public abstract class BaseScreen extends ScreenAdapter{

    protected TranceGame tranceGame;
    private float width;
    private float height;
    private Batch batch;
    private String msg;
    private FreeBitmapFont font;
    public volatile boolean showloading;

    private TextureRegion[] loadingRegion;
    private float stateTime;
    //当前帧
    private TextureRegion currentFrame;
    private Animation animation;

    private boolean isInit;

    public BaseScreen(TranceGame tranceGame){
        this.tranceGame = tranceGame;
        batch = new SpriteBatch();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        font = FreeFont.getBitmapFont("base");
        font.setColor(Color.RED);
        font.setSize(46);
        fontHeight = height / 2;
    }

    private void initAnimation(){
        loadingRegion = ResUtil.getInstance().getLoadingAnimation();
        //0.06*11=0.66 大概就是1秒钟播放完这个动画。
        animation = new Animation(0.5f, loadingRegion);
    }

    public void showMsg(String msg){
        if(msg == null || msg.trim().equals("")){
            return;
        }
        this.msg = msg;
        font.appendText(msg);
    }

    public void showLoading(){
//        if(!isInit){
//            initAnimation();
//            isInit = true;
//        }
        showloading = true;
        this.msg = "get data from server ...";
    }

    public void hideLoading(){
//        showloading = false;
//        this.msg = null;
    }


    private long showTime = System.currentTimeMillis();
    private long delayShow = 3000;
    private float fontHeight;

    @Override
    public void render(float delta) {
        super.render(delta);

        if(showloading) {
//            stateTime += delta;
//            //下一帧
//            currentFrame = animation.getKeyFrame(stateTime, true);
//            batch.begin();
//            batch.draw(currentFrame, width/2 -100, height/2 -100 , 200F, 200F);
//            batch.end();
            batch.begin();
            font.setColor(Color.WHITE);
            font.draw(batch, msg, width / 2 - 120, fontHeight);
            batch.end();
        }

        if (msg != null && !msg.equals("")) {
            batch.begin();
            font.draw(batch, msg, width / 2 - 120, fontHeight);
            batch.end();

            long now = System.currentTimeMillis();
            long d = now - showTime;
            long a = d / 1000;
            fontHeight += a;
            if (d > delayShow) {
                msg = null;
                showTime = now;
                fontHeight = height / 2;
                return;
            }
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        batch.dispose();
        isInit = false;
    }

}
