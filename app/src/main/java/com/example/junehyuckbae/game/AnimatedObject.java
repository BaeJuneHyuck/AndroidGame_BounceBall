package com.example.junehyuckbae.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AnimatedObject extends GameObject {
    private int type;
    boolean alive;
    private Context context;
    private int animation;
    private float startX,startY;
    private float endX, endY;

    public AnimatedObject(Context context, float x, float y, int type) {
        super(x, y, -1);
        this.startX = x;
        this.startY = y;
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

    public void setEndPoint(float endX, float endY) {
        this.endX = endX;
        this.endY = endY;
    }

    public float getEndX() {
        return endX;
    }
    public float getEndY() {
        return endY;
    }

    public void setMove(int frame){
        this.dx = (endX - startX) / frame;
        this.dy = (endY - startY) / frame;
    }

    public Bitmap getBitmap(){
        Resources res = context.getResources();
        Bitmap bitmap;

        if(type == 1) { // thorny bush
            animation++;
            if (animation == 30) animation = 1;
            if (animation < 10) bitmap = BitmapFactory.decodeResource(res, R.drawable.object_thron_1);
            else if (animation < 20) bitmap = BitmapFactory.decodeResource(res, R.drawable.object_thron_2);
            else bitmap = BitmapFactory.decodeResource(res, R.drawable.object_thron_3);
        }else if(type == 2) { //monkey
            animation++;
            x += dx;
            y += dy;
            if (animation == 50){
                animation = 1;
                float tempx = startX;
                float tempy = startY;
                startX = endX;
                startY = endY;
                endX = tempx;
                endY = tempy;
                setMove(50);
            }
            if(dx>0){
                if (animation % 6 == 0 || animation % 6 == 1) bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_right_1);
                else if (animation % 6 == 2 || animation % 6 == 3) bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_right_2);
                else bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_right_3);
            }else{
                if (animation % 6 == 0 || animation % 6 == 1) bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_left_1);
                else if (animation % 6 == 2|| animation % 6 == 3) bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_left_2);
                else bitmap = BitmapFactory.decodeResource(res, R.drawable.object_monkey_left_3);
            }
        }else {
            animation++;
            if (animation == 30) animation = 1;
            if (animation < 10)
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_thron_1);
            else if (animation < 20)
                bitmap = BitmapFactory.decodeResource(res, R.drawable.object_thron_2);
            else bitmap = BitmapFactory.decodeResource(res, R.drawable.object_thron_3);
        }
        return bitmap;
    }
}