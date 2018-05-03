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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Helper.SessionManager;
import com.example.kayangan.absencehrd.Helper.currentUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Time time;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    SQLiteOpenHelper helper;
    SQLiteDatabase DB;

    TextView txtUser;

    Handler handler;
    Runnable runnable;

    SessionManager session;

    Button btn_in, btn_out;
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

        //Get Session data
        HashMap<String, String> data = session.getUserDetails();
        String userName = data.get(SessionManager.KEY_NAME);
        final String userID = data.get(SessionManager.KEY_ID);

        currentUser.currentUserID = userID;

        time = new Time();
        helper = new DatabaseHandler(this);

        final TextView textView = findViewById(R.id.jam);

        textView.setText(getTime());

        //buat tampilin jam dinamis
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

        //menampilkan nama user yang sedang login
        txtUser = findViewById(R.id.user);
        txtUser.setText("Hello, " + userName);


        btn_in = findViewById(R.id.btnIn);
        btn_out= findViewById(R.id.btnOut);

        //button buat tap in
        btn_in.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DB = helper.getWritableDatabase();

                        AttendanceRecord data = new AttendanceRecord();

                        data.setFlag("1");
                        data.setClock_in(getTime().toString());
                        data.setUser_id(userID);
                        data.setDate(getDate().toString());
                        data.setClock_out("00:00:00");

                        recordIN(data);
                    }
                }
        );


        //button buat tap out
        btn_out.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        DB = helper.getWritableDatabase();

                        AttendanceRecord data = new AttendanceRecord();
                        data.setClock_out(getTime());
                        recordOut(data);
                    }
                }
        );
    }

    //fungsi buat cek waktu, apakah sesua dengan waktu pulang
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
                " WHERE " + DatabaseHandler.ATT_USER_ID + " = ? AND " + DatabaseHandler.ATT_DATE + " = ? ", new String[]{currentUser.currentUserID, tanggal});

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

    public void recordIN(AttendanceRecord data){

        ContentValues values = new ContentValues();

        String tanggal = getDate();

        if (!validateDate())
        {
            values.put(DatabaseHandler.ATT_FLAG_TAP, "");
            values.put(DatabaseHandler.ATT_IN, data.getClock_in());
            values.put(DatabaseHandler.ATT_DATE, data.getDate());
            values.put(DatabaseHandler.ATT_USER_ID, data.getUser_id());
            values.put(DatabaseHandler.ATT_OUT, data.getClock_out());

            DB.insert(DatabaseHandler.TABLE_ATTENDANCES, null, values);

            session.createTapInSession();

            Toast.makeText(this, "YOU ARE IN, THANK YOU! :)", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "YOU ARE ALREADY IN :O", Toast.LENGTH_SHORT).show();
        }
    }

    //ADA ERROR KETIKA HABIS TAP-IN TERUS LOGOUT, ERRORNYA DB NYA NULL,
    //TAPI KETIKA DI TAP IN TERUS DI TAP OUT DB NYA TIDAK NULL
    public void recordOut(AttendanceRecord data){
        ContentValues values = new ContentValues();

        values.put(DatabaseHandler.ATT_OUT, getTime());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formatDate = df.format(c);

        if (DB != null)
        {
            if (validateDate() && session.isTappedIn())
            {
                DB.update(DatabaseHandler.TABLE_ATTENDANCES, values, "date = ? AND user_id = ?",
                        new String[]{formatDate, currentUser.currentUserID});

                Toast.makeText(this, "YOU ARE OUT, HAVE A NICE DAY :)", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "TAP IN BEFORE YOU TAP OUT :D", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d("AAA", "DB NULL, DUNNO WHY :(");
            Toast.makeText(this, "SORRY, PRESS IN AGAIN BEFORE PRESS OUT :(", Toast.LENGTH_LONG).show();
        }

    }
}