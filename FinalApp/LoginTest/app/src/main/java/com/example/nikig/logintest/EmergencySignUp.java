package com.example.nikig.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EmergencySignUp extends AppCompatActivity {
    private EditText eName;
    private EditText eLast;
    private EditText phone;
    private EditText relation;
    private Button Submit;
    private Toast toast= null;

    private DatabaseReference emergencyDB;
    private FirebaseAuth Auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_signup);


        emergencyDB = FirebaseDatabase.getInstance().getReference("emergency");
        Auth = FirebaseAuth.getInstance();

        eName = (EditText) findViewById(R.id.efirst);
         eLast = (EditText) findViewById(R.id.elast);
         phone = (EditText) findViewById(R.id.primary);
       relation = (EditText) findViewById(R.id.relation);
         Submit = (Button) findViewById(R.id.emergencysubmit);


     Submit.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
             final FirebaseUser user = Auth.getCurrentUser();
             Emergency emergency = new Emergency(eName.getText().toString(), eLast.getText().toString(), phone.getText().toString(), relation.getText().toString());



             emergencyDB.child(user.getUid()).setValue(emergency);
//
//             // Now update the information in the User's Auth profile as well
           UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                   .setDisplayName(eName.getText().toString().trim() + " " + eLast.getText().toString().trim())
                     .build();

           user.updateProfile(update);
//
            toast.makeText(getApplicationContext(), "Posting Data...", Toast.LENGTH_SHORT).show();
////                wel.setText("Welcome "+ userInfo.getN());
//
            startActivity(new Intent(EmergencySignUp.this, MainActivity.class));
             finish();

         }

     });
    }
}

