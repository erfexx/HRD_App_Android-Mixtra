package com.example.kayangan.absencehrd.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayangan.absencehrd.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.R;
import com.example.kayangan.absencehrd.Utility.MyDividerItemDecoration;
import com.example.kayangan.absencehrd.Utility.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class ViewTaskActivity extends AppCompatActivity {
    private TaskAdapter mAdapter;
    private List<Task> taskList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noNotesView;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noNotesView = findViewById(R.id.empty_notes_view);

        db = new DatabaseHandler(this);

        taskList.addAll(db.getAllTasks());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAdapter = new TaskAdapter(this, taskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        showActionsDialog(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        showActionsDialog(position);
                    }
                }));

    }

    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Yes", "No"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete?");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    deleteNote(position);
                } else {
                   dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteTask(taskList.get(position));

        // removing the note from the list
        taskList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    private void showDetailsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"OK"};
        Task task = taskList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Task Detail");
        builder.setMessage(task.getTname());
        builder.setMessage(task.getTdesc());
        builder.setMessage(task.getTduedate());
        builder.setMessage(task.getTassign());
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        dialog.dismiss();
                    } else {
                    }
            }
        });
        builder.show();
    }

    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getTaskCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

}
