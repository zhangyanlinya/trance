package com.trance.view.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.trance.view.TranceGame;
import com.trance.view.constant.UiType;
import com.trance.view.dialog.base.BaseStage;
import com.trance.view.utils.ResUtil;

import var3d.net.freefont.FreeFont;

/**
 * Created by Administrator on 2016/11/15 0015.
 */

public class DialogMsgStage extends BaseStage {

    private Image bgImage;
    private boolean init;
    private Label label;

    public DialogMsgStage(TranceGame tranceGame) {
        super(tranceGame);
    }

    private void init(){
        bgImage = new Image(ResUtil.getInstance().getUi(UiType.BLANK));
        bgImage.getColor().a = 0.6f;
        bgImage.setWidth(getWidth() * 0.6f);
        bgImage.setHeight(getHeight() * 0.5f);
        bgImage.setPosition(getWidth() / 2 - bgImage.getWidth() / 2, getHeight() / 2 - bgImage.getHeight() / 2);
        addActor(bgImage);

    }

    public void show(String msg) {
        if(msg == null || msg.equals("")){
            return;
        }

        if (!init) {
            init();
            init = true;
        }

        this.setVisible(true);

        if(label != null){
            label.remove();
        }

        label = FreeFont.getLabel(msg);
        addActor(label);

     }

    @Override
    public void dispose() {
        super.dispose();
        if(init){
            init = false;
        }
    }
}