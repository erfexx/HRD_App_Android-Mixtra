package com.example.kayangan.absencehrd.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.DatabaseHandler;
import com.example.kayangan.absencehrd.Helper.OrderAdapter;
import com.example.kayangan.absencehrd.Model.SalesOrder;
import com.example.kayangan.absencehrd.R;

import java.util.ArrayList;

public class SalesOrderActivity extends AppCompatActivity {
    private ListView listView1;
    private ArrayList<SalesOrder> orderList = new ArrayList<>();
    private DatabaseHandler db;
    private OrderAdapter mAdapter;

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

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        listView1 = (ListView) findViewById(R.id.listView1);
        db = new DatabaseHandler(this);
        orderList.addAll(db.getAllOrders());

        mAdapter = new OrderAdapter(orderList,getApplicationContext());
        listView1.setAdapter(mAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addOrderIntent = new Intent(getBaseContext(), AddOrderActivity.class);
                startActivity(addOrderIntent);
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
    }
}
