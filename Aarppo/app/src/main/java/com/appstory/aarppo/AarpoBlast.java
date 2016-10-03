package com.appstory.aarppo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AarpoBlast extends AppCompatActivity {
    long timeLeft;
    TextView txtCountDown,txtarpo;// = (TextView)findViewById(R.id.txt_counter_cheer);
    Handler handler2;
    Boolean bool = false;
    Handler handler,dhandler;
    Runnable runnable;
    MediaPlayer mp;
    long matchTime;
    boolean donotplay = false;

    int vibrateLoop = 5;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private Date getDateNow()
    {
        Date today = new Date();
        //disabling time from network
        /*
        try {
            if( isNetworkAvailable()) {
                today = ISLMatchPage.getNetworkTime();
            }
            else {
                Log.d("JKS","No internet connection using local time");
                today = new Date();
            }
        }
        catch(IOException ex)
        {
        }*/
        return today;
    }

    public Runnable updateTimer = new Runnable() {
        public void run() {

            timeLeft--;
            txtCountDown.setText(""+timeLeft);

            handler2.postDelayed(this, 1000);

            if(timeLeft == 0 && donotplay == false)
            {
                handler2.removeCallbacks(updateTimer);
                txtCountDown.setVisibility(View.GONE);
                txtarpo.setVisibility(View.GONE);

                dhandler= new Handler();
                dhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Initial();
                    }
                },0);

            }


        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();



        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_aarpo_blast);

        donotplay = false;
        txtCountDown = (TextView)findViewById(R.id.txt_counter_cheer);
        txtarpo= (TextView) findViewById(R.id.textarpo);

        matchTime = getIntent().getLongExtra("todaysGameTime",0);
        Date dt= getDateNow();

        long diffTime_Match = matchTime - dt.getTime();
        long secs = TimeUnit.MILLISECONDS.toSeconds(diffTime_Match) % 60;

        // mP=new MediaPlayer();

        //  timeLeft = getIntent().getIntExtra("timeLeft",0);
        timeLeft =secs;
        handler2 = new Handler();
        handler2.postDelayed(updateTimer, 1000);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                 vibrateLoop = 5;
                int vibrateDuration = 1000;
                for(int i = 0; i < vibrateLoop ; i++)
                {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(vibrateDuration);
                    try {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException ex)
                    {

                    }

                }


            }
        });

        thread.start();

        //*********SCREEN WAKE CODE *******
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void Initial()
    {
        mp = MediaPlayer.create(this, R.raw.song);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mp)
            {
                Log.d("JKS","Exit mediaplayer");
                mp.stop();
                handler.removeCallbacks(runnable);
                finish();
            }
        });
        handler = new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 750);
                changeColor();
                mp.start();
            }
        };
        handler.postDelayed(runnable,750);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        donotplay = true;
        if(timeLeft <=0) {
            mp.stop();
            handler.removeCallbacks(runnable);
            handler2.removeCallbacks(runnable);
        }
        vibrateLoop = 0;
    }

    public void changeColor() {
        if (bool) {
            findViewById(R.id.flashscreen).setBackgroundColor(Color.YELLOW);
            bool = false;
        } else {
            findViewById(R.id.flashscreen).setBackgroundColor(Color.BLUE);
            bool = true;

        }
    }
}
