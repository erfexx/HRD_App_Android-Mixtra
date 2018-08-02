package com.example.kayangan.absencehrd.Helper;

import android.content.Context;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin E on 4/9/2018.
 * Task List Adapter
 */

public class ListAdapter extends ArrayAdapter<Task>{
    private List<Task> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView dtname;
        TextView dtdesc;
    }

    public ListAdapter(ArrayList<Task> data, Context context) {
        super(context, R.layout.task_list_row, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_list_row, parent, false);
            viewHolder.dtname = (TextView) convertView.findViewById(R.id.dtname);
            viewHolder.dtdesc = (TextView) convertView.findViewById(R.id.dtdesc);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        viewHolder.dtname.setText("Task \t\t: " + task.getTname());
        viewHolder.dtdesc.setText("Desc \t: " + task.getTdesc());
        // Return the completed view to render on screen
        return convertView;
    }
}