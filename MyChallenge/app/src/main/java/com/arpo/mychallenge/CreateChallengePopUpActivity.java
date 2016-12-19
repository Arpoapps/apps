package com.arpo.mychallenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CreateChallengePopUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("JKS", "onCreate");
        setContentView(R.layout.activity_create_challenge_pop_up);
    }
}
