package com.example.kayangan.absencehrd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Model.AttendanceRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Time time;


    //hahahahahahahahhahaha

    SQLiteOpenHelper helper;
    SQLiteDatabase DB;

    TextView txtUser;

    Handler handler;
    Runnable runnable;

    Button btn_in, btn_out;

    LoginActivity la = new LoginActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = new Time();
        helper = new DatabaseHandler(this);

        runnable = new Runnable() {
            @Override
            public void run() {
                time.setToNow();


                TextView textView = findViewById(R.id.jam);
                long date = System.currentTimeMillis();

                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String dateString = dateFormat.format(date);

                textView.setText(dateString);

                handler.postDelayed(runnable, 1000);
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 1000);


        txtUser = findViewById(R.id.user);
        txtUser.setText(currentUser.currentUser.getName().toString() + " ID: " + currentUser.currentUserID);


        btn_in = findViewById(R.id.btnIn);
        btn_out= findViewById(R.id.btnOut);

        //la.setAwal();

        btn_in.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( cekTable() ) {

                    DB = helper.getWritableDatabase();

                    AttendanceRecord data = new AttendanceRecord();

                    data.setFlag("1");
                    /*data.setClock_in(getTime().toString());
                    data.setUser_id(currentUser.currentUserID);
                    data.setDate(getDate().toString());
                    data.setClock_out("00:00:00");*/

                    addRecord(data);

                    Toast.makeText(MainActivity.this, "MASUK", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "SUDAH MASUK" , Toast.LENGTH_SHORT).show();

                Toast.makeText(MainActivity.this, "FLAG: " + getFlag(currentUser.currentUserID), Toast.LENGTH_SHORT).show();
            }
        });

        btn_out.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (cekTime() == false){
                            Toast.makeText(MainActivity.this, "BELUM JAM PULANG", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if (getFlag(currentUser.currentUserID).equals("1")) {
                                Toast.makeText(MainActivity.this, "PULANG", Toast.LENGTH_SHORT).show();
                                AttendanceRecord data = new AttendanceRecord();

                                String a = getJamMasuk(currentUser.currentUserID, getDate().toString());

                                data.setFlag("0");
                                data.setClock_out(getTime().toString());

                                updateRecord(data);
                            }
                            else
                                Toast.makeText(MainActivity.this, "SUDAH PULANG" , Toast.LENGTH_SHORT).show();
                        }
                    }

                }
        );
    }




    public boolean cekTime(){
        time.setToNow();

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String timeString = dateFormat.format(date);

        try {
            String masuk = "09:00:00";
            Date inFormat = new SimpleDateFormat("hh:mm:ss").parse(masuk);

            String pulang = "17:00:00";
            Date outFormat = new SimpleDateFormat("hh:mm:ss").parse(pulang);

            return outFormat.after(dateFormat.parse(dateFormat.format(date)));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    public String getTime(){
        time.setToNow();

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String timeString = dateFormat.format(date);

        return timeString;
    }

    public String getDate(){
        time.setToNow();

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(date);

        return dateString;
    }

    public String getJamMasuk(String id, String date){

        DB = helper.getReadableDatabase();

        Cursor cursor = DB.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_ATTENDANCES +
                " WHERE " + DatabaseHandler.ATT_USER_ID + "=? AND " + DatabaseHandler.ATT_DATE + "=?", new String[]{id,date});

        if (cursor!=null)
            cursor.moveToFirst();

        String jam = String.valueOf(cursor.getString(cursor.getColumnIndex("clock_in")));

        return jam;
    }

    public String getFlag(String id){
        DB = helper.getReadableDatabase();

        Cursor cursor = DB.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_ATTENDANCES +
                " WHERE " + DatabaseHandler.ATT_USER_ID + "=?", new String[]{id});

        String tanda = "";
        if (cursor != null) {
            cursor.moveToFirst();

            tanda = cursor.getString(cursor.getColumnIndex("flag"));
            return tanda;
        }

        return tanda;
    }

    private boolean cekTable() {
        DB = helper.getWritableDatabase();

        Cursor cursor = DB.query(DatabaseHandler.TABLE_ATTENDANCES, null, null, null, null, null, null);

        if (cursor.getCount() > 0)
            return true;

        return false ;
    }




    public void addRecord(AttendanceRecord data){

        ContentValues values = new ContentValues();

        values.put(DatabaseHandler.ATT_FLAG_TAP, data.getFlag());
        /*values.put(DatabaseHandler.ATT_IN, data.getClock_in());
        values.put(DatabaseHandler.ATT_DATE, data.getDate());
        values.put(DatabaseHandler.ATT_USER_ID, data.getUser_id());
        values.put(DatabaseHandler.ATT_OUT, data.getClock_out());*/

        DB.update(DatabaseHandler.TABLE_ATTENDANCES, values, DatabaseHandler.ATT_USER_ID + "= ?", new String[]{currentUser.currentUserID});
        //DB.close();
    }

    public void updateRecord(AttendanceRecord data){
        ContentValues values = new ContentValues();

        values.put(DatabaseHandler.ATT_OUT, data.getClock_out());
        values.put(DatabaseHandler.ATT_FLAG_TAP, data.getFlag());

        DB.update(DatabaseHandler.TABLE_ATTENDANCES, values, DatabaseHandler.ATT_USER_ID + " = ?", new String[]{currentUser.currentUserID});
    }
}