package com.mit.mobile.absencehrd.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mit.mobile.absencehrd.Helper.CommentAdapter;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Helper.RecyclerTouchListener;
import com.mit.mobile.absencehrd.Helper.SynchronizeData;
import com.mit.mobile.absencehrd.Model.Comment;
import com.mit.mobile.absencehrd.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private List<Comment> commentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    Comment comment;

    DatabaseHandler handler;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        final int position = getIntent().getIntExtra("task_id", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        handler = new DatabaseHandler(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment(position);
            }
        });

        recyclerView = findViewById(R.id.rcView);

        commentAdapter = new CommentAdapter(commentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(),
                        recyclerView,
                        new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {

                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                Comment comment = commentList.get(position);
                                showDesc(comment.getTitle(), comment.getCommenter(), comment.getCreated_at(), comment.getComment());
                            }
                        })
        );

        recyclerView.setAdapter(commentAdapter);

        commentDataList(position);
    }

    private void showDesc(String title, String commenter, String created_at, String comment) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.comment_desc, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        TextView txtTitle = view.findViewById(R.id.cdTitle);
        TextView txtCommenter = view.findViewById(R.id.cdCommenter);
        TextView txtPost = view.findViewById(R.id.cdCreatedOn);
        TextView txtComments = view.findViewById(R.id.cdComments);

        txtTitle.setText(title);
        txtCommenter.setText("By: "+commenter);
        txtPost.setText("Posted at: "+created_at);
        txtComments.setText(comment);

        builder
                .setCancelable(false)
                .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void commentDataList(int pos) {
        Cursor record = handler.getAllComments(pos);

        if (record.getCount() > 0)
        {
            while (record.moveToNext())
            {
                comment = new Comment();

                String a = record.getString(1);
                String b = record.getString(2);
                String c = record.getString(4);
                String d = record.getString(5).substring(0, 10);

                comment.setTitle(a);
                comment.setComment(b);
                comment.setCommenter(c);
                comment.setCreated_at(d);

                commentList.add(comment);
            }
        }

        commentAdapter.notifyDataSetChanged();
    }

    private void addComment(final int taskid){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.add_comment_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final EditText txtName = view.findViewById(R.id.cName);
        final EditText txtTitle = view.findViewById(R.id.cTitle);
        final EditText txtComment = view.findViewById(R.id.cComment);


        builder
                .setCancelable(false)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = txtName.getText().toString();
                        String title = txtTitle.getText().toString();
                        String comments = txtComment.getText().toString();

                        if (name.trim().length() == 0 && title.trim().length() == 0 && comments.trim().length() == 0)
                        {
                            Toast.makeText(CommentActivity.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            comment = new Comment();
                            comment.setTitle(title);
                            comment.setCommenter(name);
                            comment.setComment(comments);
                            comment.setTask_id(taskid);
                            comment.setCreated_at("2018-07-12");

                            SynchronizeData.getInstance(CommentActivity.this).syncCommentOut(comment);
                            //SynchronizeData.getInstance(CommentActivity.this).insertToDatabaseComments(comment);
                            commentAdapter.notifyDataSetChanged();

                            Toast.makeText(CommentActivity.this, "Thank You, "+comment.getCommenter(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sync)
        {
            Toast.makeText(this, "LOADING", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
