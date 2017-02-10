package com.trance.view.pools;

import com.badlogic.gdx.utils.Pool;
import com.trance.view.actors.Fitting;

/**
 * Created by Administrator on 2017/2/4 0004.
 */

public class FittingPool extends Pool<Fitting>{

    @Override
    protected Fitting newObject() {
        return new Fitting();
    }
}
