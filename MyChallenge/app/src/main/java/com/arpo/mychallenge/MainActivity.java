package com.arpo.mychallenge;

import android.net.Uri;
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
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements  HomePageFragment.OnFragmentInteractionListener,
Fragment_ChallengeMain.OnFragmentInteractionListener,
ChallengePageServer.OnFragmentInteractionListener,
ChallengePageClient.OnFragmentInteractionListener,
CreateAvatar.OnFragmentInteractionListener{

    ArpoWifi mWifiModule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mWifiModule = new ArpoWifi(this);
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {

            try {

                Fragment ft= getSupportFragmentManager().findFragmentById(R.id.flContent);
                if(ft instanceof  ChallengePageServer)
                {
                    Log.d("JKS","Pressed from challenge page serer");
                    mWifiModule.turnoff_hotspot();

                    Fragment nextFrag = Fragment_ChallengeMain.class.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, nextFrag, "ChallengeMain")
                            .addToBackStack(null)
                            .commit();
                }
                else if(ft instanceof ChallengePageClient)
                {
                    Fragment nextFrag = Fragment_ChallengeMain.class.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, nextFrag, "ChallengeMain")
                            .addToBackStack(null)
                            .commit();
                    mWifiModule.forgetConnectedWifi();
                }
                else if(ft instanceof Fragment_ChallengeMain)
                {
                    try {
                        Fragment nextFrag = HomePageFragment.class.newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.flContent, nextFrag, "HomePage")
                                .addToBackStack(null)
                                .commit();
                    }
                    catch (Exception ex)
                    {

                    }
                }
                else if(ft instanceof CreateAvatar)
                {
                    Fragment nextFrag = Fragment_ChallengeMain.class.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.flContent, nextFrag, "ChallengeMain")
                            .addToBackStack(null)
                            .commit();
                }
                else
                    this.finish();

            }
            catch (Exception ex)
            {

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

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
}
