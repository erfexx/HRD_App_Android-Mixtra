package com.mit.mobile.absencehrd.Fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mit.mobile.absencehrd.Helper.AlarmReceiver;
import com.mit.mobile.absencehrd.Helper.Constants;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Helper.NotificationScheduler;
import com.mit.mobile.absencehrd.Model.LocalData;
import com.mit.mobile.absencehrd.Helper.SynchronizeData;
import com.mit.mobile.absencehrd.Model.AttendanceRecord;
import com.mit.mobile.absencehrd.R;
import com.mit.mobile.absencehrd.Helper.SessionManager;

import org.apache.commons.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class AttendanceFragment extends Fragment {
    Time time;
    ////////////////////////////////////////////////////////////////////////////////////////////////
    SQLiteOpenHelper helper;
    SQLiteDatabase DB;

    TextView txtUser;
    TextView txtSalam, txtLat, txtLong;

    Handler handler;
    Runnable runnable;

    SessionManager session;

    LocalData localData;

    StickySwitch stickySwitch;

    RelativeLayout relativeLayout;
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public AttendanceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        // Session class instance
        session = new SessionManager(getActivity());
        localData = new LocalData(getActivity());

        relativeLayout = view.findViewById(R.id.rlay);

        stickySwitch = view.findViewById(R.id.switchs);

        txtLat = view.findViewById(R.id.latitudeTxt);
        txtLong = view.findViewById(R.id.longitudeTxt);

        //Get Session data
        HashMap<String, String> data = session.getUserDetails();
        String userName = data.get(SessionManager.KEY_NAME);
        final String userID = data.get(SessionManager.KEY_ID);
        String aa = WordUtils.capitalizeFully(userName);
        String la = data.get(SessionManager.KEY_LAT);
        String lo = data.get(SessionManager.KEY_LONG);

        txtLat.setText(la);
        txtLong.setText(lo);

        Constants.currentUserID = userID;

        Log.i("RESPONSES", ""+Constants.currentUserID);

        time = new Time();
        helper = new DatabaseHandler(getActivity());

        final TextView textView = view.findViewById(R.id.jam);

        textView.setText(Constants.currentTIME);
        //buat tampilin jam dinamis
        runnable = new Runnable() {
            @Override
            public void run() {

                Constants.getInstance(getActivity()).getServerTime();

                textView.setText(Constants.currentTIME);
                handler.postDelayed(runnable, 1000);
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 1000);

        //menampilkan nama user yang sedang login
        txtUser = view.findViewById(R.id.user);
        txtSalam = view.findViewById(R.id.salam);

        String currentUSER = "Hello, " + aa;
        txtUser.setText(currentUSER);

        if (Constants.currentTIME.compareTo("00:00:00") >= 0 && Constants.currentTIME.compareTo("10:59:59") <= 0)
        {
            txtSalam.setText("Good Morning");
        }
        else if (Constants.currentTIME.compareTo("11:00:00") >= 0 && Constants.currentTIME.compareTo("13:59:59") <= 0)
        {
            txtSalam.setText("Good Day");
        }
        else if (Constants.currentTIME.compareTo("14:00:00") >= 0 && Constants.currentTIME.compareTo("17:59:59") <= 0)
        {
            txtSalam.setText("Good Afternoon");
        }
        else if (Constants.currentTIME.compareTo("18:00:00") >= 0 && Constants.currentTIME.compareTo("23:59:59") <= 0)
        {
            txtSalam.setText("Good Evening");
        }
        else
            txtSalam.setText("How Are You?");

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

                            localData.setReminderStatus(true);

                            NotificationScheduler.setReminder(getActivity(), AlarmReceiver.class, localData.get_hour(), localData.get_min());
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

                            NotificationScheduler.cancelReminder(getActivity(), AlarmReceiver.class);
                        }
                    }
                });

        return view;
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
            SynchronizeData.getInstance(getActivity()).PostAttendance(data);

            session.createTapInSession();

            Toast.makeText(getActivity(), "YOU ARE IN, THANK YOU! :)", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(), "YOU ARE ALREADY IN :O", Toast.LENGTH_SHORT).show();
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

        SynchronizeData.getInstance(getActivity()).PostAttendance(data);
        Toast.makeText(getActivity(), "YOU ARE OUT, HAVE A NICE DAY", Toast.LENGTH_SHORT).show();

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