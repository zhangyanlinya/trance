package com.trance.view.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.trance.view.constant.BulletType;
import com.trance.view.particle.ParticleService;
import com.trance.view.pools.BulletPool;
import com.trance.view.utils.ResUtil;
import com.trance.view.utils.WorldUtils;

/**
 * Bullet
 * @author zyl
 *
 */
public class Bullet extends GameActor {
	
	private Body body;
	public BulletType type;
	private GameActor gameActor;
	private float speed = 2;//
	
	private float orgX;
	private float orgY;
	
	//纹理区域
	private TextureRegion textureRegion;

//	private long createTime;//生成时间
//	private long liveTime = 3000L;//存活时间
	
	public final static Pool<Bullet> bulletPool = new BulletPool();
	
	public Bullet() {}
	
	//初始化
	public void init(World world, Stage stage, BulletType type, GameActor gameActor, float x, float y, float width, float height){
		this.alive = true;
		this.type  = type;
		this.gameActor = gameActor;
		this.degrees = gameActor.degrees;
		this.atk = gameActor.atk;
		this.role = 1;
		textureRegion = ResUtil.getInstance().getBulletTextureRegion(type);
		
		camp = gameActor.camp;
		if(width == 0 && height == 0){
			width = textureRegion.getRegionWidth();
			height = textureRegion.getRegionHeight();
		}
		
		x += gameActor.getWidth()/2;
		y += gameActor.getHeight()/2;	
		
		//radius;
		float radius = gameActor.getHeight() > gameActor.getWidth() ? gameActor.getHeight(): gameActor.getWidth();
		radius += 1;
		float sin = -MathUtils.sin(degrees);
		float cos =  MathUtils.cos(degrees);
		x += sin * radius;
		y += cos * radius;
		orgX = x;
		orgY = y;
		this.setPosition(x, y);
		
		this.setWidth(width);
		this.setHeight(height);
		this.hw = width/2;
		this.hh = height/2;
				
		body = WorldUtils.createBullet(world, type,  x, y,width,height,degrees);
		body.setTransform(body.getPosition(), degrees);
		
		body.applyLinearImpulse(sin * speed,  cos * speed,
				body.getWorldCenter().x, body.getWorldCenter().y, true);
//		body.setLinearVelocity(sin * speed,  cos * speed);
		body.setUserData(this);

//		createTime = System.currentTimeMillis();

		Actor actor = ParticleService.getInstance().addEffectActor(x, y);
		stage.addActor(actor);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		float x = body.getPosition().x  * WorldUtils.BOX_TO_WORLD - hw;
		float y = body.getPosition().y  * WorldUtils.BOX_TO_WORLD - hh;
		this.setRotation(MathUtils.radiansToDegrees * body.getAngle());
		batch.draw(textureRegion, x, y, hw,
				hh, getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
		
		if(outofRange(x,y)){ // out of range
			dead();
		}

//		checkFireTime();
	}

	private void checkFireTime(){
//		long now  = System.currentTimeMillis();
//		if(now - createTime > liveTime){//到时间了
//			dead();
//		}
	}
	
	private boolean outofRange(float x, float y) {
		final float x_d = x - orgX;
		final float y_d = y - orgY;
		float dst = (float)Math.sqrt(x_d * x_d + y_d * y_d);
		if(dst > gameActor.range){
			return true;
		}
		return false;
	}
	
	@Override
	public void dead() {
		this.alive = false;
		this.remove();
		bulletPool.free(this);
	}

	@Override
	protected void fire() {
		//do nothing
	}

	@Override
	protected void move() {
		//do nothing
	}
}
	
	