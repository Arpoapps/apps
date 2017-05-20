package com.arpo.mychallenge;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements HomePageFragment.OnFragmentInteractionListener,
        Fragment_ChallengeMain.OnFragmentInteractionListener,
        ChallengePageServer.OnFragmentInteractionListener,
        ChallengePageClient.OnFragmentInteractionListener,
        Fragment_Pushuplist.OnFragmentInteractionListener,
        Fragment_description.OnFragmentInteractionListener,
        CreateAvatar.OnFragmentInteractionListener {

    public static FragmentManager fragmentManager;

    ArpoWifi mWifiModule;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        mWifiModule = new ArpoWifi(this);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {

            try {

                Fragment ft = getSupportFragmentManager().findFragmentById(R.id.flContent);
                if (ft instanceof ChallengePageServer) {
                    Log.d("JKS", "Pressed from challenge page serer");
                    mWifiModule.turnoff_hotspot();

                    Fragment nextFrag = Fragment_ChallengeMain.class.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, nextFrag, "ChallengeMain")
                            .addToBackStack(null)
                            .commit();
                } else if (ft instanceof ChallengePageClient) {
                    Fragment nextFrag = Fragment_ChallengeMain.class.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, nextFrag, "ChallengeMain")
                            .addToBackStack(null)
                            .commit();
                    mWifiModule.forgetConnectedWifi();
                } else if (ft instanceof Fragment_ChallengeMain) {
                    try {
                        Fragment nextFrag = HomePageFragment.class.newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.flContent, nextFrag, "HomePage")
                                .addToBackStack(null)
                                .commit();
                    } catch (Exception ex) {

                    }
                }


                else if (ft instanceof CreateAvatar) {
                    Fragment nextFrag = Fragment_ChallengeMain.class.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, nextFrag, "ChallengeMain")
                            .addToBackStack(null)
                            .commit();
                } else if (ft instanceof Fragment_Pushuplist)

                {
                    try {
                        Fragment nextFrag = HomePageFragment.class.newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.flContent, nextFrag, "HomePage")
                                .addToBackStack(null)
                                .commit();
                    } catch (Exception ex) {

                    }


                } else
                    this.finish();

            } catch (Exception ex) {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(Uri uri) {
        //you can leave it empty
    }
}
