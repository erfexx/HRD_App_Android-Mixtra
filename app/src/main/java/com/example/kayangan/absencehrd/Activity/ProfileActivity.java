package com.example.kayangan.absencehrd.Activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kayangan.absencehrd.Helper.AttendanceRecordAdapter;
import com.example.kayangan.absencehrd.Helper.Constants;
import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

  TextView txtName;

  SessionManager sessionManager;

  DatabaseHandler handler;

  ListView listView;
  ArrayList<AttendanceRecord> recordList;

  AttendanceRecord attendanceRecord;

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    sessionManager = new SessionManager(getApplicationContext());

    HashMap<String, String> data = sessionManager.getUserDetails();

    String namaUser = data.get(SessionManager.KEY_NAME);
    String idUser = data.get(SessionManager.KEY_ID);
    Constants.currentUserID = idUser;
    String zoneUser = data.get(SessionManager.KEY_ZONE);

    txtName = findViewById(R.id.txtName);
    txtName.setText(namaUser+" (" + Constants.currentUserID +")");

    listView = findViewById(R.id.listView);
    handler = new DatabaseHandler(this);

    Cursor record = handler.getAllAttendance();
    recordList = new ArrayList<>();

    if (record.getCount() > 0){
      while (record.moveToNext()){
        attendanceRecord = new AttendanceRecord(record.getString(1), record.getString(2), record.getString(3));

        recordList.add(attendanceRecord);
      }
    }

    AttendanceRecordAdapter adapter = new AttendanceRecordAdapter(this, R.layout.adapter_view_layout, recordList);
    listView.setAdapter(adapter);
  }


}
