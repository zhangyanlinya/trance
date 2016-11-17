package com.trance.view.screens.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
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
    protected Stage stage;
    protected Window window;
    protected FreeBitmapFont font;
    protected Window.WindowStyle style;
    private Texture windowBg;

    public BaseScreen(TranceGame tranceGame){
        this.tranceGame = tranceGame;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        stage = new Stage(new FillViewport(width,height));
        windowBg = new Texture(Gdx.files.internal("world/tips.png"));
        TextureRegionDrawable tips = new TextureRegionDrawable( new TextureRegion(
                windowBg));
        Drawable background = new TextureRegionDrawable(tips);
        font = FontUtil.getFont();
        style = new Window.WindowStyle(font, Color.RED, background);
        initWindow("");
    }

    private void initWindow(String msg){
        window = new Window(msg,style);
        window.setPosition(width/2 - window.getWidth()/2, height/2 - window.getHeight()/2);
        window.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
            }
        });
    }

    public void showWindow(String msg){
        if(msg == null || msg.trim().equals("")){
            return;
        }
        font.appendText(msg);
        if(window != null) {
            window.clearListeners();
            window.remove();
        }
        initWindow(msg);
        stage.addActor(window);
    }

    public void hideWindow(){
        window.clearListeners();
        window.remove();
    }


    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        windowBg.dispose();
    }
}
