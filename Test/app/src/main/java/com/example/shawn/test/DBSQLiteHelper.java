package com.example.shawn.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

public class DBSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "GuardianDB";

    public DBSQLiteHelper(Context con){
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DBContract.Users.CREATE_TABLE);
        db.execSQL(DBContract.Runners.CREATE_TABLE);
        db.execSQL(DBContract.Emergency.CREATE_TABLE);
        db.execSQL(DBContract.Devices.CREATE_TABLE);
    }

    @Override
    //Needs to be improved to support persistence in case tables are indeed dropped.
    //Only invoked if DATABASE_VERSION is changed
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Users.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Runners.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Emergency.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Devices.TABLE_NAME);

        onCreate(db);
    }

}
