package com.trance.view.dialog;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.common.util.ProtostuffUtil;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.battle.handler.BattleCmd;
import com.trance.empire.modules.battle.model.AttackInfoDto;
import com.trance.empire.modules.replay.handler.ReplayCmd;
import com.trance.empire.modules.replay.model.ReportDto;
import com.trance.empire.modules.replay.model.ReqReport;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.empire.modules.tech.model.TechDto;
import com.trance.view.TranceGame;
import com.trance.view.constant.UiType;
import com.trance.view.dialog.base.BaseStage;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.freefont.FreeFont;
import com.trance.view.screens.ReplayScreen;
import com.trance.view.utils.FontUtil;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;
import com.trance.view.utils.TimeUtil;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;


/**
 *
 */
public class DialogAttackInfoStage extends BaseStage {

	private List<AttackInfoDto> attackInfos;
	private static final int MAX_RANKING = 10;
    private boolean init;
    private final ConcurrentMap<String, ReportDto> report_map = new ConcurrentLinkedHashMap.Builder<String, ReportDto>()
            .maximumWeightedCapacity(20).build();

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

		FreeBitmapFont font = FontUtil.getFont();
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
					getTranceGame().mapScreen.setAttackInfoDailog(false);
			}
	    });
	    addActor(close);
    	
	    if(attackInfos == null){
	    	return;
	    }

        Collections.sort(attackInfos);
	    
    	int i = 1;
    	float side = bgImage.getHeight() / MAX_RANKING;
    	for(final AttackInfoDto dto : attackInfos){
			String  rewardMsg = RewardService.getRewardMsg(dto.getRewards());
			Label label = FreeFont.getLabel(TimeUtil.betweenTime(dto.getTime()) + ": " +dto.getPlayerName() +" "+ MsgUtil.getInstance().getLocalMsg("looted") +"  "+ rewardMsg);
			label.setBounds(getWidth()/2 - bgImage.getWidth()/2,  getHeight()/2 + bgImage.getHeight()/2 - side * i, side, side);
    		addActor(label);

            Image dest = new Image(ResUtil.getInstance().getUi(UiType.FIXED));
            dest.setBounds(getWidth()/2 + bgImage.getWidth()/2 - side,  getHeight()/2 + bgImage.getHeight()/2 - side * i, side, side);
            addActor(dest);

            dest.addListener(new ClickListener(){

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String reportId = dto.getPlayerId() + "_" + dto.getTime();
                    watchReplay(reportId);
                }
            });

	    	i ++;
    	}
    }

    private List<AttackInfoDto> getAttackInfo(){
    	Response response = SocketUtil.send(Request.valueOf(Module.BATTLE, BattleCmd.GET_ATTACK_INFO, null),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return null;
		}
		
		byte[] bytes = response.getValueBytes();
		Result<List<AttackInfoDto>> result = ProtostuffUtil.parseObject(bytes, Result.class);
		return result.getContent();
	}

    private void watchReplay(String reportId){
		ReportDto report = report_map.get(reportId);
        if(report == null) {
        	ReqReport req = new ReqReport();
        	req.setReportId(reportId);
            Request request = Request.valueOf(Module.REPLAY, ReplayCmd.GET_PLAYER_REPLAY, req);
            Response response = SocketUtil.send(request, true);
            if (response == null || response.getStatus() != ResponseStatus.SUCCESS) {
                return;
            }

            byte[] bytes = response.getValueBytes();
            Result<ReportDto> result = ProtostuffUtil.parseObject(bytes, Result.class);
            int code = result.getCode();
            if (code != Result.SUCCESS) {
                MsgUtil.getInstance().showMsg(Module.REPLAY, code);
                return;
            }

            report = result.getContent();
            if (report == null) {
                return;
            }

            report_map.put(reportId, report);
        }

        //TEST
        report.getTechs().clear();
        TechDto techDto = new TechDto();
        techDto.setId(1);
        techDto.setAmout(3);
        techDto.setLevel(1);
        report.getTechs().add(techDto);

        TechDto lampDto = new TechDto();
        lampDto.setId(2);
        lampDto.setAmout(3);
        lampDto.setLevel(5);
        report.getTechs().add(lampDto);

        ReplayScreen.report = report.clone();

        tranceGame.setScreen(tranceGame.replayScreen);
    }
	
	public void dispose(){
		super.dispose();
		if(init){
			init = false;
		}
	}

}
