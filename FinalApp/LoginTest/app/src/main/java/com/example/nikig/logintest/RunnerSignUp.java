package com.example.nikig.logintest;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RunnerSignUp extends AppCompatActivity {
    private TextView wel;
    private EditText first;
    private EditText last;
    private EditText dob;
    private EditText age;
    private  EditText emergency_id;
    private Button buttonSubmit;
    private Toast toast = null;

    // [START declare_database_ref]
    private DatabaseReference runnerDB;
    private FirebaseAuth Auth;
    // [END declare_database_ref]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runner_signup);

        runnerDB = FirebaseDatabase.getInstance().getReference("runner");
        Auth = FirebaseAuth.getInstance();

        wel = (TextView) findViewById(R.id.welcome);
        first = (EditText) findViewById(R.id.rfirst);
        last = (EditText) findViewById(R.id.rlast);
        dob = (EditText) findViewById(R.id.dob);
        age = (EditText) findViewById(R.id.age);
        emergency_id = (EditText) findViewById(R.id.emergency_id);
        buttonSubmit = (Button) findViewById(R.id.submit);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            //String f = first.getText().toString().trim();
           // String l = last.getText().toString().trim();
           // String d = dob.getText().toString().trim();
           // String a = age.getText().toString().trim();

            @Override
            public void onClick(View v) {

                Runner runner = new Runner(first.getText().toString(), last.getText().toString(), age.getText().toString(), dob.getText().toString(), emergency_id.getText().toString(), 0, 0);



                final FirebaseUser user = Auth.getCurrentUser();
                runnerDB.child(user.getUid()).setValue(runner);

                // Now update the information in the User's Auth profile as well
                UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                        .setDisplayName(first.getText().toString().trim() + " " + last.getText().toString().trim())
                        .build();
                user.updateProfile(update);

                toast.makeText(getApplicationContext(), "Posting Data...", Toast.LENGTH_SHORT).show();
//                wel.setText("Welcome "+ userInfo.getN());
                startActivity(new Intent(RunnerSignUp.this, MainActivity.class));
                finish();
            }

        });
    }
}

