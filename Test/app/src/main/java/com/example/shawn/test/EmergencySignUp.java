package com.example.shawn.test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class EmergencySignUp extends AppCompatActivity {
    private Toast toast = null;

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
        EditText device = (EditText) findViewById(R.id.device);
        String f = first.getText() == null ? "" : first.getText().toString();
        String l = last.getText() == null ? "" : last.getText().toString();
        String r = relation.getText() == null ? "" : relation.getText().toString();
        String p = primary.getText() == null ? "" : primary.getText().toString();
        String s = secondary.getText() == null ? "" : secondary.getText().toString();
        String v = device.getText() == null ? "" : device.getText().toString();

        if(f.equals("") || l.equals("") || r.equals("") || p.equals("") || s.equals("") || v.equals("")){
            toast = Toast.makeText(getApplicationContext(),"All fields must be filled in!",Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            Intent prior = getIntent();
            String data = prior.getStringExtra("JSON");
            try {
                JSONObject signup = new JSONObject(data);
                String email = signup.getString("email");
                String password = signup.getString("password");
                addEmergency(f, l, r, p, s, email, password, v);
            }
            catch(JSONException error){
                toast = Toast.makeText(getApplicationContext(),"An internal system error occurred. Please close the app and restart.",Toast.LENGTH_LONG);
                toast.show();
            }

            Intent next = new Intent(this, Emergency.class);
            startActivity(next);
        }
    }

    public void addEmergency(String first, String last, String relation, String primary, String secondary, String email, String pass, String device){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getWritableDatabase();

        //Add login data to user table
        ContentValues loginValues = new ContentValues();
        loginValues.put(DBContract.Users.COLUMN_EMAIL, email);
        loginValues.put(DBContract.Users.COLUMN_PASS, pass);
        loginValues.put(DBContract.Users.COLUMN_RUNNER, 0);
        //Save id of user to link to runner table
        long id = db.insert(DBContract.Users.TABLE_NAME, null, loginValues);

        //Add user data to emergency table
        ContentValues values = new ContentValues();
        values.put(DBContract.Emergency.COLUMN_FIRST, first);
        values.put(DBContract.Emergency.COLUMN_LAST, last);
        values.put(DBContract.Emergency.COLUMN_RELATE, relation);
        values.put(DBContract.Emergency.COLUMN_PRIMNUM, primary);
        values.put(DBContract.Emergency.COLUMN_SECNUM, secondary);
        //ID received from user data above
        values.put(DBContract.Emergency.COLUMN_ID, id);

        //Check if device is registered, if not register it
        if(!doesDeviceExist(device)){
            ContentValues deviceValue = new ContentValues();
            deviceValue.put(DBContract.Devices.COLUMN_DEVICE, device);
        }

        db.insert(DBContract.Emergency.TABLE_NAME, null, values);
    }

    public boolean doesDeviceExist(String device){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getReadableDatabase();

        String[] select = {
                DBContract.Devices.COLUMN_DEVICE,
        };

        String where = DBContract.Devices.COLUMN_DEVICE + " = ?";

        String[] whereValues = {device};

        Cursor cursor = db.query(
                DBContract.Devices.TABLE_NAME,     // The table to query
                select,                          // The columns to return
                where,                           // The columns for the WHERE clause
                whereValues,                     // The values for the WHERE clause
                null,                            // don't group the rows
                null,                            // don't filter by row groups
                null                             // don't sort
        );

        cursor.moveToFirst();
        if(cursor.getCount() == 0){ //No such device exists
            return false;
        }
        return true; //Device already registered
    }

    @Override
    protected void onPause () {
        super.onPause();
        //Remove any existing messages before redirecting
        if(toast!=null) {
            toast.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        //Ensure that back button cannot be used!
    }
}
