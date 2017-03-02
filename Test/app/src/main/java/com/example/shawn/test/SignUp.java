package com.example.shawn.test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class SignUp extends AppCompatActivity {
    public final static String USER_ID = "com.example.shawn.USERID";
    private boolean runner;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }

    public void signup(View view) {
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.pass);
        EditText confirm = (EditText) findViewById(R.id.confirm);
        String e = email.getText().toString();
        String p = password.getText().toString();
        String c = confirm.getText().toString();

        if(!p.equals(c)){ //If passwords don't match
            //Throw pop error message
        }

        if(addUser(e, p)){ //TRUE means Runner
            Intent run = new Intent(this, RunnerSignUp.class);
            run.putExtra(USER_ID, this.id);
            startActivity(run);
        }
        else{ //FALSE means Emergency
            Intent econtact = new Intent(this, EmergencySignUp.class);
            econtact.putExtra(USER_ID, this.id);
            startActivity(econtact);
        }
    }

    public boolean addUser(String email, String pass){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBContract.Users.COLUMN_EMAIL, email);
        values.put(DBContract.Users.COLUMN_PASS, pass);
        if(this.runner){ //If Runner
            values.put(DBContract.Users.COLUMN_RUNNER, 1);
        }
        else{ //If Emergency Contact
            values.put(DBContract.Users.COLUMN_RUNNER, 0);
        }

        this.id = db.insert(DBContract.Users.TABLE_NAME, null, values);

        return this.runner;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    this.runner = true;
                    break;
            case R.id.radio_no:
                if (checked)
                    this.runner = false;
                    break;
        }
    }

}
