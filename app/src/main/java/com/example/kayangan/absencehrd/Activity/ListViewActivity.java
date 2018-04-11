package com.example.kayangan.absencehrd.Activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kayangan.absencehrd.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {
    private ListView simpleListView;
    private ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<String> newData;
    private DatabaseHandler db;
    private CustomAdapter mAdapter;

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

        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long rowId) {
                Task task = taskList.get(position);

                AlertDialog.Builder adb = new AlertDialog.Builder(
                        ListViewActivity.this);
                adb.setTitle("Details");
                adb.setMessage("Task Name : " + task.getTname() + "\n"
                        + "Task Desc : " + task.getTdesc() + "\n"
                        + "Due Date : " + task.getTduedate() + "\n"
                        + "Assigned To : " + task.getTassign());
                adb.setPositiveButton("Ok", null);
                adb.show();
            }
        });


        /*simpleListView = (ListView)findViewById(R.id.simpleListView);
        db = new DatabaseHandler(this);
        taskList = db.getAllTasks();
        newData = new ArrayList<String>();

        // Assigning the title to our global property so we can access it
        // later after certain actions (deleting/adding)
        for (Task task : taskList) {
            newData.add(task.getTname());
        }
        mAdapter = new CustomAdapter(this, newData);

        simpleListView.setAdapter(mAdapter);*/
    }

}
