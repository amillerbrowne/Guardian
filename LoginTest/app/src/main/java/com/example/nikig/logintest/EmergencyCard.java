package com.example.nikig.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmergencyCard extends AppCompatActivity {
    private EditText EmergencyName;
    private EditText EmergencyPhone;
    private Toast toast = null;

    // [START declare_database_ref]

    private DatabaseReference relatonshipDB;
    private DatabaseReference emergencyDB;
    private DatabaseReference runnerDB;
    private FirebaseAuth Auth;
    // [END declare_database_ref]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_card);

        relatonshipDB = FirebaseDatabase.getInstance().getReference("relationships");
        emergencyDB = FirebaseDatabase.getInstance().getReference("emergency");
        runnerDB = FirebaseDatabase.getInstance().getReference("runner");
        Auth = FirebaseAuth.getInstance();





    }
}
