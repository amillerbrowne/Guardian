package com.example.nikig.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class runnerSetting extends AppCompatActivity {
    private TextView RUID;
    private FirebaseAuth Auth;
    private static String TAG = runnerSetting.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runner_setting);
        RUID= (TextView) findViewById(R.id.runner_id);
        Auth = FirebaseAuth.getInstance();

        FirebaseUser user = Auth.getCurrentUser();
        String UID=  user.getUid();
        Log.d(TAG,UID);

        RUID.setText(UID);

    }
}
