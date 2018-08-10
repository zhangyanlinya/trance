package com.trance.view.dialog;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.building.handler.BuildingCmd;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.BuildingType;
import com.trance.empire.modules.building.model.WaitBuildingDto;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.reward.result.ValueResultSet;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.view.TranceGame;
import com.trance.view.actors.BuildingImage;
import com.trance.view.constant.UiType;
import com.trance.view.dialog.base.BaseStage;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;


/**
 *
 */
public class DialogOperateStage extends BaseStage {

    private boolean init;
    private float x;
    private float y;
    private BuildingDto dto;

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
        bgImage.getColor().a = 0.3f;
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


        initBuildingOperatorButton();

    }

//    private Progressbar timer;

//    private void showTimer(long expireTime, long coolTime){
//        timer = new Progressbar(expireTime,coolTime);
//
//        timer.setPosition(getWidth()/2 - x/2 + 100,  getHeight()/2 + y/2 - 100);
//        addActor(timer);
//    }

    // 初始化操作按钮
    private void initBuildingOperatorButton(){

//        if(timer != null && !timer.isFinish()){
//            addActor(timer);
//        }else{
//            CoolQueueDto cool = Player.player.getCoolQueueByType(CoolQueueType.BUILDING.ordinal());
//            if(cool != null){
//                showTimer(cool.getExpireTime(), cool.getCoolTime());
//            }
//        }


        Texture texture = ResUtil.getInstance().getUi(UiType.LEVELUP);
        BuildingImage image = new BuildingImage(texture,dto);
        image.setWidth(100);
        image.setHeight(100);

        image.setPosition(x,y);
        addActor(image);

        image.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
//                if(timer != null && !timer.isFinish()){
//                    return;
//                }
                updateBuilding();
            }
        });
//        updateBuilding();
    }

    @SuppressWarnings("unchecked")
    private void updateBuilding(){
        if(dto == null){
            return;
        }
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("x", dto.getX());
        parms.put("y", dto.getY());

        Response response = SocketUtil.send(Request.valueOf(Module.BUILDING, BuildingCmd.UPGRADE_BUILDING_LEVEL, parms),true);
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

//            Object coolQueue = result.get("coolQueueDto");
//            if(coolQueue != null){
//                CoolQueueDto coolQueueDto = JSON.parseObject(JSON.toJSON(coolQueue).toString(), CoolQueueDto.class);
//                if(coolQueueDto != null){
//                    Player.player.getCoolQueues().put(coolQueueDto.getId(),coolQueueDto);
//                    showTimer(coolQueueDto.getExpireTime(), coolQueueDto.getCoolTime());
//                }
//            }

            ConcurrentMap<String, BuildingDto> buildings = Player.player.getBuildings();
            Object building = result.get("content");
            if(building != null){
                BuildingDto playerBuildingDto = JSON.parseObject(JSON.toJSON(building).toString(), BuildingDto.class);
                if(playerBuildingDto != null){
                    buildings.put(playerBuildingDto.getKey(),playerBuildingDto);
//                    showTimer(playerBuildingDto.getEtime(), playerBuildingDto.getCdtime());
                }
            }

            this.getTranceGame().mapScreen.refreshPlayerDtoData();

            //如果是主城升级的话  可能有新的建筑和部队
            if(dto.getId() == BuildingType.OFFICE){
                Object newBuildings  = result.get("newBuildingDtos");
                if(newBuildings != null){
                    List<WaitBuildingDto> wbuildingDtos = JSON.parseArray(JSON.toJSON(newBuildings).toString(), WaitBuildingDto.class);
                    if(wbuildingDtos != null){
                        for(WaitBuildingDto wdto : wbuildingDtos){
                            Player.player.addWaitBuilding(wdto);
                        }
//                        this.getTranceGame().mapScreen.refreshLeftBuiding();
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

    public void setBuildingDto(BuildingDto dto) {
        this.dto = dto;
    }

    public void dispose(){
		super.dispose();
		if(init){
			init = false;
		}
	}

}
