package com.trance.view.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.army.model.ArmyType;
import com.trance.empire.modules.player.model.Player;
import com.trance.view.constant.BulletType;
import com.trance.view.constant.RangeType;
import com.trance.view.mapdata.MapData;
import com.trance.view.pools.ArmyPool;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.TimeUtil;
import com.trance.view.utils.WorldUtils;


public class Army extends GameActor {
	
	public final static Pool<Army> armyPool = new ArmyPool();
	private Body body;
	public ArmyType armyType;
	private TextureRegion textureRegion;
  	private ShapeRenderer renderer;
	private float speed = 2;
	private long fireDelay = 2000;
	
	private ArmyDto dto;
	private BitmapFont font;

	/**
	 * 是否派出
	 */
	private boolean send;


	public void init(World world, ArmyType armyType, float x , float y, float width, float height, ShapeRenderer renderer){
		super.init(x, y, width, height);
		this.armyType = armyType;
		this.renderer = renderer;
		this.alive = true;
		this.move = true;
		this.camp = 2;
		this.hp = maxhp;
		this.degrees = 0;
		this.send = false;
		textureRegion = ResUtil.getInstance().getArmyTextureRegion(armyType.id);
		if(this.getWidth() == 0 && this.getHeight() == 0){
			this.setWidth(textureRegion.getRegionWidth());
			this.setHeight(textureRegion.getRegionHeight());
		}
		switch(armyType){
		case TANK:
			range = RangeType.SHORT;
			hp = 50;
			break;
		case FAT:
			atk = 20;
			fireDelay = 1000;
			break;
		case SISTER:
			range = RangeType.SHORT;
			hp = 30;
			speed = 1.5f;
			break;
		case FOOT:
			
			break;
		case FIVE:
			fireDelay = 4000;
			atk = 40;
			speed = 1.5f;
			break;
		case SIX:
			atk = 50;
			move = false;
			break;
		case SEVEN:
			hp = 40;
			break;
		case EIGHT:
			speed = 4;
			range = 20;
			break;
		case NINE:
			atk = 100;
			break;
		default:
			break;
		}
		
		ArmyDto dto = Player.player.getArmys().get(armyType.id);
		if(dto != null && dto.getLevel() > 0){
			atk *= dto.getLevel();
		    hp *= dto.getLevel();
		    maxhp = hp;
		}
		

		if(world == null){
			body = null;
			return;
		}
		body = WorldUtils.createArmy(world,armyType.id, x, y, width, height);
		body.setUserData(this);
	}
	
	public void init(World world, ArmyType armyType, float x , float y, float width, float height, ShapeRenderer renderer, BitmapFont font, ArmyDto dto){
		init(world, armyType, x, y, width, height, renderer);
		this.dto = dto;
		this.font = font;
	}
	

	
	public void move() {
		if(!MapData.gamerunning){
			return;
		}
		
		if(body == null){
			return;
		}
		
		float x = body.getPosition().x * WorldUtils.BOX_TO_WORLD - hw;
		float y = body.getPosition().y * WorldUtils.BOX_TO_WORLD - hh;
		setPosition(x,y);
		body.setLinearVelocity(vx * speed, vy * speed);
	}
	
	
	private long time;
	
	/**
	 * 111
	 */
	public void fire() {
		if(!MapData.gamerunning){
			return;
		}
		
		if (!alive) {
			return;
		}

		if(!send){
			return;
		}
		
		long now = System.currentTimeMillis();
		if((now - time) < fireDelay){
			return;
		}
		time = now;
		
		if( body == null){
			return;
		}

		int id = armyType.id;
		if(id > 6){
			id = 6;
		}

		ResUtil.getInstance().playDeadSoundFire(id);
//		sound.play();

		id--;
		Bullet bullet = Bullet.bulletPool.obtain();
		bullet.init(body.getWorld(), BulletType.valueOf(id), this, getX(), getY(), 0,
				0);
		this.getStage().addActor(bullet);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(textureRegion, getX(), getY(), hw,
				hh, getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		
		if(dto != null){
			font.draw(batch, "lv:" + dto.getLevel(), getX(), getY());
			font.draw(batch, "n :"+ dto.getAmout(), getX(), getY() - getHeight()/2);
			font.draw(batch, "a :"+ dto.getAddAmount(), getX(), getY() - getHeight());
			long leftTime = dto.getExpireTime() - TimeUtil.getServerTime();
			if(leftTime > 0 ){		
				font.draw(batch, "e :" + leftTime , getX(), getY() - getHeight() + getHeight()/2);
			}
		}
		
		if(renderer != null){
			batch.end();
			renderer.setColor(Color.GREEN);
			renderer.begin(ShapeType.Line);
			renderer.rect(getX(), getY() + getHeight(), getWidth(), 5);
			renderer.end();
			float percent = hp / maxhp;
			if(percent < 0.2){
				renderer.setColor(Color.RED);
			}else if(percent < 0.5){
				renderer.setColor(Color.YELLOW);
			}else{
				renderer.setColor(Color.GREEN);
			}
			renderer.begin(ShapeType.Filled);
			
			renderer.rect(getX() + 1, getY() + getHeight() + 1, percent
					* (getWidth() - 2), 4);
			renderer.end();
			batch.begin();
		}
		
		if(!MapData.gamerunning){
			return;
		}
		if (!alive) {
			return;
		}
		
		if(move){
			move();
		}
	}
	
	@Override
	public void dead() {
		alive = false;
		remove();
		armyPool.free(this);
	}

	public boolean isSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public float getSpeed() {
		return speed;
	}

}
