package com.example.marni.registerapp.Presentation.Presentation.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Order;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

/**
 * Created by Wilco on 9-5-2017.
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
            viewHolder.textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatus);
            viewHolder.textViewCustomer_id = (TextView) convertView.findViewById(R.id.textViewCustomer_id);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Order order = (Order) mOrderArrayList.get(position);


        viewHolder.textViewId.setText(order.getId()+"");
        viewHolder.textViewTimestamp.setText(order.getTimestamp());
        viewHolder.textViewTotal_price.setText(order.getPrice_total()+"");
        viewHolder.textViewStatus.setText(order.getStatus());
        viewHolder.textViewCustomer_id.setText(order.getCustomer_id()+"");

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