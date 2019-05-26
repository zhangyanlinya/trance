package com.trance.view.delaytask;

import com.badlogic.gdx.Gdx;
import com.trance.view.TranceGame;

public class MsgShowTask implements Runnable{

    private TranceGame tranceGame;

    private String msg;

    public MsgShowTask(TranceGame tranceGame, String msg){
        this.tranceGame = tranceGame;
        this.msg = msg;
    }

    @Override
    public void run() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                tranceGame.showMsg(msg);
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsgShowTask that = (MsgShowTask) o;
        return msg.equals(that.msg);
    }

    @Override
    public int hashCode() {
        return msg.hashCode();
    }
}
