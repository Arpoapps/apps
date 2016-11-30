package com.arpo.mychallenge;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jithin suresh on 28-11-2016.
 */
public class ChallengesAdapter extends BaseAdapter {

    Context context;
    List<ScanResult> list1;

    public ChallengesAdapter(Context context, List<ScanResult> list1){
        this.context=context;
        this.list1=list1;
    }


    @Override
    public int getCount() {
        return list1.size();
    }  /// define the string  size else nothing is dsplayed

    public String getSSID(int position)
    {
        return list1.get(position).SSID;
    }

    @Override
    public Object getItem(int position) {
        return list1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.challenge_list_view_custom, null, false);

            viewHolder.challengeName = (TextView)convertView.findViewById(R.id.txt_challenge_Name);
            viewHolder.challengeType = (TextView)convertView.findViewById(R.id.txt_type);
            viewHolder.details = (TextView)convertView.findViewById(R.id.txt_count);

            String SSID = list1.get(position).SSID;
            String[] tokens = SSID.split("/");

            viewHolder.challengeName.setText(tokens[1]);
            viewHolder.challengeType.setText(tokens[2]);
            viewHolder.details.setText(tokens[3]);

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        String SSID = list1.get(position).SSID;
        String[] tokens = SSID.split("/");

        viewHolder.challengeName.setText(tokens[1]);
        viewHolder.challengeType.setText(tokens[2]);
        viewHolder.details.setText(tokens[3]);

        return convertView;
    }

    class  ViewHolder{
        //    ImageView img;
        TextView challengeName;
        TextView challengeType;
        TextView details;
    }

}
