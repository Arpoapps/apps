package com.appstory.aarppo;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class FlashScreen extends AppCompatActivity {

    //int i = 0;
    Boolean bool = false;
    Handler handler,dhandler;
    Runnable runnable;
    MediaPlayer mp;
    boolean strted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flash_screen);


        strted = false;
        dhandler= new Handler();
        dhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                strted = true;
                Initial();
            }
        },0);
        //*********SCREEN WAKE CODE *******
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }
    /*

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
    */
    private void Initial()
    {
        mp = MediaPlayer.create(this, R.raw.song);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            public void onCompletion(MediaPlayer mp)
            {
                Log.d("JKS", "Exit mediaplayer");
                mp.stop();
                handler.removeCallbacks(runnable);
                finish();
            }
        });
        strted = true;
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
        if(strted) {
            mp.stop();
            handler.removeCallbacks(runnable);
        }
    }

    public void changeColor() {
        if (bool) {
            findViewById(R.id.activity_flash_screen).setBackgroundColor(Color.YELLOW);
            bool = false;
        } else {
            findViewById(R.id.activity_flash_screen).setBackgroundColor(Color.BLUE);
            bool = true;

        }
    }
}
