package com.appstory.aarppo;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jithin suresh on 27-09-2016.
 */
public class AdapterMatchList extends BaseAdapter {

    Context context;
    List<MatchList> list1;
    //List<Integer> img;
    Typeface mTypeFace;

    public AdapterMatchList(Context context, List<MatchList> list1){
        this.context=context;
        this.list1=list1;
        //this.list1=list1;
        //this.img=img;
        mTypeFace =  Typeface.createFromAsset(context.getAssets(), "fonts/century-gothic.ttf");
    }


    @Override
    public int getCount() {
        return list1.size();
    }  /// define the string  size else nothing is dsplayed

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
        String id= list1.get(position).getId();
        String query = "select team1, team2 from tbl_schedule where sched_id="+id;
        AarpoDb db = new AarpoDb();
        db.openConnection();
        Cursor c= db.selectData(query);
        int resource1 = 0;
        int resource2 = 0;
        if(c!=null)
        {
            while (c.moveToNext())
            {
                int team1 = c.getInt(0);
                int team2 = c.getInt(1);
                switch (team1)
                {
                    case 1 :
                        resource1 = R.drawable.team1;
                        break;
                    case 2:
                        resource1 = R.drawable.team2;
                        break;
                    case 3:
                        resource1 = R.drawable.team3;
                        break;
                    case 4:
                        resource1 = R.drawable.team4;
                        break;
                    case 5:
                        resource1 = R.drawable.team5;
                        break;
                    case 6:
                        resource1 = R.drawable.team6;
                        break;
                    case 7:
                        resource1 = R.drawable.team7;
                        break;
                    case 8:
                        resource1 = R.drawable.team8;
                        break;

                }
                switch (team2)
                {
                    case 1 :
                        resource2 = R.drawable.team1;
                        break;
                    case 2:
                        resource2 = R.drawable.team2;
                        break;
                    case 3:
                        resource2 = R.drawable.team3;
                        break;
                    case 4:
                        resource2 = R.drawable.team4;
                        break;
                    case 5:
                        resource2 = R.drawable.team5;
                        break;
                    case 6:
                        resource2 = R.drawable.team6;
                        break;
                    case 7:
                        resource2 = R.drawable.team7;
                        break;
                    case 8:
                        resource2 = R.drawable.team8;
                        break;

                }

            }
        }
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.custom_list_match_schedule, null, false);
            viewHolder.tv1=(TextView)convertView.findViewById(R.id.tv_name);

            //viewHolder.img=(ImageView)convertView.findViewById(R.id.img);
            viewHolder.team1=(TextView)convertView.findViewById(R.id.team1);
            viewHolder.team2=(TextView) convertView.findViewById(R.id.team2);
            viewHolder.place=(TextView) convertView.findViewById(R.id.txt_ground);
            viewHolder.time=(TextView) convertView.findViewById(R.id.txt_time);
            viewHolder.month=(TextView) convertView.findViewById(R.id.txt_mnth);
            viewHolder.img_team1=(ImageView) convertView.findViewById(R.id.img_team1);
            viewHolder.img_team2=(ImageView) convertView.findViewById(R.id.img_team2);

            viewHolder.tv1.setTypeface(mTypeFace);
            viewHolder.team1.setTypeface(mTypeFace);
            viewHolder.team2.setTypeface(mTypeFace);
            viewHolder.place.setTypeface(mTypeFace);
            viewHolder.time.setTypeface(mTypeFace);
            viewHolder.month.setTypeface(mTypeFace);


            viewHolder.team1.setText(list1.get(position).getTeam1());
            viewHolder.team2.setText(list1.get(position).getTeam2());
            viewHolder.place.setText(list1.get(position).getLocation());
            viewHolder.time.setText(list1.get(position).getTime());
            viewHolder.month.setText(list1.get(position).getMonth());

            viewHolder.img_team1.setImageResource(R.drawable.team1);
            viewHolder.img_team2.setImageResource(R.drawable.team2);

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        //viewHolder.img.setImageResource(list1.get(position).getImg());
        viewHolder.tv1.setTypeface(mTypeFace);
        viewHolder.team1.setTypeface(mTypeFace);
        viewHolder.team2.setTypeface(mTypeFace);
        viewHolder.place.setTypeface(mTypeFace);
        viewHolder.time.setTypeface(mTypeFace);
        viewHolder.month.setTypeface(mTypeFace);

        viewHolder.tv1.setText(list1.get(position).getName());
        viewHolder.team1.setText(list1.get(position).getTeam1());
        viewHolder.team2.setText(list1.get(position).getTeam2());
        viewHolder.place.setText(list1.get(position).getLocation());
        viewHolder.time.setText(list1.get(position).getTime());
        viewHolder.month.setText(list1.get(position).getMonth());

        viewHolder.img_team1.setImageResource(resource1);
        viewHolder.img_team2.setImageResource(resource2);
        db.closeConnection();
        return convertView;
    }

    class  ViewHolder{
    //    ImageView img;
        TextView tv1;
        TextView team1;
        TextView team2;
        TextView place;
        TextView time;
        TextView month;
        ImageView img_team1;
        ImageView img_team2;
    }

}
