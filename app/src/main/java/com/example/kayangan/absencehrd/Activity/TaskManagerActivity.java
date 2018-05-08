package com.example.kayangan.absencehrd.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Helper.ListAdapter;
import com.example.kayangan.absencehrd.Helper.SwipeDismissListViewTouchListener;
import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {
    private ListView simpleListView;
    private ArrayList<Task> taskList = new ArrayList<>();
    private DatabaseHandler db;
    private ListAdapter mAdapter;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        simpleListView = (ListView)findViewById(R.id.simpleListView);
        db = new DatabaseHandler(this);
        taskList.addAll(db.getAllTasks());

        mAdapter = new ListAdapter(taskList,getApplicationContext());
        simpleListView.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(getBaseContext(), CreateTaskActivity.class);
                startActivity(createIntent);
            }
        });

        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long rowId) {
                final Task task = taskList.get(position);

                Intent editIntent = new Intent(getBaseContext(),EditTaskActivity.class);
                editIntent.putExtra("pos",task.get_id());
                startActivity(editIntent);
                /*LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.view_task_details, null);

                TextView tvname = alertLayout.findViewById(R.id.tvname);
                TextView tvdesc = alertLayout.findViewById(R.id.tvdesc);
                TextView tvduedate = alertLayout.findViewById(R.id.tvduedate);
                TextView tvassign = alertLayout.findViewById(R.id.tvassign);
                TextView tvprogress = alertLayout.findViewById(R.id.tvprogress);

                tvname.setText(task.getTname());
                tvdesc.setText(task.getTdesc());
                tvduedate.setText(task.getTduedate());
                tvassign.setText(task.getTassign());
                String progress = String.valueOf(task.getTprogress());
                tvprogress.setText(progress);

                AlertDialog.Builder alert = new AlertDialog.Builder(TaskManagerActivity.this);
                alert.setTitle("Details");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                //alert.setCancelable(false);
                *//*alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteTask(taskList.get(position));
                        // removing the note from the list
                        taskList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
                    }
                });*//*
                alert.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent editIntent = new Intent(getBaseContext(),EditTaskActivity.class);
                        editIntent.putExtra("pos",task.get_id());
                        startActivity(editIntent);
                    }
                });
                alert.show();*/
            }
        });

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(simpleListView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {
                            db.deleteTask(taskList.get(position));
                            taskList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(getBaseContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        simpleListView.setOnTouchListener(touchListener);

    }

    @Override
    public void onBackPressed() {
       Intent backIntent = new Intent(this, MenuActivity.class);
       startActivity(backIntent);
    }
}
