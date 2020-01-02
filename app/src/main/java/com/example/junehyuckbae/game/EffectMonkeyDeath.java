package com.example.junehyuckbae.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class EffectMonkeyDeath extends GameEffect{
    private int animation;
    public EffectMonkeyDeath(Context context, float x, float y) {
        super(context, x, y);
        this.animation = 1;
    }

    public boolean nextFrame(){
        animation++;
        if (animation == 13)return true;
        else return false;
    }

    public Bitmap getBitmap(){
        Resources res = context.getResources();
        Bitmap bitmap;
        switch (animation){
            case 1:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_1);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_1);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_2);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_2);
                break;
            case 5:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_3);
                break;
            case 6:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_4);
                break;
            case 7:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_5);
                break;
            case 8:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_4);
                break;
            case 9:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_3);
                break;
            case 10:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_3);
                break;
            case 11:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_4);
                break;
            case 12:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_5);
                break;
            case 13:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_5);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_up_5);
                break;
        }
        return bitmap;
    }
}
