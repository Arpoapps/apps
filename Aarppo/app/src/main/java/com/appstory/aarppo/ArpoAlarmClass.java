package com.appstory.aarppo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jithin suresh on 07-10-2016.
 */
public class ArpoAlarmClass  extends BroadcastReceiver {
    public ArpoAlarmClass() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ARPO","Start match service @6:30");
        if(isPlayingToday()) {
            Intent ii = new Intent(context, AarpoCheckService.class);
            context.startService(ii);
        }
    }

    private  boolean isPlayingToday()
    {
        boolean result = false;

        AarpoDb db = new AarpoDb();
        db.openConnection();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date dt = new Date();
        String todaysDate = df.format(dt);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.DATE, +1);
        Date dayAfter = cal.getTime();
        cal.add(Calendar.DATE, -2);
        Date dayBefore = cal.getTime();

        String query = "select * from tbl_schedule WHERE date_time<'"+df.format(dayAfter) + "' AND date_time>'"+df.format(dayBefore)+"' AND (team1=2 OR team2=2)" ;
        Cursor c1 = db.selectData(query);

        if(c1.getCount() > 0) result = true;
        db.closeConnection();

        return result;

    }
}