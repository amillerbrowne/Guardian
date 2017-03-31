package com.example.nikig.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class EmergencySignUp extends AppCompatActivity {
    private EditText eName;
    private EditText eLast;
    private EditText phone;
    private EditText relation;
    private Button Submit;

    private DatabaseReference emergency;
    private FirebaseAuth Auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_signup);

        eName = (EditText) findViewById(R.id.efirst);
         eLast = (EditText) findViewById(R.id.elast);
         phone = (EditText) findViewById(R.id.primary);
       relation = (EditText) findViewById(R.id.relation);
         Submit = (Button) findViewById(R.id.emergencysubmit);


     Submit.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {

         }
     }
