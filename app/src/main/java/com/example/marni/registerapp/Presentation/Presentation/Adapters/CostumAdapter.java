package com.example.marni.registerapp.Presentation.Presentation.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Domain.Order;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Erin :) on 9-5-2017.
 */

public class CostumAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater mInflator;
    ArrayList mOrderArrayList;

    public CostumAdapter(Context context, LayoutInflater layoutInflator, ArrayList<Order> orderArrayList) {
        mContext = context;
        mInflator = layoutInflator;
        mOrderArrayList = orderArrayList;
    }

    @Override
    public int getCount(){
        int size = mOrderArrayList.size();
        Log.i("getCount()","=" + size);
        return size;
    }

    @Override
    public Object getItem(int position) {
        Log.i("getItem()","");
        return mOrderArrayList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflator.inflate(R.layout.listview_row, null);


            viewHolder = new ViewHolder();
            viewHolder.textViewId = (TextView) convertView.findViewById(R.id.textViewId);
            viewHolder.textViewTimestamp = (TextView) convertView.findViewById(R.id.textViewTimestamp);
            viewHolder.textViewTotal_price = (TextView) convertView.findViewById(R.id.textViewTotal_price);
            //viewHolder.textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatus);
            viewHolder.textViewCustomer_id = (TextView) convertView.findViewById(R.id.textViewCustomer_id);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Order order = (Order) mOrderArrayList.get(position);

        DecimalFormat formatter = new DecimalFormat("#0.00");

        viewHolder.textViewId.setText("Order: "+order.getOrderId());
        viewHolder.textViewTimestamp.setText(order.getDateTime());
        viewHolder.textViewTotal_price.setText("€ "+formatter.format(order.getTotalPrice()));

        //
//        if(order.getStatus() == 1) {
//            viewHolder.textViewStatus.setText("Paid");
//        } else {
//            viewHolder.textViewStatus.setText("Open");
//        }
        viewHolder.textViewCustomer_id.setText("Customer: "+order.getCustomerid());

        return convertView;
    }

    private static class ViewHolder{
        public TextView textViewId;
        public TextView textViewStatus;
        public TextView textViewTimestamp;
        public TextView textViewTotal_price;
        public TextView textViewCustomer_id;
    }
}