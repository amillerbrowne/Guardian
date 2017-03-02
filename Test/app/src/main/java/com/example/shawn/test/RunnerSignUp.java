package com.example.shawn.test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RunnerSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runnersignup);
    }

    public void rsignup(View view) {
        EditText first = (EditText) findViewById(R.id.rfirst);
        EditText last = (EditText) findViewById(R.id.rlast);
        EditText dob = (EditText) findViewById(R.id.dob);
        EditText gender = (EditText) findViewById(R.id.gender);
        EditText age = (EditText) findViewById(R.id.age);
        EditText height = (EditText) findViewById(R.id.height);
        EditText weight = (EditText) findViewById(R.id.weight);
        String f = first.getText().toString();
        String l = last.getText().toString();
        String d = dob.getText().toString();
        String g = gender.getText().toString();
        String a = age.getText().toString();
        String h = height.getText().toString();
        String w = weight.getText().toString();

        Intent prior = getIntent();
        long id = prior.getLongExtra(SignUp.USER_ID, 0L);

        addRunner(f,l,d,g,a,h,w,id);

        Intent next = new Intent(this, Runner.class);
        startActivity(next);
    }

    public void addRunner(String first, String last, String dob, String gender, String age, String height, String weight, long id){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Runners.COLUMN_FIRST, first);
        values.put(DBContract.Runners.COLUMN_LAST, last);
        values.put(DBContract.Runners.COLUMN_DOB, dob);
        values.put(DBContract.Runners.COLUMN_GENDER, gender);
        values.put(DBContract.Runners.COLUMN_AGE, age);
        values.put(DBContract.Runners.COLUMN_HEIGHT, height);
        values.put(DBContract.Runners.COLUMN_WEIGHT, weight);
        values.put(DBContract.Runners.COLUMN_ID, id);

        db.insert(DBContract.Runners.TABLE_NAME, null, values);
    }
}
