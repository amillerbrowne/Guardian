package com.example.nikig.logintest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private Toast toast = null;
    private Button buttonGetStarted;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirm;

    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //View
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.pass);
        editTextConfirm = (EditText) findViewById(R.id.confirm);
        buttonGetStarted = (Button) findViewById(R.id.signup);

        buttonGetStarted.setOnClickListener(this);


        //init firebase
        Auth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signup) {
            signUpUser();
        }
    }

    public void signUpUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String c_password = editTextConfirm.getText().toString().trim();

        if (email.equals("") || password.equals("") || c_password.equals("")) {
            toast = Toast.makeText(getApplicationContext(), "All fields must be filled in!", Toast.LENGTH_LONG);
            toast.show();
        } else if (password.length() < 6) {
            toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(c_password)) { //If passwords don't match
            toast = Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            //create user
            Auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Toast.makeText(SignUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUp.this, "Registration Successful!" + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this, Signup2.class));
                                finish();
                            }
                        }
                    });

        }
    }
}
