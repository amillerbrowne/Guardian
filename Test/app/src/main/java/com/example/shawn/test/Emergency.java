package com.example.shawn.test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class Emergency extends AppCompatActivity {
    private long id; //Keeps track of user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency);

        Intent prior = getIntent();
        id = prior.getLongExtra("ID",0);

        Cursor cursor = getUserData(id);
        cursor.moveToFirst();
        String name = cursor.getString(0);

        String message = "Hello " + name + "!";
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.emergency);
        layout.addView(textView);
    }

    public Cursor getUserData(long id){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getReadableDatabase();

        String[] select = {
                DBContract.Emergency.COLUMN_FIRST
        };

        String where = DBContract.Emergency.COLUMN_ID + " = ?";

        String[] whereValues = {String.valueOf(id)};

        Cursor cursor = db.query(
                DBContract.Emergency.TABLE_NAME, // The table to query
                select,                          // The columns to return
                where,                           // The columns for the WHERE clause
                whereValues,                     // The values for the WHERE clause
                null,                            // don't group the rows
                null,                            // don't filter by row groups
                null                             // don't sort
        );

        return cursor;
    }

    @Override
    public void onBackPressed() {
        //Ensure that back button cannot be used!
    }
}
