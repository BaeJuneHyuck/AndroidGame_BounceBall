package com.example.junehyuckbae.game;

public class GameObject {
    protected float x;
    protected float y;
    protected float dx;
    protected float dy;
    protected int playernumber;

    public GameObject(float x, float y, int playernumber) {
        this.x = x;
        this.y = y;
        this.playernumber = playernumber;
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

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public int getPlayernumber() {
        return playernumber;
    }

    public void setPlayernumber(int playernumber) {
        this.playernumber = playernumber;
    }

    public float getDistance(float dist_x, float dist_y){
        float a = (dist_x - x)*(dist_x - x) + (dist_y - y)*(dist_y - y);
        return (float)Math.sqrt(a);
    }

    public float getDistance(GameObject ob2){
        float a = (ob2.x - x)*(ob2.x - x) + (ob2.y - y)*(ob2.y - y);
        return (float)Math.sqrt(a);
    }
}
