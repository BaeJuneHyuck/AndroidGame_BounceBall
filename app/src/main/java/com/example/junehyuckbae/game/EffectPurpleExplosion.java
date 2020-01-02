package com.example.junehyuckbae.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class EffectPurpleExplosion extends GameEffect{
    private int animation;
    public EffectPurpleExplosion(Context context, float x, float y) {
        super(context, x, y);
        this.animation = 1;
    }

    public boolean nextFrame(){
        animation++;
        if (animation == 11)return true;
        else return false;
    }

    public Bitmap getBitmap(){
        Resources res = context.getResources();
        Bitmap bitmap;
        switch (animation){
            case 1:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_01);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_02);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_03);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_04);
                break;
            case 5:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_05);
                break;
            case 6:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_05);
                break;
            case 7:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_06);
                break;
            case 8:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_07);
                break;
            case 9:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_09);
                break;
            case 10:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_10);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.effect_purple_explosion_10);
                break;
        }
        return bitmap;
    }
}
