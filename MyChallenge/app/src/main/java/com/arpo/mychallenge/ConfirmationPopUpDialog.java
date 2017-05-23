package com.arpo.mychallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmationPopUpDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_pop_up_dialog);


        String message = getIntent().getStringExtra("Message");

        TextView txt_message = (TextView)findViewById(R.id.txt_confirm_message);
        txt_message.setText(message);

        Button btn_ok = (Button)findViewById(R.id.btn_confirm_ok);
        Button btn_canecl = (Button)findViewById(R.id.btn_confirm_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentData = new Intent();
                intentData.putExtra("confirmation",1);
                setResult(RESULT_OK, intentData);
                finish();

            }
        });

        btn_canecl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentData = new Intent();
                intentData.putExtra("confirmation",0);
                setResult(RESULT_OK, intentData);
                finish();
            }
        });
    }
}
