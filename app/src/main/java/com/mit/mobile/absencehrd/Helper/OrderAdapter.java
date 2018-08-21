package com.mit.mobile.absencehrd.Helper;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mit.mobile.absencehrd.Model.SalesOrder;
import com.mit.mobile.absencehrd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin E on 4/26/2018.
 */

public class OrderAdapter extends ArrayAdapter<SalesOrder>{
    private List<SalesOrder> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView voucher;
        TextView salesman;
    }

    public OrderAdapter(ArrayList<SalesOrder> data, Context context) {
        super(context, R.layout.order_list_row, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SalesOrder order = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.order_list_row, parent, false);
            viewHolder.voucher = (TextView) convertView.findViewById(R.id.voucher);
            viewHolder.salesman = (TextView) convertView.findViewById(R.id.salesman);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        viewHolder.voucher.setText(String.valueOf(order.getVoucher()));
        viewHolder.salesman.setText(order.getSalesman());
        // Return the completed view to render on screen
        return convertView;
    }
}
