package com.arpo.mychallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultPopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_pop_up);

        Intent data = getIntent();
        String pushUpTaken = data.getStringExtra("count");
        String timeTaken = data.getStringExtra("time");

        TextView txt_result = (TextView)findViewById(R.id.txt_resultPrint);

        txt_result.setText("You took "+pushUpTaken + " pushups in " + timeTaken +" seconds");
        Button btn_goBack = (Button) findViewById(R.id.btn_goBackSuccessPage);

        assert btn_goBack != null;
        btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
