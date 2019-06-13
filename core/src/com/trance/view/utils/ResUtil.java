
package com.trance.view.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.trance.view.constant.BulletType;
import com.trance.view.constant.ControlType;
import com.trance.view.constant.ExplodeType;
import com.trance.view.constant.UiType;
import com.trance.view.model.SoundInfo;

import java.util.HashMap;
import java.util.Map;

public class ResUtil extends AssetManager {
	
	private static ResUtil resUtil;
	public static ResUtil getInstance(){
		if(resUtil == null){
			resUtil = new ResUtil();
		}
		return resUtil;
	}
	
    public void init() { 
//    	Texture.setEnforcePotImages(false);//模拟器调试必须加上
		loadBuilding();
		//army
		load("army/1.png", Texture.class);
		load("army/2.png", Texture.class);
		load("army/3.png", Texture.class);
		load("army/8.png", Texture.class);
		load("army/9.png", Texture.class);

		//ui
    	load("ui/attack.png", Texture.class);
    	load("ui/to_world.png", Texture.class);
    	load("ui/to_home.png", Texture.class);
    	load("ui/rename.png", Texture.class);
       	load("ui/level.png", Texture.class);
       	load("ui/gold.png", Texture.class);
       	load("ui/foods.png", Texture.class);
       	load("ui/silver.png", Texture.class);
       	load("ui/itembox.png", Texture.class);
       	load("ui/change.png", Texture.class);
       	load("ui/train.png", Texture.class);
       	load("ui/blank.png", Texture.class);
       	load("ui/close.png", Texture.class);
       	load("ui/levelup.png", Texture.class);
       	load("ui/upbuilding.png", Texture.class);
       	load("ui/fixed.png", Texture.class);

       	load("ui/1.png", Texture.class);
       	load("ui/2.png", Texture.class);
       	load("ui/3.png", Texture.class);

		//Bullet
		loadBullet();

    	load("world/me1.png", Texture.class);
    	load("world/me2.png", Texture.class);
    	load("world/me3.png", Texture.class);
    	load("world/tips.png", Texture.class);
    	load("world/bg.jpg", Texture.class);
    	
    	load("world/tree1.png", Texture.class);
    	load("world/tree2.png", Texture.class);
    	load("world/tree3.png", Texture.class);
    	load("world/tree4.png", Texture.class);
    	load("world/tree5.png", Texture.class);
    	
    	load("world/grass1.png", Texture.class);
    	load("world/grass2.png", Texture.class);
    	load("world/grass3.png", Texture.class);
    	load("world/grass4.png", Texture.class);
    	load("world/grass5.png", Texture.class);
    	
    	load("world/soil1.png", Texture.class);
    	load("world/soil2.png", Texture.class);
    	load("world/soil3.png", Texture.class);
    	load("world/soil4.png", Texture.class);
    	
    	load("world/stone1.png", Texture.class);
    	load("world/stone2.png", Texture.class);

    	load("world/wall.png", Texture.class);

		load("explode/1.png",Texture.class);
		load("explode/2.png",Texture.class);

		loadAnimation();

		loadSound();
    }

	private void loadAnimation(){
		for(ExplodeType type : ExplodeType.values()){
			for(int i = 0; i< 10; i++){
				load("explode/" + type.getId() + "/" + i +".png", Texture.class);
			}
		}
	}

	private Map<ExplodeType, Animation<TextureRegion>> explodeTypeMap = new HashMap<ExplodeType, Animation<TextureRegion>>();

	/**
	 *  ExplodeAnimation
	 * @param type
	 * @return
     */
	public Animation<TextureRegion> getExplodeAnimation(ExplodeType type){
		Animation<TextureRegion> animation = explodeTypeMap.get(type);
		if(animation != null ){
			return animation;
		}

		TextureRegion[] regions = new TextureRegion[10];
		for(int i = 0; i < 10 ; i++){
			Texture texture = this.get("explode/"+ type.getId() +"/" + i +".png", Texture.class);
			TextureRegion region = new TextureRegion(texture);
			region.setRegionWidth(200);
			region.setRegionHeight(200);
			regions[i] = region;
		}

		float frameDuration = 0.4f;
		animation = new Animation<TextureRegion>(frameDuration, regions);
		explodeTypeMap.put(type, animation);
		return animation;
	}



	private void loadBuilding(){
		for(int i = 1; i < 12; i++){
			load("building/" + i +".png", Texture.class);
		}
	}

	private void loadBullet(){
		for(int i = 1; i < 8; i++){
			load("bullet/" + i +".png", Texture.class);
		}
	}
    
    /**
     * 获得UI
     */
    public Texture getUi(UiType uiType){
    	return this.get(uiType.getVlaue(),Texture.class);
    }
    

	/**
	 * sound
     */
    private void loadSound() {
		load("audio/explode/explode.mp3",Music.class);//mp3
    	for(int i = 0 ; i < 10; i++)
    		load("audio/" + i + ".ogg", Sound.class);
    	
    	for(int i = 1 ; i < 7; i++)
        	load("audio/fire/" + i + ".ogg", Sound.class);
	}

    public Sound getSound(int id){
    	return this.get("audio/" + id + ".ogg", Sound.class);
    }

	private Map<Integer,SoundInfo> soundIdMap = new HashMap<Integer,SoundInfo>();

    public void playDeadSoundFire(int id){
		SoundInfo info = soundIdMap.get(id);
		if(info == null) {
			Sound sound = this.get("audio/fire/" + id + ".ogg", Sound.class);
			long soundId = sound.play(0);
			info = new SoundInfo();
			info.setSound(sound);
			info.setSoundId(soundId);
			soundIdMap.put(id, info);
		}
		info.getSound().stop(info.getSoundId());
		info.getSound().play();
    }


	public TextureRegion getBuildingTextureRegion(int value) {
		return new TextureRegion(getBuildingTexture(value));
	}
	
	public Texture getBuildingTexture(int value) {
		return this.get("building/"+value+".png",Texture.class);
	}
	
	public TextureRegion getArmyTextureRegion(int armyId) {
		if(armyId > 3 && armyId < 8){
			return new TextureRegion(this.get("building/"+armyId+".png",Texture.class));
		}
		return new TextureRegion(this.get("army/"+armyId+".png",Texture.class));
	}

	public TextureRegion getExplodeTextureRegion(int techId) {
		return new TextureRegion(this.get("explode/"+techId+".png",Texture.class));
	}

    public TextureRegion getBulletTextureRegion(BulletType type) {
    	Texture texture = get(type.getValue(), Texture.class);
		return new TextureRegion(texture);
    }

    
    public Texture getControlTextureRegion(ControlType value) {
    	Texture texture = null;
    	String fileName;
    	switch(value){
    	case RENAME:
    		fileName = "ui/rename.png";
    		texture = get(fileName, Texture.class);
    		break;
    	case ATTACK:
    		fileName = "ui/attack.png";
    		texture = get(fileName, Texture.class);
    		break;
    	case WORLD:
    		fileName = "ui/to_world.png";
    		texture = get(fileName, Texture.class);
    		break;
    	case HOME:
    		fileName = "ui/to_home.png";
    		texture = get(fileName, Texture.class);
    		break;
		default:
			break;
    	}
        return texture;
    }

    public void dispose() {
    	super.dispose();
    }
}
