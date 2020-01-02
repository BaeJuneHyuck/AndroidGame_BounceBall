package com.example.junehyuckbae.game;

import android.app.Application;

public class MyApplication extends Application {
    private SoundManager soundManager;

    public SoundManager getSoundManager() {
        if (soundManager == null) {
            soundManager = new SoundManager();
            Constants.initSoundManager(this, soundManager);
        }
        return soundManager;
    }
}
