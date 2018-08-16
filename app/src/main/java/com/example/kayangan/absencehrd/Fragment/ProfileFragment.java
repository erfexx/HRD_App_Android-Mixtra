package com.example.kayangan.absencehrd.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kayangan.absencehrd.Helper.Constants;
import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Helper.SessionManager;
import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    TextView txtName;
    SessionManager sessionManager;
    DatabaseHandler handler;
    ListView listView;
    ArrayList<AttendanceRecord> recordList;
    AttendanceRecord attendanceRecord;
    PieChart pieChart;
    SimpleDateFormat clockFormat;



    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(getActivity());

        HashMap<String, String> data = sessionManager.getUserDetails();

        String namaUser = data.get(SessionManager.KEY_NAME);
        String idUser = data.get(SessionManager.KEY_ID);
        Constants.currentUserID = idUser;
        String zoneUser = data.get(SessionManager.KEY_ZONE);

        txtName = view.findViewById(R.id.txtName);
        txtName.setText(namaUser+" (" + Constants.currentUserID +")");

        int countOnTime = 0;
        int countLate = 0;
        String clockIn = "08:30:00";
        clockFormat = new SimpleDateFormat ("HH:mm:ss");

        pieChart = view.findViewById(R.id.piechart);

        ArrayList<Entry> yValues = new ArrayList<Entry>();
        yValues.add(new Entry(countOnTime, 0));
        yValues.add(new Entry(countLate, 1));
        //yValues.add(new Entry(5f, 2));

        PieDataSet dataSet = new PieDataSet(yValues, "Attendance Status");

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

        return view;
    }
}
