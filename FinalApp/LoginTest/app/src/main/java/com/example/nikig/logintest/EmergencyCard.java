package com.example.nikig.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmergencyCard extends AppCompatActivity {
    private TextView EmergencyName;
    private TextView EmergencyPhone;
    private TextView EmergencyLast;
    private TextView EmergencyRelation;
    private Toast toast = null;
    private static String TAG = EmergencyCard.class.getSimpleName();

    // [START declare_database_ref]

//    private DatabaseReference relatonshipDB;
//    private DatabaseReference emergencyDB;
//    private DatabaseReference runnerDB;
    private FirebaseAuth Auth;
    // [END declare_database_ref]

    private DatabaseReference mDatabase;

    private Emergency mEmergency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_card);
        EmergencyName = (TextView) findViewById(R.id.name_info);
        EmergencyPhone = (TextView) findViewById(R.id.phone_info);
        EmergencyLast = (TextView) findViewById(R.id.last_info);
        EmergencyRelation = (TextView) findViewById(R.id.relationship);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Log.d(TAG, intent.getStringExtra("emergencyid"));

        String eUID = intent.getStringExtra("emergencyid");

        getEmergencyInfo(eUID);

        //Auth = FirebaseAuth.getInstance();

    }

    private void getEmergencyInfo(String eUID) {
        DatabaseReference myRef = mDatabase.child("emergency").child(eUID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG,dataSnapshot.getChildren(""));
                mEmergency= dataSnapshot.getValue(Emergency.class);

                Log.d(TAG,"Runner= "+ mEmergency.getEfirstName());
//                Log.d(TAG,"Runner= "+ mRunner.getAge());
                Log.d(TAG,"Runner= "+ mEmergency.getPhone());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showEmergencyInfo();
                    }
                });

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });
    }

    private void showEmergencyInfo() {

        EmergencyName.setText(mEmergency.getEfirstName());
        EmergencyLast.setText(mEmergency.getElastName());
        EmergencyPhone.setText(mEmergency.getPhone());
        EmergencyRelation.setText(mEmergency.getRelation());

    }
}


//        relatonshipDB = FirebaseDatabase.getInstance().getReference("relationships");
//        emergencyDB = FirebaseDatabase.getInstance().getReference("emergency");
//        runnerDB = FirebaseDatabase.getInstance().getReference("runner");
