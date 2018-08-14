package com.example.kayangan.absencehrd.Activity;

import android.app.Notification;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.AlarmReceiver;
import com.example.kayangan.absencehrd.Helper.Constants;
import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Helper.NotificationScheduler;
import com.example.kayangan.absencehrd.Model.LocalData;
import com.example.kayangan.absencehrd.Helper.SynchronizeData;
import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Helper.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class MainActivity extends AppCompatActivity {
    Time time;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    SQLiteOpenHelper helper;
    SQLiteDatabase DB;

    TextView txtUser;

    Handler handler;
    Runnable runnable;

    SessionManager session;

    LocalData localData;

    StickySwitch stickySwitch;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Session class instance
        session = new SessionManager(getApplicationContext());

        Constants.getInstance(MainActivity.this).getServerTime();

        stickySwitch = findViewById(R.id.switchs);

        //Get Session data
        HashMap<String, String> data = session.getUserDetails();
        String userName = data.get(SessionManager.KEY_NAME);
        final String userID = data.get(SessionManager.KEY_ID);

        Constants.currentUserID = userID;

        SynchronizeData.getInstance(MainActivity.this).LastAttRecord(Constants.currentUserID);

        time = new Time();
        helper = new DatabaseHandler(this);

        final TextView textView = findViewById(R.id.jam);

        textView.setText(Constants.currentTIME);
        //buat tampilin jam dinamis
        runnable = new Runnable() {
            @Override
            public void run() {
                Constants.getInstance(MainActivity.this).getServerTime();
                textView.setText(Constants.currentTIME);
                handler.postDelayed(runnable, 1000);
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 1000);

        //menampilkan nama user yang sedang login
        txtUser = findViewById(R.id.user);
        String currentUSER = "Hello, " + userName;
        txtUser.setText(currentUSER);

        localData.set_hour(17);
        localData.set_min(30);

        cekSwitch();
        stickySwitch.setOnSelectedChangeListener(
                new StickySwitch.OnSelectedChangeListener() {
                    @Override
                    public void onSelectedChange(StickySwitch.Direction direction, String s) {
                        //buat absen masuk
                        if (s.equals("IN"))
                        {
                            session.createTapInSession();

                            DB = helper.getWritableDatabase();

                            AttendanceRecord data = new AttendanceRecord();


                            data.setAttendanceType("IN");
                            data.setCheckTime(Constants.currentTIME);
                            data.setEmployeeID(userID);
                            data.setModifiedDate(getDate());
                            data.setModifiedDate(getDate());
                            data.setId(userID);

                            recordIN(data);

                            //localData.setReminderStatus(true);

                            //NotificationScheduler.setReminder(MainActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                        }
                        //buat absen pulang
                        else if (s.equals("OUT")){
                            session.createTapOutSession();

                            //DB = helper.getWritableDatabase();

                            AttendanceRecord data = new AttendanceRecord();

                            data.setAttendanceType("OUT");
                            data.setCheckTime(Constants.currentTIME);
                            data.setEmployeeID(userID);
                            data.setModifiedDate(getDate());
                            data.setId(userID);

                            recordOut(data);

                            NotificationScheduler.cancelReminder(MainActivity.this, AlarmReceiver.class);
                        }
                    }
                }
        );
    }

    public void recordIN(AttendanceRecord data){

        ContentValues values = new ContentValues();

        if (!validateDate())
        {
            /*values.put(DatabaseHandler.ATT_IN, data.getClock_in());
            values.put(DatabaseHandler.ATT_DATE, data.getDate());
            values.put(DatabaseHandler.ATT_USER_ID, data.getUser_id());
            values.put(DatabaseHandler.ATT_OUT, data.getClock_out());
            values.put(DatabaseHandler.ATT_CREATED_AT, data.getCreated_at());
            values.put(DatabaseHandler.ATT_STATUS, data.getStatus());*/

            //long id = DB.insert(DatabaseHandler.TABLE_ATTENDANCES, null, values);

            //Log.i("DBCEK", "Status: "+data.getAttendanceType());

            //upload data attendance record ke db server
            SynchronizeData.getInstance(MainActivity.this).PostAttendance(data);

            session.createTapInSession();

            Toast.makeText(this, "YOU ARE IN, THANK YOU! :)", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "YOU ARE ALREADY IN :O", Toast.LENGTH_SHORT).show();
        }
    }

    public void recordOut(AttendanceRecord data){
        ContentValues values = new ContentValues();

        //values.put(DatabaseHandler.ATT_OUT, data.getClock_out());

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");

        String formatDate = df.format(c);
        String formatDate2 = df2.format(c);

        SynchronizeData.getInstance(MainActivity.this).PostAttendance(data);
        Toast.makeText(this, "YOU ARE OUT, HAVE A NICE DAY", Toast.LENGTH_SHORT).show();

    }






    ///////////////////////////////////////////////////
    public void cekSwitch(){
        if (!session.isTappedIn())
        {
            stickySwitch.setDirection(StickySwitch.Direction.LEFT);
        }
        else
            stickySwitch.setDirection(StickySwitch.Direction.RIGHT);
    }

    //fungsi untuk get waktu terkini
    public String getTime(){
        time.setToNow();

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String timeString = dateFormat.format(date);

        return timeString;
    }

    //fungsi untuk get tanggal terkini
    public String getDate(){
        time.setToNow();

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = String.valueOf(dateFormat.format(date));

        return dateString;
    }

    public boolean validateDate(){
        DB = helper.getReadableDatabase();

        String tanggal = getDate();

        Cursor cursor = DB.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_ATTENDANCES +
                " WHERE " + DatabaseHandler.ATT_EMP_ID + " = ? AND " + DatabaseHandler.ATT_MOD_DATE + " = ? ", new String[]{Constants.currentUserID, tanggal});

        if (cursor != null){
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();

                String date = String.valueOf(cursor.getString(1));

                if (getDate().equals(date))
                    return true;
            }
        }

        return false;
    }
}