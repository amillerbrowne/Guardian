package com.example.nikig.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nikig.logintest.Emergency;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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

        emergencyDB = FirebaseDatabase.getInstance().getReference("emergency");
        runnerDB = FirebaseDatabase.getInstance().getReference("runner");
        relatonshipDB = FirebaseDatabase.getInstance().getReference("relationship");
        Auth = FirebaseAuth.getInstance();


        getAllUsersFromFirebase();

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
//

    }

    public void getAllUsersFromFirebase() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Runner.ARG_User)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren()
                                .iterator();
                        List<Emergency> emergency = new ArrayList<>();
                        while (dataSnapshots.hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshots.next();
                            Emergency emergency1 = dataSnapshotChild.getValue(Emergency.class);
                            if (!TextUtils.equals(emergency.uid,
                                    FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                emergency.add(emergency1);
                            }

                        // All users are retrieved except the one who is currently logged
                        // in device.
                    }


                }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
