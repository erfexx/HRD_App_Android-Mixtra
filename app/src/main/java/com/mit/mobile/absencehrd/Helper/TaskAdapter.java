package com.mit.mobile.absencehrd.Helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.mit.mobile.absencehrd.R;
import com.mit.mobile.absencehrd.Model.Task;

/**
 * Created by Kevin E on 4/5/2018.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{
    private Context context;
    private List<Task> taskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tname;
        public TextView tdesc;


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
        
        holder.tdesc.setText(task.getTdesc());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
