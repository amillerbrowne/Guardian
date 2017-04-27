package com.example.nikig.logintest;

import android.nfc.Tag;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;


public class Home extends AppCompatActivity implements View.OnClickListener {
    private Button buttonLogout;
    private Button buttonCard;
    private Button buttonrSetting;
    private TextView msg_welcome;
    private TextView UserID;
    //create a runner instance
    private Runner mRunner;


    private Toast toast = null;

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener firebase;
    private DatabaseReference mDatabase;

        //added this for troubleshooting
    private static String TAG = Home.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //session check
        Auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = Auth.getCurrentUser();

        if (user == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        String userId = Auth.getCurrentUser().getUid();
        buttonLogout = (Button) findViewById(R.id.log_out);
        buttonCard= (Button) findViewById(R.id.card);
        buttonrSetting= (Button) findViewById(R.id.rSetting);
        msg_welcome = (TextView) findViewById(R.id.welcome);

        UserID = (TextView) findViewById(R.id.userID);
        //msg_welcome.setText("Welcome "+Runner.getCurrentuser().getFirstName());
        msg_welcome.setText("Welcome "+ user.getDisplayName());

        //created user and get current UID for their info
       String UID=  user.getUid();
        //print to make sure accuracy
        Log.d(TAG,UID);
        //pass it into function to get user info with unique ID
        getUserInfo(UID);
        UserID.setText(UID);


        buttonLogout.setOnClickListener(this);
        buttonCard.setOnClickListener(this);
        buttonrSetting.setOnClickListener(this);
    }


    private void getUserInfo(final String UID) {
        //referencing/ checking if that uid has a child
        DatabaseReference myRef = mDatabase.child("runner").child(UID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG,dataSnapshot.getChildren(""));
                mRunner= dataSnapshot.getValue(Runner.class);

                Log.d(TAG,"Runner= "+ mRunner.getEmergencyid());
//                Log.d(TAG,"Runner= "+ mRunner.getAge());
                Log.d(TAG,"Runner= "+ mRunner.getFirstName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //   mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
 //       @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
//            User user = dataSnapshot.getValue(User.class);
//
//            Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail());
//        }
//
//        @Override
//        public void onCancelled(DatabaseError error) {
//            // Failed to read value
//            Log.w(TAG, "Failed to read value.", error.toException());
//        }
//    });
//

    @Override
    public void onClick(View v) {
        if (v == buttonLogout) {
            Auth.signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
         if (v == buttonCard){
             Intent intent = new Intent(this, EmergencyCard.class);
             intent.putExtra("emergencyid", mRunner.getEmergencyid() );
            startActivity(intent);
        }
        if (v == buttonrSetting){
            Intent intent = new Intent(this, runnerSetting.class);
//            intent.putExtra("runnerid", getUserInfo(UserID));
            startActivity(intent);
        }

    }
}


//    private void logoutUser() {
//
//        Auth.signOut();
//        if (Auth.getCurrentUser() == null) {
//            startActivity(new Intent(Home.this, MainActivity.class));
//            finish();
//            toast.makeText(getApplicationContext(), "log out", Toast.LENGTH_LONG);
//            toast.show();

// this listener will be called when there is change in firebase user session
//        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth Auth) {
//                FirebaseUser user = Auth.getCurrentUser();
//                if (user == null) {
//                    // user auth state is changed - user is null
//                    // launch login activity
//                    startActivity(new Intent(Home.this, MainActivity.class));
//                    finish();
//                }
//            }





