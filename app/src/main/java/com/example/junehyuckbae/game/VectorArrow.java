package com.example.junehyuckbae.game;

public class VectorArrow{
    private float x;
    private float y;
    private float angle;
    private float size;
    boolean visible;


    public VectorArrow(float x, float y) {
        this.x = x;
        this.y = y;
        visible = false;
        this.size = 1;
    }

    public float getDistance(float toX, float toY){
        float a = (toX - x)*(toX - x) + (toY - y)*(toY - y);
        return (float)Math.sqrt(a);
    }

    public boolean isVisible() {
        return visible;
    }

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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    public float getAngle(){ return this.angle;}

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        if(size >2.5){
            this.size = 2.5F;
        }else if(size >1){
            this.size = size;
        }else{
            this.size = 1;
        }
    }
}
