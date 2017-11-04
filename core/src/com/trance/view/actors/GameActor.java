package com.trance.view.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.trance.view.constant.RangeType;

public abstract class GameActor extends Actor {
	
	/**
	 * 0 - 静态  或坦克  1- 子弹
	 */
	public int role;
	
	/**
	 *  1 -防守  2-进攻  0- npc 
	 */
	public int camp;
	
	float degrees;

	float hp = 10;
	float maxhp = 10;
	float atk = 10 ;
	
	public boolean alive = true;

	public float range = RangeType.NORMAL;
	boolean move;
	
	float vx;
	float vy;
	float hw;
	float hh;

	public boolean firing;

	boolean face = true;


	
	public void init(float x, float y, float width,
			float height) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.hw = width/2;
		this.hh = height/2;
		this.alive = true;
	}
	
	/**
	 * 受到攻击
	 * @param a
	 * @return
	 */
	public void byAttack(GameActor a) {
		hp -= a.atk;
		if (hp <= 0) {
			dead();
		}
	}
	
	private void stop() {
		vx = 0;
		vy = 0;
		
	}

	public abstract void dead();
	
	
	public void faceTo(float destX, float destY) {
		float disX = destX - (this.getX() + hw);
		float disY = destY - (this.getY() + hh);
		degrees = -MathUtils.atan2(disX, disY);
		vx = -MathUtils.sin(degrees);
		vy =  MathUtils.cos(degrees);
		if(face) {
			setRotation(degrees * MathUtils.radiansToDegrees);
		}
	}

	private float dst(float x, float y) {
		final float x_d = x - getX();
		final float y_d = y - getY();
		return (float)Math.sqrt(x_d * x_d + y_d * y_d);
	}

	public GameActor scan(Array<GameActor> actors) {
		GameActor dest = null;
		float min = 0;
		for(int i = 0; i < actors.size; i++){
			GameActor actor = actors.get(i);
			if(!actor.alive){
				actors.removeValue(actor, true);
				continue;
			}

			float dst = actor.dst(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
			if(min == 0 || dst < min){
				min = dst;
				dest = actor;
			}
		}
		if(dest == null){
			return null;
		}
		
		if(camp == 2){
			faceTo(dest.getX() + dest.getWidth() / 2, dest.getY() + dest.getHeight() / 2);
			if(min < range){
				stop();
				fire();
				firing = true;
			}else{
				firing = false;
			}
		}else{
			if(min < range){
				faceTo(dest.getX() + dest.getWidth() / 2, dest.getY() + dest.getHeight() / 2);
				fire();
				firing = true;
			}else{
				firing = false;
			}
		}

		return dest;
	}

	protected abstract void fire();

	protected abstract void move();
	
}
