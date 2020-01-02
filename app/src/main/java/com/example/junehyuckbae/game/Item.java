package com.example.junehyuckbae.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Item extends GameObject {
    private int type;
    boolean alive;
    private Context context;
    private int animation;

    public Item(Context context, float x, float y, int type) {
        super(x, y, -1);
        this.context = context;
        this.type = type;
        this.alive = true;
        this.animation = 1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Bitmap getBitmap(){
        Resources res = context.getResources();
        Bitmap bitmap;
        switch (type){
            case 1: // hp recover
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_recover_small);
                break;
            case 2: // energy shield
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_shield_small);
                break;
            case 3: // Instant explosion
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_bomb_small);
                break;
            case 4: // Power Dash
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_power_small);
                break;
            case 5: // Banana
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_banana_small);
                break;
            case 6: // Random
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_question_small);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_recover_small);
                break;
        }
        return bitmap;
    }

    public Bitmap getGlitterBitmap(){
        Resources res = context.getResources();
        Bitmap bitmap;

        animation++;
        if (animation == 15) animation = 1;
        switch (animation){
            case 1:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_01);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_02);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_03);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_04);
                break;
            case 5:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_05);
                break;
            case 6:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_06);
                break;
            case 7:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_07);
                break;
            case 8:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_08);
                break;
            case 9:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_09);
                break;
            case 10:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_10);
                break;
            case 11:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_11);
                break;
            case 12:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_12);
                break;
            case 13:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_13);
                break;
            case 14:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_14);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(res, R.drawable.item_glitter_14);
                break;
        }
        return bitmap;
    }
}
