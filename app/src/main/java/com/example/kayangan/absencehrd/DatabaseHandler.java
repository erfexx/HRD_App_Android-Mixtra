package com.example.kayangan.absencehrd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KAYANGAN on 2/28/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "hrdManager.db";

    // Table name
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ATTENDANCES = "attendances";

    // Users Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASS = "password";


    public static final String ATT_ID = "id";
    public static final String ATT_DATE = "date";
    public static final String ATT_IN = "clock_in";
    public static final String ATT_OUT = "clock_out";
    public static final String ATT_USER_ID = "user_id";
    public static final String ATT_FLAG_TAP= "flag";


    SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_USERS +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT)"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_ATTENDANCES +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, clock_in TEXT, clock_out TEXT, user_id TEXT, flag TEXT, " +
                        "FOREIGN KEY(user_id) REFERENCES users(id))"
        );


        this.db = db;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCES);

        // Create tables again
        onCreate(db);
    }
}
