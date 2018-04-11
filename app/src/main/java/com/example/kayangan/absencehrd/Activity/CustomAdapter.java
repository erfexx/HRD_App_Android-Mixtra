package com.example.kayangan.absencehrd.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin E on 4/9/2018.
 */

public class CustomAdapter extends ArrayAdapter<Task>{
    /*Context c;
    ArrayList<Task> tasks;
    LayoutInflater inflater;

    public CustomAdapter(Context c, ArrayList<Task> tasks) {
        this.c = c;
        this.tasks = tasks;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null)
        {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.task_list_row,parent,false);
        }

        TextView dtname = (TextView) convertView.findViewById(R.id.dtname);
        TextView dtdesc = (TextView) convertView.findViewById(R.id.dtdesc);
        dtname.setText(tasks.get(position).getTname());
        dtdesc.setText(tasks.get(position).getTdesc());

        final int pos=position;
        return convertView;
    }*/
    private List<Task> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView dtname;
        TextView dtdesc;
    }

    public CustomAdapter(ArrayList<Task> data, Context context) {
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

        viewHolder.dtname.setText(task.getTname());
        viewHolder.dtdesc.setText(task.getTdesc());
        // Return the completed view to render on screen
        return convertView;
    }

    /*private ArrayList<String> dataSet;
    private Context context;


    public CustomAdapter(@NonNull Context context, ArrayList<String> dataSet) {
        super(context, R.layout.task_list_row, dataSet);

        this.context = context;
        this.dataSet = dataSet;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.task_list_row, parent, false);

        TextView dtdesc = (TextView) rowView.findViewById(R.id.dtdesc);

        // Setting the text to display
        dtdesc.setText(dataSet.get(position));

        return rowView;
    }*/
}

