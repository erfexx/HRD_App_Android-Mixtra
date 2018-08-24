package com.mit.mobile.absencehrd.Fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mit.mobile.absencehrd.Helper.AppController;
import com.mit.mobile.absencehrd.Helper.AttendanceRecordAdapter;
import com.mit.mobile.absencehrd.Helper.Constants;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Model.AttendanceRecord;
import com.mit.mobile.absencehrd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileDetailFragment extends Fragment {

    private List<AttendanceRecord> recordList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AttendanceRecordAdapter adapter;
    AttendanceRecord rec;

    ProgressDialog dialog;

    DatabaseHandler handler;

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

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Retrieving Data ...");
        dialog.setTitle("Loading");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        recyclerView = view.findViewById(R.id.listAttendance);

        adapter = new AttendanceRecordAdapter(recordList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        handler = new DatabaseHandler(getActivity());
        prepareData();

        return view;
    }

    //function to get data from DB local or DB Server
    private void prepareData() {

//        Cursor  cursor = handler.getAllAttendance();
//
//        if (cursor.getCount() > 0){
//            while (cursor.moveToNext()){
//                rec = new AttendanceRecord(
//                        cursor.getString(cursor.getColumnIndex("ModifiedDate"))
//                                .substring(0, 10),
//                        cursor.getString(cursor.getColumnIndex("check_time"))
//                                .substring(11, cursor.getString(cursor.getColumnIndex("check_time")).length()),
//                        cursor.getString(cursor.getColumnIndex("AttendanceType"))
//                );
//
//                recordList.add(rec);
//            }
//        }

        dialog.setCancelable(false);
        dialog.show();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Constants.url+"attendances/"+Constants.currentUserID,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i<response.length(); i++)
                            {
                                JSONObject obj = response.getJSONObject(i);

                                String date = obj.getString("modifiedDate").substring(0, 10);
                                String checkTime = obj.getString("checkTime").substring(11, 19);
                                String attendanceType = obj.getString("attendanceType");

                                rec = new AttendanceRecord(date, checkTime,attendanceType);
                                recordList.add(rec);
                            }
                            dialog.dismiss();
                            adapter.notifyDataSetChanged();
                        }
                        catch (JSONException e)
                        {
                            Log.e("SYNC DATA", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SYNC DATA", error.toString());
                    }
                }
        );
        AppController.getInstance(getActivity()).addToRequestque(arrayRequest);
    }




}
