package com.trance.view.dialog;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.trance.common.basedb.BasedbService;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.common.util.ProtostuffUtil;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.basedb.CityElement;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.PlayerDto;
import com.trance.empire.modules.ranking.handler.RankingCmd;
import com.trance.empire.modules.world.handler.WorldCmd;
import com.trance.empire.modules.world.model.ReqSpyAnyOne;
import com.trance.empire.modules.world.model.ResSpyAnyOne;
import com.trance.view.TranceGame;
import com.trance.view.actors.RankImage;
import com.trance.view.constant.UiType;
import com.trance.view.dialog.base.BaseStage;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.mapdata.MapData;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 *
 */
public class DialogRankUpStage extends BaseStage {

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

		Image bgImage = new Image(ResUtil.getInstance().getUi(UiType.BLANK));
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
    	for(final PlayerDto dto : players){
			Texture texture = ResUtil.getInstance().get("building/1.png");
			if(i == 1){
				texture = ResUtil.getInstance().getUi(UiType.ONE);
			}else if(i == 2){
				texture = ResUtil.getInstance().getUi(UiType.TWO);
			}else if(i == 3){
				texture = ResUtil.getInstance().getUi(UiType.THREE);
			}
    		RankImage rank = new RankImage(texture, font, dto);
    		rank.setBounds(getWidth()/2 - bgImage.getWidth()/2 + side,  getHeight()/2 + bgImage.getHeight()/2 - side * i, side, side);
    		addActor(rank);

			Image dest = new Image(ResUtil.getInstance().getUi(UiType.FIXED));
			dest.setBounds(getWidth()/2 + bgImage.getWidth()/2 - side,  getHeight()/2 + bgImage.getHeight()/2 - side * i, side, side);
			addActor(dest);

			dest.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(Player.player.getId() == dto.getId()){
						return;
					}
					
					ReqSpyAnyOne req = new ReqSpyAnyOne();
					req.setTargetId(dto.getId());
					Response response = SocketUtil.send(Request.valueOf(Module.WORLD, WorldCmd.SPY_ANYONE, req),true);
					if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
						return;
					}
					byte[] bytes = response.getValueBytes();
					Result<ResSpyAnyOne> result = ProtostuffUtil.parseObject(bytes,Result.class);
					int code = result.getCode();
					if(code != Result.SUCCESS){
						MsgUtil.getInstance().showMsg(Module.WORLD, code);
						return;
					}

					ResSpyAnyOne res = result.getContent();
					if (res.getMap() != null) {
						dto.setMap(res.getMap());
					}else{
						dto.setMap(MapData.clonemap());
					}

					List<BuildingDto> buildings  = res.getBuildings();
					if(buildings != null){
						for(BuildingDto bto : buildings){
							dto.addBuilding(bto);
						}
					}

					getTranceGame().mapScreen.setRankUpDailog(false);
					getTranceGame().mapScreen.setPlayerDto(dto);
					getTranceGame().setScreen(getTranceGame().mapScreen);
				}
			});
	    	i ++;
    	}
    }
    

    private List<PlayerDto> getUpRank(){
    	Response response = SocketUtil.send(Request.valueOf(Module.RANKING, RankingCmd.GET_PLAYER_UP_RANKING, null),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return null;
		}
		
		byte[] bytes = response.getValueBytes();
		Result<List<PlayerDto>> result = ProtostuffUtil.parseObject(bytes, Result.class);
		if(result == null){
			return null;
			
		}
		int code = result.getCode();
		if(code != Result.SUCCESS){
			MsgUtil.getInstance().showMsg(Module.RANKING,code);
			return null;
		}
		
		return result.getContent();
	}
	
	public void dispose(){
		super.dispose();
		if(init){
			init = false;
		}
	}

}
