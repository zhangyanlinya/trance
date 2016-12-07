package com.trance.view.screens.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.trance.view.TranceGame;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.freefont.FreeFont;


/**
 * Created by Administrator on 2016/11/16 0016.
 */

public abstract class BaseScreen extends ScreenAdapter{

    protected TranceGame tranceGame;
    private float width;
    private float height;
    private Batch batch;
    private String msg;
    private FreeBitmapFont basefont;
    public volatile boolean showloading;



    /**
     * @param tranceGame
     */
    public BaseScreen(TranceGame tranceGame){
        this.tranceGame = tranceGame;
        batch = new SpriteBatch();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        basefont = FreeFont.getBitmapFont("base");
        basefont.setColor(Color.RED);
        basefont.setSize(46);
        fontHeight = height / 2;
    }


    public void showMsg(String msg){
        if(msg == null || msg.trim().equals("")){
            return;
        }
        this.msg = msg;
        basefont.appendText(msg);
        showTime = System.currentTimeMillis();
    }

    public void showLoading(){
        showloading = true;
    }

    public void hideLoading(){
        showloading = false;
    }


    private long showTime;
    private long delayShow = 3000;
    private float fontHeight;

    @Override
    public void render(float delta) {
        super.render(delta);
        if (msg != null && !msg.equals("")) {
            batch.begin();
            basefont.draw(batch, msg, width / 2 - 200, fontHeight);
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
    private  boolean destory;
    @Override
    public void dispose() {
        if(destory){
            return;
        }
        super.dispose();
//        try {
            basefont.dispose();  //TODO 还是要解决掉。
//        }catch (Exception e){}
        batch.dispose();
        destory = true;
    }

}
