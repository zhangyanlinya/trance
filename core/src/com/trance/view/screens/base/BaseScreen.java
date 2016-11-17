package com.trance.view.screens.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.trance.view.TranceGame;
import com.trance.view.utils.FontUtil;

import var3d.net.freefont.FreeBitmapFont;

/**
 * Created by Administrator on 2016/11/16 0016.
 */

public class BaseScreen extends ScreenAdapter{

    protected TranceGame tranceGame;
    protected float width;
    protected float height;
    protected Batch batch;
    protected String msg;
    protected FreeBitmapFont font;


    public BaseScreen(TranceGame tranceGame){
        this.tranceGame = tranceGame;
        batch = new SpriteBatch();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        font = FontUtil.getFont();
    }



    public void showWindow(String msg){
        if(msg == null || msg.trim().equals("")){
            return;
        }
        this.msg = msg;
        font.appendText(msg);
    }

    public void hideWindow(){

    }

    private long showTime;

    @Override
    public void render(float delta) {
        super.render(delta);
        if(msg != null && !msg.equals("")) {
            batch.begin();
            font.setColor(Color.RED);
            font.draw(batch, msg, width / 2, height / 2);
            batch.end();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        batch.dispose();
    }
}
