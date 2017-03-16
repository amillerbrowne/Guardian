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

public class RunnerSignUp extends AppCompatActivity {
    private Toast toast = null;

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
        EditText device = (EditText) findViewById(R.id.device);
        String f = first.getText() == null ? "" : first.getText().toString();
        String l = last.getText() == null ? "" : last.getText().toString();
        String d = dob.getText() == null ? "" : dob.getText().toString();
        String g = gender.getText() == null ? "" : gender.getText().toString();
        String a = age.getText() == null ? "" : age.getText().toString();
        String h = height.getText() == null ? "" : height.getText().toString();
        String w = weight.getText() == null ? "" : weight.getText().toString();
        String v = device.getText() == null ? "" : device.getText().toString();

        //Logic to add device ID to db if need be
        int dev = doesDeviceExist(v);
        boolean check = false;
        if(dev == -1){
            check = true;
        }

        if(f.equals("") || l.equals("") || d.equals("") || g.equals("") || a.equals("") || h.equals("") || w.equals("") || v.equals("")){
            toast = Toast.makeText(getApplicationContext(),"All fields must be filled in!",Toast.LENGTH_LONG);
            toast.show();
        }
        //Does device already exist to user of same type?
        else if(dev == 1){
            toast = Toast.makeText(getApplicationContext(),"An Emergency Contact already owns this device ID!",Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            Intent prior = getIntent();
            String data = prior.getStringExtra("JSON");
            try {
                JSONObject signup = new JSONObject(data);
                String email = signup.getString("email");
                String password = signup.getString("password");
                long id = addRunner(f, l, d, g, a, h, w, email, password, v, check);

                Intent next = new Intent(this, Runner.class);
                next.putExtra("ID",id);
                startActivity(next);
            }
            catch(JSONException error) {
                toast = Toast.makeText(getApplicationContext(), "An internal system error occurred. Please close the app and restart.", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public long addRunner(String first, String last, String dob, String gender, String age, String height, String weight, String email, String pass, String device, boolean addDevice){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getWritableDatabase();

        //Add login data to user table
        ContentValues loginValues = new ContentValues();
        loginValues.put(DBContract.Users.COLUMN_EMAIL, email);
        loginValues.put(DBContract.Users.COLUMN_PASS, pass);
        loginValues.put(DBContract.Users.COLUMN_RUNNER, 1);
        //Save id of user to link to runner table
        long id = db.insert(DBContract.Users.TABLE_NAME, null, loginValues);

        //Properly capitalize names
        String f = first.toLowerCase();
        f = f.substring(0,1).toUpperCase() + f.substring(1);
        String l = last.toLowerCase();
        l = l.substring(0,1).toUpperCase() + l.substring(1);

        //Add user data to runner table
        ContentValues values = new ContentValues();
        values.put(DBContract.Runners.COLUMN_FIRST, f);
        values.put(DBContract.Runners.COLUMN_LAST, l);
        values.put(DBContract.Runners.COLUMN_DOB, dob);
        values.put(DBContract.Runners.COLUMN_GENDER, gender);
        values.put(DBContract.Runners.COLUMN_AGE, age);
        values.put(DBContract.Runners.COLUMN_HEIGHT, height);
        values.put(DBContract.Runners.COLUMN_WEIGHT, weight);
        values.put(DBContract.Runners.COLUMN_DEVICE, device);
        //ID received from user data above
        values.put(DBContract.Runners.COLUMN_ID, id);

        //Add device ID if it doesn't already exist in table
        if(addDevice){
            ContentValues deviceValue = new ContentValues();
            deviceValue.put(DBContract.Devices.COLUMN_DEVICE, device);
            //Set the type of device first registered to Runner
            deviceValue.put(DBContract.Devices.COLUMN_TYPE, 0);
        }

        db.insert(DBContract.Runners.TABLE_NAME, null, values);

        return id;
    }

    //RETURN: -1 = no device, 0 = device registered to other type, 1 = device already registered to same type
    public int doesDeviceExist(String device){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getReadableDatabase();

        String[] select = {
                DBContract.Devices.COLUMN_DEVICE,
                DBContract.Devices.COLUMN_TYPE
        };

        String where = DBContract.Devices.COLUMN_DEVICE + " = ?";

        String[] whereValues = {device};

        Cursor cursor = db.query(
                DBContract.Devices.TABLE_NAME,   // The table to query
                select,                          // The columns to return
                where,                           // The columns for the WHERE clause
                whereValues,                     // The values for the WHERE clause
                null,                            // don't group the rows
                null,                            // don't filter by row groups
                null                             // don't sort
        );

        cursor.moveToFirst();
        if(cursor.getCount() == 0){ //No such device exists
            return -1;
        }
        else if(cursor.getInt(1) == 1){ //Device registered to a contact (GOOD)
            return 0;
        }
        else                       //Device registered to a runner (BAD)
            return 1;
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
