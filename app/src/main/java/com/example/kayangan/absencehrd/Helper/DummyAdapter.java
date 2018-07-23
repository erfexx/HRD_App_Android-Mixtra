package com.example.kayangan.absencehrd.Helper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kayangan.absencehrd.Model.DummyModel;
import com.example.kayangan.absencehrd.R;

import java.util.List;

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.MyViewHolder> {
    List<DummyModel> mList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_list_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DummyAdapter.MyViewHolder holder, int position) {
        DummyModel dummyModel = mList.get(position);
        holder.item.setText(dummyModel.getItem());
        holder.category.setText(dummyModel.getCategory());
        holder.price.setText("Rp. "+String.valueOf(dummyModel.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView item, category, branch, department, price;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            category = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
        }
    }

    public DummyAdapter(List<DummyModel> mList){
        this.mList = mList;
    }


}
