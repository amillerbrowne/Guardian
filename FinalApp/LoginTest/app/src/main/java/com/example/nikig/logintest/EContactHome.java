package com.example.nikig.logintest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EContactHome extends AppCompatActivity {

    private Button buttonLogout, quickViewButton, listRunnersButton;
    private TextView msg_welcome;
    private TextView UserID;
    //create a runner instance
    private Emergency mContact;

    private Toast toast = null;

    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener firebase;
    private DatabaseReference mDatabase;

    //added this for troubleshooting
    private static String TAG = EContactHome.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_econtact_home);

        //session check
        Auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = Auth.getCurrentUser();

        if (user == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

//      String userId = Auth.getCurrentUser().getUid();
        buttonLogout = (Button) findViewById(R.id.contactLogOutButton);
        quickViewButton = (Button) findViewById(R.id.quickViewRunnerButton);
        msg_welcome = (TextView) findViewById(R.id.eContactWelcome);

        //UserID = (TextView) findViewById(R.id.eUserID);
        //msg_welcome.setText("Welcome "+Runner.getCurrentuser().getFirstName());
        msg_welcome.setText("Welcome "+ user.getDisplayName());

        //created user and get current UID for their info
        String UID = user.getUid();
        //print to make sure accuracy
        Log.d(TAG,UID);
        //pass it into function to get user info with unique ID
        getUserInfo(UID);

        //UserID.setText(UID);

        // logout button handler
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EContactHome.this);
                builder.setTitle("Confirm Logout");
                builder.setMessage("Are you sure you want to logout of Guardian?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Auth.signOut();
                        finish();
                        Intent intent = new Intent(EContactHome.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        quickViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EContactHome.this, RunnerCard.class);
                intent.putExtra("runnerid", mContact.getRunnerID());
                startActivity(intent);
            }
        });
    }

    public void locateRunner(View v) {
        // TODO: Open Map and pull coordinates to drop a marker
        Intent intent = new Intent(EContactHome.this, ContactMapsActivity.class);
        intent.putExtra("runnerid", mContact.getRunnerID());
        startActivity(intent);
    }

    private void getUserInfo(final String UID) {
        //referencing/ checking if that uid has a child
        DatabaseReference myRef = mDatabase.child("emergency").child(UID);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//              Log.d(TAG,dataSnapshot.getChildren(""));
                mContact= dataSnapshot.getValue(Emergency.class);

                Log.d(TAG,"Contact= "+ mContact.getEfirstName());
//              Log.d(TAG,"Runner= "+ mRunner.getAge());
                Log.d(TAG,"Contact= "+ mContact.getElastName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
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



//public class EContactHome extends AppCompatActivity {
//    /** Our Class member variables **/
//    private Emergency eContact;
//    private Runner eContactsRunner;
//
//    /** Debugging members **/
//    public static final String TAG = EContactHome.class.getSimpleName();
//
//    /** primitive member variables **/
//    private double latitude, longitude;
//    String eContactID;
//    String runnerID;
//
//    /** database variables **/
//    private Runner runner;
//    private FirebaseAuth Auth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//    private DatabaseReference databaseReference;
//    private FirebaseUser firebaseUser;
//
//    /** Android Views **/
//    private TextView runnerNameTV, LatLngTV;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_econtact_home);
//
//        // set up database connection
//        Auth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        firebaseUser = Auth.getCurrentUser();
//
//        if(firebaseUser == null) {
//            finish();
//            startActivity(new Intent(this, MainActivity.class));
//        }
//
//        // set up the views
//        runnerNameTV = (TextView) findViewById(R.id.runnerNameTV);
//        LatLngTV = (TextView) findViewById(R.id.locationTV);
//
////        // grab the emergency contact's runner info from the database
//        eContactID = firebaseUser.getUid();
////
//        Log.d(TAG, eContactID); // log for debugging
////
////        // pull the contact's info from the database, including the associated runner
//        getEContactInfo(eContactID);
//
//    }
//
//    private void getEContactInfo(final String eId) {
//        DatabaseReference reference = databaseReference.child("emergency").child(firebaseUser.getUid());
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                eContact = dataSnapshot.getValue(Emergency.class);
//                Log.d("runnerID", eContact.getRunnerID());
//
//                // Check that the emergency contact has a runner associated with them
//                // if they don't, prompt them to add their runner with the correct ID
//                if(eContact.getRunnerID() == null) {
//                    Intent intent = new Intent(getApplicationContext(), SetContactRunner.class);
//                    startActivity(intent);
//                } else {
//                    runnerID = eContact.getRunnerID();
//                }
//                Log.d(TAG, "ContactName = " + eContact.getEfirstName() + eContact.getElastName());
//                Log.d(TAG, "PhoneNo = " + eContact.getPhone());
//                if(runnerID == null) {
//                    Log.d(TAG, "Runner ID is null");
//                } else getRunnerInfo(runnerID);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//    private void getRunnerInfo(String id) {
//        DatabaseReference reference = databaseReference.child("runner").child(id);
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                eContactsRunner = dataSnapshot.getValue(Runner.class);
//                Log.d(TAG, "RunnerName = " + eContactsRunner.getLastName());
//                Log.d(TAG, "RunnerLocation = " + getFormattedRunnerName(eContactsRunner.getFirstName(), eContactsRunner.getLastName()));
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        runnerNameTV.setText(getFormattedRunnerName(
//                                eContactsRunner.getFirstName(), eContactsRunner.getLastName()));
//
//                        LatLngTV.setText(getLATLONGFormatted(
//                                eContactsRunner.getLatitude(), eContactsRunner.getLongitude()));
//                    }
//                });
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) { }
//        });
//    }
//
//    private String getLATLONGFormatted(double lat, double lon) {
//        return String.format("Latitude:%f::Longitude:%f", lat, lon);
//    }
//
//    private  String getFormattedRunnerName(String first, String last) {
//        return String.format("%s %s", first, last);
//    }
//
//}
