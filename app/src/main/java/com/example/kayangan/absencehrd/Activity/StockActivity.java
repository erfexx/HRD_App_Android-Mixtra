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
                Toast.makeText(getApplicationContext(), movieModel.getTitle() + " is selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                DummyModel model = modelList.get(position);
                showDesc(model.getTitle(), model.getGenre(), model.getYear());
            }
        }));

        recyclerView.setAdapter(adapter);
        prepareListData();
    }

    private void showDesc(String title, String genre, String year) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.stock_desc, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        TextView txtTitle = view.findViewById(R.id.txtMovieTitle);
        TextView txtGenre = view.findViewById(R.id.txtMovieGenre);
        TextView txtYear = view.findViewById(R.id.txtMovieYear);

        txtTitle.setText(title);
        txtGenre.setText(genre);
        txtYear.setText(year);

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
        DummyModel movie = new DummyModel("Mad Max: Fury Road", "Action & Adventure", "2015");
        modelList.add(movie);

        movie = new DummyModel("Inside Out", "Animation, Kids & Family", "2015");
        modelList.add(movie);

        movie = new DummyModel("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
        modelList.add(movie);

        movie = new DummyModel("Shaun the Sheep", "Animation", "2015");
        modelList.add(movie);

        movie = new DummyModel("The Martian", "Science Fiction & Fantasy", "2015");
        modelList.add(movie);

        movie = new DummyModel("Mission: Impossible Rogue Nation", "Action", "2015");
        modelList.add(movie);

        movie = new DummyModel("Up", "Animation", "2009");
        modelList.add(movie);

        movie = new DummyModel("Star Trek", "Science Fiction", "2009");
        modelList.add(movie);

        movie = new DummyModel("The LEGO Movie", "Animation", "2014");
        modelList.add(movie);

        movie = new DummyModel("Iron Man", "Action & Adventure", "2008");
        modelList.add(movie);

        movie = new DummyModel("Aliens", "Science Fiction", "1986");
        modelList.add(movie);

        movie = new DummyModel("Chicken Run", "Animation", "2000");
        modelList.add(movie);

        movie = new DummyModel("Back to the Future", "Science Fiction", "1985");
        modelList.add(movie);

        movie = new DummyModel("Raiders of the Lost Ark", "Action & Adventure", "1981");
        modelList.add(movie);

        movie = new DummyModel("Goldfinger", "Action & Adventure", "1965");
        modelList.add(movie);

        movie = new DummyModel("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        modelList.add(movie);

        adapter.notifyDataSetChanged();
    }
}
