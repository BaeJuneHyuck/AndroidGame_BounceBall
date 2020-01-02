package com.example.junehyuckbae.game;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class MainActivity extends AppCompatActivity {

    private final static int MAX_VOLUME = 1;
    private static float volume_sound;
    private static float volume_music;

    SoundManager sm;

    private MediaPlayer mp;
    private PublisherAdView mPublisherAdView;

    Button [] menuButtons;
    TextView t1;
    SeekBar soundBar;
    SeekBar musicBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        SharedPreferences gameCache = PreferenceManager.getDefaultSharedPreferences(this);
        volume_music = gameCache.getFloat("volumem",MAX_VOLUME/2);
        volume_sound = gameCache.getFloat("volumes",MAX_VOLUME/2);

        MyApplication myApp = (MyApplication) getApplication();
        sm = myApp.getSoundManager();
        sm.setSoundPool_volume(volume_sound);

        menuButtons = new Button[3];
        menuButtons[0] = findViewById(R.id.button1);
        menuButtons[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sm.playSound(Constants.SOUND_BUTTON);
                showStageList(1);
            }
        });

        menuButtons[1] = findViewById(R.id.button2);
        menuButtons[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sm.playSound(Constants.SOUND_BUTTON);
                showSettings();
            }
        });

        menuButtons[2] = findViewById(R.id.button3);
        menuButtons[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sm.playSound(Constants.SOUND_BUTTON);
                onBackPressed();
            }
        });

        mp = MediaPlayer.create(this , R.raw.bgm1 );
        mp.setVolume(volume_music,volume_music);
        mp.start();
        mp.setLooping(true);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer m)
            {
                m.stop();
                m.release();
            }
        });

        t1 = findViewById(R.id.textView2 );
        t1.setText("");
        MobileAds.initialize(this,
                "ca-app-pub-9364388107483050~2530993489");

        // 광고추가
        mPublisherAdView = findViewById(R.id.adView);
        mPublisherAdView.setAdListener(new AdListener() {

            public void onAdLoaded() {  t1.append("AD loaded"); }
            public void onAdFailedToLoad(int errorCode) {
                t1.append("AD error:"+errorCode);
            }
            public void onAdOpened() {  t1.append("AD opened"); }
            public void onAdLeftApplication(){  t1.append("AD left"); }
            public void onAdClosed() {  t1.append("AD closed"); }
        });
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mp != null) mp.stop();
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.gameOutTitle)
                .setMessage(R.string.gameOutMessage)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showStageList(int world){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.stage_select);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        final Context context = this;
        GridView gridview = (GridView) dialog.findViewById(R.id.gridview);
        TextView title = (TextView)dialog.findViewById(R.id.world_name);
        ImageButton nextWorld = (ImageButton)dialog.findViewById(R.id.button_next);
        ImageButton prevWorld = (ImageButton)dialog.findViewById(R.id.button_prev);
        if(world==1){
            String WorldName = getResources().getString(R.string.world_1);
            Spannable wordtoSpan = new SpannableString(WorldName);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 10, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.LTGRAY), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            title.setText(wordtoSpan);

            gridview.setAdapter(new StageAdapter(context,this,world));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    dialog.dismiss();
                    sm.playSound(Constants.SOUND_BUTTON);
                    Intent i = new Intent(  MainActivity.this, Main2Activity.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("world",1);
                    i.putExtra("stage", position+1);
                    startActivity(i);
                    //finish();
                }
            });

            prevWorld.setVisibility(View.GONE);
            nextWorld.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    showStageList(2);
                    sm.playSound(Constants.SOUND_PAGE);
                }
            });
        }else if(world == 2){
            String WorldName = getResources().getString(R.string.world_2);
            Spannable wordtoSpan = new SpannableString(WorldName);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(210,181,91)), 11, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            title.setText(wordtoSpan);

            gridview.setAdapter(new StageAdapter(context,this,world));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    dialog.dismiss();
                    sm.playSound(Constants.SOUND_BUTTON);
                    Intent i = new Intent(  MainActivity.this, Main2Activity.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("world",2);
                    i.putExtra("stage", position+1);
                    startActivity(i);
                    //finish();
                }
            });

            prevWorld.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    showStageList(1);
                    sm.playSound(Constants.SOUND_PAGE);
                }
            });
            nextWorld.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    showStageList(3);
                    sm.playSound(Constants.SOUND_PAGE);
                }
            });
        }else{
            String WorldName = getResources().getString(R.string.world_3); // "Over the rainbow"
            Spannable wordtoSpan = new SpannableString(WorldName);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(143,0,255)), 15, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(75,0,130)), 14, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(0,0,255)), 13, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(0,255,0)), 12, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(210,181,91)), 11, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(255,127,0)), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(255,0,0)), 9, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.LTGRAY), 5, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            title.setText(wordtoSpan);

            gridview.setAdapter(new StageAdapter(context,this,world));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    dialog.dismiss();
                    sm.playSound(Constants.SOUND_BUTTON);
                    Intent i = new Intent(  MainActivity.this, Main2Activity.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("world",3);
                    i.putExtra("stage", position+1);
                    startActivity(i);
                    //finish();
                }
            });
            nextWorld.setVisibility(View.GONE);
            prevWorld.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    showStageList(2);
                    sm.playSound(Constants.SOUND_PAGE);
                }
            });
        }

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public static float getVolume_sound() {
        return volume_sound;
    }

    public static float getVolume_music() {
        return volume_music;
    }

    public void showSettings(){
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.settingbox);

        SharedPreferences gameCache = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = gameCache.edit();
        final float loaded_volumeM = gameCache.getFloat("volumem",MAX_VOLUME/2);
        final float loaded_volumeS = gameCache.getFloat("volumes",MAX_VOLUME/2);

        soundBar = (SeekBar)dialog.findViewById(R.id.seekBarSound);
        musicBar = (SeekBar)dialog.findViewById(R.id.seekBarBgm);
        final Button okButton = (Button)dialog.findViewById(R.id.button_adjust);

        //init
        soundBar.setMax(MAX_VOLUME);
        soundBar.setProgress((int)(loaded_volumeS*100));
        musicBar.setMax(MAX_VOLUME);
        musicBar.setProgress((int)(loaded_volumeM*100));

        soundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float new_volumeS = 0.01F * progress;
                volume_sound = new_volumeS;
            }

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }
        });
        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final float new_volumeM = 0.01F * progress;
                volume_music = new_volumeM;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                editor.putFloat("volumes",volume_sound);
                editor.putFloat("volumem",volume_music);
                editor.commit();
                mp.setVolume(volume_music,volume_music);
                sm.setSoundPool_volume(volume_sound);
                sm.playSound(Constants.SOUND_BUTTON);
            }
        });

        dialog.show();
    }
}
