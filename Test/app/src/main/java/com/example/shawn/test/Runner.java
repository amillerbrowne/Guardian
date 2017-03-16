package com.example.shawn.test;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Runner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runner);

        String message = "Hello Runner!";
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.runner);
        layout.addView(textView);
    }

    public void contacts(View view){
        Intent intent = new Intent(this, Contacts.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //Ensure that back button cannot be used!
    }
}
