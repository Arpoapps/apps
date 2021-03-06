package com.appstory.aarppo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BlastersSplash extends AppCompatActivity {

    public static long ClockdriftTime = 0;
    Handler handler;
    Runnable runnable;
    boolean nopreview = false;
    public static long getTimeDelay()
    {
        return ClockdriftTime;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_blasters_splash);

        /***************************************************************************************/
        /* SET ALARM FOR ARPO */
        Intent myIntent = new Intent(this, ArpoAlarmClass.class);
        PendingIntent pendingIntent  = PendingIntent.getBroadcast(this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        // Set the alarm to start at approximately 6:30:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 30);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateee= calendar.getTime();
        Log.d("ARPO","Date today = "+df.format(dateee));
        Log.d("ARPO","set alarm at 6:30");
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        /***************************************************************************************/
        ClockdriftTime = 0;
/*
        Thread thread = new Thread() {
            @Override
            public void run() {
                ClockdriftTime = 0;
                try {
                    if(isNetworkAvailable()) {
                        Date netWorkTime = ISLMatchPage.getNetworkTime();
                        Date curTime = new Date();
                        long diffTime = netWorkTime.getTime() - curTime.getTime();

                        long secs = TimeUnit.MILLISECONDS.toSeconds(diffTime) % 60;
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(diffTime) % 60;
                        long hours = TimeUnit.MILLISECONDS.toHours(diffTime) % 24;
                        long days = TimeUnit.MILLISECONDS.toDays(diffTime);

                        Log.d("JKS","+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        Log.d("JKS", "Time drift" + days + ":" + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
                                + String.format("%02d", secs));
                        Log.d("JKS","+++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                        ClockdriftTime = diffTime;
                    }
                }catch (IOException ex)
                {

                }
            }
        };

        thread.start();*/


     /*   Log.d("JKS", "Get network time exits");
        SQLiteDatabase mdb = openOrCreateDatabase("aarpoDB", Context.MODE_PRIVATE, null);
        String query = "CREATE TABLE IF NOT EXISTS tbl_firstTime(started INTEGER)";
        mdb.execSQL(query);

        query = "select * from tbl_firstTime";
        Cursor c = mdb.rawQuery(query,null);
        if(c.getCount() == 0)
        {
            Log.d("JKS", "This is first time start");
            query = "insert into tbl_firstTime (started) values(1)";
            mdb.execSQL(query);
            handler = new Handler();
           runnable = new Runnable() {
                @Override
                public void run() {

                    if(nopreview) {
                        Intent i = new Intent(BlastersSplash.this, PreviewActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }else {

                        Intent i = new Intent(BlastersSplash.this, BlastersMain.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                    }

                }
            };
            handler.postDelayed(runnable,2000);
        }
        else {
            Log.d("JKS", "This is not the first that the application is runnnig");*/

            handler=new Handler();
            runnable=new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(BlastersSplash.this, BlastersMain.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    if( android.provider.Settings.Global.getInt(getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0)== 1)
                    {
                      //  Log.d("JKS","Auto update time is on");
                    }
                    else
                    {
                     //   Log.d("JKS","Auto update time is off");
                        int requestCode = 99;
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), requestCode) ;
                        Toast.makeText(getApplicationContext(), "Please enable automatic date and time in your settings",Toast.LENGTH_LONG).show();

                    }

                }
            };
            handler.postDelayed(runnable,2000);
        }
        /*mdb.close();*/

    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
