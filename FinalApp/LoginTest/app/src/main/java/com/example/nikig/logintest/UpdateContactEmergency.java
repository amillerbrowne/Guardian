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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateContactEmergency extends AppCompatActivity {
    private Button Back;
    private TextView currentEID, currentRID;
    private EditText newEmergencyID;
    private static String TAG = UpdateContactEmergency.class.getSimpleName();

    /**
     * database variables
     **/
    private Runner runner;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_contact_emergency);
        final String rUID;
        currentEID = (TextView) findViewById(R.id.currentID);
        currentRID = (TextView) findViewById(R.id.myID);
        newEmergencyID = (EditText) findViewById(R.id.newID);
        Back = (Button) findViewById(R.id.backBttn);

//        FirebaseUser user = Auth.getCurrentUser();

//        String UID= user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        Log.d(TAG, intent.getStringExtra("emergencyid"));

        String eUID = intent.getStringExtra("emergencyid");
        rUID = intent.getStringExtra("runnerid");

        currentEID.setText(eUID);
        currentRID.setText(rUID);
        Log.d(TAG, intent.getStringExtra("runnerid"));

        Toast.makeText(getApplicationContext(), rUID, Toast.LENGTH_LONG).show();
//        Log.d(TAG, UID);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public String toString() {
                return "$classname{}";
            }

            @Override
            public void onClick(View v) {
                if (newEmergencyID.getText().toString().length() > 0) {
                    DatabaseReference reference = databaseReference.child("runner").child(rUID);
                    reference.child("emergencyid").setValue(newEmergencyID.getText().toString());

                    Toast.makeText(getApplicationContext(), "updated Database!", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(UpdateContactEmergency.this, Home.class);
//                    startActivity(intent);
                }
                else{

                    Toast.makeText(getApplicationContext(), "Nothing to update!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }







//        if (newEmergencyID.getText().toString().trim().length() == 0) {

//        }
//        else{
//                Log.d(TAG, "editText is empty");
//                Back.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(UpdateContactEmergency.this, Home.class);
//                        startActivity(intent);
//                    }
//                });
//            }

        private void doThings(){
                        if (newEmergencyID.getText().toString().trim().length() > 0) {
                            Log.d(TAG, "editText is not empty");
                            Back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference reference = databaseReference.child("runner").child(firebaseUser.getUid());
                                    Log.d(TAG, firebaseUser.getUid());

                                    reference.child("emergencyid").setValue(newEmergencyID);

                                    finish();
                                    Intent intent = new Intent(UpdateContactEmergency.this, Home.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d(TAG, "editText is empty");
                            Back.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(UpdateContactEmergency.this, Home.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }



}



