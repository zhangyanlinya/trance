package com.trance.view.dialog;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.trance.view.TranceGame;
import com.trance.view.constant.UiType;
import com.trance.view.dialog.base.BaseStage;
import com.trance.view.utils.ResUtil;


/**
 *
 */
public class DialogOperateStage extends BaseStage {

    private boolean init;

    public DialogOperateStage(TranceGame tranceGame) {
        super(tranceGame);
    }

    private void init() {

        init = true;
    }
    
    public void show(){
    	if(!init){
    		init();
    	}

    	this.clear();
    	this.setVisible(true);

		Image bgImage = new Image(ResUtil.getInstance().getUi(UiType.BLANK));
        bgImage.getColor().a = 0.6f;
	    bgImage.setWidth(100);
	    bgImage.setHeight(50);
	    bgImage.setPosition(getWidth()/2 - bgImage.getWidth()/2,  getHeight()/2 - bgImage.getHeight()/2);
	    addActor(bgImage);
	      
	    Image close = new Image(ResUtil.getInstance().getUi(UiType.CLOSE));
	    close.setPosition(getWidth()/2 + bgImage.getWidth()/2,  getHeight()/2 + bgImage.getHeight()/2);
	    close.addListener(new ClickListener(){
	
			@Override
			public void clicked(InputEvent event, float x, float y) {
					getTranceGame().mapScreen.setOperateStageDailog(false, x, y);
			}
	    });
	    addActor(close);

    	

    }
    

	public void dispose(){
		super.dispose();
		if(init){
			init = false;
		}
	}

}
