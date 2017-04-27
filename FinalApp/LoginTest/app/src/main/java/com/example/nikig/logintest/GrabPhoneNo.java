package com.example.nikig.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GrabPhoneNo extends AppCompatActivity {


    private String eID,rID, phoneNumber, deviceAddress;
    private FirebaseAuth Auth;
    private DatabaseReference mDatabase;
    private Emergency mEmergency;
    private static String TAG = GrabPhoneNo.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grab_phone_no);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Log.d(TAG, intent.getStringExtra("emergencyid"));
        eID = intent.getStringExtra("emergencyid");
        rID = intent.getStringExtra("runnerid");
        deviceAddress = intent.getStringExtra("address");

        getEmergencyInfo(eID);
    }

    private void getEmergencyInfo(String eUID) {
        DatabaseReference myRef = mDatabase.child("emergency").child(eUID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mEmergency= dataSnapshot.getValue(Emergency.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        phoneNumber = mEmergency.getPhone();
                        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                        i.putExtra("address", deviceAddress);
                        i.putExtra("emergencyid", eID);
                        i.putExtra("runnerid", rID);
                        i.putExtra("phoneNo", phoneNumber);
                        startActivity(i);
                        finish();
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}
