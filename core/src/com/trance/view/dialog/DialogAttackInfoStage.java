package com.trance.view.dialog;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.modules.battle.handler.BattleCmd;
import com.trance.empire.modules.battle.model.AttackInfoDto;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.view.TranceGame;
import com.trance.view.constant.UiType;
import com.trance.view.dialog.base.BaseStage;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.freefont.FreeFont;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;
import com.trance.view.utils.TimeUtil;

import java.util.List;


/**
 *
 */
public class DialogAttackInfoStage extends BaseStage {

    private Image bgImage;
    private List<AttackInfoDto> attackInfos;
    private FreeBitmapFont font;
    private static final int MAX_RANKING = 10;
    private boolean init;

    public DialogAttackInfoStage(TranceGame tranceGame) {
        super(tranceGame);
    }

    private void init() {
		attackInfos = getAttackInfo();
		StringBuilder sb = new StringBuilder();
		if(attackInfos != null){
			for(AttackInfoDto info : attackInfos){
				sb.append(info.getPlayerName());
			}
		}

		font = FontUtil.getFont();
		font.appendText(sb.toString());
        init = true;
    }
    
    public void show(){
    	if(!init){
    		init();
    	}

    	this.clear();
    	this.setVisible(true);
    	
    	bgImage = new Image(ResUtil.getInstance().getUi(UiType.BLANK));
        bgImage.getColor().a = 0.6f;
	    bgImage.setWidth(getWidth() * 0.6f);
	    bgImage.setHeight(getHeight() * 0.5f);
	    bgImage.setPosition(getWidth()/2 - bgImage.getWidth()/2,  getHeight()/2 - bgImage.getHeight()/2);
	    addActor(bgImage);
	      
	    Image close = new Image(ResUtil.getInstance().getUi(UiType.CLOSE));
	    close.setPosition(getWidth()/2 + bgImage.getWidth()/2,  getHeight()/2 + bgImage.getHeight()/2);
	    close.addListener(new ClickListener(){
	
			@Override
			public void clicked(InputEvent event, float x, float y) {
					getTranceGame().mapScreen.setAttackInfoDailog(false);
			}
	    });
	    addActor(close);
    	
	    if(attackInfos == null){
	    	return;
	    }
	    
    	int i = 1;
    	float side = bgImage.getHeight() / MAX_RANKING;
    	for(AttackInfoDto dto : attackInfos){
			String  rewardMsg = RewardService.getRewardMsg(dto.getRewards());
			Label label = FreeFont.getLabel(TimeUtil.betweenTime(dto.getTime()) + ": " +dto.getPlayerName() +" "+ MsgUtil.getInstance().getLocalMsg("looted") +"  "+ rewardMsg);
			label.setBounds(getWidth()/2 - bgImage.getWidth()/2,  getHeight()/2 + bgImage.getHeight()/2 - side * i, side, side);
    		addActor(label);
	    	i ++;
    	}
    }
    

    private List<AttackInfoDto> getAttackInfo(){
    	Response response = SocketUtil.send(Request.valueOf(Module.BATTLE, BattleCmd.GET_ATTACK_INFO, null),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return null;
		}
		
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		if(text != null){
			List<AttackInfoDto> rankList = JSON.parseArray(text, AttackInfoDto.class);
			return rankList;
		}
		return null;
	}
	
	public void dispose(){
		super.dispose();
		if(init){
			init = false;
		}
	}

}
