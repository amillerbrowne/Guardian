package com.example.shawn.test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Contacts extends AppCompatActivity {
    private long id; //Keeps track of user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        //Get the user ID
        Intent prior = getIntent();
        id = prior.getLongExtra("ID", 0);

        //Get the user's emergency contact
        Cursor cursor = getContactInfo(id);
        cursor.moveToFirst();

        //Get all text views in xml
        TextView name = (TextView)findViewById(R.id.name);
        TextView primary = (TextView)findViewById(R.id.primary);
        TextView secondary = (TextView)findViewById(R.id.secondary);
        TextView relation = (TextView)findViewById(R.id.relation);

        //Set all text views with DB info
        name.setText(cursor.getString(0) + " " + cursor.getString(1));
        primary.setText(cursor.getString(2));
        secondary.setText(cursor.getString(3));
        relation.setText(cursor.getString(4));
    }

    public Cursor getContactInfo(long id){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getReadableDatabase();

        //Get the user's device ID
        String[] deviceSelect = {
                DBContract.Runners.COLUMN_DEVICE
        };

        String deviceWhere = DBContract.Runners.COLUMN_ID + " = ?";

        String[] deviceWhereValues = {String.valueOf(id)};

        Cursor deviceCursor = db.query(
                DBContract.Runners.TABLE_NAME,   // The table to query
                deviceSelect,                    // The columns to return
                deviceWhere,                     // The columns for the WHERE clause
                deviceWhereValues,               // The values for the WHERE clause
                null,                            // don't group the rows
                null,                            // don't filter by row groups
                null                             // don't sort
        );

        deviceCursor.moveToFirst();
        String device = String.valueOf(deviceCursor.getInt(0));

        //Use the device ID to match the user's emergency contact
        String[] select = {
                DBContract.Emergency.COLUMN_FIRST,
                DBContract.Emergency.COLUMN_LAST,
                DBContract.Emergency.COLUMN_PRIMNUM,
                DBContract.Emergency.COLUMN_SECNUM,
                DBContract.Emergency.COLUMN_RELATE
        };

        String where = DBContract.Emergency.COLUMN_DEVICE + " = ?";

        String[] whereValues = {device};

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
}
