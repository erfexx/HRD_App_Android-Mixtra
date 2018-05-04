package com.example.kayangan.absencehrd.Activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kayangan.absencehrd.Helper.DummyAdapter;
import com.example.kayangan.absencehrd.Helper.RecyclerTouchListener;
import com.example.kayangan.absencehrd.Model.DummyModel;
import com.example.kayangan.absencehrd.R;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {
    private List<DummyModel> modelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DummyAdapter adapter;
    Spinner spinner, spinner1;

    ArrayAdapter<CharSequence> arrayAdapter;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);

        spinner = findViewById(R.id.sepinner);
        spinner1 = findViewById(R.id.sepinner_satu);

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner1.setAdapter(arrayAdapter);



        adapter = new DummyAdapter(modelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DummyModel movieModel = modelList.get(position);
                Toast.makeText(getApplicationContext(), movieModel.getItem() + " is selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                DummyModel model = modelList.get(position);
                showDesc(model.getItem(), model.getCategory(), model.getPrice());
            }
        }));
        
        recyclerView.setAdapter(adapter);
        prepareListData();
    }

    private void showDesc(String item, String category, String price) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.stock_desc, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        TextView txtTitle = view.findViewById(R.id.txtMovieTitle);
        TextView txtGenre = view.findViewById(R.id.txtMovieGenre);
        TextView txtYear = view.findViewById(R.id.txtMovieYear);

        txtTitle.setText(item);
        txtGenre.setText(category);
        txtYear.setText(price);

        builder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void prepareListData() {
        DummyModel model = new DummyModel("Buku", "ATK", "Alfa", "Echo", "5000");
        modelList.add(model);

        DummyModel model1 = new DummyModel("Cetak", "Print", "Alfa", "Echo", "5000");
        modelList.add(model1);

        adapter.notifyDataSetChanged();
    }
}
