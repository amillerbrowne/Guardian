package com.example.nikig.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

public class Signup2 extends AppCompatActivity implements View.OnClickListener {
    private Button buttonRunner;
    private Button buttonEc;
    private Toast toast = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup2);


            buttonRunner = (Button) findViewById(R.id.runner_btn);
            buttonEc = (Button) findViewById(R.id.ec_btn);

            buttonEc.setOnClickListener(this);
             buttonRunner.setOnClickListener(this);


        }

    @Override
    public void onClick(View v) {
        if (v == buttonEc) {
            startActivity(new Intent(Signup2.this, EmergencySignUp.class));
            finish();
        }
        else if (v == buttonRunner) {
            startActivity(new Intent(Signup2.this, RunnerSignUp.class));
            finish();
        }

    }
}
