package com.example.junehyuckbae.game;

import android.content.Context;
import android.graphics.Bitmap;

public abstract class GameEffect {
    protected float x;
    protected float y;
    protected Context context;

    public GameEffect(Context context, float x, float y) {
        this.context = context;
        this.x = x;
        this.y = y;
    }

    public abstract Bitmap getBitmap();
    public abstract boolean nextFrame();

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDistance(float dist_x, float dist_y){
        float a = (dist_x - x)*(dist_x - x) + (dist_y - y)*(dist_y - y);
        return (float)Math.sqrt(a);
    }

    public float getDistance(GameEffect ob2){
        float a = (ob2.x - x)*(ob2.x - x) + (ob2.y - y)*(ob2.y - y);
        return (float)Math.sqrt(a);
    }

}
