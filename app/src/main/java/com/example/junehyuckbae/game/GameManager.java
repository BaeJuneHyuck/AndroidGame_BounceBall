package com.example.junehyuckbae.game;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GameManager extends TextureView {

    private Context context;

    private ArrayList<Ball> b_list;             // 게임에서 사용되는 Ball들의 list
    private ArrayList<DeathAnimation> d_list;   // 죽은 ball들의 애니메이션 재생을 위한 리스트
    private ArrayList<Item> i_list;             // item list
    private ArrayList<GameEffect> e_list;       // effect list
    private ArrayList<AnimatedObject> o_list;       // Object list
    private VectorArrow arrow;                  // Ball 선택시 보여지는 화살표
    private int targetIndex;                    // 가장 최근 선택한 알의 list에서의 index
    public static int displayWidth; // 기기의 크기
    public static int displayHeight;
    private static final int RADIUS = 80;
    private SoundManager sm;
    private canvasThread cThread;

    private TextView textLeft,textRight;

    // game status
    private int world;
    private int current_stage;
    private boolean boss_stage;
    private int border_three_star;
    private int border_two_star;
    private int total_white;        // 0번 흰색
    private int total_black;        // 1번 검은색
    private int shootChance;        // 남은 공격 횟수
    private boolean ballIsMoving;    // 아무 공이라도 움직이고 잇는가
    private int purpleSkillMoving;
    private boolean purpleSkillSignal;
    private boolean gameEnd; // 마지막 공이 파괴될때 패배/승리/스킬발동 안되도록 true 해줄것임

    public GameManager(Context context, int world) {
        super(context);
        //초기화
        this.context = context;
        this.world = world;
        this.b_list = new ArrayList<>();
        this.d_list = new ArrayList<>();
        this.i_list = new ArrayList<>();
        this.e_list = new ArrayList<>();
        this.o_list = new ArrayList<>();
        this.total_black = 0;
        this.total_white = 0;
        this.arrow = new VectorArrow(0,0);
        this.ballIsMoving = false;
        this.boss_stage = false;
        this.purpleSkillMoving = -1;
        this.purpleSkillSignal = false;
        this.gameEnd = false;

        MyApplication myApp = (MyApplication) getContext().getApplicationContext();
        sm = myApp.getSoundManager();

        // 디스플레이 크기 계산

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayWidth = size.x;
        displayHeight = size.y - RADIUS;
    }

    public boolean isBossStage(){
        return boss_stage;
    }

    public void setStage(int stage) {
        current_stage = stage;
        switch (current_stage) {
            case 1:
                add(new Ball(displayWidth * 0.3F, displayHeight * 0.25F, 0));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.25F, 0));

                add(new Ball(displayWidth * 0.5F, displayHeight * 0.75F, 1));

                shootChance = 16;
                border_three_star = 8;
                border_two_star = 4;
                break;
            case 2:
                add(new Ball(displayWidth * 0.3F, displayHeight * 0.25F, 0));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.25F, 0));

                add(new Ball(displayWidth * 0.3F, displayHeight * 0.75F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.75F, 1));
                shootChance = 16;
                border_three_star = 8;
                border_two_star = 4;
                break;
            case 3:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.25F, 0));

                add(new Ball(displayWidth * 0.5F, displayHeight * 0.55F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.75F, 1));
                b_list.get(0).setSkill(2, 1);

                shootChance = 15;
                border_three_star = 10;
                border_two_star = 5;
                break;
            case 4:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.25F, 0));

                add(new Ball(displayWidth * 0.3F, displayHeight * 0.75F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.75F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.75F, 1));

                b_list.get(0).setSkill(2, 1);
                b_list.get(0).setHp(100);
                b_list.get(1).setSkill(1, 1);
                b_list.get(3).setSkill(1, 1);
                shootChance = 15;
                border_three_star = 10;
                border_two_star = 5;
                break;
            case 5:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.25F, 0));

                add(new Ball(displayWidth * 0.5F, displayHeight * 0.65F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.3F, displayHeight * 0.8F, 1));

                add(new Item(context, displayWidth * 0.8F, displayHeight * 0.7F, 3));
                add(new Item(context, displayWidth * 0.2F, displayHeight * 0.7F, 3));
                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.9F, 3));

                b_list.get(0).setSkill(2, 1);

                shootChance = 10;
                border_three_star = 7;
                border_two_star = 4;
                break;
            case 6:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.25F, 0));

                add(new Ball(displayWidth * 0.5F, displayHeight * 0.65F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.3F, displayHeight * 0.8F, 1));

                b_list.get(0).setSkill(2, 1);
                b_list.get(1).setSkill(0, 1);

                shootChance = 15;
                border_three_star = 8;
                border_two_star = 4;
                break;
            case 7:

                add(new Ball(displayWidth * 0.5F, displayHeight * 0.25F, 0));

                add(new Ball(displayWidth * 0.4F, displayHeight * 0.65F, 1));
                add(new Ball(displayWidth * 0.6F, displayHeight * 0.65F, 1));

                b_list.get(0).setSkill(3, 1);

                shootChance = 12;
                border_three_star = 8;
                border_two_star = 4;
                break;
            case 8:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.2F, 0));

                add(new Ball(displayWidth * 0.65F, displayHeight * 0.6F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.35F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.85F, 1));
                add(new Ball(displayWidth * 0.35F, displayHeight * 0.6F, 1));

                b_list.get(0).setSkill(4, 1);

                shootChance = 12;
                border_three_star = 8;
                border_two_star = 4;
                break;
            case 9:

                add(new Ball(displayWidth * 0.5F, displayHeight * 0.22F, 0));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.9F, 0));

                add(new Ball(displayWidth * 0.35F, displayHeight * 0.6F, 1));
                add(new Ball(displayWidth * 0.35F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.65F, displayHeight * 0.6F, 1));
                add(new Ball(displayWidth * 0.65F, displayHeight * 0.8F, 1));

                b_list.get(1).setSkill(2, 1);

                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.7F, 3));
                shootChance = 9;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 10:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.22F, 0));
                add(new Ball(displayWidth * 0.2F, displayHeight * 0.9F, 0));
                add(new Ball(displayWidth * 0.8F, displayHeight * 0.9F, 0));

                add(new Ball(displayWidth * 0.5F, displayHeight * 0.4F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.52F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.64F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.76F, 1));

                b_list.get(0).setSkill(2, 1);
                b_list.get(1).setSkill(1, 1);
                b_list.get(2).setSkill(1, 1);
                shootChance = 12;
                border_three_star = 8;
                border_two_star = 4;

                boss_stage = true;
                break;
            case 11:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.5F, 0));

                add(new Ball(displayWidth * 0.65F, displayHeight * 0.5F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.35F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.66F, 1));
                add(new Ball(displayWidth * 0.35F, displayHeight * 0.5F, 1));

                b_list.get(0).setSkill(2, 1);

                add(new Item(context, displayWidth * 0.22F, displayHeight * 0.22F, 3));
                add(new Item(context, displayWidth * 0.78F, displayHeight * 0.78F, 3));

                current_stage = 11;
                shootChance = 12;
                border_three_star = 8;
                border_two_star = 4;

                break;
            case 12:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.22F, 0));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.35F, 0));

                add(new Ball(displayWidth * 0.3F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.8F, 1));

                b_list.get(0).setSkill(2, 1);
                b_list.get(1).setSkill(1, 1);
                b_list.get(3).setSkill(1, 1);
                b_list.get(3).setSkill(2, 1);
                b_list.get(3).setSkill(3, 1);
                shootChance = 16;
                border_three_star = 10;
                border_two_star = 5;
                break;
            case 13:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.5F, 0));

                add(new Ball(displayWidth * 0.3F, displayHeight * 0.2F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.2F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.2F, 1));
                add(new Ball(displayWidth * 0.3F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.8F, 1));

                //b_list.get(0).setSkill(2,1);
                for (int i = 1; i < 7; i++) {
                    b_list.get(i).setSkill(1, 1);
                    b_list.get(i).setHp(-50);
                }

                add(new Item(context, displayWidth * 0.22F, displayHeight * 0.5F, 1));
                add(new Item(context, displayWidth * 0.78F, displayHeight * 0.5F, 1));
                shootChance = 14;
                border_three_star = 10;
                border_two_star = 6;
                break;
            case 14:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.2F, 0));

                add(new Ball(displayWidth * 0.35F, displayHeight * 0.7F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.7F, 1));
                add(new Ball(displayWidth * 0.65F, displayHeight * 0.7F, 1));

                b_list.get(0).setSkill(3, 1);
                b_list.get(1).setSkill(1, 1);
                b_list.get(2).setSkill(1, 1);
                b_list.get(3).setSkill(1, 1);

                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.4F, 2));
                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.55F, 1));

                shootChance = 14;
                border_three_star = 10;
                border_two_star = 6;
                break;
            case 15:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.2F, 0));

                add(new Ball(displayWidth * 0.5F, displayHeight * 0.7F, 1));

                add(new Item(context, displayWidth * 0.2F, displayHeight * 0.45F, 4));
                add(new Item(context, displayWidth * 0.8F, displayHeight * 0.45F, 4));

                shootChance = 7;
                border_three_star = 4;
                border_two_star = 2;
                break;
            case 16:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.2F, 0));

                add(new Ball(displayWidth * 0.3F, displayHeight * 0.7F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.7F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.7F, 1));

                add(new Item(context, displayWidth * 0.3F, displayHeight * 0.35F, 4));
                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.45F, 4));
                add(new Item(context, displayWidth * 0.7F, displayHeight * 0.35F, 4));
                add(new Item(context, displayWidth * 0.3F, displayHeight * 0.85F, 2));
                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.85F, 2));
                add(new Item(context, displayWidth * 0.7F, displayHeight * 0.85F, 2));

                for (int i = 1; i < 4; i++) {
                    b_list.get(i).setHp(-40);
                }
                shootChance = 9;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 17:
                add(new Ball(displayWidth * 0.2F, displayHeight * 0.2F, 0));

                add(new Ball(displayWidth * 0.4F, displayHeight * 0.4F, 1));
                add(new Ball(displayWidth * 0.6F, displayHeight * 0.6F, 1));
                add(new Ball(displayWidth * 0.8F, displayHeight * 0.8F, 1));

                b_list.get(1).setSkill(1, 1);
                b_list.get(2).setHp(-40);
                b_list.get(3).setHp(-40);

                add(new Item(context, displayWidth * 0.9F, displayHeight * 0.5F, 4));
                add(new Item(context, displayWidth * 0.3F, displayHeight * 0.7F, 4));

                shootChance = 10;
                border_three_star = 5;
                border_two_star = 3;
                break;
            case 18:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.2F, 0));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.8F, 0));

                add(new Ball(displayWidth * 0.3F, displayHeight * 0.6F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.4F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.6F, 1));

                b_list.get(1).setSkill(4, 1);
                b_list.get(1).setHp(-80);

                for (int i = 2; i < 5; i++) {
                    b_list.get(i).setHp(20);
                }

                add(new Item(context, displayWidth * 0.2F, displayHeight * 0.3F, 4));
                add(new Item(context, displayWidth * 0.8F, displayHeight * 0.3F, 4));

                shootChance = 10;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 19:
                add(new Ball(displayWidth * 0.2F, displayHeight * 0.8F, 0));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.8F, 0));
                add(new Ball(displayWidth * 0.8F, displayHeight * 0.8F, 0));

                add(new Ball(displayWidth * 0.1F, displayHeight * 0.3F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.3F, 1));
                add(new Ball(displayWidth * 0.9F, displayHeight * 0.3F, 1));
                add(new Ball(displayWidth * 0.3F, displayHeight * 0.15F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.15F, 1));

                b_list.get(0).setSkill(4, 1);
                b_list.get(2).setSkill(4, 1);
                b_list.get(0).setHp(-80);
                b_list.get(2).setHp(-80);

                for (int i = 3; i < 6; i++) {
                    b_list.get(i).setHp(20);
                }
                b_list.get(6).setHp(-70);
                b_list.get(7).setHp(-70);

                shootChance = 10;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 20:
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.5F, 0));

                add(new Ball(displayWidth * 0.2F, displayHeight * 0.2F, 1));
                add(new Ball(displayWidth * 0.2F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.8F, displayHeight * 0.2F, 1));
                add(new Ball(displayWidth * 0.8F, displayHeight * 0.8F, 1));

                b_list.get(1).setSkill(1, 1);
                b_list.get(3).setSkill(1, 1);

                add(new Item(context, displayWidth * 0.1F, displayHeight * 0.1F, 4));
                add(new Item(context, displayWidth * 0.1F, displayHeight * 0.9F, 4));
                add(new Item(context, displayWidth * 0.9F, displayHeight * 0.1F, 2));
                add(new Item(context, displayWidth * 0.9F, displayHeight * 0.9F, 2));

                shootChance = 10;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 21:
                add(new Ball(displayWidth * 0.3F, displayHeight * 0.2F, 0));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.2F, 0));

                add(new Ball(displayWidth * 0.3F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.5F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.8F, 1));

                b_list.get(3).setSkill(1, 1);

                add(new Item(context, displayWidth * 0.2F, displayHeight * 0.5F, 5));
                add(new Item(context, displayWidth * 0.4F, displayHeight * 0.5F, 5));
                add(new Item(context, displayWidth * 0.6F, displayHeight * 0.5F, 5));
                add(new Item(context, displayWidth * 0.8F, displayHeight * 0.5F, 5));

                shootChance = 15;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 22:
                add(new Ball(displayWidth * 0.2F, displayHeight * 0.2F, 0));
                add(new Ball(displayWidth * 0.8F, displayHeight * 0.2F, 0));

                add(new Ball(displayWidth * 0.2F, displayHeight * 0.8F, 1));
                add(new Ball(displayWidth * 0.8F, displayHeight * 0.8F, 1));

                add(new Item(context, displayWidth * 0.2F, displayHeight * 0.5F, 5));
                add(new Item(context, displayWidth * 0.4F, displayHeight * 0.5F, 5));
                add(new Item(context, displayWidth * 0.6F, displayHeight * 0.5F, 5));
                add(new Item(context, displayWidth * 0.8F, displayHeight * 0.5F, 5));
                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.2F, 5));
                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.4F, 5));
                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.6F, 5));
                add(new Item(context, displayWidth * 0.5F, displayHeight * 0.8F, 5));

                shootChance = 14;
                border_three_star = 8;
                border_two_star = 4;
                break;
            case 23:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.2F,0));

                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.4F,1));
                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.6F,1));
                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.8F,1));

                for(int i = 1 ; i<10 ;i++){
                    add(new Item(context,displayWidth*0.1F,displayHeight *0.1F * i ,5));
                    add(new Item(context,displayWidth*0.9F,displayHeight *0.1F * i ,5));
                }

                b_list.get(0).setSkill(2,1);

                shootChance = 14;
                border_three_star = 8;
                border_two_star = 4;
                break;
            case 24:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.23F,0));

                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.4F,1));
                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.6F,1));
                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.8F,1));

                for(int i = 1 ; i<10 ;i++){
                    add(new Item(context,displayWidth*0.1F,displayHeight *0.1F * i ,5));
                    add(new Item(context,displayWidth*0.9F,displayHeight *0.1F * i ,5));
                }

                for(int i = 1 ; i<3 ;i++){
                    add(new Item(context,displayWidth*0.3F * i,displayHeight *0.1F ,5));
                    add(new Item(context,displayWidth*0.3F * i,displayHeight *0.9F ,5));
                }

                b_list.get(0).setSkill(3,1);

                shootChance = 12;
                border_three_star = 8;
                border_two_star = 4;
                break;

            case 25:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.2F,0));
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.4F,0));

                add(new Ball(displayWidth * 0.3F ,displayHeight * 0.85F,1));
                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.85F,1));
                add(new Ball(displayWidth * 0.7F ,displayHeight * 0.85F,1));

                for(int i = 1 ; i<4 ;i++){
                    add(new Item(context,displayWidth*0.25F * i,displayHeight *0.5F ,5));
                    add(new Item(context,displayWidth*0.25F * i,displayHeight *0.65F ,5));
                }

                b_list.get(0).setSkill(0,1);
                b_list.get(1).setSkill(4,1);
                b_list.get(1).setHp(-80);

                shootChance = 10;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 26:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.2F,0));

                add(new Ball(displayWidth * 0.3F ,displayHeight * 0.85F,1));
                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.85F,1));
                add(new Ball(displayWidth * 0.7F ,displayHeight * 0.85F,1));

                b_list.get(1).setHp(-50);
                b_list.get(3).setHp(-50);

                add(new Item(context,displayWidth*0.25F,displayHeight *0.5F ,6));
                add(new Item(context,displayWidth*0.75F,displayHeight *0.5F ,6));

                shootChance = 12;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 27:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.2F,0));
                add(new Ball(displayWidth * 0.5F - RADIUS *1.5F,displayHeight * 0.2F+RADIUS *1.5F,0));
                add(new Ball(displayWidth * 0.5F + RADIUS *1.5F,displayHeight * 0.2F+RADIUS *1.5F,0));

                add(new Ball(displayWidth * 0.3F ,displayHeight * 0.85F,1));
                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.85F,1));
                add(new Ball(displayWidth * 0.7F ,displayHeight * 0.85F,1));

                add(new Item(context,displayWidth*0.25F,displayHeight *0.5F ,6));
                add(new Item(context,displayWidth*0.75F,displayHeight *0.5F ,6));

                shootChance = 12;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 28:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.2F,0));

                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.8F,1));

                add(new Item(context,displayWidth * 0.35F,displayHeight *0.8F ,5));
                add(new Item(context,displayWidth * 0.65F,displayHeight *0.8F ,5));
                add(new Item(context,displayWidth * 0.35F,displayHeight *0.65F ,5));
                add(new Item(context,displayWidth * 0.5F,displayHeight *0.65F ,5));
                add(new Item(context,displayWidth * 0.65F,displayHeight *0.65F ,5));
                add(new Item(context,displayWidth * 0.35F,displayHeight *0.95F ,5));
                add(new Item(context,displayWidth * 0.5F,displayHeight *0.95F ,5));
                add(new Item(context,displayWidth * 0.65F,displayHeight *0.95F ,5));

                shootChance = 10;
                border_three_star = 5;
                border_two_star = 3;
                break;
            case 29:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.2F,0));

                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.8F,1));

                add(new Item(context,displayWidth * 0.15F,displayHeight *0.4F ,5));
                add(new Item(context,displayWidth * 0.85F,displayHeight *0.4F ,5));

                add(new AnimatedObject(context, displayWidth * 0.3F,displayHeight*0.5F,1));
                add(new AnimatedObject(context, displayWidth * 0.4F,displayHeight*0.5F,1));
                add(new AnimatedObject(context, displayWidth * 0.5F,displayHeight*0.5F,1));
                add(new AnimatedObject(context, displayWidth * 0.6F,displayHeight*0.5F,1));
                add(new AnimatedObject(context, displayWidth * 0.7F,displayHeight*0.5F,1));

                shootChance = 10;
                border_three_star = 5;
                border_two_star = 3;
                break;
            case 30:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.2F,0));

                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.7F,1));

                add(new Item(context,displayWidth * 0.1F,displayHeight *0.7F ,3));
                add(new Item(context,displayWidth * 0.9F,displayHeight *0.7F ,3));
                add(new Item(context,displayWidth * 0.5F,displayHeight *0.4F ,3));

                for(int i = 1 ; i<6 ;i++){
                    add(new AnimatedObject(context, displayWidth * (0.2F +0.1F * i),displayHeight*0.5F,1));
                    add(new AnimatedObject(context, displayWidth * (0.2F +0.1F * i),displayHeight*0.9F,1));
                }
                for(int i = 1 ; i<6 ;i++){
                    add(new AnimatedObject(context, displayWidth * 0.3F,displayHeight*(0.4F +0.1F * i),1));
                    add(new AnimatedObject(context, displayWidth * 0.7F,displayHeight*(0.4F +0.1F * i),1));
                }
                shootChance = 10;
                border_three_star = 6;
                border_two_star = 3;
                boss_stage = true;
                break;
            case 31:

                add(new Ball(displayWidth * 0.4F,displayHeight * 0.2F,0));
                add(new Ball(displayWidth * 0.6F,displayHeight * 0.2F,0));

                add(new Ball(displayWidth * 0.4F ,displayHeight * 0.7F,1));
                add(new Ball(displayWidth * 0.6F ,displayHeight * 0.7F,1));

                add(new Item(context,displayWidth * 0.5F,displayHeight *0.4F ,3));

                b_list.get(0).setSkill(2,1);
                b_list.get(1).setSkill(2,1);
                b_list.get(2).setSkill(1,1);
                b_list.get(3).setSkill(1,1);

                for(int i = 1 ; i<5 ;i++){
                    add(new AnimatedObject(context, displayWidth * 0.2F * i,displayHeight*0.5F,1));
                    add(new AnimatedObject(context, displayWidth * 0.2F * i,displayHeight*0.9F,1));
                }
                shootChance = 10;
                border_three_star = 6;
                border_two_star = 3;
                break;
            case 32:
                add(new Ball(displayWidth * 0.4F,displayHeight * 0.2F,0));
                add(new Ball(displayWidth * 0.6F,displayHeight * 0.2F,0));

                add(new Ball(displayWidth * 0.4F ,displayHeight * 0.7F,1));
                add(new Ball(displayWidth * 0.6F ,displayHeight * 0.7F,1));

                add(new Item(context,displayWidth * 0.5F,displayHeight *0.4F ,3));

                b_list.get(2).setSkill(5,1);
                b_list.get(3).setSkill(5,1);

                for(int i = 1 ; i<5 ;i++){
                    add(new AnimatedObject(context, displayWidth * 0.2F,displayHeight*0.2F*i,1));
                    add(new AnimatedObject(context, displayWidth * 0.8F,displayHeight*0.2F*i,1));
                }
                shootChance = 14;
                border_three_star = 10;
                border_two_star = 6;
                break;
            case 33:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.1F,0));
                add(new Ball(displayWidth * 0.38F,displayHeight * 0.22F,0));
                add(new Ball(displayWidth * 0.62F,displayHeight * 0.22F,0));

                add(new Ball(displayWidth * 0.4F ,displayHeight * 0.7F,1));
                add(new Ball(displayWidth * 0.6F ,displayHeight * 0.7F,1));

                b_list.get(1).setSkill(3,1);
                b_list.get(2).setSkill(3,1);
                b_list.get(3).setSkill(5,1);
                b_list.get(3).setSkill(1,1);
                b_list.get(4).setSkill(5,1);
                b_list.get(4).setSkill(1,1);

                for(int i = 1 ; i<5 ;i++){
                    add(new AnimatedObject(context, displayWidth * 0.2F,displayHeight*0.2F*i,1));
                    add(new AnimatedObject(context, displayWidth * 0.8F,displayHeight*0.2F*i,1));
                }

                add(new AnimatedObject(context, displayWidth * 0.4F,displayHeight*0.9F,1));
                add(new AnimatedObject(context, displayWidth * 0.6F,displayHeight*0.9F,1));

                shootChance = 12;
                border_three_star = 8;
                border_two_star = 4;
                break;

            case 34:
                add(new Ball(displayWidth * 0.2F,displayHeight * 0.22F,0));
                add(new Ball(displayWidth * 0.8F,displayHeight * 0.22F,0));

                add(new Ball(displayWidth * 0.4F ,displayHeight * 0.7F,1));
                add(new Ball(displayWidth * 0.6F ,displayHeight * 0.7F,1));

                add(new Item(context,displayWidth * 0.4F,displayHeight *0.5F ,2));
                add(new Item(context,displayWidth * 0.4F,displayHeight *0.6F ,4));
                add(new Item(context,displayWidth * 0.6F,displayHeight *0.5F ,2));
                add(new Item(context,displayWidth * 0.6F,displayHeight *0.6F ,4));


                b_list.get(2).setSkill(5,1);
                b_list.get(2).setSkill(1,1);
                b_list.get(3).setSkill(5,1);
                b_list.get(3).setSkill(1,1);

                for(int i = 1 ; i<5 ;i++){
                    add(new Item(context,displayWidth * 0.2F,displayHeight *0.2F * i ,5));
                    add(new Item(context,displayWidth * 0.8F,displayHeight *0.2F * i ,5));
                    add(new AnimatedObject(context, displayWidth * 0.2F * i,displayHeight * 0.9F,1));
                }

                shootChance = 12;
                border_three_star = 8;
                border_two_star = 4;
                break;
            case 35:
                add(new Ball(displayWidth * 0.2F,displayHeight * 0.22F,0));
                add(new Ball(displayWidth * 0.8F,displayHeight * 0.22F,0));

                add(new Ball(displayWidth * 0.4F ,displayHeight * 0.7F,1));
                add(new Ball(displayWidth * 0.6F ,displayHeight * 0.7F,1));

                add(new Item(context,displayWidth * 0.4F,displayHeight *0.5F ,2));
                add(new Item(context,displayWidth * 0.4F,displayHeight *0.6F ,4));
                add(new Item(context,displayWidth * 0.6F,displayHeight *0.5F ,2));
                add(new Item(context,displayWidth * 0.6F,displayHeight *0.6F ,4));


                b_list.get(2).setSkill(5,1);
                b_list.get(2).setSkill(1,1);
                b_list.get(3).setSkill(5,1);
                b_list.get(3).setSkill(1,1);
                b_list.get(2).setHp(-20);
                b_list.get(3).setHp(-20);

                add(new AnimatedObject(context, displayWidth * 0.2F,displayHeight * 0.9F,2));
                o_list.get(0).setEndPoint(displayWidth * 0.8F,displayHeight * 0.9F);
                o_list.get(0).setMove(50);

                shootChance = 14;
                border_three_star = 9;
                border_two_star = 5;
                break;
            case 36:
                add(new Ball(displayWidth * 0.3F,displayHeight * 0.1F,0));
                add(new Ball(displayWidth * 0.3F,displayHeight * 0.25F,0));
                add(new Ball(displayWidth * 0.7F,displayHeight * 0.1F,0));
                add(new Ball(displayWidth * 0.7F,displayHeight * 0.25F,0));

                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.6F,1));
                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.75F,1));

                b_list.get(3).setSkill(5,1);
                b_list.get(4).setSkill(5,1);

                add(new AnimatedObject(context, displayWidth * 0.1F,displayHeight * 0.2F,2));
                o_list.get(0).setEndPoint(displayWidth * 0.3F,displayHeight * 0.9F);
                o_list.get(0).setMove(50);

                add(new AnimatedObject(context, displayWidth * 0.9F,displayHeight * 0.2F,2));
                o_list.get(1).setEndPoint(displayWidth * 0.7F,displayHeight * 0.9F);
                o_list.get(1).setMove(50);

                shootChance = 12;
                border_three_star = 8;
                border_two_star = 4;
                break;
            case 37:
                add(new Ball(displayWidth * 0.8F,displayHeight * 0.1F,0));

                add(new Ball(displayWidth * 0.2F ,displayHeight * 0.9F,1));

                add(new Item(context,displayWidth * 0.2F,displayHeight *0.2F ,4));
                add(new Item(context,displayWidth * 0.8F,displayHeight *0.8F ,4));


                add(new AnimatedObject(context, displayWidth * 0.75F,displayHeight * 0.2F,2));
                o_list.get(0).setEndPoint(displayWidth * 0.25F,displayHeight * 0.8F);
                o_list.get(0).setMove(50);

                add(new AnimatedObject(context, displayWidth * 0.25F,displayHeight * 0.8F,2));
                o_list.get(1).setEndPoint(displayWidth * 0.75F,displayHeight * 0.2F);
                o_list.get(1).setMove(50);

                shootChance = 7;
                border_three_star = 4;
                border_two_star = 2;
                break;
            case 38:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.1F,0));
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.25F,0));

                add(new Ball(displayWidth * 0.4F ,displayHeight * 0.9F,1));
                add(new Ball(displayWidth * 0.6F ,displayHeight * 0.9F,1));

                for(int i = 1; i<5;i++) {
                    add(new Item(context, displayWidth * 0.2F * i, displayHeight * 0.4F, 5));
                    add(new Item(context, displayWidth * 0.2F * i, displayHeight * 0.6F, 5));
                }

                add(new AnimatedObject(context, displayWidth * 0.1F,displayHeight * 0.4F,2));
                o_list.get(0).setEndPoint(displayWidth * 0.9F,displayHeight * 0.4F);
                o_list.get(0).setMove(50);

                add(new AnimatedObject(context, displayWidth * 0.9F,displayHeight * 0.6F,2));
                o_list.get(1).setEndPoint(displayWidth * 0.1F,displayHeight * 0.6F);
                o_list.get(1).setMove(50);

                b_list.get(0).setSkill(4,1);
                b_list.get(1).setSkill(4,1);
                b_list.get(2).setSkill(5,1);
                b_list.get(3).setSkill(5,1);
                shootChance = 14;
                border_three_star = 9;
                border_two_star = 3;
                break;
            case 39:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.2F,0));
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.32F,0));

                add(new Ball(displayWidth * 0.4F ,displayHeight * 0.9F,1));
                add(new Ball(displayWidth * 0.6F ,displayHeight * 0.9F,1));

                add(new AnimatedObject(context, displayWidth * 0.1F,displayHeight * 0.4F,2));
                o_list.get(0).setEndPoint(displayWidth * 0.9F,displayHeight * 0.4F);
                o_list.get(0).setMove(50);

                add(new AnimatedObject(context, displayWidth * 0.9F,displayHeight * 0.6F,2));
                o_list.get(1).setEndPoint(displayWidth * 0.1F,displayHeight * 0.6F);
                o_list.get(1).setMove(50);

                for(int i = 1; i<5;i++) {
                    add(new Item(context, displayWidth * 0.15F, displayHeight * 0.2F * i, 5));
                    add(new Item(context, displayWidth * 0.85F, displayHeight * 0.2F * i, 5));
                    add(new AnimatedObject(context, displayWidth * 0.2F*i,displayHeight*0.05F,1));
                    add(new AnimatedObject(context, displayWidth * 0.2F*i,displayHeight*0.95F,1));
                }

                b_list.get(0).setSkill(3,1);
                b_list.get(1).setSkill(4,1);
                b_list.get(1).setHp(-80);
                b_list.get(2).setSkill(5,1);
                b_list.get(3).setSkill(5,1);
                b_list.get(2).setHp(20);
                b_list.get(3).setHp(20);

                shootChance = 14;
                border_three_star = 9;
                border_two_star = 3;
                break;
            case 40:
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.2F,0));
                add(new Ball(displayWidth * 0.5F,displayHeight * 0.32F,0));

                add(new Ball(displayWidth * 0.4F ,displayHeight * 0.9F,1));
                add(new Ball(displayWidth * 0.6F ,displayHeight * 0.9F,1));
                add(new Ball(displayWidth * 0.5F ,displayHeight * 0.7F,1));

                add(new AnimatedObject(context, displayWidth * 0.05F,displayHeight * 0.5F,2));
                o_list.get(0).setEndPoint(displayWidth * 0.5F,displayHeight * 0.5F);
                o_list.get(0).setMove(50);

                add(new AnimatedObject(context, displayWidth * 0.95F,displayHeight * 0.5F,2));
                o_list.get(1).setEndPoint(displayWidth * 0.5F,displayHeight * 0.5F);
                o_list.get(1).setMove(50);

                for(int i = 1; i<5;i++) {
                    add(new AnimatedObject(context, displayWidth * 0.2F*i,displayHeight*0.95F,1));
                }

                add(new Item(context, displayWidth * 0.05F, displayHeight * 0.05F, 5));
                add(new Item(context, displayWidth * 0.05F, displayHeight * 0.95F, 5));
                add(new Item(context, displayWidth * 0.95F, displayHeight * 0.05F, 5));
                add(new Item(context, displayWidth * 0.95F, displayHeight * 0.95F, 5));
                add(new Item(context, displayWidth * 0.15F, displayHeight * 0.8F, 3));
                add(new Item(context, displayWidth * 0.85F, displayHeight * 0.8F, 3));

                b_list.get(0).setSkill(0,1);
                b_list.get(1).setSkill(2,1);
                b_list.get(2).setSkill(5,1);
                b_list.get(2).setSkill(3,1);
                b_list.get(2).setSkill(1,1);
                b_list.get(3).setSkill(5,1);
                b_list.get(3).setSkill(1,1);
                b_list.get(3).setSkill(3,1);

                shootChance = 14;
                border_three_star = 9;
                border_two_star = 3;
                boss_stage = true;
                break;
            // default
            default:
                add(new Ball(displayWidth * 0.3F, displayHeight * 0.25F, 0));
                add(new Ball(displayWidth * 0.7F, displayHeight * 0.25F, 0));

                add(new Ball(displayWidth * 0.5F, displayHeight * 0.75F, 1));

                shootChance = 16;
                border_three_star = 8;
                border_two_star = 4;
                break;
        }
        showHint(stage);
    }

    public void add(Ball e){
        b_list.add(e);
        if (e.getPlayernumber() == 0){
            total_white++;
        }else{
            total_black++;
        }
    }

    public void add(Item i){
        i_list.add(i);
    }

    public void add(GameEffect e){
        e_list.add(e);
    }

    public void add(AnimatedObject o){
        o_list.add(o);
    }

    // Main2Activity 로부터 입력받은 터치를 통해 주변에 선택가능 한 공이있으면 index를 리턴해줌
    // 터치 거리 (90)안에 여러개 잇으면 가장 가까운 공이 선택됨
    public int isValidTouch(float x, float y){
        int minIndex = -1;
        float minDistance = 100000;
        float currentDistance;
        float ValidTouch_MaxDistance = 90;

        for(int i = 0; i<b_list.size(); i++){
            Ball e = b_list.get(i);
            if(e.getPlayernumber() == 1){continue;}
            currentDistance = e.getDistance(x,y-RADIUS);
            if (currentDistance < ValidTouch_MaxDistance){
                if(currentDistance < minDistance){
                    minDistance = currentDistance;
                    minIndex = i;
                }
            }
        }
        return minIndex;
    }

    public void shootBall(){
        ballIsMoving = true;
        shootChance--;
    }

    public boolean isballIsMoving() {
        return ballIsMoving;
    }

    public int isPurpleSkillMoving() {
        return purpleSkillMoving;
    }

    public void setPurpleSkillSignal(boolean signal){
        this.purpleSkillSignal = signal;
    }

    public boolean setSpeedIndex(int index, float toX, float toY){
        if (index > b_list.size()){
            return false;
        }else{
            Ball ball = b_list.get(index);
            if (ball.getDistance(toX,toY) < 100) return false;  // 너무 짧은 명령은 무시하고
            ball.setSpeedByDest(toX,toY);
            if(ball.getSkillLevel(4) >= 1){
                purpleSkillMoving = index;
            }
            return true;
        }
    }

    public void makeArrow(int target){
        float x = b_list.get(target).getX();
        float y = b_list.get(target).getY();
        targetIndex=target;

        arrow.setAngle(0);
        arrow.setX(x);
        arrow.setY(y);
        arrow.setSize(1);
        arrow.setVisible(true);
    }

    public void hideArrow(){
        arrow.setVisible(false);
    }

    public void setArrowAngle(float touchX, float touchY){

        double x = b_list.get(targetIndex).getX();
        double y = b_list.get(targetIndex).getY();
        float distance = arrow.getDistance(touchX,touchY);
        double theta = Math.atan2(touchY - y, touchX - x);
        theta += Math.PI/2.0;
        double angle = Math.toDegrees(theta);
        if (angle < 0) {
            angle += 360;
        }

        arrow.setAngle((float)angle);
        arrow.setSize(distance/RADIUS*10);
    }

    public void display(){
        cThread = new canvasThread();
        cThread.view = this;
        cThread.running = true;
        cThread.start();
    }

    public void lose(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                showFailBox(current_stage);
            }
        });
    }

    public void checkEnd(){
        if(gameEnd)return;
        if (total_white == 0 ){     // lose
            gameEnd = true;
            lose();
        }else if (total_black == 0){    //win
            gameEnd = true;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    showClearBox(current_stage);
                }
            });
        }
    }

    public void showHint(int stage){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.hintbox);
        dialog.setCancelable(false);

        TextView text = (TextView) dialog.findViewById(R.id.text);
        TextView border = (TextView) dialog.findViewById(R.id.border);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);

        if (stage == 1){
            text.setText(R.string.hint01);
            image.setImageResource(R.drawable.stage01_hint);
        }else if (stage == 2){
            text.setText(R.string.hint02);
            image.setImageResource(R.drawable.stage01_hint);
        }else if (stage == 3){
            text.setText(R.string.hint03);
            image.setImageResource(R.drawable.stage03_hint);
        }else if (stage == 4){
            text.setText(R.string.hint04);
            image.setImageResource(R.drawable.stage04_hint);
        }else if (stage == 5){
            text.setText(R.string.hint05);
            image.setImageResource(R.drawable.stage05_hint);
        }else if (stage == 6){
            text.setText(R.string.hint06);
            image.setImageResource(R.drawable.stage06_hint);
        }else if (stage == 7){
            text.setText(R.string.hint07);
            image.setImageResource(R.drawable.stage07_hint);
        }else if (stage == 8){
            text.setText(R.string.hint08);
            image.setImageResource(R.drawable.stage08_hint);
        }else if (stage == 9){// no hint
        }else if (stage == 10){
            text.setText(R.string.hint10);
            image.setImageResource(R.drawable.image_skull_red);
        }else if (stage == 11){text.setText(R.string.hint11);
        }else if (stage == 12){
            text.setText(R.string.hint12);
            image.setImageResource(R.drawable.stage12_hint);
        }else if (stage == 13){
            text.setText(R.string.hint13);
            image.setImageResource(R.drawable.item_recover);
        }else if (stage == 14){
            text.setText(R.string.hint14);
            image.setImageResource(R.drawable.item_shield);
        }else if (stage == 15){
            text.setText(R.string.hint15);
            image.setImageResource(R.drawable.item_power);
        }else if (stage == 16){
            text.setText(R.string.hint16);
        }else if (stage == 20){
            text.setText(R.string.hint20);
            image.setImageResource(R.drawable.image_skull_red);
        }else if (stage == 21){
            text.setText(R.string.hint21);
            image.setImageResource(R.drawable.item_banana);
        }else if (stage == 25){
            text.setText(R.string.hint25);
            image.setImageResource(R.drawable.stage06_hint);
        }else if (stage == 26){
            text.setText(R.string.hint26);
            image.setImageResource(R.drawable.item_question);
        }else if (stage == 29){
            text.setText(R.string.hint29);
            image.setImageResource(R.drawable.object_thron_1);
        }else if (stage == 30){
            text.setText(R.string.hint30);
            image.setImageResource(R.drawable.image_skull_red);
        }else if (stage == 32){
            text.setText(R.string.hint32);
            image.setImageResource(R.drawable.stage32_hint);
        }else if (stage == 33){
            text.setText(R.string.hint33);
            image.setImageResource(R.drawable.stage07_hint);
        }else if (stage == 35){
            text.setText(R.string.hint35);
            image.setImageResource(R.drawable.object_monkey_right_1);
        }else if (stage == 40){
            text.setText(R.string.hint40);
            image.setImageResource(R.drawable.image_skull_red);
        }
        border.setText("★★★("+border_three_star+"▲) / ★★☆("+border_two_star +"▲) / ★☆☆(0▲)" );
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sm.playSound(Constants.SOUND_BUTTON);
            }
        });
        dialog.show();
    }

    public void showClearBox(int stage){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.clearbox);

        SharedPreferences gameCache = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = gameCache.edit();
        int prev_star = gameCache.getInt("stage"+current_stage,0);
        int current_star;
        if (shootChance >= border_three_star) {
            current_star = 3;
        } else if (shootChance >= border_two_star) {
            current_star = 2;
        } else{
            current_star = 1;
        }
        if(current_star > prev_star)
            editor.putInt("stage" + current_stage, current_star);
        editor.commit();

        TextView text = (TextView) dialog.findViewById(R.id.text);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        dialog.setTitle(R.string.clearTitle);
        text.setText(R.string.clearText);
        dialog.setCancelable(false);
        image.setImageResource(R.drawable.clearbox_clear);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sm.playSound(Constants.SOUND_BUTTON);
                cThread.setRunning(false);
                Intent intent = new Intent(context, MainActivity.class );
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //It is use to finish current activity
                context.startActivity(intent);
            }
        });
        dialog.show();
    }

    public void showFailBox(int stage){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.clearbox);

        sm.playSound(Constants.SOUND_LOSE);
        //soundPlay(8);

        TextView text = (TextView) dialog.findViewById(R.id.text);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        dialog.setTitle(R.string.fail);
        dialog.setCancelable(false);
        text.setText(R.string.failText);
        image.setImageResource(R.drawable.clearbox_fail);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sm.playSound(Constants.SOUND_BUTTON);
                cThread.setRunning(false);
                Intent intent = new Intent(context, MainActivity.class );
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //It is use to finish current activity
                context.startActivity(intent);
            }
        });
        dialog.show();
    }

    public void stopThread(){
        cThread.setRunning(false);
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    class canvasThread extends Thread{
        TextureView view ;
        private boolean running;

        public void setRunning(boolean stop){
            this.running = stop;
        }

        public void ballDead(Ball b){
            d_list.add(new DeathAnimation(b.getX(), b.getY(), b.getPlayernumber()));

            sm.playSound(Constants.SOUND_BALL_DESTROYED);
            if (b.getPlayernumber() == 0) {
                total_white--;
                checkEnd();
            } else {
                total_black--;
                checkEnd();
            }
        }

        public void run(){
            while(running){
                Canvas canvas = view.lockCanvas();
                if (canvas == null) continue;

                canvas.drawColor( Color.WHITE);
                Drawable d;
                if(world == 1){
                    if(boss_stage){
                        d = ContextCompat.getDrawable(context, R.drawable.background_stage2);
                    }else{
                        d = ContextCompat.getDrawable(context, R.drawable.background_stage1);
                    }
                }else if(world == 2){
                    if(boss_stage){
                        d = ContextCompat.getDrawable(context, R.drawable.background_stage4);
                    }else{
                        d = ContextCompat.getDrawable(context, R.drawable.background_stage3);
                    }
                }else{
                    d = ContextCompat.getDrawable(context, R.drawable.background_stage1);
                }

                d.setBounds(0, 0, displayWidth, displayHeight+RADIUS);
                d.draw(canvas);

                boolean movingFlag = false; // any ball is moving

                // if skill 4 red activated (tapped)
                if(purpleSkillSignal && !gameEnd){
                    Ball ball = b_list.get(purpleSkillMoving);
                    float x = ball.getX();
                    float y = ball.getY();
                    float dx = ball.getDx();
                    float dy = ball.getDy();

                    add(new EffectPurpleExplosion(context,x,y));

                    sm.playSound(Constants.SOUND_BALL_SKILL_PURPLE);

                    ball.setSpeed(0,0);
                    for(Ball ball2 : b_list){
                        if(ball2.isAlive() == true && ball2.getPlayernumber() == 1 && ball.getDistance(ball2) < 4.5*RADIUS) {
                            if (!ball2.getDamage(50)) {
                                ballDead(ball2);
                            }else{
                                float destX = ball2.getX() - x;
                                float destY = ball2.getY() - y;

                                float angle = (float)Math.sqrt(dx*dx+dy*dy);
                                float powerVector = (float)Math.sqrt(destX*destX + destY*destY);
                                float newDx = destX/powerVector*angle / 2 ;
                                float newDy = destY/powerVector*angle / 2;
                                ball2.setSpeed(newDx,newDy);
                            }
                        }
                    }
                    purpleSkillSignal = false;
                    purpleSkillMoving = -1;
                    if(!ball.getDamage(10)){// 데미지를 주고, 죽엇을경우
                        ballDead(ball);
                    }
                }

                for(Ball e : b_list){
                    if(!e.isAlive()){
                        continue;
                    }
                    e.moving();

                    float x = e.getX();
                    float y = e.getY();
                    float dx = e.getDx();
                    float dy = e.getDy();

                    if( dx !=0 || dy !=0){
                        movingFlag = true;
                    }

                    // item collide
                    for(int index = 0; index<i_list.size();index++){
                    //for (Item item : i_list){
                        Item item = i_list.get(index);
                        if(item.getDistance(e) <2*(RADIUS)){
                            int item_type = item.getType();
                            if(item_type == 6){ // random item
                                int random = (int)(Math.random() * 100);
                                if(random < 20){
                                    item.setType(2);
                                }else if(random < 40){
                                    item.setType(4);
                                }else if(random < 60){
                                    e.setSkill(2,1);
                                }else if(random < 80){
                                    e.setSkill(3,1);
                                }else{
                                    e.setSkill(4,1);
                                }
                            }
                            item_type = item.getType();
                            switch(item_type){
                                case 1: //recover
                                    e.setHp(e.getHP() + 50);
                                    sm.playSound(Constants.SOUND_ITEM_HEAL);
                                    break;
                                case 2: // shield
                                    e.setItem_shield(true,5);
                                    sm.playSound(Constants.SOUND_ITEM_SHIELD);
                                    break;
                                case 3: // explosion
                                    add(new EffectExplosion(context,x,y));
                                    sm.playSound(Constants.SOUND_ITEM_EXPLODE);
                                    for (Ball e2 : b_list){
                                        if(e2.isAlive() == true && e2.getPlayernumber()== 1 && item.getDistance(e2) < 4.5*RADIUS) {
                                            if (!e2.getDamage(50)) {
                                                ballDead(e2);
                                            }
                                        }
                                    }
                                    for(int index2 = 0 ; index2<o_list.size() ; index2++){
                                        AnimatedObject object = o_list.get(index2);
                                        if(object.getDistance(e) < 5*RADIUS && object.getType()==1){
                                            o_list.remove(object);
                                        }
                                    }
                                    break;
                                case 4: //power
                                    e.setItem_powered(true);
                                    sm.playSound(Constants.SOUND_ITEM_POWERUP);
                                    break;
                                case 5: // banana
                                    e.setItem_banana(true);
                                    sm.playSound(Constants.SOUND_ITEM_BANANA);
                                    break;
                                default:
                                    break;
                            }
                            i_list.remove(item);
                        }
                    }

                    // handle Ball collide
                    for(Ball e2: b_list){
                        if(!e2.isAlive())continue;
                        if (e2.getDistance(e) < 2*(RADIUS) && e != e2){
                            // if collide
                            sm.playSound(Constants.SOUND_BALL_COLLIDE);
                            e2.setAttacked(); // 공격받은 egg2의 attacked 를 설정하여 바운스 공격력 보너스를 받지못하게함
                            e.setX(x - dx);// 겹쳐지지 않도록 위치 직접 수정
                            e.setY(y - dy);

                            if(e2.getSkillLevel(1) >0 && e2.getPlayernumber()!= e.getPlayernumber()){
                                // if skill 0, Heavy Counter
                                e2.addSpeed(dx * 0.1F, dy * 0.1F);
                                e.setSpeed(dx * -0.9F,dy * -0.9F );
                                e.setHeavy_countered(true);
                            }else if(e.getSkillLevel(3) >0){
                                // if skill 3, Red Power
                                float destX = e2.getX() - x;
                                float destY = e2.getY() - y;
                                float angle = (float)Math.sqrt(dx*dx+dy*dy);
                                float powerVector = (float)Math.sqrt(destX*destX + destY*destY);
                                float newDx = destX/powerVector*angle;
                                float newDy = destY/powerVector*angle;
                                if(newDy >= RADIUS) newDy = RADIUS;
                                if(newDx >= RADIUS) newDx = RADIUS;
                                e2.setSpeed(newDx,newDy);
                                e.setSpeed(dx * 0.7F,dy * 0.7F );
                            }else{
                                e2.addSpeed(dx * 0.45F, dy * 0.45F);
                                e.setSpeed(dx * -0.3F,dy * -0.3F );
                            }

                            if(e2.isAlive() == true && e2.getPlayernumber()!= e.getPlayernumber()){ // 서로 적일 경우
                                if(!e.attackBall(e2)){// 데미지를 주고, 죽엇을경우
                                    ballDead(e2);
                                }
                            }else{// 아군일경우
                                e2.getPowered(e.getAttack());
                                if(e2.getSkillLevel(0) > 0){
                                    e.setHp(e.getHP()+10);
                                }
                            }

                        }

                    }
                    // handle object collide
                    for(int index = 0 ; index<o_list.size() ; index++){
                        AnimatedObject object = o_list.get(index);
                        if(object.getType() == 1){
                            if(object.getDistance(e) <= 120 && e.getSkillLevel(3) ==0) {
                                if(!e.isObject_bush()){
                                    sm.playSound(Constants.SOUND_OBJECT_BUSH);
                                    e.setObject_bush(true);
                                    e.setDx(e.getDx()*0.05F);
                                    e.setDy(e.getDy()*0.05F);
                                    if (!e.getDamage(2)) {
                                        ballDead(e);
                                    }
                                }
                            }
                        }if(object.getType() == 2){
                            if(object.getDistance(e) <= 120) {
                                if(!e.isObject_monkey()){
                                    e.setObject_monkey(true);
                                    sm.playSound(Constants.SOUND_OBJECT_MONKEY);
                                    if(dx>0){
                                        e.setDx(50 + (float)Math.random() * 50 );
                                    }else{
                                        e.setDx(-50 + (float)Math.random() * -50);
                                    }
                                    if(dy>0){
                                        e.setDy(50 + (float)Math.random() * 50);
                                    }else{
                                        e.setDy(-50 + (float)Math.random() * -50);
                                    }
                                    o_list.remove(object);
                                    add(new EffectMonkeyDeath(context,x,y));
                                }
                            }
                        }
                    }

                    // 벽에 튕겻을때
                    boolean wall_bounced = false;
                    if( x > displayWidth - RADIUS){
                        if(!e.isX_border_hit()){
                            e.setX_border_hit(true);
                            wall_bounced = true;
                            if (e.getSkillLevel(2) > 0) {      // skill 2, super bounce
                                e.setSpeed(-1.5F * dx, 1.5F * dy);
                                e.getPowered(10);
                            } else {
                                e.setSpeed(-dx, dy);
                            }
                            while(e.getX() > displayWidth - RADIUS){
                                e.setX(e.getX() - 1);
                            }
                        }
                    }else if( x < RADIUS ){
                        if(!e.isX_border_hit()){
                            e.setX_border_hit(true);
                            wall_bounced = true;
                            if (e.getSkillLevel(2) > 0) {
                                e.setSpeed(-1.5F * dx, 1.5F * dy);
                                e.getPowered(10);
                            } else {
                                e.setSpeed(-dx, dy);
                            }
                            while(e.getX() < RADIUS){
                                e.setX(e.getX() + 1);
                            }
                        }
                    }else if( y > displayHeight - RADIUS) {
                        if(!e.isY_border_hit()) {
                            e.setY_border_hit(true);
                            wall_bounced = true;
                            if (e.getSkillLevel(2) > 0) {
                                e.setSpeed(1.5F * dx, -1.5F * dy);
                                e.getPowered(10);
                            } else {
                                e.setSpeed(dx, -dy);
                            }
                            while(e.getY() > displayHeight - RADIUS){
                                e.setY(e.getY() - 1);
                            }

                        }
                    }else if( y < RADIUS) {
                        if (!e.isY_border_hit()) {
                            e.setY_border_hit(true);
                            wall_bounced = true;
                            if (e.getSkillLevel(2) > 0) {
                                e.setSpeed(1.5F * dx, -1.5F * dy);
                                e.getPowered(10);
                            } else {
                                e.setSpeed(dx, -dy);
                            }
                            while(e.getY()<RADIUS){
                                e.setY(e.getY() + 1);
                            }
                        }
                    }else{
                        e.setY_border_hit(false);
                        e.setX_border_hit(false);
                    }

                    if(wall_bounced){ // apply in common in case of bounced
                        sm.playSound(Constants.SOUND_BALL_COLLIDE_WALL);
                        if (e.isHeavy_countered() == true) {    // skill 1 heavy countered
                            e.setHeavy_countered(false);
                            if (!e.getDamage(e.getAttack())) {
                                ballDead(e);
                            }
                        }
                        if (!e.isAttacked()) e.getPowered(10); // 공격받아서 튕겨져 나갔을경우는 공격력추가 안함

                    }

                    // Draw Circle, skills
                    Paint p = new Paint();

                    if(e.isHeavy_countered() == true){
                        p.setStyle( Paint.Style.FILL );
                        p.setColor(Color.MAGENTA);
                        canvas.drawCircle( e.getX(), e.getY(),RADIUS * 1.36F,p);
                    }
                    if(e.isItem_shield() == true){
                        p.setStyle( Paint.Style.FILL );
                        p.setColor(Color.rgb(135,206,250));
                        canvas.drawCircle( e.getX(), e.getY(),RADIUS * 1.22F,p);
                    }
                    if(e.isItem_powered() == true){
                        p.setStyle( Paint.Style.FILL );
                        p.setColor(Color.RED);
                        canvas.drawCircle( e.getX(), e.getY(),RADIUS * 1.12F,p);
                    }

                    p.setStyle( Paint.Style.FILL );
                    if(e.getPlayernumber() == 0){
                        p.setColor(Color.LTGRAY);
                    }
                    else{
                        p.setColor(Color.BLACK);
                    }
                    double ani_phase = e.getAnimationPhase() * 6;
                    int total_skill = e.getTotal_skil();
                    int sIndex = 0;
                    canvas.drawCircle( e.getX(), e.getY(),RADIUS,p);
                    if(e.getSkillLevel(0) > 0){
                        sIndex++;
                        float angle = 360/total_skill*sIndex;
                        angle = (float)Math.toRadians((angle));
                        float cx = e.getX()+(float)Math.cos(ani_phase+angle)*RADIUS*0.7F;
                        float cy = e.getY()+(float)Math.sin(ani_phase+angle)*RADIUS*0.7F;
                        p.setStyle( Paint.Style.FILL );
                        p.setColor(Color.GREEN);
                        canvas.drawCircle( cx, cy,RADIUS/3,p);
                    }if(e.getSkillLevel(1) > 0){
                        sIndex++;
                        float angle = 360/total_skill*sIndex;
                        angle = (float)Math.toRadians((angle));
                        float cx = e.getX()+(float)Math.cos(ani_phase+angle)*RADIUS*0.7F;
                        float cy = e.getY()+(float)Math.sin(ani_phase+angle)*RADIUS*0.7F;
                        p.setStyle( Paint.Style.FILL );
                        p.setColor(Color.YELLOW);
                        canvas.drawCircle( cx, cy,RADIUS/3,p);
                    }if(e.getSkillLevel(2) > 0){
                        sIndex++;
                        float angle = 360/total_skill*sIndex;
                        angle = (float)Math.toRadians((angle));
                        float cx = e.getX()+(float)Math.cos(ani_phase+angle)*RADIUS*0.7F;
                        float cy = e.getY()+(float)Math.sin(ani_phase+angle)*RADIUS*0.7F;
                        p.setStyle( Paint.Style.FILL );
                        p.setColor(Color.CYAN);
                        canvas.drawCircle( cx, cy,RADIUS/3,p);
                    }if(e.getSkillLevel(3) > 0){
                        sIndex++;
                        float angle = 360/total_skill*sIndex;
                        angle = (float)Math.toRadians((angle));
                        float cx = e.getX()+(float)Math.cos(ani_phase+angle)*RADIUS*0.7F;
                        float cy = e.getY()+(float)Math.sin(ani_phase+angle)*RADIUS*0.7F;
                        p.setStyle( Paint.Style.FILL );
                        p.setColor(Color.RED);
                        canvas.drawCircle( cx, cy,RADIUS/3,p);
                    }if(e.getSkillLevel(4) > 0){
                        sIndex++;
                        float angle = 360/total_skill*sIndex;
                        angle = (float)Math.toRadians((angle));
                        float cx = e.getX()+(float)Math.cos(ani_phase+angle)*RADIUS*0.7F;
                        float cy = e.getY()+(float)Math.sin(ani_phase+angle)*RADIUS*0.7F;
                        p.setStyle( Paint.Style.FILL );
                        p.setColor(Color.rgb(143,0,255));
                        canvas.drawCircle( cx, cy,RADIUS/3,p);
                    }if(e.getSkillLevel(5) > 0){
                        sIndex++;
                        float angle = 360/total_skill*sIndex;
                        angle = (float)Math.toRadians((angle));
                        float cx = e.getX()+(float)Math.cos(ani_phase+angle)*RADIUS*0.7F;
                        float cy = e.getY()+(float)Math.sin(ani_phase+angle)*RADIUS*0.7F;
                        p.setStyle( Paint.Style.FILL );
                        p.setColor(Color.rgb(255,255,255));
                        canvas.drawCircle( cx, cy,RADIUS*0.38F,p);
                        p.setColor(Color.rgb(0,0,0));
                        canvas.drawCircle( cx, cy,RADIUS/3,p);
                    }
                }   // end of for(Ball e : b_list)

                // 이동 종료시, check move of ball
                boolean prevMovingFlag = ballIsMoving;
                ballIsMoving = movingFlag;
                if(!ballIsMoving) {
                    purpleSkillMoving = -1;
                    if(prevMovingFlag && !ballIsMoving){ // 움직이다가 멈춘 한순간에만
                        for(Ball b : b_list){   // 모든공의 bush, monkey flag를 초기화
                            b.setObject_bush(false);
                            b.setObject_monkey(false);
                        }
                    }
                }

                // draw items
                for(Item i : i_list){
                    if(i.alive){
                        Bitmap bitmap = i.getBitmap();
                        float h = bitmap.getHeight();
                        float w = bitmap.getWidth();
                        canvas.drawBitmap(bitmap,i.getX()-w/2,i.getY()-h/2,null);

                        Bitmap bitmap2 = i.getGlitterBitmap();
                        canvas.drawBitmap(bitmap2,i.getX()-w/2,i.getY()-h/2,null);
                    }
                }

                // draw circle
                //for(DeathAnimation dead_egg : d_list){
                for(int index = 0; index<d_list.size();index++){
                    DeathAnimation dead_egg = d_list.get(index);
                    if(dead_egg.draw(canvas,RADIUS)) {
                        d_list.remove(dead_egg);
                    }
                }

                // draw animated objects
                for(int index = 0 ; index<o_list.size() ; index++){
                    AnimatedObject object = o_list.get(index);
                    Bitmap bitmap = object.getBitmap();
                    float h = bitmap.getHeight();
                    float w = bitmap.getWidth();
                    canvas.drawBitmap(bitmap,object.getX()-w/2,object.getY()-h/2,null);
                }

                // draw effects
                for(int index = 0 ; index<e_list.size() ; index++){
                    GameEffect effect = e_list.get(index);
                    if(effect.nextFrame()){
                        e_list.remove(index);
                        continue;
                    }
                    Bitmap bitmap = effect.getBitmap();
                    float h = bitmap.getHeight();
                    float w = bitmap.getWidth();
                    canvas.drawBitmap(bitmap,effect.getX()-w/2,effect.getY()-h/2,null);
                }

                // draw arrow
                if (arrow.isVisible()){
                    Resources res = getResources();
                    Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.arrow);
                    int h = bitmap.getHeight();
                    int w = bitmap.getWidth();
                    int resizedW = (int)(w * arrow.getSize());
                    int resizedH = (int)(h * arrow.getSize());
                    Matrix matrix = new Matrix();
                    matrix.postRotate(arrow.getAngle()+225);
                    Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    //canvas.drawBitmap(rotated,arrow.getX()-w/2,arrow.getY()-h/2,null);
/*
                    // RESIZE THE BIT MAP
                    matrix.postScale(resizedW, resizedH);
                    bitmap.recycle();
                    // "RECREATE" THE NEW BITMAP
                    Bitmap resizedBitmap = Bitmap.createBitmap(rotated, 0, 0, rotated.getWidth(), rotated.getHeight(), matrix, false);
                    rotated.recycle();
*/
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(rotated,resizedW,resizedH,true);
                    canvas.drawBitmap(resizedBitmap,arrow.getX()-resizedBitmap.getWidth()/2,arrow.getY()-resizedBitmap.getHeight()/2,null);
                }

                // Draw hp, ap
                for (Ball e : b_list){
                    if(!e.isAlive()){
                        continue;
                    }
                    int hp = e.getHP();
                    int attack = e.getAttack();
                    Paint p = new Paint();
                    p.setTextSize(40);
                    p.setTypeface(Typeface.create("Arial",Typeface.BOLD));

                    p.setColor(Color.RED);

                    canvas.drawText(Integer.toString(hp),e.getX() -  RADIUS*1.5F,e.getY() + RADIUS,p);

                    p.setColor(Color.BLUE);
                    if(e.isItem_powered()) p.setTextSize(55);
                    canvas.drawText(Integer.toString(attack),e.getX() +  RADIUS,e.getY() +  RADIUS,p);
                }

                // Draw current stage, attack chance
                Paint p = new Paint();
                p.setTextSize(50);
                p.setTypeface(Typeface.create("",Typeface.BOLD));
                p.setColor(Color.BLACK);
                canvas.drawText("Stage : " +Integer.toString(current_stage),50,50,p);

                if(shootChance >= border_three_star){
                    p.setColor(Color.BLACK);
                }else if(shootChance >= border_two_star){
                    p.setColor(Color.rgb(218,165,32));
                }else{
                    p.setColor(Color.RED);
                }

                Resources res = getResources();
                String text = res.getString(R.string.currentShootChance);
                canvas.drawText(text+ " : " +Integer.toString(shootChance),50,120,p);

                view.unlockCanvasAndPost(canvas);

                // if no egg is moving and 0 chnace
                if(!ballIsMoving && shootChance == 0){
                    running = false;
                    lose();
                }
            }
        }
    }
}
