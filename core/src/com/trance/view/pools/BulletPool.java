package com.trance.view.pools;

import com.badlogic.gdx.utils.Pool;
import com.trance.view.actors.Bullet;


public class BulletPool extends Pool<Bullet> {
	
	@Override
	protected Bullet newObject() {
		return  new Bullet();
	}
}
