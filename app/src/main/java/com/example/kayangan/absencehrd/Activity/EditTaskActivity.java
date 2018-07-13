package com.example.kayangan.absencehrd.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Model.Task;
import com.example.kayangan.absencehrd.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    DatabaseHandler db;
    Spinner spinner;
    private DatePickerDialog datepick;
    private SimpleDateFormat dateFormatter;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int position = getIntent().getIntExtra("pos",0);

        db = new DatabaseHandler(this);

        //dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        final TextView tname = (TextView) findViewById(R.id.dtname);
        final TextView tdesc = (TextView) findViewById(R.id.dtdesc);
        final TextView tduedate = (TextView) findViewById(R.id.dtduedate);
        tduedate.setInputType(InputType.TYPE_NULL);
        spinner = (Spinner) findViewById(R.id.dtassign);
        final TextView tprogress = (TextView) findViewById(R.id.dtprogress);
        SeekBar progressBar = (SeekBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        final Button bSubmit = (Button) findViewById(R.id.bSubmit);

        final Task task = db.getTask(position);
        tname.setText(task.getTname());
        tdesc.setText(task.getTdesc());
        tduedate.setText(task.getTduedate());
        String sprogress = String.valueOf(task.getTprogress());
        tprogress.setText(sprogress);
        progressBar.setProgress(task.getTprogress());

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String value = String.valueOf(progress);
                tprogress.setText(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        /*tduedate.setOnClickListener(new View.OnClickListener() {
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
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));*/

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tassign = spinner.getSelectedItem().toString();
                String sProgress = tprogress.getText().toString();
                int progress = Integer.parseInt(sProgress);
                task.setTname(tname.getText().toString());
                task.setTdesc(tdesc.getText().toString());
                task.setTduedate(tduedate.getText().toString());
                task.setTassign(tassign);
                task.setTprogress(progress);
                db.updateTask(task);
                Intent viewIntent = new Intent(getBaseContext(),TaskManagerActivity.class);
                startActivity(viewIntent);
                finish();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this,TaskManagerActivity.class);
        startActivity(backIntent);
        finish();
    }
}
