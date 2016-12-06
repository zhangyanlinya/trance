package com.trance.view.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.trance.view.mapdata.MapData;
import com.trance.view.pools.ArmyPool;
import com.trance.view.screens.GameScreen;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.TimeUtil;
import com.trance.view.utils.WorldUtils;


public class Army extends GameActor {
	
	public final static Pool<Army> armyPool = new ArmyPool();
	public Body body;
	public ArmyType armyType;
	private TextureRegion textureRegion;
  	public ShapeRenderer renderer;
	public float speed = 2;
	public long fireDelay = 1000;
	
	private ArmyDto dto;
	private BitmapFont font;
	
	private TextureRegion[] girlRegion;
    private float stateTime;
    //当前帧
    private TextureRegion currentFrame;
    private Animation animation;
	
	
	public void init(World world, ArmyType armyType, float x , float y, float width, float height, ShapeRenderer renderer){
		super.init(x, y, width, height);
		this.armyType = armyType;
		this.renderer = renderer;
		this.alive = true;
		this.move = true;
		this.camp = 2;
		this.hp = maxhp;
		this.degrees = 0;
		textureRegion = ResUtil.getInstance().getArmyTextureRegion(armyType.id);
		if(this.getWidth() == 0 && this.getHeight() == 0){
			this.setWidth(textureRegion.getRegionWidth());
			this.setHeight(textureRegion.getRegionHeight());
		}
		switch(armyType){
		case TANK:
			range = 300;
			atk = 20;
			fireDelay = 300;
			speed = 2;
			break;
		case FAT:
			range = 100;
			hp = 100;
			break;
		case SISTER:
			range = 500;
			hp = 50;
			speed = 1;
			break;
		case FOOT:
			
			break;
		case FIVE:
			fireDelay = 4000;
			atk = 50;
			speed = 0.5f;
			break;
		case SIX:
			range = 800;
			atk = 100;
			move = false;
			break;
		case SEVEN:
			hp = 100;
			break;
		case EIGHT:
			speed = 4;
			range = 20;
			break;
		case NINE:
			atk = 150;
			break;
		default:
			break;
		}
		
		ArmyDto dto = Player.player.getArmys().get(armyType.id);
		if(dto != null && dto.getLevel() > 0){
//			atk *= dto.getLevel();//等级加成
			atk *= dto.getLevel();
		    hp *= dto.getLevel();
		    maxhp = hp;
		}
		
		initAnimation(armyType.id);
		
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
	
	private void initAnimation(int armyId){
		girlRegion = ResUtil.getInstance().getArmyAnimation(armyId);
        //0.06*11=0.66 大概就是1秒钟播放完这个动画。
        animation = new Animation(0.1f, girlRegion);
	}
	
	
	public void move() {
		if(!MapData.gamerunning){
			return;
		}
		
		if(body == null){
			return;
		}
		
		float x = body.getPosition().x * GameScreen.BOX_TO_WORLD - hw;
		float y = body.getPosition().y * GameScreen.BOX_TO_WORLD - hh;
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
		
		long now = System.currentTimeMillis();
		if((now - time) < fireDelay){
			return;
		}
		time = now;
		
		if( body == null){
			return;
		}
		
		Bullet bullet = Bullet.bulletPool.obtain();
		bullet.init(body.getWorld(), BulletType.COMMON, this, getX(), getY(), 0,
				0);
		this.getStage().addActor(bullet);
		
		int id = armyType.id;
		if(id > 6){
			id = 6;
		}
		Sound sound = ResUtil.getInstance().getSoundFire(id);
		sound.play();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		stateTime += Gdx.graphics.getDeltaTime();
        //下一帧
        currentFrame = animation.getKeyFrame(stateTime, true);
		batch.draw(currentFrame, getX(), getY(), hw,
				hh, getWidth(), getHeight(), getScaleX(),
				getScaleY(), 0);
		
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
}
