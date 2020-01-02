package com.example.junehyuckbae.game;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class StageAdapter extends BaseAdapter {
    private Context mContext;
    private Activity activity;
    private int[] clearData;
    private int world;
    private final int TOTALSTAGE = 21; // actually 20

    private Integer[] mThumbIds = {
            R.drawable.result_star_0,
            R.drawable.result_star_1,
            R.drawable.result_star_2,
            R.drawable.result_star_3,
    };

    public StageAdapter(Context c, Activity activity, int world) {
        this.activity = activity;
        this.clearData = new int[61];
        this.mContext = c;
        this.world = world;
    }

    public int getCount() {
        //return mThumbIds.length;
        return TOTALSTAGE-1;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder
    {
        public ImageView imgViewFlag;
        public TextView txtViewTitle;
        public TextView worldName;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        SharedPreferences gameCache = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = gameCache.edit();
        int index = 20*(world-1)+1;
        int index_end = 20*(world)+1;
        for(; index < index_end ; index ++){
            clearData[index] = gameCache.getInt("stage"+index ,0);
        }

        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();
        if(convertView==null)
        {
            view = new ViewHolder();
            if(position == 9 || position == 19){
                convertView = inflator.inflate(R.layout.gridview_boss, null);
            }else{
                convertView = inflator.inflate(R.layout.gridview_row, null);
            }

            view.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
            view.imgViewFlag = (ImageView) convertView.findViewById(R.id.imageView1);

            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }

        view.txtViewTitle.setText("Stage" + (position+20*(world-1)+1));
        if(clearData[20*(world-1)+position+1] == 1){
            view.imgViewFlag.setImageResource(mThumbIds[1]);
        }else if(clearData[20*(world-1)+position+1] == 2){
            view.imgViewFlag.setImageResource(mThumbIds[2]);
        }else if(clearData[20*(world-1)+position+1] == 3){
            view.imgViewFlag.setImageResource(mThumbIds[3]);
        }else{
            view.imgViewFlag.setImageResource(mThumbIds[0]);
        }

        return convertView;
    }

}