package com.trance.view.pools;

import com.badlogic.gdx.utils.Pool;
import com.trance.view.actors.Explode;

public class ExplodePool extends Pool<Explode> {

	@Override
	protected Explode newObject() {
		return new Explode();
	}
}
