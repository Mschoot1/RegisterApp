package com.example.marni.registerapp.Presentation.Presentation.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Domain.Register;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Erin :) on 9-5-2017.
 */

public class RegisterHistoryAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater mInflator;
    ArrayList mRegisterArrayList;

    public RegisterHistoryAdapter(Context context, LayoutInflater layoutInflator, ArrayList<Register> registerArrayList) {
        mContext = context;
        mInflator = layoutInflator;
        mRegisterArrayList = registerArrayList;
    }

    @Override
    public int getCount(){
        int size = mRegisterArrayList.size();
        Log.i("getCount()","=" + size);
        return size;
    }

    @Override
    public Object getItem(int position) {
        Log.i("getItem()","");
        return mRegisterArrayList.get(position);
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
            viewHolder.textViewCustomer_id = (TextView) convertView.findViewById(R.id.textViewCustomer_id);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Register register = (Register) mRegisterArrayList.get(position);

        DecimalFormat formatter = new DecimalFormat("#0.00");

        viewHolder.textViewId.setText("Order: "+register.getOrderId());
        viewHolder.textViewTimestamp.setText(register.getDateTime());
        viewHolder.textViewTotal_price.setText("€ "+formatter.format(register.getTotalPrice()));
        viewHolder.textViewCustomer_id.setText("Customer: "+register.getCustomerId());

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