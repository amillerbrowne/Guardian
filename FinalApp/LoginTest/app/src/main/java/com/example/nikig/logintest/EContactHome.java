package com.example.nikig.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EContactHome extends AppCompatActivity {

    /** Our Class member variables **/
    private Emergency eContact;
    private Runner eContactsRunner;

    /** Debugging members **/
    public static final String TAG = MapsActivity.class.getSimpleName();

    /** primitive member variables **/
    private double latitude, longitude;
    String eContactID;
    String runnerID;

    /** database variables **/
    private Runner runner;
    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    /** Android Views **/
    private TextView runnerNameTV, LatLngTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_econtact_home);

        // set up database connection
        Auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = Auth.getCurrentUser();

        if(firebaseUser == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        // set up the views
        runnerNameTV = (TextView) findViewById(R.id.runnerNameTV);
        LatLngTV = (TextView) findViewById(R.id.locationTV);

        // grab the emergency contact's runner info from the database
        eContactID = firebaseUser.getUid();
        Log.d(TAG, eContactID); // log for debugging

        // pull the contact's info from the database, including the associated runner
        getEContactInfo(eContactID);

        if(runnerID != null) {
            getRunnerInfo(runnerID);
        }

    }

    private void getEContactInfo(String id) {
        DatabaseReference reference = databaseReference.child("emergency").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eContact = dataSnapshot.getValue(Emergency.class);

                // Check that the emergency contact has a runner associated with them
                // if they don't, prompt them to add their runner with the correct ID
                if(eContact.getRunnerID() == null) {
                    // TODO: HAVE USER ENTER RUNNER ID TO CONNECT THEM
                    // for now if null set to "null"
                    eContact.setRunnerID("null");
                } else {
                    runnerID = eContact.getRunnerID();
                }
                Log.d(TAG, "ContactName = " + eContact.getEfirstName() + eContact.getElastName());
                Log.d(TAG, "PhoneNo = " + eContact.getPhone());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getRunnerInfo(String id) {
        DatabaseReference reference = databaseReference.child("runner").child(id);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eContactsRunner = dataSnapshot.getValue(Runner.class);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runnerNameTV.setText(getFormattedRunnerName(
                                eContactsRunner.getFirstName(), eContactsRunner.getLastName()));

                        LatLngTV.setText(getLATLONGFormatted(
                                eContactsRunner.getLatitude(), eContactsRunner.getLongitude()));
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private String getLATLONGFormatted(double lat, double lon) {
        return String.format("Latitude:%f::Longitude:%f", lat, lon);
    }

    private  String getFormattedRunnerName(String first, String last) {
        return String.format("%s %s", first, last);
    }

}
