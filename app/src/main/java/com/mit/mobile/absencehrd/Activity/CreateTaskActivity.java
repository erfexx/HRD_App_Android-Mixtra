package com.mit.mobile.absencehrd.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.mit.mobile.absencehrd.Fragment.TaskManagerFragment;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Model.Task;
import com.mit.mobile.absencehrd.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CreateTaskActivity extends AppCompatActivity implements OnItemSelectedListener{
    DatabaseHandler db;
    Spinner spinner;
    private DatePickerDialog datepick;
    private SimpleDateFormat dateFormatter;
    Task task;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        final int user_id = getIntent().getIntExtra("user", 0);

        final EditText tname = (EditText) findViewById(R.id.dtname);
        final EditText tdesc = (EditText) findViewById(R.id.dtdesc);
        final EditText tduedate = (EditText) findViewById(R.id.dtduedate);
        tduedate.setInputType(InputType.TYPE_NULL);
        spinner = (Spinner) findViewById(R.id.dtassign);
        final TextView tprogress = (TextView) findViewById(R.id.dtprogress);
        tprogress.setText("0");
        final Button bSubmit = (Button) findViewById(R.id.bSubmit);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        tduedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepick.show();
            }
        });
        Calendar newCalendar = Calendar.getInstance();
        datepick = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tduedate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        db = new DatabaseHandler(this);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tassign = getBeforeSpace(spinner.getSelectedItem().toString());
                int progress = Integer.parseInt(tprogress.getText().toString());

                task = new Task();
                task.setTname(tname.getText().toString());
                task.setTdesc(tdesc.getText().toString());
                task.setTduedate(tduedate.getText().toString());
                task.setTassign(Integer.parseInt(tassign));
                task.setTassign_by(user_id);
                task.setTprogress(progress);

                db.addTask(task);

                //SynchronizeData.getInstance(CreateTaskActivity.this).uploadTask(task);

                startActivity(new Intent(CreateTaskActivity.this, TaskManagerFragment.class));
                finish();
                spinner.setAdapter(null);
            }
        });
    }

    private String getBeforeSpace(String kata){
        if (kata.contains(" "))
        {
            kata = kata.substring(0, kata.indexOf(" "));
        }

        return kata;
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, TaskManagerFragment.class);
        startActivity(backIntent);
        finish();
    }
}
