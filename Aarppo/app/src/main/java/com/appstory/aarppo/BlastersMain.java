package com.appstory.aarppo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class BlastersMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , BlastersMatchFragment.OnFragmentInteractionListener,
        SettingsPage.OnFragmentInteractionListener,MatchSchedule.OnFragmentInteractionListener, ISLMatchPage.OnFragmentInteractionListener, ContactUs.OnFragmentInteractionListener, Credits.OnFragmentInteractionListener
{

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blasters_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if( android.provider.Settings.Global.getInt(getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0)== 1)
                {
                    Log.d("JKS","Auto update time is on");
                }
                else
                {
                    Log.d("JKS","Auto update time is off");
                    int requestCode = 99;
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), requestCode) ;

                }

            }
        }, 2000);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        Class fragmentClass = BlastersMatchFragment.class;
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blasters_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        selectDrawerItem(item);
        return true;
    }
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_match_blasters:
                fragmentClass = BlastersMatchFragment.class;
                break;
            case R.id.nav_schedule:
                fragmentClass = MatchSchedule.class;
                break;
            case R.id.nav_settings:
                fragmentClass = SettingsPage.class;
                break;
            case R.id.nav_contact:
                fragmentClass = ContactUs.class;
                Log.d("JKS","Clicked on contact us");
                break;
            case R.id.nav_credit:
                fragmentClass = Credits.class;
                Log.d("JKS","Clicked on Credits");
                break;
            default:
                fragmentClass = BlastersMatchFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
    }
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }


    //**********LOGOUT CODE***********



    public void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BlastersMain.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    //******* BACK KEY PRESS CODE*********


    @Override
    public void onBackPressed() {
        Fragment ft= getSupportFragmentManager().findFragmentById(R.id.flContent);
        if(ft instanceof BlastersMatchFragment)
        {    if(drawer.isDrawerOpen(GravityCompat.START))
        { drawer.closeDrawers();}
        else{  logout();}
        }
        else {
            super.onBackPressed();
        }
    }
}
