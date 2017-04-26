package com.example.nikig.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RunnerCard extends AppCompatActivity {
    private TextView runnerName;
    private TextView runnerLast;
    private TextView rLongitude;
    private TextView rLatitude;
    private Toast toast = null;
    private static String TAG = RunnerCard.class.getSimpleName();

    private DatabaseReference mDatabase;
    private FirebaseAuth Auth;
    private Runner mRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runner_card);

        runnerName = (TextView) findViewById(R.id.runnerName);
        runnerLast = (TextView) findViewById(R.id.runnerLast);
        rLongitude = (TextView) findViewById(R.id.rLongitude);
        rLatitude = (TextView) findViewById(R.id.rLatitude);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Log.d(TAG, intent.getStringExtra("runnerid"));

        String rUID = intent.getStringExtra("runnerid");


        getRunnerInfo(rUID);

        //Auth = FirebaseAuth.getInstance();
    }

    private void getRunnerInfo(String rUID) {
        DatabaseReference myRef = mDatabase.child("runner").child(rUID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRunner = dataSnapshot.getValue(Runner.class);
                Log.d(TAG, "IN HERE");
                Log.d(TAG, "Runner= " + mRunner.getFirstName());
//                Log.d(TAG,"Runner= "+ mRunner.getAge());
                Log.d(TAG, "Runner= " + mRunner.getLastName());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showRunnerInfo();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRunnerInfo() {
        runnerName.setText(mRunner.getFirstName());
        runnerLast.setText(mRunner.getLastName());
        String LongAsString = new Double(mRunner.getLongitude()).toString();
        rLongitude.setText(LongAsString);
        String LatAsString = new Double(mRunner.getLatitude()).toString();
        rLatitude.setText(LatAsString);
    }
}

