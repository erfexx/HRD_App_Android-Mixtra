package com.example.kayangan.absencehrd.Helper;

import android.content.Context;
import android.database.Cursor;
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
  public static final String TABLE_STOCKS = "stocks";
  public static final String TABLE_LOCATIONS = "locations";

  // Users Table Column names
  public static final String KEY_ID = "id";
  public static final String KEY_NAME = "name";
  public static final String KEY_PASS = "password";


  // Attendances Table Column names
  public static final String ATT_ID = "id";
  public static final String ATT_DATE = "date";
  public static final String ATT_IN = "clock_in";
  public static final String ATT_OUT = "clock_out";
  public static final String ATT_USER_ID = "user_id";
  public static final String ATT_FLAG_TAP= "flag";

  // Stocks Table Column names
  public static final String STOCK_ID = "id";
  public static final String STOCK_ITEM = "item";
  public static final String STOCK_CATEGORY = "category";
  public static final String STOCK_BRANCH = "branch";
  public static final String STOCK_DEPARTMENT = "department";
  public static final String STOCK_PRICE = "price";

  //Locations Table Column names
  public static final String LOC_ID = "id";
  public static final String LOC_LAT = "latitude";
  public static final String LOC_LONG = "longitude";
  public static final String LOC_ZONE_TYPE = "zone";
  public static final String LOC_NAME = "place_name";

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

    db.execSQL("CREATE TABLE " + TABLE_STOCKS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, category TEXT, " +
            "branch TEXT, department TEXT, price INTEGER)");

    db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_LOCATIONS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, latitude TEXT, longitude TEXT, zone TEXT, place TEXT)");

    this.db = db;
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCES);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKS);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

    // Create tables again
    onCreate(db);
  }

  public Cursor getAllAttendance(){
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCES + " WHERE " + ATT_USER_ID + " =? ORDER BY " + ATT_DATE + " ASC", new String[]{currentUser.currentUserID});
    return data;
  }

  public Cursor getAllCoordinates(){
      SQLiteDatabase database = this.getReadableDatabase();
      Cursor datass = database.rawQuery("SELECT * FROM " + TABLE_LOCATIONS, new String[]{});
      return datass;
  }
}
