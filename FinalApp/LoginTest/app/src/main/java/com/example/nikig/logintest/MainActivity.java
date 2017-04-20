package com.example.nikig.logintest;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private TextView buttonforgot;
    private TextView buttonSignup;
    private EditText editTextEmail;
    private EditText editTextPassword;
    public static final String TAG = MainActivity.class.getSimpleName();



    private FirebaseAuth Auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.pass);
        buttonLogin = (Button) findViewById(R.id.loginbutton);
        buttonSignup = (TextView) findViewById(R.id.signupbutton);
        buttonforgot = (TextView) findViewById(R.id.forgot_pass);

        //activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        buttonLogin.setOnClickListener(this);
        buttonSignup.setOnClickListener(this);
        buttonforgot.setOnClickListener(this);

        //init Firebase Auth
        Auth = FirebaseAuth.getInstance();
        firebaseUser = Auth.getCurrentUser();
/*
        if(Auth.getCurrentUser() != null) {
            Log.d(TAG, firebaseUser.getUid());
            checkUserTypeAndGoHome(firebaseUser.getUid());
            finish();
        }

*/
        //check already session, if ok -> DashBoard

        if (Auth.getCurrentUser() != null) {
            String userId = firebaseUser.getUid();
            Log.d(TAG,userId);
            checkUserTypeAndGoHome(userId);

//            startActivity(new Intent(MainActivity.this, Home.class));

        }
        // check if user is an emergency contact



    }

//    private void checkIfContact(final String userId) {
//            Log.d("Entering:",  "checkIfContact()");
//            DatabaseReference ref = databaseReference.child("emergency");
//            ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Log.d("Entering:", "onDataChange()");
//                    if (dataSnapshot.hasChild(userId)) {
//                        Log.d("Entered:", "dataSnapshot.hasChild(userId)");
//                        // go to contact homepage instead
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                startActivity(new Intent(getApplicationContext(), EContactHome.class));
//                                finish();
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.d("Canceled:", "checkIfContact() Canceled");
//                }
//            });
//        }


    private void checkUserTypeAndGoHome(final String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference runnerRef = databaseReference.child("runner");
        runnerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userId)) {
                    // is runner
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"is a runner");
                            Log.d(TAG,userId);
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();
                        }
                    });

                } else { // is contact
                    startActivity(new Intent(getApplicationContext(), EContactHome.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void onClick(View v) {

        if(v == buttonforgot){
            startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            finish();
        }

        else if (v == buttonLogin) {
            userLogin();
        }
        else if (v == buttonSignup) {
            finish();
            startActivity(new Intent(this, SignUp.class));
        }

    }


    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length()<6) {
            Toast.makeText(getApplicationContext(), "Enter password correctly!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //start profile activity
//                                String userId = firebaseUser.getUid();
//                                Log.d(TAG,userId);
//                                checkUserTypeAndGoHome(userId);
//                                startActivity(new Intent(getApplicationContext(), Home.class));
                                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                checkUserTypeAndGoHome(Auth.getCurrentUser().getUid());
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }
}
