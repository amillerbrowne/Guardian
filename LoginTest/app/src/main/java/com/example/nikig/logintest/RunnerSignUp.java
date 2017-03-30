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


public class RunnerSignUp extends AppCompatActivity {
    private TextView wel;
    private EditText first;
    private EditText last;
    private EditText dob;
    private EditText age;
    private Button buttonSubmit;
    private Toast toast = null;

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private FirebaseAuth Auth;
    // [END declare_database_ref]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runner_signup);


        //initialize FirebaseDatavase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Auth = FirebaseAuth.getInstance();


        wel = (TextView) findViewById(R.id.welcome);
        first = (EditText) findViewById(R.id.rfirst);
        last = (EditText) findViewById(R.id.rlast);
        dob = (EditText) findViewById(R.id.dob);
        age = (EditText) findViewById(R.id.age);
        buttonSubmit = (Button) findViewById(R.id.submit);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            //String f = first.getText().toString().trim();
           // String l = last.getText().toString().trim();
           // String d = dob.getText().toString().trim();
           // String a = age.getText().toString().trim();

            @Override
            public void onClick(View v) {
               mDatabase = FirebaseDatabase.getInstance().getReference("users");

// Creating new user node, which returns the unique key value
// new user node would be /users/$userid/
                String userId = Auth.getCurrentUser().getUid();

// creating user object


// pushing user to 'users' node using the userId

//                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                UserInformation userInfo = new UserInformation(first.getText().toString().trim(),
                       last.getText().toString().trim(),dob.getText().toString().trim(), age.getText().toString().trim());
//
//
//
//// pushing user to 'users' node using the userId
               mDatabase.child(userId).setValue(userInfo);

               final FirebaseUser user = Auth.getCurrentUser();
//
                mDatabase.child(user.getUid()).setValue(userInfo);

                toast.makeText(getApplicationContext(), "Posting Data...", Toast.LENGTH_SHORT).show();

                //Value event listener for realtime data update


//                            String string = "Name: "+person.getName()+"\nAddress: "+person.getAddress()+"\n\n";

//                            //Displaying it on textview

                           wel.setText("Welcome "+ userInfo.getN());

                        }




    //startActivity(new Intent(RunnerSignUp.this, Home.class));
                //finish();

                //finish();

            });
        }
    }

