package com.example.shawn.test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private Toast toast = null;
   // public final static String EXTRA_MESSAGE = "com.example.shawn.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.login);

    }
    public void login(View view) {
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.pass);
        String e = email.getText() == null ? "" : email.getText().toString();
        String p = password.getText() == null ? "" : password.getText().toString();

        if(e.equals("") || p.equals("")){
            toast = Toast.makeText(getApplicationContext(),"All fields must be filled in!",Toast.LENGTH_LONG);
            toast.show();
        }
        else if(validUser(e, p)==1){
            Intent intent = new Intent(this, Runner.class);
            startActivity(intent);
        }
        else if(validUser(e, p)==2){
            Intent intent = new Intent(this, Emergency.class);
            startActivity(intent);
        }
        else{
            toast = Toast.makeText(getApplicationContext(),"Invalid credentials!",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void signup(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    //Returns: 0-No such user ; 1-User is Runner ; 2-User is Emergency Contact
    public int validUser(String email, String pass){
        DBSQLiteHelper DBhelper = new DBSQLiteHelper(getApplicationContext());
        SQLiteDatabase db = DBhelper.getReadableDatabase();

        String[] select = {
                DBContract.Users.COLUMN_EMAIL,
                DBContract.Users.COLUMN_PASS,
                DBContract.Users.COLUMN_RUNNER,
                DBContract.Users._ID
        };

        String where = DBContract.Users.COLUMN_EMAIL + " = ? AND " + DBContract.Users.COLUMN_PASS + " = ?";

        String[] whereValues = {email, pass};

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
        if(cursor.getCount() == 0){ //No such user/password combination exists
            return 0;
        }
        else if(cursor.getInt(2) == 1){ //Runner
            return 1;
        }
        return 2; //Emergency Contact
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
