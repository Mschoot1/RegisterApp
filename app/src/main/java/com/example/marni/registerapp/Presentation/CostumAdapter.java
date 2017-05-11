package com.example.marni.registerapp.Presentation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
            viewHolder.textViewID = (TextView) convertView.findViewById(R.id.textViewID);
            viewHolder.textViewDateTime = (TextView) convertView.findViewById(R.id.textViewDateTime);
            viewHolder.textViewTotalPrice = (TextView) convertView.findViewById(R.id.textViewTotalPrice);
            viewHolder.textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatus);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Order order = (Order) mOrderArrayList.get(position);

        viewHolder.textViewID.setText(order.getId()+"");
        viewHolder.textViewDateTime.setText(order.getDate());
        viewHolder.textViewTotalPrice.setText(order.getPrice()+"");
        viewHolder.textViewStatus.setText(order.getStatus());

        return convertView;
    }

    private static class ViewHolder{
        public TextView textViewID;
        public TextView textViewDateTime;
        public TextView textViewTotalPrice;
        public TextView textViewStatus;
    }
}