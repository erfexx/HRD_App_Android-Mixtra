package com.mit.mobile.absencehrd.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mit.mobile.absencehrd.Fragment.TaskManagerFragment;
import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Model.Task;
import com.mit.mobile.absencehrd.R;

import java.text.SimpleDateFormat;
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
        final int[] f = {0};

        db = new DatabaseHandler(this);

        //dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        final TextView tvSeek = findViewById(R.id.txtSeekbar);
        final EditText tname = findViewById(R.id.dtname);
        final EditText tdesc = findViewById(R.id.dtdesc);
        final EditText tduedate = findViewById(R.id.dtduedate);
        tduedate.setInputType(InputType.TYPE_NULL);

        tname.setEnabled(false);
        tname.setClickable(false);

        tdesc.setEnabled(false);
        tdesc.setClickable(false);

        tduedate.setEnabled(false);
        tduedate.setClickable(false);

        spinner = (Spinner) findViewById(R.id.dtassign);

        SeekBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);

        final Button bSubmit = (Button) findViewById(R.id.bSubmit);

        final Task task = db.getTask(position);
        tname.setText("  "+task.getTname());
        tdesc.setText("  "+task.getTdesc());
        tduedate.setText("  "+task.getTduedate());
        tvSeek.setText("Progress Percentage ("+task.getTprogress()+"%)");

        progressBar.setProgress(task.getTprogress());

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progresschangedvalue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progresschangedvalue = progress;
                tvSeek.setText("Progress Percentage ("+progresschangedvalue+"%)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvSeek.setText("Progress Percentage ("+progresschangedvalue+"%)");
                f[0] = progresschangedvalue;
            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();


        bSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        task.setTprogress(f[0]);
                        db.updateTask(task);
                        //SynchronizeData.getInstance(EditTaskActivity.this).TaskEdit(task, task.get_id());
                        startActivity(new Intent(EditTaskActivity.this, TaskManagerFragment.class));
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
        spinner.setEnabled(false);
        spinner.setClickable(false);
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
        Intent backIntent = new Intent(this,TaskManagerFragment.class);
        startActivity(backIntent);
        finish();
    }
}
