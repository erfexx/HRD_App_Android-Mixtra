package com.mit.mobile.absencehrd.Fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mit.mobile.absencehrd.Helper.Constants;
import com.mit.mobile.absencehrd.Helper.SessionManager;
import com.mit.mobile.absencehrd.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.apache.commons.text.WordUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    TextView txtName;
    SessionManager sessionManager;
    PieChart pieChart;
    SimpleDateFormat clockFormat;

    ImageView FOTO;

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
        String aa = WordUtils.capitalizeFully(namaUser);
        txtName.setText(aa+" (" + Constants.currentUserID +")");

        FOTO = view.findViewById(R.id.userPict);

        Resources resources = getResources();

        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.userimage);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
        drawable.setCircular(true);

        FOTO.setImageDrawable(drawable);

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
