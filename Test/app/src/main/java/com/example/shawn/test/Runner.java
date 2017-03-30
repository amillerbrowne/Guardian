package com.example.shawn.test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Runner extends AppCompatActivity {
    private long id; //Keeps track of user
    private String device; //The user's device
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runner);
        Toast.makeText(Runner.this, "Welcome Runner!",
                Toast.LENGTH_LONG).show();
    }
}

//
//        Intent prior = getIntent();
//        id = prior.getLongExtra("ID",0);
//
//        Cursor cursor = getUserData(id);
//        cursor.moveToFirst();
//        String name = cursor.getString(0);
//        device = String.valueOf(cursor.getInt(1));
//
//        String message = "Hello " + name + "!";
//        TextView textView = new TextView(this);
//        textView.setTextSize(40);
//        textView.setText(message);
//
//        ViewGroup layout = (ViewGroup) findViewById(R.id.runner);
//        layout.addView(textView);
//    }
//
//    public Cursor getUserData(long id){
//        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
//        SQLiteDatabase db = DBhelper.getReadableDatabase();
//
//        String[] select = {
//                DBContract.Runners.COLUMN_FIRST,
//                DBContract.Runners.COLUMN_DEVICE
//        };
//
//        String where = DBContract.Runners.COLUMN_ID + " = ?";
//
//        String[] whereValues = {String.valueOf(id)};
//
//        Cursor cursor = db.query(
//                DBContract.Runners.TABLE_NAME,   // The table to query
//                select,                          // The columns to return
//                where,                           // The columns for the WHERE clause
//                whereValues,                     // The values for the WHERE clause
//                null,                            // don't group the rows
//                null,                            // don't filter by row groups
//                null                             // don't sort
//        );
//
//        return cursor;
//    }
//
//    public void contacts(View view){
//        if(contactExists(device)) {
//            Intent intent = new Intent(this, Contacts.class);
//            intent.putExtra("ID", id);
//            startActivity(intent);
//        }
//        else{
//            toast = Toast.makeText(getApplicationContext(),"You currently have no emergency contacts signed up.",Toast.LENGTH_LONG);
//            toast.show();
//        }
//    }
//
//    public boolean contactExists(String dev){
//        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
//        SQLiteDatabase db = DBhelper.getReadableDatabase();
//
//        //Use the device ID to match the user's emergency contact
//        String[] select = {
//                DBContract.Emergency.COLUMN_ID
//        };
//
//        String where = DBContract.Emergency.COLUMN_DEVICE + " = ?";
//
//        String[] whereValues = {dev};
//
//        Cursor cursor = db.query(
//                DBContract.Emergency.TABLE_NAME, // The table to query
//                select,                          // The columns to return
//                where,                           // The columns for the WHERE clause
//                whereValues,                     // The values for the WHERE clause
//                null,                            // don't group the rows
//                null,                            // don't filter by row groups
//                null                             // don't sort
//        );
//
//        if(cursor.getCount() == 0){ //No contact
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        //Ensure that back button cannot be used!
//    }
//}
