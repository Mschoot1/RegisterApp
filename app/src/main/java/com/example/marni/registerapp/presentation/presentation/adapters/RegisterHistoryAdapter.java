package com.example.marni.registerapp.presentation.presentation.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.marni.registerapp.presentation.domain.Register;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Erin :) on 9-5-2017.
 */

public class RegisterHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflator;
    private List mRegisterArrayList;

    public RegisterHistoryAdapter(Context context, LayoutInflater layoutInflator, List<Register> registerArrayList) {
        mContext = context;
        mInflator = layoutInflator;
        mRegisterArrayList = registerArrayList;
    }

    @Override
    public int getCount() {
        return mRegisterArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i("getItem()", "");
        return mRegisterArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {
            view = mInflator.inflate(R.layout.listview_row, null);

            viewHolder = new ViewHolder();
            viewHolder.textViewId = (TextView) view.findViewById(R.id.textViewId);
            viewHolder.textViewTimestamp = (TextView) view.findViewById(R.id.textViewTimestamp);
            viewHolder.textViewTotalprice = (TextView) view.findViewById(R.id.textViewTotal_price);
            viewHolder.textViewCustomerid = (TextView) view.findViewById(R.id.textViewCustomer_id);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Register register = (Register) mRegisterArrayList.get(position);

        DecimalFormat formatter = new DecimalFormat("#0.00");

        String id = "Order: " + register.getOrderId();
        String dateTime = register.getDateTime();
        String price = "€ " + formatter.format(register.getTotalPrice());
        String customerId = "Customer: " + register.getCustomerId();


        viewHolder.textViewId.setText(id);
        viewHolder.textViewTimestamp.setText(dateTime);
        viewHolder.textViewTotalprice.setText(price);
        viewHolder.textViewCustomerid.setText(customerId);

        return view;
    }

    private static class ViewHolder {
        TextView textViewId;
        TextView textViewTimestamp;
        TextView textViewTotalprice;
        TextView textViewCustomerid;
    }
}