package com.trance.view.dialog;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
import com.trance.view.utils.TimeUtil;

import java.util.Collection;
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

    public void show(float x, float y) {
        this.x = x;
        this.y = y;
        show();
    }

    public void show() {
        if (!init) {
            init();
        }

        this.clear();
        this.setVisible(true);

        Image bgImage = new Image(ResUtil.getInstance().getUi(UiType.BLANK));
        bgImage.getColor().a = 0.3f;
        bgImage.setWidth(300);
        bgImage.setHeight(100);
        bgImage.setPosition(x - bgImage.getWidth() / 2, y);
        addActor(bgImage);

        Image close = new Image(ResUtil.getInstance().getUi(UiType.CLOSE));
        close.setPosition(x + bgImage.getWidth() / 2, y + bgImage.getHeight());
        close.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                getTranceGame().mapScreen.setOperateStageDailog(false, x, y);
            }
        });
        addActor(close);

        initBuildingOperatorButton();

    }

    // 初始化操作按钮
    private void initBuildingOperatorButton() {
        Texture texture = ResUtil.getInstance().getUi(UiType.LEVELUP);
        BuildingImage image = new BuildingImage(texture, dto);
        image.setWidth(100);
        image.setHeight(100);

        image.setPosition(x - 100, y);
        addActor(image);

        image.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateBuilding(false);
            }
        });


        // gold
        Texture textureGold = ResUtil.getInstance().getUi(UiType.GOLD);
        BuildingImage imageGold = new BuildingImage(textureGold, dto);
        imageGold.setWidth(100);
        imageGold.setHeight(100);

        imageGold.setPosition(x, y);
        addActor(imageGold);

        imageGold.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateBuilding(true);
            }
        });


    }

    // 是否有正在升级的建筑
    private boolean hasUpdatingBuilding() {
        for (BuildingDto dto : Player.player.getBuildings().values()) {
            if (dto.getEtime() > TimeUtil.getServerTime()) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void updateBuilding(boolean byGold) {
        if (dto == null) {
            return;
        }

        if(byGold){
            if(Player.player.getGold() < dto.getLvl() * 10){
                MsgUtil.getInstance().showMsg(Module.BUILDING, -8);
                return;
            }
        }

        if (!byGold && hasUpdatingBuilding()) {
            MsgUtil.getInstance().showMsg(Module.BUILDING, -10004);
            return;
        }

        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("x", dto.getX());
        parms.put("y", dto.getY());

        int cmd = BuildingCmd.UPGRADE_BUILDING_LEVEL;
        if (byGold) {
            cmd = BuildingCmd.UPGRADE_BUILDING_LEVEL_BY_GOLD;
        }
        Response response = SocketUtil.send(Request.valueOf(Module.BUILDING, cmd, parms), true);
        if (response == null || response.getStatus() != ResponseStatus.SUCCESS) {
            return;
        }

        byte[] bytes = response.getValueBytes();
        String text = new String(bytes);
        HashMap<String, Object> result = JSON.parseObject(text, HashMap.class);
        if (result != null) {
            int code = Integer.valueOf(String.valueOf(result.get("result")));
            if (code != Result.SUCCESS) {
                MsgUtil.getInstance().showMsg(Module.BUILDING, code);
                return;
            }
            Object valueResult = result.get("valueResultSet");
            if (valueResult != null) {
                ValueResultSet valueResultSet = JSON.parseObject(JSON.toJSON(valueResult).toString(), ValueResultSet.class);
                RewardService.executeRewards(valueResultSet);
            }

            ConcurrentMap<String, BuildingDto> buildings = Player.player.getBuildings();
            Object building = result.get("content");
            if (building != null) {
                BuildingDto playerBuildingDto = JSON.parseObject(JSON.toJSON(building).toString(), BuildingDto.class);
                if (playerBuildingDto != null) {
                    BuildingDto dto = buildings.get(playerBuildingDto.getKey());
                    if(dto != null){
                        dto.setCdtime(playerBuildingDto.getCdtime());
                        dto.setEtime(playerBuildingDto.getEtime());
                        dto.setLvl(playerBuildingDto.getLvl());
                    }
                }
            }

            this.getTranceGame().mapScreen.refreshPlayerDtoData();

            //如果是主城升级的话  可能有新的建筑和部队
            if (dto.getMid() == BuildingType.OFFICE) {
                Object newArmys = result.get("newArmyDtos");
                if (newArmys != null) {
                    List<ArmyDto> armyDtos = JSON.parseArray(JSON.toJSON(newArmys).toString(), ArmyDto.class);
                    if (armyDtos != null) {
                        for (ArmyDto armyDto : armyDtos) {
                            Player.player.addAmry(armyDto);
                        }
//						 dialogArmyStage.refresh();
                    }
                }

                // waitBuildings
                Collection<CityElement> list = BasedbService.listAll(CityElement.class);
                for(CityElement  element : list){
                    if(element.getOpenLevel() == dto.getLvl()){
                        int hasBuildNum = Player.player.getHasBuildingSize(element.getId());
                        int leftNum = element.getAmount() - hasBuildNum;
                        if(leftNum > 0) {
                            this.getTranceGame().mapScreen.refreshLeftBuiding();
                            break;
                        }
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

    public void dispose() {
        super.dispose();
        if (init) {
            init = false;
        }
    }

}
