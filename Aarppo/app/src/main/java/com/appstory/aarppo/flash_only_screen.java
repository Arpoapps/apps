package com.appstory.aarppo;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class flash_only_screen extends AppCompatActivity {

    //int i = 0;
    Boolean bool = false;
    Handler handler,dhandler;
    Runnable runnable;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_flash_only_screen);

        dhandler= new Handler();
        dhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Initial();
            }
        },1000);
        //*********SCREEN WAKE CODE *******
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

    }

    private void loadInterstitial() {

        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }
    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdClosed() {
            }
        });
        return interstitialAd;
    }
    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
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
        showInterstitial();
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
