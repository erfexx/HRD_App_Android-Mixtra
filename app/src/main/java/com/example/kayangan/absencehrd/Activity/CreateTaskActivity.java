package com.example.kayangan.absencehrd.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kayangan.absencehrd.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.Model.User;
import com.example.kayangan.absencehrd.R;

import java.util.List;

public class CreateTaskActivity extends AppCompatActivity implements OnItemSelectedListener {
    DatabaseHandler db;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        final EditText tname = (EditText) findViewById(R.id.dtname);
        final EditText tdesc = (EditText) findViewById(R.id.dtdesc);
        final EditText tduedate = (EditText) findViewById(R.id.dtduedate);
        spinner = (Spinner) findViewById(R.id.dtassign);
        final Button bSubmit = (Button) findViewById(R.id.bSubmit);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        db = new DatabaseHandler(this);
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tassign = spinner.getSelectedItem().toString();
                db.addTask(new Task(
                        tname.getText().toString(),
                        tdesc.getText().toString(),
                        tduedate.getText().toString(),
                        tassign));
                Intent createIntent = new Intent(CreateTaskActivity.this, MainActivity.class);
                startActivity(createIntent);

            }
        });
    }
    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        // Spinner Drop down elements
        List<String> names = db.getNames();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, names);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        

    }
}
