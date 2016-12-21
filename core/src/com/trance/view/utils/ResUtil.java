
package com.trance.view.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.trance.view.constant.BulletType;
import com.trance.view.constant.ControlType;
import com.trance.view.constant.UiType;

public class ResUtil extends AssetManager {
	
	public TextureAtlas textureAtlas;
	public TextureAtlas textureAtlas2;
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
    	
//    	initAnimation();

		loadSound();
    }

	private void loadBuilding(){
		for(int i = 1; i < 10; i++){
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
     * @param
     * @return
     */
    public Texture getUi(UiType uiType){
    	return this.get(uiType.getVlaue(),Texture.class);
    }
    
//    private void initAnimation() {
//    	for(ArmyType type : ArmyType.values()){
//    	 for (int i = 0; i < 6; i++) {
//    		 load("army/"+type.id + "/zoulu/" + i + ".png", Texture.class);
//         }
//    	}
//	}

	/**
	 * sound
     */
    private void loadSound() {
    	for(int i = 0 ; i < 10; i++)
    		load("audio/" + i + ".ogg", Sound.class);
    	
    	for(int i = 1 ; i < 7; i++)
        	load("audio/fire/" + i + ".ogg", Sound.class);
	}
    
    public Sound getSound(int id){
    	return this.get("audio/" + id + ".ogg", Sound.class);
    }
    public Sound getSoundFire(int id){
    	return this.get("audio/fire/" + id + ".ogg", Sound.class);
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
	
//	private Map<Integer, TextureRegion[]> armyAnimations = new HashMap<Integer, TextureRegion[]>();
	
//	public TextureRegion[] getArmyAnimation(int armyId) {
//		TextureRegion[] regions = armyAnimations.get(armyId);
//		if(regions != null && regions.length > 0){
//			return regions;
//		}
//
//		regions = new TextureRegion[6];
//        //把Texture转换下
//        for (int i = 0; i < 6; i++) {
//        	Texture animation = this.get("army/"+armyId+"/zoulu/"+i+".png", Texture.class);
//        	regions[i] = new TextureRegion(animation);
//        }
//        armyAnimations.put(armyId, regions);
//		return regions;
//	}
    
    public TextureRegion getBulletTextureRegion(BulletType type) {
    	Texture texture = get(type.getValue(), Texture.class);
    	TextureRegion textureRegion = new TextureRegion(texture);
        return textureRegion;
    }

    
    public Texture getControlTextureRegion(ControlType value) {
    	Texture texture = null;
    	String fileName = null;
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
//    	armyAnimations.clear();
    }
}
