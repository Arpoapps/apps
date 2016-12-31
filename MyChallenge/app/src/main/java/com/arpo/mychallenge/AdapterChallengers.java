package com.arpo.mychallenge;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jithin on 11/12/16.
 */
public class AdapterChallengers extends BaseAdapter {

    List<ListAvatar> list1;
    Context context;

    AdapterChallengers(Context C)
    {
         context = C;
    }

    AdapterChallengers(Context C, List< ListAvatar> list)
    {
        context = C;
        list1 = list;
    }

    @Override
    public int getCount() {
        return list1.size();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.grid_avatar, null, false);

            viewHolder.avatar_name = (TextView)convertView.findViewById(R.id.txt_avatarName);
            viewHolder.avatar_pushupTaken = (TextView)convertView.findViewById(R.id.txt_pushupCount);
            viewHolder.avatar_timeTaken = (TextView)convertView.findViewById(R.id.txt_pushUpTimeTaken);
            viewHolder.border = (RelativeLayout)convertView.findViewById(R.id.border);

/*

            viewHolder.border.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("JKS","Clicked on layout");
                }
            });
*/


            viewHolder.avatar_name.setText(list1.get(position).getName());
            viewHolder.avatar_pushupTaken.setText(list1.get(position).getPushUpCount());
            viewHolder.avatar_timeTaken.setText(list1.get(position).getPushUpTimeTaken());

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.avatar_name.setText(list1.get(position).getName());
        viewHolder.avatar_pushupTaken.setText(list1.get(position).getPushUpCount());
        viewHolder.avatar_timeTaken.setText(list1.get(position).getPushUpTimeTaken());

        if(list1.get(position).getSelected())
        {
            viewHolder.border.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        else
            viewHolder.border.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

        return convertView;
    }
    class  ViewHolder{
        //    ImageView img;
        TextView avatar_name;
        TextView avatar_pushupTaken;
        TextView avatar_timeTaken;
        RelativeLayout border;
    }
}
