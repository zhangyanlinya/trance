package com.trance.view.dialog;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.ranking.handler.RankingCmd;
import com.trance.view.TranceGame;
import com.trance.view.actors.WorldImage;
import com.trance.view.constant.UiType;
import com.trance.view.dialog.base.BaseStage;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;

import java.util.HashMap;
import java.util.List;


/**
 *
 */
public class DialogRankUpStage extends BaseStage {

    private Image bgImage;
    private List<PlayerDto> players;
    private FreeBitmapFont font;
    private static final int MAX_RANKING = 10;
    private boolean init;
    
    public DialogRankUpStage(TranceGame tranceGame) {
        super(tranceGame);
    }

    private void init() {
        players = getUpRank();
        StringBuilder sb = new StringBuilder();
        if(players != null){
        	for(PlayerDto player : players){
        		sb.append(player.getPlayerName());
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
					getTranceGame().mapScreen.setRankUpDailog(false);
			}
	    });
	    addActor(close);
    	
	    if(players == null){
	    	return;
	    }
	    
    	int i = 1;
    	float side = bgImage.getHeight() / MAX_RANKING;
    	for(PlayerDto dto : players){
    		WorldImage rank = new WorldImage(ResUtil.getInstance().get("army/2/zoulu/0.png", Texture.class), font, dto);
    		rank.setBounds(getWidth()/2 - bgImage.getWidth()/2 + side,  getHeight()/2 + bgImage.getHeight()/2 - side * i, side, side);
    		addActor(rank);
	    	i ++;
    	}
    }
    

    private List<PlayerDto> getUpRank(){
    	Response response = SocketUtil.send(Request.valueOf(Module.RANKING, RankingCmd.GET_PLAYER_UP_RANKING, null),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return null;
		}
		
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		HashMap<String,Object> result = JSON.parseObject(text, HashMap.class);
		if(result == null){
			return null;
			
		}
		int code = Integer.valueOf(String.valueOf(result.get("result")));
		if(code != Result.SUCCESS){
			MsgUtil.getInstance().showMsg(Module.RANKING,code);
			return null;
		}
		
		Object cobj = result.get("content");
		if(cobj != null){
			List<PlayerDto> rankList = JSON.parseArray(JSON.toJSON(cobj).toString(), PlayerDto.class);
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
