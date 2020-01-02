package com.example.junehyuckbae.game;

public class Ball extends GameObject{
    private boolean alive;
    private boolean attacked;

    /* status */
    private int attack;
    private int hp;
    private int [] skills;
    // 0 : green    : recover
    // 1 : yellow   : heavy
    // 2 : sky blue : super bounce
    // 3 : red      : power
    // 4 : purple   : explosion
    // 5 : black    : reduce damage

    private int total_skil;
    private int animationSpin;
    private boolean spin_type;

    /* buff */
    private boolean heavy_countered;
    private boolean item_shield;
    private int item_shield_count;
    private boolean item_powered;
    private boolean item_banana;
    private int item_banana_count;
    private boolean object_bush;
    private boolean object_monkey;
    /* debug */
    private boolean x_border_hit;
    private boolean y_border_hit;
    private final float MAX_SPEED = 120;

    public Ball(float x, float y, int playernumber) {
        super(x,y,playernumber);
        this.alive = true;
        this.attacked = false;
        this.item_banana = false;
        this.object_bush = false;
        this.hp = 100;
        this.attack = 10;

        this.animationSpin =0 ;

        if((Math.random()*100) % 2 == 0){
            this.spin_type = true;
        }else this.spin_type = false;

        this.total_skil = 0;
        skills = new int [10];
        for(int i = 0 ; i < skills.length ; i++){
            skills[i] = 0;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAttacked(){
        this.attacked =true;
    }

    public boolean isAttacked(){
        return attacked;
    }

    public void setSpeed(float dX,float dY){
        dx = dX;
        dy = dY;
    }

    public void addSpeed(float externalX, float externalY){
        dx = dx + externalX;
        dy = dy + externalY;
    }

    public void setSpeedByDest(float toX,float toY){
        float distX = toX - x;
        float distY = toY - y;
        float moving = 4;
        dx = distX / moving;
        dy = distY / moving;
    }

    public void moving(){
        if (item_banana){
            float r = (float)Math.sqrt(dx*dx+dy*dy);
            float angle = (float) Math.toDegrees(Math.atan2(dy,dx));

            if (angle < 0) {
                angle += 360;
            }
            angle = (float)Math.toRadians(angle+10);
            dx = r * (float)Math.cos(angle);
            dy = r * (float)Math.sin(angle);

            item_banana_count--;
            if(item_banana_count == 0) item_banana = false;
        }
        if(spin_type){
            if (animationSpin == -60) animationSpin =0;
            else animationSpin--;
        }else{
            animationSpin = (animationSpin+1)%60;
        }

        if (dx != 0){
            dx = dx * 0.96F;
            x += dx;
        }

        if (dy != 0){
            dy = dy * 0.96F;
            y += dy;
        }

        if (dx > -1 && dx < 1){
            dx = 0;
        }
        if (dy > -1 && dy < 1){
            dy = 0;
        }

        // adjust max speed
        if ( dx >= MAX_SPEED ||  dy >= MAX_SPEED){
            dx = dx * 0.8F;
            dy = dy * 0.8F;
        }

        // totally stopped
        if( dx == 0 && dy==0){
            heavy_countered = false;
            attacked = false;
            item_banana = false;
            if(skills[3]>0){
                attack = 20;
            }else{
                attack = 10;
            }
        }
    }

    public boolean attackBall(Ball target){
        int damage = getAttack();
        item_powered=false;

        if(target.skills[5]>0){
            if(damage >10)damage -= 10;
            else return true;
        }
        if(target.item_shield_count>=1){
            item_shield_count--;
            if (item_shield_count == 0) item_shield=false;
            damage = damage / 2;
        }
        if(target.hp <= damage){
            target.alive = false;
            return false;
        }else{
            target.hp = target.hp-damage;
            return true;
        }
    }

    public boolean getDamage(int damage){
        if(skills[5]>0){
            if(damage >10)damage -= 10;
            else return true;
        }
        if(item_shield_count>=1){
            item_shield_count--;
            damage = damage/2;
        }
        if(hp <= damage){
            alive = false;
            return false;
        }else{
            hp = hp - damage;
            return true;
        }
    }

    public void getPowered(int power){
        attack += power;
    }
    public int getHP(){
        return hp;
    }
    public void setHp(int _hp){
        hp+= _hp;
        if(hp > 200){
            hp = 200;
        }
    }

    public int getAttack(){
        int realAP = attack;
        if (item_powered) realAP = realAP *2;
        return realAP;
    }

    public void setSkill(int skil_index , int level){
        if(skills[skil_index] == 0){
            total_skil++;
        }
        if(skil_index==3) attack= 20;
        skills[skil_index] = level;
    }
    public int getSkillLevel(int index){
        return skills[index];
    }
    public int getTotal_skil(){return total_skil;}

    public boolean isHeavy_countered() {
        return heavy_countered;
    }
    public void setHeavy_countered(boolean set){
        heavy_countered =set;
    }

    public boolean isX_border_hit() {
        return x_border_hit;
    }

    public void setX_border_hit(boolean x_border_hit) {
        this.x_border_hit = x_border_hit;
    }

    public boolean isY_border_hit() {
        return y_border_hit;
    }

    public void setY_border_hit(boolean y_border_hit) {
        this.y_border_hit = y_border_hit;
    }

    public int getAnimationPhase() {
        return animationSpin;
    }

    public boolean isItem_shield() {
        return item_shield;
    }

    public void setItem_shield(boolean item_shield, int count)
    {
        this.item_shield = item_shield;
        this.item_shield_count = count;
    }

    public boolean isItem_powered() {
        return item_powered;
    }

    public void setItem_powered(boolean item_powered) {
        this.item_powered = item_powered;
    }

    public boolean isItem_banana() {
        return item_banana;
    }

    public void setItem_banana(boolean item_banana) {
        this.item_banana = item_banana;
        if(item_banana)this.item_banana_count=10;
    }

    public boolean isObject_bush() {
        return object_bush;
    }

    public void setObject_bush(boolean object_bush) {
        this.object_bush = object_bush;
    }


    public boolean isObject_monkey() {
        return object_monkey;
    }

    public void setObject_monkey(boolean object_monkey) {
        this.object_monkey = object_monkey;
    }
}
