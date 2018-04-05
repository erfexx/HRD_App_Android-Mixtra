package com.example.kayangan.absencehrd.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kayangan.absencehrd.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.R;

public class CreateTaskActivity extends AppCompatActivity {
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        final EditText tname = (EditText) findViewById(R.id.tname);
        final EditText tdesc = (EditText) findViewById(R.id.tdesc);
        final EditText tduedate = (EditText) findViewById(R.id.tduedate);
        final EditText tassign = (EditText) findViewById(R.id.tassign);
        final Button bSubmit = (Button) findViewById(R.id.bSubmit);

        db = new DatabaseHandler(this);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.addTask(new Task(
                        tname.getText().toString(),
                        tdesc.getText().toString(),
                        tduedate.getText().toString(),
                        tassign.getText().toString()));
                Intent createIntent = new Intent(CreateTaskActivity.this, MainActivity.class);
                startActivity(createIntent);

            }
        });

    }
}
