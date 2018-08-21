package com.mit.mobile.absencehrd.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mit.mobile.absencehrd.Model.AttendanceRecord;
import com.mit.mobile.absencehrd.R;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRecordAdapter extends RecyclerView.Adapter<AttendanceRecordAdapter.MyViewHolder> {
    private Context mContext;
    int mResource;

    private List<AttendanceRecord> recAtt;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AttendanceRecord rec = recAtt.get(position);
        holder.tvTime.setText(rec.getCheckTime());
        holder.tvDate.setText(rec.getModifiedDate());
        holder.tvType.setText(rec.getAttendanceType());
    }

    @Override
    public int getItemCount() {
        return recAtt.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate;
        TextView tvTime;
        TextView tvType;

        public MyViewHolder(View view)
        {
            super(view);
            tvDate = view.findViewById(R.id.txtDate);
            tvTime = view.findViewById(R.id.txtClock);
            tvType = view.findViewById(R.id.txtType);
        }

    }

    public AttendanceRecordAdapter(List<AttendanceRecord> attList)
    {
        this.recAtt = attList;
    }
}
