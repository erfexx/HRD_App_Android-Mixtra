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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Model.SalesOrder;
import com.mit.mobile.absencehrd.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AddOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    DatabaseHandler db;
    Spinner spSalesman;
    private DatePickerDialog transdate;
    private SimpleDateFormat dateFormatter;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        final EditText etTransDate = (EditText) findViewById(R.id.etTransDate);
        etTransDate.setInputType(InputType.TYPE_NULL);
        spSalesman = (Spinner) findViewById(R.id.spSalesman);
        final EditText etVoucher = (EditText) findViewById(R.id.etVoucher);
        final Button bSubmit = (Button) findViewById(R.id.bSubmit);

        // Spinner click listener
        spSalesman.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        etTransDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transdate.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();

        transdate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etTransDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        db = new DatabaseHandler(this);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sales = spSalesman.getSelectedItem().toString();
                String voucher = etVoucher.getText().toString();
                int voucherNo = Integer.parseInt(voucher);
                db.addOrder(new SalesOrder(
                        etTransDate.getText().toString(),
                        sales, voucherNo));
                Intent orderIntent = new Intent(getBaseContext(), SalesOrderActivity.class);
                startActivity(orderIntent);
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
        spSalesman.setAdapter(dataAdapter);
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
        Intent backIntent = new Intent(this, SalesOrderActivity.class);
        startActivity(backIntent);
        finish();
    }
}
