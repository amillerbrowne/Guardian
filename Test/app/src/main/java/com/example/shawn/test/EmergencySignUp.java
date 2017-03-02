package com.example.shawn.test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EmergencySignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergencysignup);
    }

    public void esignup(View view) {
        EditText first = (EditText) findViewById(R.id.efirst);
        EditText last = (EditText) findViewById(R.id.elast);
        EditText relation = (EditText) findViewById(R.id.relation);
        EditText primary = (EditText) findViewById(R.id.primary);
        EditText secondary = (EditText) findViewById(R.id.secondary);
        String f = first.getText().toString();
        String l = last.getText().toString();
        String r = relation.getText().toString();
        String p = primary.getText().toString();
        String s = secondary.getText().toString();

        Intent prior = getIntent();
        long id = prior.getLongExtra(SignUp.USER_ID, 0L);

        addEmergency(f,l,r,p,s,id);

        Intent next = new Intent(this, Emergency.class);
        startActivity(next);
    }

    public void addEmergency(String first, String last, String relation, String primary, String secondary, long id){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Emergency.COLUMN_FIRST, first);
        values.put(DBContract.Emergency.COLUMN_LAST, last);
        values.put(DBContract.Emergency.COLUMN_RELATE, relation);
        values.put(DBContract.Emergency.COLUMN_PRIMNUM, primary);
        values.put(DBContract.Emergency.COLUMN_SECNUM, secondary);
        values.put(DBContract.Emergency.COLUMN_ID, id);

        db.insert(DBContract.Emergency.TABLE_NAME, null, values);
    }
}
