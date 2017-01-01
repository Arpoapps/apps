package com.arpo.mychallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Winner extends AppCompatActivity {

    private void print(String str) { Log.d("JKS", str);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        Intent data  = getIntent();
        String firstName = data.getStringExtra("firstName");
        String firstPushUp = data.getStringExtra("firstPushUpCount");
        String firstTime = data.getStringExtra("firstTime");

        print("First : "+firstName +" Took "+ firstPushUp +" in "+ firstTime +" seconds");

        TextView txt_first = (TextView)findViewById(R.id.txt_firstData);
        txt_first.setText(firstName +" Took "+ firstPushUp +" push ups in "+ firstTime +" seconds");
    }
}
