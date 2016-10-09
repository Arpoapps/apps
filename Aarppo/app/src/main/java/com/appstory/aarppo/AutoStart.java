package com.appstory.aarppo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jithin suresh on 30-09-2016.
 */
public class AutoStart extends BroadcastReceiver
{
    public void onReceive(Context arg0, Intent arg1)
    {
/*        Intent intent = new Intent(arg0,AarpoCheckService.class);
        arg0.startService(intent);*/

        /***************************************************************************************/
        /* SET ALARM FOR ARPO */
        Intent myIntent = new Intent(arg0, ArpoAlarmClass.class);
        PendingIntent pendingIntent  = PendingIntent.getBroadcast(arg0, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)arg0.getSystemService(arg0.ALARM_SERVICE);
        // Set the alarm to start at approximately 6:30:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 30);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateee= calendar.getTime();
        Log.d("ARPO","STARTING ARPO SERVICES AFTER BOOT");
        Log.d("ARPO","Date today = "+df.format(dateee));
        Log.d("ARPO","set alarm at 6:30");
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        /***************************************************************************************/
    }
}