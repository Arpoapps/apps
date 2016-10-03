package com.appstory.aarppo;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class flash_only_screen extends AppCompatActivity {

    //int i = 0;
    Boolean bool = false;
    Handler handler,dhandler;
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_flash_only_screen);

        dhandler= new Handler();
        dhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Initial();
            }
        },1000);


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
        handler = new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 500);
                changeColor();
            }
        };
        handler.postDelayed(runnable,500);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
        dhandler.removeCallbacks(runnable);
    }

    public void changeColor() {
        if (bool) {
            findViewById(R.id.flash_only).setBackgroundColor(Color.YELLOW);
            bool = false;
        } else {
            findViewById(R.id.flash_only).setBackgroundColor(Color.BLUE);
            bool = true;

        }
    }
}
