package com.mit.mobile.absencehrd.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mit.mobile.absencehrd.Helper.AttendanceRecordAdapter;
import com.mit.mobile.absencehrd.Model.AttendanceRecord;
import com.mit.mobile.absencehrd.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileDetailFragment extends Fragment {

    private List<AttendanceRecord> recordList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AttendanceRecordAdapter adapter;

    public ProfileDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        recyclerView = view.findViewById(R.id.listAttendance);

        adapter = new AttendanceRecordAdapter(recordList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareMovieData();

        return view;
    }

    //function to get data from DB local or DB Server
    private void prepareMovieData() {
        AttendanceRecord rec = new AttendanceRecord("2018-08-15","08:30:00","IN");
        recordList.add(rec);

        rec = new AttendanceRecord("2018-08-15","17:30:00","OUT");
        recordList.add(rec);

        rec = new AttendanceRecord("2018-08-16","08:30:00","IN");
        recordList.add(rec);

        rec = new AttendanceRecord("2018-08-16","17:30:00","OUT");
        recordList.add(rec);

        rec = new AttendanceRecord("2018-08-17","08:30:00","IN");
        recordList.add(rec);

        rec = new AttendanceRecord("2018-08-17","17:30:00","OUT");
        recordList.add(rec);

        adapter.notifyDataSetChanged();
    }
}
