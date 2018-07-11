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
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_ORDERS = "orders";

    // Users Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASS = "password";


    public static final String ATT_ID = "id";
    public static final String ATT_DATE = "date";
    public static final String ATT_IN = "clock_in";
    public static final String ATT_OUT = "clock_out";
    public static String ATT_USER_ID = "user_id";
    public static final String ATT_FLAG_TAP= "flag";

    private static final String TASK_ID = "tid";
    private static final String TASK_TNAME = "tname";
    private static final String TASK_TDESC = "tdesc";
    private static final String TASK_TDUEDATE = "tduedate";
    private static final String TASK_TASSIGN = "tassign";
    private static final String TASK_TPROGRESS = "tprogress";

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
                + TASK_TASSIGN + " TEXT,"
                + TASK_TPROGRESS + " INTEGER,"
                + "FOREIGN KEY(tassign) REFERENCES users(id)" + ")");

        db.execSQL("CREATE TABLE " + TABLE_ORDERS + "("
                + ORDER_ID + " INTEGER PRIMARY KEY,"
                + ORDER_TRANS + " TEXT,"
                + ORDER_SALESMAN + " TEXT,"
                + ORDER_VOUCHER + " INTEGER,"
                + "FOREIGN KEY(salesman) REFERENCES users(id)" + ")");

        this.db = db;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);

        // Create tables again
        onCreate(db);
    }

    public List<String> getNames(){
        List<String> names = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(1));
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
                cursor.getString(4),
                Integer.parseInt(cursor.getString(5)));
        // return task
        cursor.close();
        return task;
    }
    // Getting All task
    public ArrayList<Task> getAllTasks(String name) {
        ArrayList<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + TASK_TASSIGN + " = " + "'" + name + "'";

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
                task.setTassign(cursor.getString(4));
                task.setTprogress(Integer.parseInt(cursor.getString(5)));
                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        // return task list
        return taskList;
    }
    // Updating single task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TASK_TNAME, task.getTname());
        values.put(TASK_TDESC, task.getTdesc());
        values.put(TASK_TDUEDATE, task.getTduedate());
        values.put(TASK_TASSIGN, task.getTassign());
        values.put(TASK_TPROGRESS, task.getTprogress()); // Task Progress

        // updating row
        return db.update(TABLE_TASKS, values, TASK_ID + " = ?",
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
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_ATTENDANCES + " WHERE " + ATT_USER_ID + " =? ORDER BY " + ATT_DATE + " ASC", new String[]{currentUser.currentUserID});
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
}
