package com.example.kayangan.absencehrd.Activity;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.kayangan.absencehrd.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity{
    private ListView simpleListView;
    private ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<String> newData;
    private DatabaseHandler db;
    private CustomAdapter mAdapter;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        simpleListView = (ListView)findViewById(R.id.simpleListView);
        db = new DatabaseHandler(this);
        taskList.addAll(db.getAllTasks());

        mAdapter = new CustomAdapter(taskList,getApplicationContext());
        simpleListView.setAdapter(mAdapter);

        simpleListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, final long rowId) {
                final Task task = taskList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ListViewActivity.this);
                builder.setTitle(task.getTname());
                builder.setMessage("What do you want to do with this task?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        db.deleteTask(taskList.get(position));
                        // removing the note from the list
                        taskList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Intent editIntent = new Intent(getBaseContext(),EditTaskActivity.class);
                        editIntent.putExtra("pos",task.get_id());
                        taskList.set(position,task);
                        mAdapter.notifyDataSetChanged();
                        startActivity(editIntent);
                    }
                });
                builder.show();
                return true;
            }
        });

        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                Task task = taskList.get(position);
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.view_task_details, null);

                TextView tvname = alertLayout.findViewById(R.id.tvname);
                TextView tvdesc = alertLayout.findViewById(R.id.tvdesc);
                TextView tvduedate = alertLayout.findViewById(R.id.tvduedate);
                TextView tvassign = alertLayout.findViewById(R.id.tvassign);

                tvname.setText(task.getTname());
                tvdesc.setText(task.getTdesc());
                tvduedate.setText(task.getTduedate());
                tvassign.setText(task.getTassign());

                AlertDialog.Builder alert = new AlertDialog.Builder(ListViewActivity.this);
                alert.setTitle("Details");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setPositiveButton("OK",null);
                alert.show();
            }
        });
    }
}
