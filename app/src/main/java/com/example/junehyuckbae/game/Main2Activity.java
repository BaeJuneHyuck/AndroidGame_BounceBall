package com.example.junehyuckbae.game;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TextureView;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
    Intent intent;
    private GameManager game;
    private int target = -1;
    private int world;
    private int stage;
    public static MediaPlayer mp;
    private float music_volume;
    SoundManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication myApp = (MyApplication) getApplication();
        sm = myApp.getSoundManager();

        intent = getIntent();
        stage = intent.getIntExtra("stage",1);
        world = intent.getIntExtra("world",1);
        stage = 20*(world-1)+stage; // true stage
        game = new GameManager(this,world);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        music_volume = MainActivity.getVolume_music();

        game.setStage(stage);

        if(world == 1){
            if(game.isBossStage()){
                mp = MediaPlayer.create(this , R.raw.bgm3 );
            }else {
                mp = MediaPlayer.create(this , R.raw.bgm2 );
            }
        }else if(world == 2){
            if(game.isBossStage()){
                mp = MediaPlayer.create(this , R.raw.bgm5 );
            }else {
                mp = MediaPlayer.create(this , R.raw.bgm4 );
            }
        }else if(world == 3){
            if(game.isBossStage()){
                mp = MediaPlayer.create(this , R.raw.bgm7 );
            }else {
                mp = MediaPlayer.create(this , R.raw.bgm6 );
            }
        }

        mp.start();
        mp.setVolume(music_volume,music_volume);
        mp.setLooping(true);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer m)
            {
                m.stop();
                m.release();
            }
        });
        setContentView(game);
        game.display();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mp != null) mp.stop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(game.isballIsMoving() && !game.isGameEnd()){
            if(game.isPurpleSkillMoving() >= 0){
                game.setPurpleSkillSignal(true);
            }
            return false;
        }

        float touchX = event.getX();
        float touchY = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                target = game.isValidTouch(touchX,touchY);
                if (target == -1) return false;
                game.makeArrow(target);
                break;

            case MotionEvent.ACTION_MOVE:
                game.setArrowAngle(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                if (target == -1) return false;
                if (game.setSpeedIndex(target, touchX, touchY)){
                    game.shootBall();
                }
                target = -1;
                game.hideArrow();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        sm.playSound(Constants.SOUND_PAGE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stage"+stage)
                .setMessage(R.string.stageOut)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){

                    public void onClick(DialogInterface dialog, int whichButton){
                        game.stopThread();
                        sm.playSound(Constants.SOUND_BUTTON);
                        Intent i = new Intent(  Main2Activity.this , MainActivity.class );
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //It is use to finish current activity
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                        sm.playSound(Constants.SOUND_BUTTON);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
