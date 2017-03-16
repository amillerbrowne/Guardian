package com.example.shawn.test;

import android.provider.BaseColumns;

public final class DBContract {
    private DBContract(){
    }

    public static class Users implements BaseColumns{
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASS = "pass";
        public static final String COLUMN_RUNNER = "runner";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASS + " TEXT, " +
                COLUMN_RUNNER + " INTEGER, " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE)";
    }

    public static class Runners implements BaseColumns{
        public static final String TABLE_NAME = "runners";
        public static final String COLUMN_ID = "rid";
        public static final String COLUMN_FIRST = "first";
        public static final String COLUMN_LAST = "last";
        public static final String COLUMN_DOB = "dateofbirth";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_DEVICE = "device";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "FOREIGN KEY(" + COLUMN_ID + ") REFERENCES " +
                Users.TABLE_NAME + "(" + Users._ID + "), " +
                COLUMN_FIRST + " TEXT, " +
                COLUMN_LAST + " TEXT, " +
                COLUMN_DOB + " INTEGER, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_HEIGHT + " INTEGER, " +
                COLUMN_WEIGHT + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_DEVICE + ") REFERENCES " +
                Devices.TABLE_NAME + "(" + Devices.COLUMN_DEVICE + "))";
    }

    public static class Emergency implements BaseColumns{
        public static final String TABLE_NAME = "emergency";
        public static final String COLUMN_ID = "eid";
        public static final String COLUMN_FIRST = "first";
        public static final String COLUMN_LAST = "last";
        public static final String COLUMN_RELATE = "rel";
        public static final String COLUMN_PRIMNUM = "prim";
        public static final String COLUMN_SECNUM = "sec";
        public static final String COLUMN_DEVICE = "device";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                "FOREIGN KEY(" + COLUMN_ID + ") REFERENCES " +
                Users.TABLE_NAME + "(" + Users._ID + "), " +
                COLUMN_FIRST + " TEXT, " +
                COLUMN_LAST + " TEXT, " +
                COLUMN_RELATE + " TEXT, " +
                COLUMN_PRIMNUM + " INTEGER, " +
                COLUMN_SECNUM + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_DEVICE + ") REFERENCES " +
                Devices.TABLE_NAME + "(" + Devices.COLUMN_DEVICE + "))";
    }

    public static class Devices implements BaseColumns{
        public static final String TABLE_NAME = "devices";
        public static final String COLUMN_DEVICE = "activeDevice";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + "(" +
                COLUMN_DEVICE + " INTEGER UNIQUE)";
    }
}
