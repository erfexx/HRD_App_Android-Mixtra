package com.example.kayangan.absencehrd.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kayangan.absencehrd.Model.AttendanceRecord;
import com.example.kayangan.absencehrd.R;

import java.util.ArrayList;

public class AttendanceRecordAdapter extends ArrayAdapter<AttendanceRecord> {
    private Context mContext;
    int mResource;

    public AttendanceRecordAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AttendanceRecord> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String date = getItem(position).getModifiedDate();
        String in = getItem(position).getCheckTime();
        String out = getItem(position).getCheckTime();

        AttendanceRecord record = new AttendanceRecord(date, in, out);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvDate = convertView.findViewById(R.id.textview1);
        TextView tvIn = convertView.findViewById(R.id.textview2);
        TextView tvout = convertView.findViewById(R.id.textview3);

        tvDate.setText(date);
        tvIn.setText(in);
        tvout.setText(out);

        return convertView;
    }
}
