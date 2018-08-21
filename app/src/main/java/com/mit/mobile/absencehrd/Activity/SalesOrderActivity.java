package com.mit.mobile.absencehrd.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Helper.OrderAdapter;
import com.mit.mobile.absencehrd.Model.SalesOrder;
import com.mit.mobile.absencehrd.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SalesOrderActivity extends AppCompatActivity {
    private ListView listView1;
    private ArrayList<SalesOrder> orderList = new ArrayList<>();
    private ArrayList<SalesOrder> orderSearch = new ArrayList<>();
    private DatabaseHandler db;
    private OrderAdapter mAdapter, sAdapter;
    private DatePickerDialog startdate, enddate;
    private SimpleDateFormat dateFormatter;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        final EditText etStartDate = (EditText) findViewById(R.id.etStartDate);
        final EditText etEndDate = (EditText) findViewById(R.id.etEndDate);
        etStartDate.setInputType(InputType.TYPE_NULL);
        etEndDate.setInputType(InputType.TYPE_NULL);
        final ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startdate.show();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enddate.show();
            }
        });
        Calendar newCalendar = Calendar.getInstance();
        startdate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etStartDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        enddate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etEndDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        listView1 = (ListView) findViewById(R.id.listView1);
        db = new DatabaseHandler(this);
        orderList.addAll(db.getAllOrders());

        mAdapter = new OrderAdapter(orderList,getApplicationContext());
        listView1.setAdapter(mAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etStartDate.getText().toString() == null && etEndDate.getText().toString() == null)
                {
                    Toast.makeText(getBaseContext(), "Please enter the date", Toast.LENGTH_SHORT).show();
                }
                else if(etStartDate.getText().toString() != null && etEndDate.getText().toString() != null)
                {
                    sAdapter = new OrderAdapter(orderSearch,getApplicationContext());
                    sAdapter.clear();
                    sAdapter.notifyDataSetChanged();
                    orderSearch.addAll(db.getSearchOrders(etStartDate.getText().toString(), etEndDate.getText().toString()));
                    listView1.setAdapter(sAdapter);
                    Toast.makeText(getBaseContext(), "Start Date:" + etStartDate.getText().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(getBaseContext(), AddOrderActivity.class);
                startActivity(createIntent);
                finish();
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final SalesOrder order = orderList.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(SalesOrderActivity.this);
                alert.setTitle("Options");
                alert.setMessage("What do you want to do?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteOrder(orderList.get(position));
                        // removing the note from the list
                        orderList.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "Order Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, MenuActivity.class);
        startActivity(backIntent);
        finish();
    }
}
