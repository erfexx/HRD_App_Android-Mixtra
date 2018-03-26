package com.example.kayangan.absencehrd;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kayangan.absencehrd.Model.Task;

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
    private static final String TABLE_TASKS = "tasks";

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

    private static final String TASK_ID = "tid";
    private static final String TASK_TNAME = "tname";
    private static final String TASK_TDESC = "tdesc";
    private static final String TASK_TDUEDATE = "tduedate";
    private static final String TASK_TASSIGN = "tassign";


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

        db.execSQL("CREATE TABLE " + TABLE_TASKS + "("
                + TASK_ID + " INTEGER PRIMARY KEY,"
                + TASK_TNAME + " TEXT,"
                + TASK_TDESC + " TEXT,"
                + TASK_TDUEDATE + " TEXT,"
                + TASK_TASSIGN + " INTEGER,"
                + "FOREIGN KEY(tassign) REFERENCES users(id)" + ")");


        this.db = db;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new task
    void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_TNAME, task.getTname()); // Task Name
        values.put(TASK_TDESC, task.getTdesc()); // Task Desc
        values.put(TASK_TDUEDATE, task.getTduedate()); // Task Due Date
        values.put(TASK_TASSIGN, task.getTassign()); // Task Assign

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }
}
