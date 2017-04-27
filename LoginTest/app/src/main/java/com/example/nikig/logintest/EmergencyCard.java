package com.example.nikig.logintest;

import android.content.Intent;
import android.support.test.espresso.core.deps.guava.util.concurrent.FakeTimeLimiter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nikig.logintest.Emergency;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.text.TextUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class EmergencyCard extends AppCompatActivity {
    private TextView EmergencyName;
    private TextView EmergencyPhone;
    private TextView EmergencyLast;
    private TextView EmergencyRelation;
    private Toast toast = null;
    private static String TAG = EmergencyCard.class.getSimpleName();

    private DatabaseReference mDatabase;
    private FirebaseAuth Auth;
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


//        final String Find = EmergencyName.getText().toString();   //Get text for search edit text box
//        DatabaseReference myProjRef = mDatabase.child("emergency");
//        Query queryRef = firebase.database().ref("efirstName");
//        // System.out.println(dataSnapshot.getKey() + "is" + value.get("socialNumber"));
//        System.out.println(Find);
//
//        queryRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                System.out.println(dataSnapshot.getValue());
//                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
//
//                String name1 = String.valueOf(value.get("efirstName"));
//                //System.out.println(dataSnapshot.getKey() + "is" + value.get("fullName").toString());
//                if (name1.equals(Find)) {
//                    System.out.println("Name" + value.get("efirstName"));
//                } else {
//                    System.out.println("its is null");
//                }
//
//            }
//

//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }





        //String econtact = Runner.currentuser.getAge();
        // relatonshipDB.child()
        // emergencyDB.child(econtact).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //store information in Dictionary and then call that
////                Emergency emergency= dataSnapshot.getValue(Emergency.class);
////                System.out.println(emergency);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
