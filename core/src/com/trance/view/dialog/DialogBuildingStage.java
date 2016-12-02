package com.trance.view.dialog;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.trance.common.basedb.BasedbService;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.building.handler.BuildingCmd;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.BuildingType;
import com.trance.empire.modules.building.model.basedb.CityElement;
import com.trance.empire.modules.coolqueue.model.CoolQueueDto;
import com.trance.empire.modules.coolqueue.model.CoolQueueType;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.reward.result.ValueResultSet;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.view.TranceGame;
import com.trance.view.actors.BuildingImage;
import com.trance.view.actors.Timer;
import com.trance.view.constant.UiType;
import com.trance.view.dialog.base.BaseStage;
import com.trance.view.freefont.FreeFont;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class DialogBuildingStage extends BaseStage {

    private Image bgImage;
    private ShapeRenderer renderer;
    private Collection<CityElement> buildings ;
    private boolean init;

    public DialogBuildingStage(TranceGame tranceGame) {
        super(tranceGame);
    }

    private void init() {
        renderer = new ShapeRenderer();
        buildings = BasedbService.listAll(CityElement.class);
    }
    
    public void show(){
    	if(!init){
    		init();
    		init = true;
    	}
    	this.setVisible(true);
    	this.clear();
    	
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
					getTranceGame().mapScreen.setBuildingDailog(false);
			}
	    });
	    addActor(close);
    	
    	ConcurrentMap<Integer, BuildingDto> building_map = Player.player.getBuildings();
    	int i = 0;
    	float side = bgImage.getWidth() / 4;
    	for(final CityElement building : buildings){
			Texture texture = ResUtil.getInstance().getBuildingTexture(building.getId());
			BuildingDto dto = building_map.get(building.getId());
			BuildingImage image = new BuildingImage(texture,dto);
			image.setWidth(side);
			image.setHeight(side);
			
			int rate  = i % 3;
			float orgX =getWidth()/2 - bgImage.getWidth()/2;
			float x = rate * side + orgX + side/2;
			int rate2 = i/3 + 1;
			float orgY = getHeight()/2 + bgImage.getHeight()/2;
			float y =  orgY - (side/2 * 2 + rate2 * side );
			
			image.setPosition(x,y);
			addActor(image);
			i ++;
			
			image.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(timer != null && !timer.isFinish()){
						return;
					}
					updateBuilding(building.getId());
				}
			});
    	}

		Label label = FreeFont.getLabel(MsgUtil.getInstance().getLocalMsg("Click Build Image to upgrade"));
		label.setPosition(getWidth()/2,  getHeight()/2 - bgImage.getHeight()/2 + 100, Align.center);
		addActor(label);
    	
    	if(timer != null && !timer.isFinish()){
    		addActor(timer);
    	}else{
			CoolQueueDto cool = Player.player.getCoolQueueByType(CoolQueueType.BUILDING.ordinal());
	    	if(cool != null){
	    		showTimer(cool.getExpireTime());
	    	}
    	}
    }
    
    private void showTimer(long expireTime){
    	timer = new Timer(expireTime);
		timer.setPosition(getWidth()/2 - bgImage.getWidth()/2 + 100,  getHeight()/2 + bgImage.getHeight()/2 - 100);
		addActor(timer);
    }
    
    private Timer timer;
    


    @SuppressWarnings("unchecked")
	private void updateBuilding(int buildingId){
		Response response = SocketUtil.send(Request.valueOf(Module.BUILDING, BuildingCmd.UPGRADE_BUILDING_LEVEL, buildingId),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return;
		}
		
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		HashMap<String,Object> result = JSON.parseObject(text, HashMap.class);
		if(result != null){
			int code = Integer.valueOf(String.valueOf(result.get("result")));
			if(code != Result.SUCCESS){
				MsgUtil.getInstance().showMsg(Module.BUILDING,code);
				return ;
			}
			Object valueResult = result.get("valueResultSet");
			if(valueResult != null){
				ValueResultSet valueResultSet = JSON.parseObject(JSON.toJSON(valueResult).toString(), ValueResultSet.class);
				RewardService.executeRewards(valueResultSet);
			}
			
			Object coolQueue = result.get("coolQueueDto");
			if(coolQueue != null){
				CoolQueueDto coolQueueDto = JSON.parseObject(JSON.toJSON(coolQueue).toString(), CoolQueueDto.class);
				if(coolQueueDto != null){
					Player.player.getCoolQueues().put(coolQueueDto.getId(),coolQueueDto);
					showTimer(coolQueueDto.getExpireTime());
				}
			}
			
			ConcurrentMap<Integer, BuildingDto> buildings = Player.player.getBuildings();
			Object building = result.get("content");
			if(building != null){
				BuildingDto playerBuildingDto = JSON.parseObject(JSON.toJSON(building).toString(), BuildingDto.class);
				if(playerBuildingDto != null){
					BuildingDto pbd = buildings.get(playerBuildingDto.getId());
					if(pbd != null){
						pbd.setLevel(playerBuildingDto.getLevel());
						if(pbd.getId() != BuildingType.OFFICE){
							pbd.setAmount(playerBuildingDto.getLevel());
						}
					}
					this.getTranceGame().mapScreen.refreshLeftBuiding();
				}
			}
			
			this.getTranceGame().mapScreen.refreshPlayerDtoData();
			
			//如果是主城升级的话  可能有新的建筑和部队
			if(buildingId == BuildingType.OFFICE){
				Object newBuildings  = result.get("newBuildingDtos");
				if(newBuildings != null){
				  List<BuildingDto> buildingDtos = JSON.parseArray(JSON.toJSON(newBuildings).toString(), BuildingDto.class);
				  if(buildingDtos != null){
					  for(BuildingDto buildingDto : buildingDtos){
						  buildings.put(buildingDto.getId(), buildingDto);
					  }
					 this.getTranceGame().mapScreen.refreshLeftBuiding();
				  }
				}
				
				Object newArmys = result.get("newArmyDtos");
				if(newArmys != null){
					List<ArmyDto> armyDtos = JSON.parseArray(JSON.toJSON(newArmys).toString(), ArmyDto.class);
					if(armyDtos != null){
						for(ArmyDto armyDto : armyDtos){
							Player.player.addAmry(armyDto);
						}
//						 dialogArmyStage.refresh();
					}
				}
			}
			
			Sound sound = ResUtil.getInstance().getSound(0);
			sound.play();
		}
	}
	public void dispose(){
		super.dispose();
		if(init){
			renderer.dispose();
			init = false;
		}
	}
}
