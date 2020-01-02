package com.example.junehyuckbae.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class DeathAnimation extends GameObject {
    private int stage;

    public int getStage() {
        return stage;
    }

    public void nextStage() {
        stage++;
    }

    public DeathAnimation(float x, float y, int playernumber) {
        super(x,y,playernumber);
        this.stage = 0;
    }

    public void getRandomColor(Paint p){
        int color1 = (int)(Math.random()*255);
        int color2 = (int)(Math.random()*255);
        int color3 = (int)(Math.random()*255);
        p.setColor(Color.rgb(color1,color2,color3));
        p.setStyle(Paint.Style.FILL);
    }

    public boolean draw(Canvas canvas, int RADIUS){

        Paint p1 = new Paint();
        Paint p2 = new Paint();
        Paint p3 = new Paint();
        Paint p4 = new Paint();

        getRandomColor(p1);
        getRandomColor(p2);
        getRandomColor(p3);
        getRandomColor(p4);

        switch(stage) {
            case 0:
                float zoom = 0.5F * RADIUS;
                RectF r1 = new RectF(x - RADIUS, y - RADIUS, x + RADIUS + zoom, y + RADIUS + zoom);
                RectF r2 = new RectF(x - RADIUS - zoom, y - RADIUS, x + RADIUS, y + RADIUS + zoom);
                RectF r3 = new RectF(x - RADIUS - zoom, y - RADIUS - zoom, x + RADIUS, y + RADIUS);
                RectF r4 = new RectF(x - RADIUS, y - RADIUS - zoom, x + RADIUS + zoom, y + RADIUS);
                canvas.drawArc(r1, 0F, 90F, true, p1);
                canvas.drawArc(r2, 90F, 90F, true, p2);
                canvas.drawArc(r3, 180F, 90F, true, p3);
                canvas.drawArc(r4, 270F, 90F, true, p4);
                break;
            case 1:
                zoom = 1 * RADIUS;
                r1 = new RectF(x - RADIUS, y - RADIUS, x + RADIUS + zoom, y + RADIUS + zoom);
                r2 = new RectF(x - RADIUS - zoom, y - RADIUS, x + RADIUS, y + RADIUS + zoom);
                r3 = new RectF(x - RADIUS - zoom, y - RADIUS - zoom, x + RADIUS, y + RADIUS);
                r4 = new RectF(x - RADIUS, y - RADIUS - zoom, x + RADIUS + zoom, y + RADIUS);
                canvas.drawArc(r1, 0F, 90F, true, p1);
                canvas.drawArc(r2, 90F, 90F, true, p2);
                canvas.drawArc(r3, 180F, 90F, true, p3);
                canvas.drawArc(r4, 270F, 45F, true, p4);
                canvas.drawArc(r4, 315F, 45F, true, p4);

                break;
            case 2:
                zoom = 1.5F * RADIUS;
                r1 = new RectF(x - RADIUS, y - RADIUS, x + RADIUS + zoom, y + RADIUS + zoom);
                r2 = new RectF(x - RADIUS - zoom, y - RADIUS, x + RADIUS, y + RADIUS + zoom);
                r3 = new RectF(x - RADIUS - zoom, y - RADIUS - zoom, x + RADIUS, y + RADIUS);
                r4 = new RectF(x - RADIUS, y - RADIUS - zoom, x + RADIUS + zoom, y + RADIUS);
                canvas.drawArc(r1, 0F, 90F, true, p1);
                canvas.drawArc(r2, 90F, 90F, true, p2);
                canvas.drawArc(r3, 180F, 30F, true, p3);
                canvas.drawArc(r3, 210F, 60F, true, p4);
                canvas.drawArc(r4, 270F, 45F, true, p4);
                canvas.drawArc(r4, 315F, 45F, true, p3);

                break;
            case 3:
                zoom = 2 * RADIUS;
                r1 = new RectF(x - RADIUS, y - RADIUS, x + RADIUS + zoom, y + RADIUS + zoom);
                r2 = new RectF(x - RADIUS - zoom, y - RADIUS, x + RADIUS, y + RADIUS + zoom);
                r3 = new RectF(x - RADIUS - zoom, y - RADIUS - zoom, x + RADIUS, y + RADIUS);
                r4 = new RectF(x - RADIUS, y - RADIUS - zoom, x + RADIUS + zoom, y + RADIUS);
                canvas.drawArc(r1, 0F, 60F, true, p1);
                canvas.drawArc(r1, 60F, 30F, true, p2);
                canvas.drawArc(r2, 90F, 90F, true, p2);
                canvas.drawArc(r3, 180F, 30F, true, p3);
                canvas.drawArc(r3, 210F, 60F, true, p1);
                canvas.drawArc(r4, 270F, 45F, true, p4);
                canvas.drawArc(r4, 315F, 45F, true, p3);

                break;
            case 4:
                zoom = 2.5F * RADIUS;
                r1 = new RectF(x - RADIUS, y - RADIUS, x + RADIUS + zoom, y + RADIUS + zoom);
                r2 = new RectF(x - RADIUS - zoom, y - RADIUS, x + RADIUS, y + RADIUS + zoom);
                r3 = new RectF(x - RADIUS - zoom, y - RADIUS - zoom, x + RADIUS, y + RADIUS);
                r4 = new RectF(x - RADIUS, y - RADIUS - zoom, x + RADIUS + zoom, y + RADIUS);

                canvas.drawArc(r1, 0F, 60F, true, p1);
                canvas.drawArc(r1, 60F, 30F, true, p3);
                canvas.drawArc(r2, 90F, 40F, true, p2);
                canvas.drawArc(r2, 130F, 50F, true, p1);
                canvas.drawArc(r3, 180F, 30F, true, p3);
                canvas.drawArc(r3, 210F, 60F, true, p1);
                canvas.drawArc(r4, 270F, 45F, true, p4);
                canvas.drawArc(r4, 315F, 45F, true, p2);

                break;
            case 5:
                zoom = 3 * RADIUS;
                r1 = new RectF(x - RADIUS, y - RADIUS, x + RADIUS + zoom, y + RADIUS + zoom);
                r2 = new RectF(x - RADIUS - zoom, y - RADIUS, x + RADIUS, y + RADIUS + zoom);
                r3 = new RectF(x - RADIUS - zoom, y - RADIUS - zoom, x + RADIUS, y + RADIUS);
                r4 = new RectF(x - RADIUS, y - RADIUS - zoom, x + RADIUS + zoom, y + RADIUS);
                canvas.drawArc(r1, 0F, 10F, true, p1);
                canvas.drawArc(r1, 10F, 40F, true, p2);
                canvas.drawArc(r1, 50F, 20F, true, p1);
                canvas.drawArc(r1, 70F, 20F, true, p3);
                canvas.drawArc(r2, 90F, 40F, true, p2);
                canvas.drawArc(r2, 130F, 15F, true, p4);
                canvas.drawArc(r2, 145F, 50F, true, p2);
                canvas.drawArc(r3, 180F, 30F, true, p3);
                canvas.drawArc(r3, 210F, 20F, true, p1);
                canvas.drawArc(r3, 230F, 40F, true, p3);
                canvas.drawArc(r4, 270F, 45F, true, p2);
                canvas.drawArc(r4, 315F, 45F, true, p4);

                break;
            case 6:
                zoom = 3.5F * RADIUS;
                r1 = new RectF(x - RADIUS, y - RADIUS, x + RADIUS + zoom, y + RADIUS + zoom);
                r2 = new RectF(x - RADIUS - zoom, y - RADIUS, x + RADIUS, y + RADIUS + zoom);
                r3 = new RectF(x - RADIUS - zoom, y - RADIUS - zoom, x + RADIUS, y + RADIUS);
                r4 = new RectF(x - RADIUS, y - RADIUS - zoom, x + RADIUS + zoom, y + RADIUS);
                canvas.drawArc(r1, 0F, 10F, true, p1);
                canvas.drawArc(r1, 10F, 40F, true, p2);
                canvas.drawArc(r1, 50F, 20F, true, p1);
                canvas.drawArc(r1, 70F, 20F, true, p3);
                canvas.drawArc(r2, 90F, 40F, true, p2);
                canvas.drawArc(r2, 130F, 15F, true, p4);
                canvas.drawArc(r2, 145F, 50F, true, p2);
                canvas.drawArc(r3, 180F, 30F, true, p3);
                canvas.drawArc(r3, 210F, 20F, true, p1);
                canvas.drawArc(r3, 230F, 40F, true, p3);
                canvas.drawArc(r4, 270F, 45F, true, p2);
                canvas.drawArc(r4, 315F, 45F, true, p4);
                break;
            default:
                break;
        }
        stage++;
        if(stage == 7)return true;
        else return false;
    }
}
