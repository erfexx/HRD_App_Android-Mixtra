package com.example.kayangan.absencehrd.Activity;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kayangan.absencehrd.Helper.AttendanceRecordAdapter;
import com.example.kayangan.absencehrd.Helper.Constants;
import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Helper.SynchronizeData;
import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Helper.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    TextView txtName;

    SessionManager sessionManager;

    DatabaseHandler handler;

    ListView listView;
    ArrayList<AttendanceRecord> recordList;

    AttendanceRecord attendanceRecord;

    PieChart pieChart;

    SimpleDateFormat clockFormat;

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

        int countOnTime = 0;
        int countLate = 0;
        String clockIn = "08:30:00";
        clockFormat = new SimpleDateFormat ("HH:mm:ss");

        listView = findViewById(R.id.listView);
        handler = new DatabaseHandler(this);

        Cursor record = handler.getAllAttendance();
        recordList = new ArrayList<>();


        if (record.getCount() > 0){
            while (record.moveToNext()){

                attendanceRecord = new AttendanceRecord(record.getString(3), record.getString(2), record.getString(4));
                try {
                    Date clock = clockFormat.parse(clockIn);
                    Date clock1 = clockFormat.parse(record.getString(2));
                    if (record.getString(4) == "IN" && clock1.after(clock))
                    {
                        countLate +=1;
                    }
                    else if (record.getString(4) == "IN" && clock1.before(clock))
                    {
                        countOnTime +=1;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                recordList.add(attendanceRecord);
            }
        }

        AttendanceRecordAdapter adapter = new AttendanceRecordAdapter(this, R.layout.adapter_view_layout, recordList);
        listView.setAdapter(adapter);

        pieChart = (PieChart)findViewById(R.id.piechart);

        ArrayList<Entry> yValues = new ArrayList<Entry>();
        yValues.add(new Entry(countOnTime, 0));
        yValues.add(new Entry(countLate, 1));
        //yValues.add(new Entry(5f, 2));

        PieDataSet dataSet = new PieDataSet(yValues, "Attendace Status");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("On-Time");
        xVals.add("Late");
        //xVals.add("Absent");

        PieData pieData = new PieData(xVals, dataSet);
        // In Percentage
        pieData.setValueFormatter(new PercentFormatter());
        // Default value
        //data.setValueFormatter(new DefaultValueFormatter(0));
        pieChart.setData(pieData);
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(38f);
        pieChart.setHoleRadius(58f);

        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.DKGRAY);
    }


}
