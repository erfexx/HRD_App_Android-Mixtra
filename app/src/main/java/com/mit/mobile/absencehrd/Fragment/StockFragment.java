package com.mit.mobile.absencehrd.Fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mit.mobile.absencehrd.Helper.DatabaseHandler;
import com.mit.mobile.absencehrd.Helper.DummyAdapter;
import com.mit.mobile.absencehrd.Helper.RecyclerTouchListener;
import com.mit.mobile.absencehrd.Model.DummyModel;
import com.mit.mobile.absencehrd.R;

import java.util.ArrayList;
import java.util.List;

public class StockFragment extends Fragment {
    private List<DummyModel> modelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DummyAdapter adapter;

    Spinner spinner, spinner1;
    Button btnFilter;

    ArrayAdapter<CharSequence> arrayAdapter;

    DummyModel model;

    DatabaseHandler handler;


    public StockFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_stock, container, false);

        handler = new DatabaseHandler(getActivity());

        recyclerView = view.findViewById(R.id.recycler_view);

        spinner = view.findViewById(R.id.sepinner);
        spinner1 = view.findViewById(R.id.sepinner_satu);

        btnFilter = view.findViewById(R.id.btnFilter);


        arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.planets_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner1.setAdapter(arrayAdapter);



        adapter = new DummyAdapter(modelList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //DummyModel movieModel = modelList.get(position);
                //Toast.makeText(getApplicationContext(), movieModel.getItem() + " is selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                DummyModel model = modelList.get(position);
                showDesc(model.getItem(), model.getCategory(), model.getPrice(), model.getBranch(), model.getDepartment());
            }
        }));

        prepareListData();
        recyclerView.setAdapter(adapter);

        btnFilter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //show all data
                        if (spinner.getSelectedItem().toString().equals("--") && spinner1.getSelectedItem().toString().equals("--")) {
                            Toast.makeText(getActivity(), "Select Branch", Toast.LENGTH_SHORT).show();
                            modelList.clear();
                            prepareListData();

                            recyclerView.setAdapter(adapter);
                        }
                        else {
                            if (!spinner.getSelectedItem().toString().equals("--") && spinner1.getSelectedItem().toString().equals("--")){
                                //filter branch only
                                modelList.clear();
                                filterDataBranch(spinner.getSelectedItem().toString());
                            }
                            else if(spinner.getSelectedItem().toString().equals("--") && !spinner1.getSelectedItem().toString().equals("--")){
                                //filter department only
                                modelList.clear();
                                filterDataDepartment(spinner1.getSelectedItem().toString());
                            }
                            else{
                                //filter branch and department
                                modelList.clear();
                                filterData(spinner.getSelectedItem().toString(), spinner1.getSelectedItem().toString());
                            }
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }
        );

        return view;
    }




    private void showDesc(String item, String category, int price, String branch, String department) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.stock_desc, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        TextView txtItem = view.findViewById(R.id.txtItem);
        TextView txtCategory = view.findViewById(R.id.txtCategory);
        TextView txtPrice = view.findViewById(R.id.txtPrice);
        TextView txtBranch = view.findViewById(R.id.txtBranch);
        TextView txtDepartment = view.findViewById(R.id.txtDepartment);

        txtItem.setText(item);
        txtCategory.setText("Category: "+category);
        txtPrice.setText("Price: Rp. "+String.valueOf(price));
        txtBranch.setText("Branch: "+branch);
        txtDepartment.setText("Department: "+department);

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
        Cursor record = handler.getAllStocks();

        if (record.getCount() > 0)
        {
            while (record.moveToNext())
            {
                model = new DummyModel();

                String item = record.getString(record.getColumnIndexOrThrow("item"));
                String category = record.getString(record.getColumnIndexOrThrow("category"));
                String branch = record.getString(record.getColumnIndexOrThrow("branch"));
                String department = record.getString(record.getColumnIndexOrThrow("department"));
                int price = record.getInt(record.getColumnIndexOrThrow("price"));

                model.setItem(item);
                model.setBranch(branch);
                model.setCategory(category);
                model.setDepartment(department);
                model.setPrice(price);

                modelList.add(model);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void filterData(String branchs, String departments) {
        Cursor record = handler.getSpecStocksAll(branchs, departments);

        if (record.getCount() > 0)
        {
            while (record.moveToNext())
            {
                model = new DummyModel();

                String item = record.getString(record.getColumnIndexOrThrow("item"));
                String category = record.getString(record.getColumnIndexOrThrow("category"));
                String branch = record.getString(record.getColumnIndexOrThrow("branch"));
                String department = record.getString(record.getColumnIndexOrThrow("department"));
                int price = record.getInt(record.getColumnIndexOrThrow("price"));

                model.setItem(item);
                model.setBranch(branch);
                model.setCategory(category);
                model.setDepartment(department);
                model.setPrice(price);

                modelList.add(model);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void filterDataDepartment(String departments) {
        Cursor record = handler.getSpecStockDepartment(departments);

        if (record.getCount() > 0)
        {
            while (record.moveToNext())
            {
                model = new DummyModel();

                String item = record.getString(record.getColumnIndexOrThrow("item"));
                String category = record.getString(record.getColumnIndexOrThrow("category"));
                String branch = record.getString(record.getColumnIndexOrThrow("branch"));
                String department = record.getString(record.getColumnIndexOrThrow("department"));
                int price = record.getInt(record.getColumnIndexOrThrow("price"));

                model.setItem(item);
                model.setBranch(branch);
                model.setCategory(category);
                model.setDepartment(department);
                model.setPrice(price);

                modelList.add(model);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void filterDataBranch(String branchs) {
        Cursor record = handler.getSpecStockBranch(branchs);

        if (record.getCount() > 0)
        {
            while (record.moveToNext())
            {
                model = new DummyModel();

                String item = record.getString(record.getColumnIndexOrThrow("item"));
                String category = record.getString(record.getColumnIndexOrThrow("category"));
                String branch = record.getString(record.getColumnIndexOrThrow("branch"));
                String department = record.getString(record.getColumnIndexOrThrow("department"));
                int price = record.getInt(record.getColumnIndexOrThrow("price"));

                model.setItem(item);
                model.setBranch(branch);
                model.setCategory(category);
                model.setDepartment(department);
                model.setPrice(price);

                modelList.add(model);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
