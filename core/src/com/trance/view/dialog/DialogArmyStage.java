package com.trance.view.dialog;


import com.alibaba.fastjson.JSON;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.trance.common.basedb.BasedbService;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.army.handler.ArmyCmd;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.army.model.basedb.ArmyTrain;
import com.trance.empire.modules.coolqueue.model.CoolQueueDto;
import com.trance.empire.modules.coolqueue.model.CoolQueueType;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.reward.result.ValueResultSet;
import com.trance.empire.modules.reward.service.RewardService;
import com.trance.view.TranceGame;
import com.trance.view.actors.ArmyImage;
import com.trance.view.actors.Timer;
import com.trance.view.constant.UiType;
import com.trance.view.dialog.base.BaseStage;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.SocketUtil;
import com.trance.view.utils.TimeUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class DialogArmyStage extends BaseStage {

    private Image bgImage;
    private ShapeRenderer renderer;
    private Collection<ArmyTrain> armyTrains;
    private boolean init;

    public DialogArmyStage(TranceGame tranceGame) {
        super(tranceGame);
    }

    private void init() {
        renderer = new ShapeRenderer();
        armyTrains = BasedbService.listAll(ArmyTrain.class);
    }
    
    public void show(){
    	if(!init){
    		init();
    		init = true;
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
					getTranceGame().mapScreen.setArmyDailog(false);
			}
	    });
	    addActor(close);
    	
    	ConcurrentMap<Integer, ArmyDto> army_map = Player.player.getArmys();
    	int i = 1;
    	float side = bgImage.getHeight() / armyTrains.size();
		float wd = bgImage.getWidth()/12;
    	for(final ArmyTrain armyTrain : armyTrains){
	    	TextureRegion region = ResUtil.getInstance().getArmyTextureRegion(armyTrain.getId());
	    	final ArmyDto armyDto = army_map.get(armyTrain.getId());
	    	if(armyDto == null){
	    		continue;
	    	}

			int amount = armyDto.getAmout();
//			if(amount == 0){
//				amount = 1; //等于0 就附值为1
//			}

			for(int k = 0; k < amount; k++){
				Image item = new Image(region);
				item.setBounds(getWidth()/2 - bgImage.getWidth()/2 + wd * k,  getHeight()/2 + bgImage.getHeight()/2 - side * i , side, side);
				addActor(item);
			}

	    	ArmyImage image = new ArmyImage(region,renderer,armyTrain.getPerTime() * 1000 ,armyDto); //按毫秒算
	    	image.setBounds(getWidth()/2 - bgImage.getWidth()/2,  getHeight()/2 + bgImage.getHeight()/2 - side * i, side, side);
	    	addActor(image);
	    	
	    	Image train  = new Image(ResUtil.getInstance().getUi(UiType.LEVELUP));
	    	train.setBounds(getWidth()/2 + bgImage.getWidth()/2 - side,  getHeight()/2 + bgImage.getHeight()/2 - side * i , side, side);
	    	train.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					int max = armyTrain.getMax();
					if(armyDto.getAmout() >= max){
						MsgUtil.getInstance().showMsg(Module.ARMY, -10004);
						return;
					}
					
					long now = TimeUtil.getServerTime();
					if(armyDto.getAddAmount() > 0 && armyDto.getExpireTime() >  now){//正在训练
						return;
					}
					if(armyDto.getAddAmount() <= 0 ){//未到期
						trainArmy(armyTrain.getId());//
					}else{
						obtainArmy(armyTrain.getId());
					}
				}
	    	});
	    	addActor(train);
	    	
	    	Image levelup  = new Image(ResUtil.getInstance().getUi(UiType.LEVELUP));
	    	levelup.setBounds(getWidth()/2 + bgImage.getWidth()/2 - side * 2,  getHeight()/2 + bgImage.getHeight()/2 - side * i , side, side);
	    	levelup.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(timer != null && !timer.isFinish()){
						return;
					}
					levelup(armyTrain.getId());
				}
	    	});
	    	addActor(levelup);

	    	i ++;
    	}
    	
    	if(timer != null && !timer.isFinish()){
    		addActor(timer);
    	}else{
			CoolQueueDto cool = Player.player.getCoolQueueByType(CoolQueueType.TECH.ordinal());
	    	if(cool != null){
	    		showTimer(cool.getExpireTime());
	    	}
    	}
    }
    private void showTimer(long expireTime){
    	timer = new Timer(expireTime);
		timer.setPosition(getWidth()/2 - bgImage.getWidth()/2 + 100,  getHeight() / 2 + bgImage.getHeight() / 2);
		addActor(timer);
    }
    
    private Timer timer;


    private void levelup(int armyId) {
		Response response = SocketUtil.send(Request.valueOf(Module.ARMY, ArmyCmd.UPGRADE_LEVEL, armyId),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return;
		}
		
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		@SuppressWarnings("unchecked")
		HashMap<String,Object> result = JSON.parseObject(text, HashMap.class);
		if(result != null){
			int code = Integer.valueOf(String.valueOf(result.get("result")));
			if(code != Result.SUCCESS){
				MsgUtil.getInstance().showMsg(Module.ARMY,code);
				return ;
			}
			
			ArmyDto armyDto = Player.player.getArmys().get(armyId);
			if(armyDto != null){
				armyDto.setLevel(armyDto.getLevel() + 1);
			}
			
			Object cobj = result.get("content");
			if(cobj != null){
				CoolQueueDto coolQueueDto = JSON.parseObject(JSON.toJSON(cobj).toString(), CoolQueueDto.class);
				Player.player.getCoolQueues().put(coolQueueDto.getId(),coolQueueDto);
				showTimer(coolQueueDto.getExpireTime());
			}
			
			Object valueResult = result.get("valueResultSet");
			if(valueResult != null){
				ValueResultSet valueResultSet = JSON.parseObject(JSON.toJSON(valueResult).toString(), ValueResultSet.class);
				RewardService.executeRewards(valueResultSet);
			}
			
			Sound sound = ResUtil.getInstance().getSound(1);
			sound.play();
		}
	}

    
    private void trainArmy(int armyId){
		Map<String, Object> params = new HashMap<String, Object>();
		int addAmount = 1;
		params.put("armyId", armyId);
		params.put("amount", addAmount);
		Response response = SocketUtil.send(Request.valueOf(Module.ARMY, ArmyCmd.TRAIN_ARMY, params),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return;
		}
		
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		@SuppressWarnings("unchecked")
		HashMap<String,Object> result = JSON.parseObject(text, HashMap.class);
		if(result != null){
			int code = Integer.valueOf(String.valueOf(result.get("result")));
			if(code != Result.SUCCESS){
				MsgUtil.getInstance().showMsg(Module.ARMY,code);
				return ;
			}
			Object valueResult = result.get("content");
			if(valueResult != null){
				ValueResultSet valueResultSet = JSON.parseObject(JSON.toJSON(valueResult).toString(), ValueResultSet.class);
				RewardService.executeRewards(valueResultSet);
			}
			
			long expireTime = (Long) result.get("expireTime");
			ConcurrentMap<Integer, ArmyDto> army_map = Player.player.getArmys();
			ArmyDto armyDto = army_map.get(armyId);
			if(armyDto != null){
				armyDto.setExpireTime(expireTime);
				armyDto.setAddAmount(addAmount);
			}
			
			Sound sound = ResUtil.getInstance().getSound(3);
			sound.play();
		}
	}
	
	private void obtainArmy(int armyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("armyId", armyId);
		Response response = SocketUtil.send(Request.valueOf(Module.ARMY, ArmyCmd.OBTAIN_ARMY, params),true);
		if(response == null || response.getStatus() != ResponseStatus.SUCCESS){
			return;
		}
		
		byte[] bytes = response.getValueBytes();
		String text = new String(bytes);
		@SuppressWarnings("unchecked")
		HashMap<String,Object> result = JSON.parseObject(text, HashMap.class);
		if(result != null){
			int code = Integer.valueOf(String.valueOf(result.get("result")));
			if(code != Result.SUCCESS){
				MsgUtil.getInstance().showMsg(Module.ARMY,code);
				return ;
			}
			Object valueResult = result.get("content");
			if(valueResult != null){
				ValueResultSet valueResultSet = JSON.parseObject(JSON.toJSON(valueResult).toString(), ValueResultSet.class);
				RewardService.executeRewards(valueResultSet);
			}
			ConcurrentMap<Integer, ArmyDto> army_map = Player.player.getArmys();
			ArmyDto armyDto = army_map.get(armyId);
			if(armyDto != null){
				armyDto.setExpireTime(0);
				armyDto.setAmout(armyDto.getAmout() + armyDto.getAddAmount());
				armyDto.setAddAmount(0);
			}
			
			Sound sound = ResUtil.getInstance().getSound(4);
			sound.play();
			show();
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
