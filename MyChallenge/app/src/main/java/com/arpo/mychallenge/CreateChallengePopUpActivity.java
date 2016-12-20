package com.arpo.mychallenge;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateChallengePopUpActivity extends AppCompatActivity {

    CheckBox chk_timeChallenge;
    CheckBox chk_pushUpChallenge;

    CheckBox chk_selection1;
    CheckBox chk_selection2;
    CheckBox chk_selection3;
    CheckBox chk_selection4;
    CheckBox chk_selection5;

    CheckBox chk_enableHotspot;
    boolean enableHotspot = false;

    TextView txt_selectionText;

    ArpoWifi mArpoWifiModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge_pop_up);


        chk_pushUpChallenge = (CheckBox)findViewById(R.id.chk_push_up);
        chk_timeChallenge = (CheckBox) findViewById(R.id.chk_time_challenge);

        chk_selection1 = (CheckBox)findViewById(R.id.chk_selection1);
        chk_selection2 = (CheckBox)findViewById(R.id.chk_selection2);
        chk_selection3 = (CheckBox)findViewById(R.id.chk_selection3);
        chk_selection4 = (CheckBox)findViewById(R.id.chk_selection4);
        chk_selection5 = (CheckBox)findViewById(R.id.chk_selection5);

        chk_enableHotspot = (CheckBox)findViewById(R.id.chk_enableHotspot);
        enableHotspot = false;

        txt_selectionText = (TextView) findViewById(R.id.txt_selectText);

        mArpoWifiModule = new ArpoWifi(this);

        chk_timeChallenge.setChecked(true);
        chk_pushUpChallenge.setChecked(false);

        EditText edit_name = (EditText) findViewById(R.id.edit_challengeNAME);
        SharedPreferences prefs = getSharedPreferences("AVATAR_INFO", MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        edit_name.setText(restoredText+"'s Challenge");

        chk_pushUpChallenge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chk_pushUpChallenge.isChecked() == true) {
                    txt_selectionText.setText("Select PushUp Count");
                    chk_selection1.setText("20 pushups");
                    chk_selection2.setText("30 pushups");
                    chk_selection3.setText("50 pushups");
                    chk_selection4.setText("100 pushups");
                    chk_timeChallenge.setChecked(false);
                } else {
                    txt_selectionText.setText("Select Time");
                    chk_selection1.setText("30 seconds");
                    chk_selection2.setText("1 minute");
                    chk_selection3.setText("2 minute");
                    chk_selection4.setText("5 minute");
                    chk_timeChallenge.setChecked(true);
                }
            }
        });

        chk_timeChallenge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chk_timeChallenge.isChecked() == true) {
                    txt_selectionText.setText("Select Time");
                    chk_selection1.setText("30 seconds");
                    chk_selection2.setText("1 minute");
                    chk_selection3.setText("2 minute");
                    chk_selection4.setText("5 minute");
                    chk_pushUpChallenge.setChecked(false);
                } else {
                    txt_selectionText.setText("Select PushUp Count");
                    chk_selection1.setText("20 pushups");
                    chk_selection2.setText("30 pushups");
                    chk_selection3.setText("50 pushups");
                    chk_selection4.setText("100 pushups");
                    chk_pushUpChallenge.setChecked(true);
                }
            }
        });


        chk_selection5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chk_selection5.setChecked(false);
                /*if(chk_selection5.isChecked() == true)
                {
                    chk_selection1.setChecked(false);
                    chk_selection2.setChecked(false);
                    chk_selection3.setChecked(false);
                    chk_selection4.setChecked(false);

                }
                else {

                }*/
            }
        });

        chk_selection1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chk_selection1.isChecked() == true) {
                    chk_selection5.setChecked(false);
                    chk_selection2.setChecked(false);
                    chk_selection3.setChecked(false);
                    chk_selection4.setChecked(false);

                }
            }
        });
        chk_selection2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chk_selection2.isChecked() == true)
                {
                    chk_selection1.setChecked(false);
                    chk_selection5.setChecked(false);
                    chk_selection3.setChecked(false);
                    chk_selection4.setChecked(false);

                }
            }
        });
        chk_selection3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chk_selection3.isChecked() == true)
                {
                    chk_selection1.setChecked(false);
                    chk_selection2.setChecked(false);
                    chk_selection5.setChecked(false);
                    chk_selection4.setChecked(false);

                }
            }
        });
        chk_selection4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chk_selection4.isChecked() == true)
                {
                    chk_selection1.setChecked(false);
                    chk_selection2.setChecked(false);
                    chk_selection3.setChecked(false);
                    chk_selection5.setChecked(false);

                }
            }
        });

        chk_enableHotspot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chk_enableHotspot.isChecked() == true)
                    enableHotspot = true;
                else
                    enableHotspot = false;
            }
        });

        Button startChallange = (Button)findViewById(R.id.btn_startCHALLENGE);

        if (startChallange != null) {
            startChallange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText edit_name = (EditText) findViewById(R.id.edit_challengeNAME);
                    String challengeName = edit_name.getText().toString();
                    if(challengeName.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Enter challenge name",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(enableHotspot == true) {


                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    EditText edit_name = (EditText) findViewById(R.id.edit_challengeNAME);
                                    String challengeName = edit_name.getText().toString();
                                    String challengeType;
                                    String challengeCount="0";
                                    if(chk_pushUpChallenge.isChecked())
                                    {
                                        challengeType ="pushup";

                                        if(chk_selection1.isChecked()) {
                                            challengeCount="20";
                                        } else if(chk_selection2.isChecked()) {
                                            challengeCount="30";
                                        } else if(chk_selection3.isChecked()) {
                                            challengeCount="50";
                                        } else if(chk_selection4.isChecked()) {
                                            challengeCount="100";
                                        } else if(chk_selection5.isChecked()) {
                                            challengeCount="xx";
                                        }
                                    }
                                    else {
                                        challengeType ="time";

                                        if(chk_selection1.isChecked()) {
                                            challengeCount="30";
                                        } else if(chk_selection2.isChecked()) {
                                            challengeCount="1";
                                        } else if(chk_selection3.isChecked()) {
                                            challengeCount="2";
                                        } else if(chk_selection4.isChecked()) {
                                            challengeCount="5";
                                        } else if(chk_selection5.isChecked()) {
                                            challengeCount="xx";
                                        }
                                    }
                                    String apName = String.format("%s/%s/%s",challengeName,challengeType,challengeCount);
                                    apName = "ARPO/"+apName;


                                    mArpoWifiModule.turnOn_hotspot(apName);

                                    Fragment nextFrag = ChallengePageServer.class.newInstance();
                                    MainActivity.fragmentManager.beginTransaction()
                                            .replace(R.id.flContent, nextFrag, "ChallengePageServer")
                                            .addToBackStack(null)
                                            .commitAllowingStateLoss();
                                    finish();


                                }
                                catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                catch (InstantiationException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        thread.start();
                    }
                    else
                    {
                        try {
                            Fragment nextFrag = ChallengePageServer.class.newInstance();
                            MainActivity.fragmentManager.beginTransaction()
                                    .replace(R.id.flContent, nextFrag, "ChallengePageServer")
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                            finish();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }


                }
            });
        }
    }
}
