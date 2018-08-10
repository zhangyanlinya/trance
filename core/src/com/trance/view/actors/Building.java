package com.trance.view.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.BuildingType;
import com.trance.empire.modules.building.model.WaitBuildingDto;
import com.trance.view.constant.BulletType;
import com.trance.view.constant.RangeType;
import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.freefont.FreeFont;
import com.trance.view.mapdata.MapData;
import com.trance.view.pools.BuildingPool;
import com.trance.view.screens.GameScreen;
import com.trance.view.utils.RandomUtil;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.WorldUtils;

/**
 * 单位
 * @author zhangyl
 *
 */
public class Building extends GameActor {
	
	public final static Pool<Building> buildingPool = new BuildingPool();
	private Body body;
	public int type;
	public int i;
	public int j;
	private TextureRegion textureRegion;
  	private ShapeRenderer renderer;
	private long fireDelay = 2000;
	public float linearDamping;
	public float density;
	public float friction;
	public float restitution;
	public static FreeBitmapFont font;
	private BuildingDto dto;
	private WaitBuildingDto wdto;
	private boolean hasfire;
    private int inScreenType; // 0-map 1-game

	static {
		if(font == null){
			font = FreeFont.getBitmapFont("building");
			font.setSize(15);
			font.appendText("1234567890levelcount");
		}
	}

	/**
	 * 初始化
	 * @param type
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void init(World world, final int type, float x , float y, float width, float height, ShapeRenderer renderer){
		super.init(x, y, width, height);
		this.renderer = renderer;
		this.alive = true;
		this.camp = 1;
		this.type = type;
		this.linearDamping = 2;
		this.density = 10f;//密度
		this.friction = 1.0f;//摩擦力
		this.restitution = 0.1f;//弹力
		this.hasfire = false;
		if(type <= 0){
			textureRegion = null;
			return;
		}
		textureRegion = ResUtil.getInstance().getBuildingTextureRegion(type);
		if(this.getWidth() == 0 && this.getHeight() == 0){
			this.setWidth(textureRegion.getRegionWidth());
			this.setHeight(textureRegion.getRegionHeight());
		}
		
		this.role = 0;
		
		switch(type){
		case BuildingType.OFFICE:
			hp = 400;
			break;
		case BuildingType.HOUSE:
			break;
		case BuildingType.BARRACKS:
			break;
		case BuildingType.CANNON:
			fireDelay = 5000;
			atk = 300;
			range = RangeType.LONG;
//			linearDamping = 0;
			density = 0;
			break;
		case BuildingType.ROCKET:
			fireDelay = 3500;
			range = RangeType.LONG;
//			linearDamping = 0;
			density = 0;
			atk = 1;
			break;
		case BuildingType.FLAME:
			fireDelay = 1000;
			atk = 30;
//			linearDamping = 0;
			density = 0;
			break;
		case BuildingType.GUN:
//			linearDamping = 0;
			density = 0;
			break;
		case BuildingType.TOWER:
			face = false;
			range = RangeType.TOOLONG;
			break;
		case BuildingType.MORTAR:
			face = false;
			break;
		
		}
		
		if(dto != null){
			atk *= dto.getLevel();
		    hp *= dto.getLevel();
		}
		hp *= 10;
		this.maxhp = hp;
		
		if(world == null){
			body = null;
			return;
		}
		body = WorldUtils.createBuilding(world, this, x, y, width, height);
		body.setUserData(this);

	}
	
	public void init(World world, int type, float x , float y, float width, float height, ShapeRenderer renderer, BuildingDto dto){
		init(world, type, x, y, width, height, renderer);
		this.dto = dto;
	}

	public void init(World world, int type, float x , float y, float width, float height, ShapeRenderer renderer, WaitBuildingDto wdto){
		init(world, type, x, y, width, height, renderer);
		this.wdto = wdto;
	}

	public void setIndex(int i,int j){
		this.i = i;
		this.j = j;
	}
	
	
	@Override
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
		float speed = 3;
		body.setLinearVelocity(vx * speed, vy * speed);
	}
	
//	private long faceDelay = 3000;
	
	private float tmpRotation = RandomUtil.nextInt(360) ;
	private boolean flag = RandomUtil.nextBoolean();
	private long faceTime;
	private void randomDir(){
		if(hasfire){
			return;
		}
		setRotation(tmpRotation);
		long now = System.currentTimeMillis();
		if((now - faceTime) < RandomUtil.betweenValue(50, 100)){
			return;
		}
		faceTime = now;
		if(flag){
			tmpRotation ++;
		}else{
			tmpRotation --;
		}
	}
	
	
	private long time;
	
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
		bullet.init(body.getWorld(), this.getStage(), BulletType.valueOf(type - 4), this, getX(), getY(), 0,
				0);
		this.getStage().addActor(bullet);

		int id = type;
		if(id > 6){
			id = 6;
		}

		ResUtil.getInstance().playDeadSoundFire(id);

		hasfire = true;
		move = true;
	}

	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(textureRegion == null){
			return;
		}

		if(body != null) {
			float x = body.getPosition().x * WorldUtils.BOX_TO_WORLD - hw;
			float y = body.getPosition().y * WorldUtils.BOX_TO_WORLD - hh;
			this.setPosition(x, y);
//			this.setRotation(MathUtils.radiansToDegrees * body.getAngle());
			batch.draw(textureRegion, x, y, hw,
					hh, getWidth(), getHeight(), getScaleX(),
					getScaleY(), getRotation());
		}else {
			batch.draw(textureRegion, getX(), getY(), hw,
				hh, getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());

		}


		if(dto != null){
			font.draw(batch,  dto.getLevel() + "" , getX() + hw/2 , getY() + getHeight());
            float current = dto.getEtime() - System.currentTimeMillis();
            if(current > 0){
                current = dto.getCdtime() - current;
                drawProgress(batch, renderer, current, dto.getCdtime());
            }
		}

		if(wdto != null){
            font.draw(batch, "count  " + wdto.getAmount(), getX(), getY() - getHeight()/2);
        }

		if(inScreenType == 1){
            drawProgress(batch, renderer, hp, maxhp);
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
		
		if(!firing && type > 3 && type < 8){
			randomDir();
		}

	}

	private void drawProgress(Batch batch, ShapeRenderer renderer,  float current, float max){
        batch.end();
        renderer.setColor(Color.GREEN);
        renderer.begin(ShapeType.Line);
        renderer.rect(getX(), getY() + getHeight(), getWidth(), 5);
        renderer.end();
        float percent = current / max;
        if(percent < 0.2){
            renderer.setColor(Color.RED);
        }else if(percent < 0.5){
            renderer.setColor(Color.YELLOW);
        }else{
            renderer.setColor(Color.BLUE);
        }
        renderer.begin(ShapeType.Filled);

        renderer.rect(getX() + 1, getY() + getHeight() + 1, percent
                * (getWidth() - 2), 4);
        renderer.end();
        batch.begin();
    }

    public BuildingDto getDto() {
        return dto;
    }

    public int getInScreenType() {
        return inScreenType;
    }

    public void setInScreenType(int inScreenType) {
        this.inScreenType = inScreenType;
    }

    @Override
	public void dead() {
		alive = false;
		remove();
		buildingPool.free(this);
		GameScreen.buildings.removeValue(this, true);
		
		if(type == BuildingType.OFFICE){
			MapData.gamerunning = false;
			GameScreen.finishBattle(true);
		}
	}

}
