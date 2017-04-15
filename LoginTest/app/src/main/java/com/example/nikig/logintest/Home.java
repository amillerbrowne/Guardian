package com.example.nikig.logintest;

import android.nfc.Tag;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Home extends AppCompatActivity implements View.OnClickListener {
    private Button buttonLogout;
    private Button buttonCard;
    private TextView msg_welcome;

    private Toast toast = null;

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener firebase;
    private DatabaseReference mDatabase;

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
        msg_welcome = (TextView) findViewById(R.id.welcome);
        //msg_welcome.setText("Welcome "+Runner.getCurrentuser().getFirstName());
        msg_welcome.setText("Welcome "+ user.getDisplayName());

        buttonLogout.setOnClickListener(this);
        buttonCard.setOnClickListener(this);
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
            startActivity(new Intent(this, EmergencyCard.class));

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





