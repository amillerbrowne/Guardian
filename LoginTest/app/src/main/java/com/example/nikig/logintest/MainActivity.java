package com.example.nikig.logintest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonLogin;
    private TextView buttonforgot;
    private TextView buttonSignup;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private FirebaseAuth Auth;

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

//        //check already session, if ok -> DashBoard
  if(Auth.getCurrentUser() != null)
        startActivity(new Intent(MainActivity.this, Home.class));
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
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                //get information for the database
                                //getValue();
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
