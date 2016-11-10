package com.trance.view.pools;

import com.badlogic.gdx.utils.Pool;
import com.trance.view.actors.Building;

public class BuildingPool extends Pool<Building> {
	
	@Override
	protected Building newObject() {
		return new Building();
	}
}
