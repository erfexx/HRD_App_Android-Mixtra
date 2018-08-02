package com.example.kayangan.absencehrd.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kayangan.absencehrd.Model.SalesOrder;
import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.Model.User;

import java.util.ArrayList;
import java.util.List;
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
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_COMMENTS = "comments";

    // Users Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASS = "password";
    public static final String KEY_ZONE = "zone";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_UPDATED_AT = "updated_at";

    // Attendances Table Columns names
    public static final String ATT_ID = "id";
    public static final String ATT_DATE = "date";
    public static final String ATT_IN = "clock_in";
    public static final String ATT_OUT = "clock_out";
    public static final String ATT_USER_ID = "user_id";
    public static final String ATT_FLAG_TAP= "flag";
    public static final String ATT_CREATED_AT= "created_at";
    public static final String ATT_UPDATED_AT= "updated_at";
    public static final String ATT_STATUS= "status";

    // Stocks Table Columns names
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
    public static final String LOC_NAME = "place";

    public static final String TASK_ID = "tid";
    public static final String TASK_TNAME = "tname";
    public static final String TASK_TDESC = "tdesc";
    public static final String TASK_TDUEDATE = "tduedate";
    public static final String TASK_TASSIGN = "tassign";
    public static final String TASK_TASSIGN_BY = "tassign_by";
    public static final String TASK_TPROGRESS = "tprogress";
    public static final String TASK_CREATED_AT = "created_at";
    public static final String TASK_UPDATED_AT = "updated_at";


    public static final String COM_ID = "id";
    public static final String COM_TITLE = "title";
    public static final String COM_COMMENT = "comment";
    public static final String COM_TASK_ID = "task_id";
    public static final String COM_COMMENTER = "commenter";
    public static final String COM_CREATED_AT = "created_at";
    public static final String COM_UPDATED_AT = "updated_at";

    private static final String ORDER_ID = "oid";
    private static final String ORDER_TRANS = "transdate";
    private static final String ORDER_SALESMAN = "salesman";
    private static final String ORDER_VOUCHER = "voucher";

    SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_USERS +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, password TEXT, zone TEXT, created_at TEXT, updated_at TEXT)"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_ATTENDANCES +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, clock_in TEXT, clock_out TEXT, user_id TEXT, created_at TEXT, updated_at TEXT, status TEXT, " +
                        "FOREIGN KEY(user_id) REFERENCES users(id))"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_STOCKS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, category TEXT, " +
                        "branch TEXT, department TEXT, price INTEGER)"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_LOCATIONS + "(id INTEGER PRIMARY KEY AUTOINCREMENT, latitude TEXT, longitude TEXT, zone TEXT, place TEXT)"
        );


        db.execSQL("CREATE TABLE " + TABLE_TASKS + "("
                + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASK_TNAME + " TEXT,"
                + TASK_TDESC + " TEXT,"
                + TASK_TDUEDATE + " TEXT,"
                + TASK_TASSIGN + " INTEGER,"
                + TASK_TASSIGN_BY + " INTEGER,"
                + TASK_TPROGRESS + " INTEGER,"
                + TASK_CREATED_AT + " TEXT,"
                + TASK_UPDATED_AT + " TEXT,"
                + "FOREIGN KEY(tassign) REFERENCES users(id)" + ")");

        db.execSQL("CREATE TABLE " + TABLE_ORDERS + "("
                + ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ORDER_TRANS + " TEXT,"
                + ORDER_SALESMAN + " TEXT,"
                + ORDER_VOUCHER + " INTEGER,"
                + "FOREIGN KEY(salesman) REFERENCES users(id)" + ")");

        db.execSQL("CREATE TABLE " + TABLE_COMMENTS + "("
                + COM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COM_TITLE + " TEXT, "
                + COM_COMMENT + " TEXT, "
                + COM_TASK_ID + " INTEGER, "
                + COM_COMMENTER + " TEXT, "
                + COM_CREATED_AT + " TEXT, "
                + COM_UPDATED_AT + " TEXT, "
                + "FOREIGN KEY(task_id) REFERENCES tasks(id)" + ")");

        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);

        // Create tables again
        onCreate(db);
    }

    public Cursor getAllComments(int pos){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM comments WHERE task_id = "+pos, null);

        return data;
    }




    public List<String> getNames(){
        List<String> names = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0)+" - "+cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return names;
    }


    // Adding new task
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_TNAME, task.getTname()); // Task Name
        values.put(TASK_TDESC, task.getTdesc()); // Task Desc
        values.put(TASK_TDUEDATE, task.getTduedate()); // Task Due Date
        values.put(TASK_TASSIGN, task.getTassign()); // Task Assign
        values.put(TASK_TPROGRESS, task.getTprogress()); // Task Progress

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }
    // Getting single task
    public Task getTask(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[] { TASK_ID,
                        TASK_TNAME, TASK_TDESC, TASK_TDUEDATE, TASK_TASSIGN, TASK_TPROGRESS }, TASK_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task task = new Task(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                Integer.parseInt(cursor.getString(5)));
        // return task
        cursor.close();
        return task;
    }
    // Getting All task
    public ArrayList<Task> getAllTasks(int id) {
        ArrayList<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + TASK_TASSIGN + " = "+ id + " OR " + TASK_TASSIGN_BY + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.set_id(Integer.parseInt(cursor.getString(0)));
                task.setTname(cursor.getString(1));
                task.setTdesc(cursor.getString(2));
                task.setTduedate(cursor.getString(3));
                task.setTassign(cursor.getInt(4));
                task.setTassign_by(cursor.getInt(5));
                task.setTprogress(Integer.parseInt(cursor.getString(6)));
                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        // return task list
        return taskList;
    }
    // Updating single task
    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_TPROGRESS, task.getTprogress()); // Task Progress

        // updating row
        db.update(TABLE_TASKS, values, TASK_ID + " = ?",
                new String[] { String.valueOf(task.get_id()) });
    }
    // Deleting single task
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, TASK_ID + " = ?",
                new String[] { String.valueOf(task.get_id()) });
        db.close();
    }
    // Getting task Count
    public int getTaskCount() {
        String countQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }



    public Cursor getAllAttendance(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCES + " WHERE " + ATT_USER_ID + " =? ORDER BY " + ATT_DATE + " ASC", new String[]{Constants.currentUserID});
        return data;
    }

    public Cursor getAllCoordinates(String zone){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor datass = database.rawQuery("SELECT * FROM " + TABLE_LOCATIONS + " WHERE " + LOC_ZONE_TYPE + " =?", new String[]{zone});
        return datass;
    }

    public Cursor getAllStocks(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_STOCKS, null);
        return data;
    }

    public Cursor getSpecStocksAll(String branch, String departments){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM " + TABLE_STOCKS + " WHERE " + STOCK_BRANCH + " =? AND " + STOCK_DEPARTMENT + " =? ORDER BY " + STOCK_BRANCH + " ASC", new String[]{branch, departments});
        return data;
    }

    public Cursor getSpecStockBranch(String branch){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM " + TABLE_STOCKS + " WHERE " + STOCK_BRANCH + " =? ORDER BY " + STOCK_BRANCH +" ASC", new String[]{branch});
        return data;
    }

    public Cursor getSpecStockDepartment(String department){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM " + TABLE_STOCKS + " WHERE " + STOCK_DEPARTMENT + " =? ORDER BY " + STOCK_DEPARTMENT + " ASC", new String[]{department});
        return data;
    }



    // Adding new order
    public void addOrder(SalesOrder order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ORDER_TRANS, order.getTransdate()); // Order Start Date
        values.put(ORDER_SALESMAN, order.getSalesman()); // Order Salesman
        values.put(ORDER_VOUCHER, order.getVoucher()); // Order Voucher No.

        // Inserting Row
        db.insert(TABLE_ORDERS, null, values);
        db.close(); // Closing database connection
    }
    // Getting single order
    public SalesOrder getOrder(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDERS, new String[] { ORDER_ID,
                        ORDER_TRANS, ORDER_SALESMAN, ORDER_VOUCHER }, ORDER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SalesOrder order = new SalesOrder(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)));
        // return task
        cursor.close();
        return order;
    }
    // Getting All order
    public ArrayList<SalesOrder> getAllOrders() {
        ArrayList<SalesOrder> orderList = new ArrayList<SalesOrder>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SalesOrder order = new SalesOrder();
                order.set_id(Integer.parseInt(cursor.getString(0)));
                order.setTransdate(cursor.getString(1));
                order.setSalesman(cursor.getString(2));
                order.setVoucher(Integer.parseInt(cursor.getString(3)));
                // Adding order to list
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        // return order list
        return orderList;
    }
    // Getting order based on search
    public ArrayList<SalesOrder> getSearchOrders(String startdate, String enddate) {
        ArrayList<SalesOrder> orderList = new ArrayList<SalesOrder>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " WHERE DATE(" + ORDER_TRANS + ") BETWEEN '" + startdate + "' AND '" + enddate + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SalesOrder order = new SalesOrder();
                order.set_id(Integer.parseInt(cursor.getString(0)));
                order.setTransdate(cursor.getString(1));
                order.setSalesman(cursor.getString(2));
                order.setVoucher(Integer.parseInt(cursor.getString(3)));
                // Adding order to list
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        // return order list
        return orderList;
    }
    // Updating single order
    public int updateOrder(SalesOrder order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ORDER_TRANS, order.getTransdate());
        values.put(ORDER_SALESMAN, order.getSalesman());
        values.put(ORDER_VOUCHER, order.getVoucher());

        // updating row
        return db.update(TABLE_ORDERS, values, ORDER_ID + " = ?",
                new String[] { String.valueOf(order.get_id()) });
    }
    // Deleting single order
    public void deleteOrder(SalesOrder order) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, ORDER_ID + " = ?",
                new String[] { String.valueOf(order.get_id()) });
        db.close();
    }





    public String searchUser(int id)
    {
        String nama;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT name FROM users WHERE id = "+id, null);

        if (data.getCount() > 0)
        {
            data.moveToFirst();
            nama = data.getString(0);
            return nama;
        }
        else
            return "NULL";

    }

    public boolean isTableExists()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor record = db.rawQuery("SELECT COUNT(*) FROM users", null);

        if (record != null) {
            record.moveToFirst();                       // Always one row returned.
            if (record.getInt (0) == 0) {               // Zero count means empty table.
                return true;
            }
        }

        return false;
    }

    public boolean isTableTaskExists()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor record = db.rawQuery("SELECT COUNT(*) FROM tasks", null);

        if (record != null) {
            record.moveToFirst();                       // Always one row returned.
            if (record.getInt (0) == 0) {               // Zero count means empty table.
                return true;
            }
        }

        return false;
    }

    public void deleteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, TASK_ID + " > 0", null);
    }
}
