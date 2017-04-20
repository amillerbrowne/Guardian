package com.example.nikig.logintest;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetContactRunner extends AppCompatActivity {

    private String eContactID;
    private EditText inputRunnerID;
    private TextView eContactIDET;

    private FirebaseAuth Auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private Emergency eContact;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_contact_runner);

        Auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseUser = Auth.getCurrentUser();
        eContactID = firebaseUser.getUid();
        Log.d("uid", eContactID);

        eContactIDET = (TextView) findViewById(R.id.eContactIDET);
        eContactIDET.setText(eContactID);
        inputRunnerID = (EditText) findViewById(R.id.runnerIDET);
    }

    public void updateContactsRunner(View view) {
        String inputID = inputRunnerID.getText().toString();
        if(inputID.length() != 28) {
            new AlertDialog.Builder(this).setMessage("Enter a valid Runner ID Code")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
            return;
        } else {
            DatabaseReference reference = databaseReference.child("emergency").child(eContactID);
            reference.child("runnerid").setValue(inputID);
            startActivity(new Intent(getApplicationContext(), EContactHome.class));
        }
    }
}
