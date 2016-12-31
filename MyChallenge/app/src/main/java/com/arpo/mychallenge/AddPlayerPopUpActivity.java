package com.arpo.mychallenge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPlayerPopUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player_pop_up);

        Button btn_addPlayer = (Button)findViewById(R.id.btn_addPlayer);

        if (btn_addPlayer != null) {
            btn_addPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText in_name = (EditText)findViewById(R.id.edit_playerName);
                    EditText in_age = (EditText)findViewById(R.id.edit_playerAge);

                    String name = in_name.getText().toString();
                    String age = in_age.getText().toString();

                    if(!name.equals("") || !age.equals("")) {
                        Log.d("JKS", "Name = " + name + " Age=" + age);
                        Intent data = new Intent();
                        data.putExtra("name", name);
                        data.putExtra("age", age);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                    Toast.makeText(getApplicationContext(),"Fill the feilds",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
