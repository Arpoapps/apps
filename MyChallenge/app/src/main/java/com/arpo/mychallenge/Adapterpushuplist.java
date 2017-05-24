package com.arpo.mychallenge;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SONY on 29-03-2017.
 */

public class Adapterpushuplist extends BaseAdapter {

    Context context;
    List<pushupc> list1;

    public Adapterpushuplist(Context context, List<pushupc> list1) {
        this.context = context;
        this.list1 = list1;
    }


    @Override
    public int getCount() {
        return list1.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.customlistviewexcersises, null, false);
            viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv_name);
/*            viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tv_next);
            viewHolder.tv3 = (TextView) convertView.findViewById(R.id.tv_top);
            viewHolder.tv4 = (TextView) convertView.findViewById(R.id.tv_best);*/
            viewHolder.tv5 = (TextView) convertView.findViewById(R.id.tv_besttime);
            viewHolder.tv6 = (TextView) convertView.findViewById(R.id.tv_nextbest);
            viewHolder.tv7 = (TextView) convertView.findViewById(R.id.tv_topbest);
            viewHolder.percent = (TextView) convertView.findViewById(R.id.percentComplete);

            viewHolder.progressLayout = (RelativeLayout)convertView.findViewById(R.id.percentageProgress);

            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        viewHolder.img.setImageResource(list1.get(position).getImage());
        viewHolder.tv1.setText(list1.get(position).getName());
       /* viewHolder.tv2.setText(list1.get(position).getNext());
        viewHolder.tv3.setText(list1.get(position).getTop());
        viewHolder.tv4.setText(list1.get(position).getBest());*/
        viewHolder.tv5.setText(list1.get(position).getBesttime());
        viewHolder.tv6.setText(list1.get(position).getNextbest());
        viewHolder.tv7.setText(list1.get(position).getTopbest());


        int percentageTaken = ((Integer.parseInt(list1.get(position).getTaken())) * 100)/(Integer.parseInt(list1.get(position).getTopbest()));

        if(percentageTaken != 0 && percentageTaken < 20)
        {
            //red color
            viewHolder.progressLayout.setBackgroundColor(context.getResources().getColor(R.color.red));
        }
        else if(percentageTaken >=20 && percentageTaken < 40 )
        {
            // yellow color
            viewHolder.progressLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            viewHolder.percent.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        else if(percentageTaken >=40 && percentageTaken < 80 )
        {
            // light green
            viewHolder.progressLayout.setBackgroundColor(context.getResources().getColor(R.color.light_green));
        }
        else if(percentageTaken >=80 && percentageTaken <= 100 )
        {
            // green color
            viewHolder.progressLayout.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        else
        {
            // default color
            viewHolder.progressLayout.setBackgroundColor(context.getResources().getColor(R.color.grey));
        }

        viewHolder.percent.setText(percentageTaken+"%");

        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
        TextView tv5;
        TextView tv6;
        TextView tv7;
        TextView percent;
        RelativeLayout progressLayout;

    }

}
