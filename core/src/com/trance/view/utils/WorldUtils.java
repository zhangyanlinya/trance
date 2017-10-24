/*
 * Copyright (c) 2014. William Mora
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trance.view.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.trance.view.actors.Building;
import com.trance.view.actors.Explode;
import com.trance.view.constant.BulletType;

public class WorldUtils {

	public static final float WORLD_TO_BOX = 0.05f;
	public static final float BOX_TO_WORLD = 20f;

    public static World createWorld() {
        return new World(new Vector2(0, 0), true);
    }

    public static Body createBorder(World world, float x, float y, float width, float height) {
    	// Create ball body and shape
    	x = x * WORLD_TO_BOX;
    	y = y * WORLD_TO_BOX;
        width=width*WORLD_TO_BOX;
        height=height* WORLD_TO_BOX;
    	
    	BodyDef bodyDef = new BodyDef();
    	bodyDef.type = BodyType.StaticBody;
    	Body body = world.createBody(bodyDef);
    	EdgeShape edge = new EdgeShape();
        FixtureDef boxShapeDef = new FixtureDef();
        boxShapeDef.shape = edge;
//        edge.set(new Vector2(x, y), new Vector2(width, y));
//        body.createFixture(boxShapeDef);
        edge.set(new Vector2(x, y), new Vector2(x, height));
        body.createFixture(boxShapeDef);
        edge.set(new Vector2(width, y), new Vector2(width, height));
        body.createFixture(boxShapeDef);
        edge.set(new Vector2(width, height), new Vector2(x, height));
        body.createFixture(boxShapeDef);
        edge.dispose();
//        body.setAwake(false);
        return body;
        
    }
    public static Body createBuilding(World world, Building building, float x, float y, float width, float height) {
    	BodyDef bodyDef = new BodyDef();
    	bodyDef.type = BodyType.StaticBody;
    	bodyDef.fixedRotation = true;
    	bodyDef.linearDamping = building.linearDamping;
    	bodyDef.position.set((x + width/2) * WORLD_TO_BOX, (y + height/ 2) * WORLD_TO_BOX);
    	CircleShape shape = new CircleShape();
//    	shape.setAsBox((width/ 2 - 2) * GameScreen.WORLD_TO_BOX, (height / 2  - 2) * GameScreen.WORLD_TO_BOX);
    	shape.setRadius((width/ 2 - 4) * WORLD_TO_BOX);
    	Body body = world.createBody(bodyDef);
    	FixtureDef f = new FixtureDef();
    	f.shape = shape;//夹具的形状
    	f.density = building.density;//夹具的密度
    	f.friction = building.friction;//夹具的摩擦力
    	f.restitution = building.restitution; //弹力
    	body.createFixture(f);//刚体创建夹具.
    	shape.dispose();
    	return body;
    }
    
    public static Body createBullet(World world, BulletType type, float x, float y, float width, float height, float rotation) {
    	BodyDef bodyDef = new BodyDef();
    	bodyDef.type = BodyType.DynamicBody;
    	bodyDef.fixedRotation  = true;
		bodyDef.linearDamping = 0f;
//    	bodyDef.bullet = true;
//    	PolygonShape shape = new PolygonShape();
    	CircleShape shape = new CircleShape();
    	
//    	float hx = width/2 * GameScreen.WORLD_TO_BOX;
//    	float hy = height/2 * GameScreen.WORLD_TO_BOX;
    	
//    	shape.setAsBox(hx,hy);
    	shape.setRadius(width/2 * WORLD_TO_BOX);
    	bodyDef.position.set(x * WORLD_TO_BOX, y * WORLD_TO_BOX);
    	Body body = world.createBody(bodyDef);
    	FixtureDef f = new FixtureDef();
    	f.shape = shape;//夹具的形状
		float density = 0.2f;
		switch (type){
			case ONE:
				density = 0.4f;
				break;
			case TWO:
				density = 0.5f;
				break;
			case THREE:
				density = 0.6f;
				break;
			case FOUR:
				density = 0.7f;
				break;
			case FIVE:
				density = 1.0f;
				break;
			case SEVEN:
				density = 0.3f;
				break;
			case EIGHT:
				density = 1.0f;
				break;
			case NIE:
				density = 1.0f;
				break;
			default:
				break;
		}
    	f.density = density;//夹具的密度
    	f.friction = 0f;//夹具的摩擦力
    	f.restitution = 0.9f;//反弹
		f.filter.categoryBits = 4;
		f.filter.maskBits = 4;
    	body.createFixture(f);//刚体创建夹具.
    	shape.dispose();
    	return body;
    }

	public static Body createArmy(World world, int type, float x, float y,
								  float width, float height) {
    	BodyDef bodyDef = new BodyDef();
    	bodyDef.type = BodyType.DynamicBody;
    	bodyDef.fixedRotation = true;
//    	bodyDef.linearDamping = 1f;
    	bodyDef.position.set((x + width/2) * WORLD_TO_BOX, (y + height/ 2) * WORLD_TO_BOX);
    	CircleShape shape = new CircleShape();
//    	shape.setAsBox((width/ 2 - 2) * GameScreen.WORLD_TO_BOX, (height / 2  - 2) * GameScreen.WORLD_TO_BOX);
    	shape.setRadius((width/ 2 - 6) * WORLD_TO_BOX);
    	Body body = world.createBody(bodyDef);
    	FixtureDef f = new FixtureDef();
    	f.shape = shape;//夹具的形状
    	f.density = 0.1f;//夹具的密度
    	f.friction = 0.1f;//夹具的摩擦力
    	f.restitution = 1.0f; //弹力
    	body.createFixture(f);//刚体创建夹具.
    	shape.dispose();
    	return body;
    }
	public static Body createFitting(World world, int type, float x, float y,
								  float width, float height) {
    	BodyDef bodyDef = new BodyDef();
    	bodyDef.type = BodyType.DynamicBody;
    	bodyDef.fixedRotation = true;
//    	bodyDef.linearDamping = 1f;
    	bodyDef.position.set((x + width/2) * WORLD_TO_BOX, (y + height/ 2) * WORLD_TO_BOX);
    	CircleShape shape = new CircleShape();
//    	shape.setAsBox((width/ 2 - 2) * GameScreen.WORLD_TO_BOX, (height / 2  - 2) * GameScreen.WORLD_TO_BOX);
    	shape.setRadius((width/ 2 - 6) * WORLD_TO_BOX);
    	Body body = world.createBody(bodyDef);
    	FixtureDef f = new FixtureDef();
    	f.shape = shape;//夹具的形状
    	f.density = 0.1f;//夹具的密度
    	f.friction = 0.1f;//夹具的摩擦力
    	f.restitution = 1.0f; //弹力
    	body.createFixture(f);//刚体创建夹具.
    	shape.dispose();
    	return body;
    }

	public static void createExplode(World world, Explode explode, int numRays, float x, float y, float power){
		for (int i = 0; i < numRays; i++) {
			float angle = (i / (float)numRays) * 360 * MathUtils.degreesToRadians;

			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.fixedRotation = true; // rotation not necessary
			bd.bullet = true; // prevent tunneling at high speed
			bd.linearDamping = 0f; // drag due to moving through air
			bd.gravityScale = 0; // ignore gravity
			bd.position.set(x * WORLD_TO_BOX, y * WORLD_TO_BOX);
			Body body = world.createBody(bd);

			CircleShape shape = new CircleShape();
			shape.setRadius(0.5f); // very small

			FixtureDef fd = new FixtureDef();
			fd.shape = shape;
			fd.density = 60 / (float)numRays; // very high - shared across all particles
			fd.friction = 0; // friction not necessary
			fd.restitution = 0.99f; // high restitution to reflect off obstacles
			fd.filter.groupIndex = -1; // particles should not collide with each other
			body.createFixture(fd);
			shape.dispose();
			body.setUserData(explode);
			body.setLinearVelocity(MathUtils.sin(angle) * power, MathUtils.cos(angle) * power);
		}
	}
}
