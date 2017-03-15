package com.example.shawn.test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {
    private Toast toast = null;
    public final static String USER_ID = "com.example.shawn.USERID";
    private int runner = 0; //radio button check : 0 = not checked; 1 = runner; 2 = emergency contact

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }

    public void signup(View view) {
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.pass);
        EditText confirm = (EditText) findViewById(R.id.confirm);
        String e = email.getText() == null ? "" : email.getText().toString();
        String p = password.getText() == null ? "" : password.getText().toString();
        String c = confirm.getText() == null ? "" : confirm.getText().toString();

        if(e.equals("") || p.equals("") || c.equals("") || this.runner == 0){
            toast = Toast.makeText(getApplicationContext(),"All fields must be filled in!",Toast.LENGTH_LONG);
            toast.show();
        }
        else if(!p.equals(c)){ //If passwords don't match
            toast = Toast.makeText(getApplicationContext(),"Passwords do not match!",Toast.LENGTH_LONG);
            toast.show();
        }
        else if(doesUserExist(e)) {//If email is already used
            toast = Toast.makeText(getApplicationContext(),"An account with that email already exists!",Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            try {
                JSONObject data = new JSONObject();
                data.put("email", "e");
                data.put("password", "p");
                //determines what form to go to next
                if (this.runner == 1) { //Runner
                    Intent run = new Intent(this, RunnerSignUp.class);
                    run.putExtra("JSON", data.toString());
                    startActivity(run);
                } else if (this.runner == 2) { //Emergency Contact
                    Intent econtact = new Intent(this, EmergencySignUp.class);
                    econtact.putExtra("JSON", data.toString());
                    startActivity(econtact);
                }
            }
            catch (JSONException error){
                toast = Toast.makeText(getApplicationContext(),"An internal system error occurred. Please close the app and restart.",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public boolean doesUserExist(String email){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getReadableDatabase();

        String[] select = {
                DBContract.Users.COLUMN_EMAIL,
        };

        String where = DBContract.Users.COLUMN_EMAIL + " = ?";

        String[] whereValues = {email};

        Cursor cursor = db.query(
                DBContract.Users.TABLE_NAME,     // The table to query
                select,                          // The columns to return
                where,                           // The columns for the WHERE clause
                whereValues,                     // The values for the WHERE clause
                null,                            // don't group the rows
                null,                            // don't filter by row groups
                null                             // don't sort
        );

        cursor.moveToFirst();
        if(cursor.getCount() == 0){ //No such user email exists
            return false;
        }
        return true; //User email already exists
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    this.runner = 1;
                    break;
            case R.id.radio_no:
                if (checked)
                    this.runner = 0;
                    break;
        }
    }

    @Override
    protected void onPause () {
        super.onPause();
        //Remove any existing messages before redirecting
        if(toast!=null) {
            toast.cancel();
        }
    }

}
