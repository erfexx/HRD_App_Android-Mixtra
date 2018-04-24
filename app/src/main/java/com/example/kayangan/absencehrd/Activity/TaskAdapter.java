package com.example.kayangan.absencehrd.Activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Model.Task;

/**
 * Created by Kevin E on 4/5/2018.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{
    private Context context;
    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tname;
        public TextView tdesc;
        /*public TextView tduedate;
        public TextView tassign;*/


        public MyViewHolder(View view) {
            super(view);
            tname = view.findViewById(R.id.dtname);
            tdesc = view.findViewById(R.id.dtdesc);
        }
    }


    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.tname.setText(task.getTname());

        // Formatting and displaying timestamp
        holder.tdesc.setText(task.getTdesc());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
