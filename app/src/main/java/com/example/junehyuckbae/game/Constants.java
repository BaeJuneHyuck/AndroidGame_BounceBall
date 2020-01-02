package com.example.junehyuckbae.game;

import android.content.Context;

public class Constants {
    public static final int SOUND_BALL_COLLIDE = R.raw.sound01;
    public static final int SOUND_BALL_DESTROYED = R.raw.sound02;
    public static final int SOUND_BALL_COLLIDE_WALL = R.raw.sound03;
    public static final int SOUND_ITEM_HEAL = R.raw.sound_item_heal;
    public static final int SOUND_ITEM_SHIELD = R.raw.sound_item_shield;
    public static final int SOUND_ITEM_EXPLODE = R.raw.sound_item_explode;
    public static final int SOUND_ITEM_POWERUP = R.raw.sound_item_powerup;
    public static final int SOUND_ITEM_BANANA = R.raw.sound_item_banana;
    public static final int SOUND_BALL_SKILL_PURPLE = R.raw.sound_purple_explode;
    public static final int SOUND_WIN = R.raw.sound_win;
    public static final int SOUND_LOSE = R.raw.sound_lose;
    public static final int SOUND_BUTTON = R.raw.sound_button;
    public static final int SOUND_PAGE = R.raw.sound_page;
    public static final int SOUND_OBJECT_MONKEY = R.raw.sound_monkey1;
    public static final int SOUND_OBJECT_BUSH = R.raw.sound_bush;

    public static final void initSoundManager(Context context, SoundManager soundManager){
        soundManager.addSound(context, SOUND_BALL_COLLIDE);
        soundManager.addSound(context, SOUND_BALL_DESTROYED);
        soundManager.addSound(context, SOUND_BALL_COLLIDE_WALL);
        soundManager.addSound(context, SOUND_ITEM_HEAL);
        soundManager.addSound(context, SOUND_ITEM_SHIELD);
        soundManager.addSound(context, SOUND_ITEM_EXPLODE);
        soundManager.addSound(context, SOUND_ITEM_POWERUP);
        soundManager.addSound(context, SOUND_ITEM_BANANA);
        soundManager.addSound(context, SOUND_BALL_SKILL_PURPLE);
        soundManager.addSound(context, SOUND_WIN);
        soundManager.addSound(context, SOUND_LOSE);
        soundManager.addSound(context, SOUND_PAGE);
        soundManager.addSound(context, SOUND_BUTTON);
        soundManager.addSound(context, SOUND_OBJECT_BUSH);
        soundManager.addSound(context, SOUND_OBJECT_MONKEY);
    }
}
