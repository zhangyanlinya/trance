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
    private float x;
    private float y;
    private int buildingType;

    public DialogOperateStage(TranceGame tranceGame) {
        super(tranceGame);
    }

    private void init() {

        init = true;
    }

    public void show(float x, float y){
        this.x = x;
        this.y = y;
        show();
    }
    
    public void show(){
    	if(!init){
    		init();
    	}

    	this.clear();
    	this.setVisible(true);

		Image bgImage = new Image(ResUtil.getInstance().getUi(UiType.BLANK));
        bgImage.getColor().a = 0.6f;
	    bgImage.setWidth(300);
	    bgImage.setHeight(100);
	    bgImage.setPosition(x - bgImage.getWidth()/2, y);
	    addActor(bgImage);
	      
	    Image close = new Image(ResUtil.getInstance().getUi(UiType.CLOSE));
	    close.setPosition(x + bgImage.getWidth()/2,  y + bgImage.getHeight());
	    close.addListener(new ClickListener(){
	
			@Override
			public void clicked(InputEvent event, float x, float y) {
					getTranceGame().mapScreen.setOperateStageDailog(false, x, y);
			}
	    });
	    addActor(close);

        if(buildingType > 0){
            initBuildingOperatorButton(buildingType);
        }
    }

    // 初始化操作按钮
    private void initBuildingOperatorButton(int buildingType){

    }
    

	public void dispose(){
		super.dispose();
		if(init){
			init = false;
		}
	}

}
