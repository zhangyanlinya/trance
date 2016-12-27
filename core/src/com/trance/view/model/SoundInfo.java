package com.trance.view.model;

import com.badlogic.gdx.audio.Sound;

/**
 * Created by Administrator on 2016/12/26 0026.
 */

public class SoundInfo {

    private Sound sound;
    private long soundId;

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public long getSoundId() {
        return soundId;
    }

    public void setSoundId(long soundId) {
        this.soundId = soundId;
    }
}
