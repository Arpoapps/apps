package com.arpo.mychallenge;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class TakeChallenge extends AppCompatActivity  implements TextToSpeech.OnInitListener,SensorEventListener {

    int challengeType = -1;
    int time=-1;
    int pushUpCount=-1;

    boolean stopClk = false;

    public long startTime = 0;
    Timer stopwatchTimer;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    TextToSpeech tts;
    int nearCount = 0;
    TextView tv_num;

    String timeTaken;
    String pushUpTaken;

    boolean stopChallenge = false;

    boolean calledCompleteCb = false;

    void print(String str) {Log.d("JKS", str);}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_take_challenge);

        calledCompleteCb = false;

        SharedPreferences prefs = getSharedPreferences("GAME_INFO", MODE_PRIVATE);
        String challengeName = prefs.getString("name", null);

        print("ChallangeName=" + challengeName);

        String[] tokens = challengeName.split("/");
       /* for (String t : tokens)
            print("s=" + t);
        print("NAME:" + tokens[1] + " TYPE=" + tokens[2] + " COUNT=" + tokens[3]);
*/
        TextView txt_heading = (TextView) findViewById(R.id.txt_takechallenge_heading);
        txt_heading.setText("");

        if(tokens[2].equals("time"))
        {
            challengeType = 0;
            time=Integer.parseInt(tokens[3]);
            txt_heading.setText("PUSH UP TIME CHALLENGE");
        }
        else if(tokens[2].equals("pushup"))
        {
            challengeType=1;
            pushUpCount=Integer.parseInt(tokens[3]);
            txt_heading.setText("PUSH UP CHALLENGE");
        }
        else if(tokens[2].equals("staminatest"))
        {
            txt_heading.setText("STAMINA TEST CHALLENGE");
            challengeType=0;
            time = 10;
        }

        /*
        time challenge is a challenge where the time is fixed and the person who takes maximum pushups will win
        pushup challenge is the challenge where pushup count is fixed and the person who takes less time to take that much of pushups will win
         */

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 10);

        tv_num = (TextView)findViewById(R.id.runningCounter);

        if(tv_num != null)
            tv_num.setText("0");

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        nearCount = 0;

        stopClk = false;
        if(challengeType == 1) {
            nearCount = pushUpCount;
            startTimer();
        } else if(challengeType == 0)
        {
            nearCount = 0;
            startCountDownTimer(time);
        }
        tv_num.setText(String.format("%d", nearCount));

    }

    private void completedCb()
    {

        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                Intent challengeResult = new Intent(TakeChallenge.this, ResultPopUp.class);

                challengeResult.putExtra("time",timeTaken);
                challengeResult.putExtra("count",pushUpTaken);

                startActivityForResult(challengeResult, 301);
            }
        };
        thread.start();
    }

    public void startCountDownTimer(int time) {
        stopwatchTimer = new Timer();
        startTime = System.currentTimeMillis() + time * 1000;
        stopwatchTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView timerTextView = (TextView) findViewById(R.id.runningTimer);
                        try {
                            timerTextView.setText(stopwatch_CountDown());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (stopClk) {
                            stopwatchTimer.cancel();
                        }
                    }
                });
            }
        }, 0, 10);
    }


    public void startTimer() {
        stopwatchTimer = new Timer();
        startTime = System.currentTimeMillis();
        stopwatchTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView timerTextView = (TextView) findViewById(R.id.runningTimer);
                        try {
                            timerTextView.setText(stopwatch());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (stopClk) {
                            stopwatchTimer.cancel();
                        }
                    }
                });
            }
        }, 0, 10);
    }


    public String stopwatch() {
        long nowTime = System.currentTimeMillis();
        long cast = nowTime - startTime;
        cast -= TimeZone.getDefault().getRawOffset();
        Date date = new Date(cast);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss.S");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        timeTaken = simpleDateFormat.format(date);
        return simpleDateFormat.format(date);
    }

    public String stopwatch_CountDown() {
        long nowTime = System.currentTimeMillis();
        long cast = startTime - nowTime;
        if(cast <= 0)
        {
            stopClk= true;
            cast = 0;
            stopChallenge = true;

        }
        cast -= TimeZone.getDefault().getRawOffset();
        Date date = new Date(cast);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss.S");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        if(stopClk == true && stopChallenge == true && calledCompleteCb == false)
        {
            pushUpTaken = Integer.toString(nearCount);
            timeTaken = Integer.toString(time);

            calledCompleteCb = true;
            completedCb();
        }
        return simpleDateFormat.format(date);
    }

    @Override
    public void onBackPressed()
    {
        stopClk = true;
        finish();
    }

    // ************PROXIMITY SENSOR LISTENER ***********

    protected void onResume() {

        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    //******PROXIMITY SENSOR CHANGE EVENT *****
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(stopChallenge) return;

        if (event.values[0] < event.sensor.getMaximumRange()) {
            if(challengeType == 0)
            nearCount++;
            else nearCount--;
            if(challengeType == 1 && nearCount == 0 && calledCompleteCb == false)
            {
                stopChallenge = true;
                stopClk = true;
                pushUpTaken = Integer.toString(pushUpCount);

                completedCb();
            }
            tv_num.setText(String.format("%d", nearCount));
            speakOut();

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //********* TEXT TO SPEECH CODE***********
    public void onDestroy() {
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                tts = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        } else if(requestCode == 301 )
        {

            Intent intentData = new Intent();
            intentData.putExtra("time",timeTaken);
            intentData.putExtra("count",pushUpTaken);

            setResult(RESULT_OK, intentData);
            finish();
        }
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if(tts.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(Locale.US);
        }
        else if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void speakOut() {

        String text =tv_num.getText().toString();

        if(Build.VERSION.RELEASE.startsWith("5"))
        {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }
        else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

}
