package com.trance.view.pools;

import com.badlogic.gdx.utils.Pool;
import com.trance.view.actors.Army;

public class ArmyPool extends Pool<Army> {

	@Override
	protected Army newObject() {
		return new Army();
	}
}
