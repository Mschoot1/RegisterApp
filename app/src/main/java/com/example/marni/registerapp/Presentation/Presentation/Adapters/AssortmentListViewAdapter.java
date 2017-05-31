package com.example.marni.registerapp.Presentation.Presentation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Wallaard on 30-5-2017.
 */

public class AssortmentListViewAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> products;

    public AssortmentListViewAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Product> products){
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder viewHolder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.assortment_single_item,parent,false);

            viewHolder = new ViewHolder();
//            viewHolder.spinnerCategory = (Spinner) convertView.findViewById(R.id.assortment_category_spinner);
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.assortment_product);
            viewHolder.textViewPrice = (TextView) convertView.findViewById(R.id.assortment_price);
            viewHolder.textViewSize = (TextView) convertView.findViewById(R.id.assortment_size);
            viewHolder.textViewAlcohol = (TextView) convertView.findViewById(R.id.assortment_alcohol);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product product = products.get(position);

        DecimalFormat formatter = new DecimalFormat("#0");
        DecimalFormat formatter2 = new DecimalFormat("#0.00");

        viewHolder.textViewName.setText(product.getName());
        viewHolder.textViewPrice.setText("€ "+formatter2.format(product.getPrice()));
        viewHolder.textViewSize.setText(product.getSize()+" ML");

        if(product.getAlcohol_percentage()==0) {
            viewHolder.textViewAlcohol.setText("");
        }else{
            viewHolder.textViewAlcohol.setText(formatter.format(product.getAlcohol_percentage()) + "% Alc.");
        }

        return convertView;

    }

    private static class ViewHolder{
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewSize;
        TextView textViewQuantity;
        TextView textViewAlcohol;
        Spinner spinnerCategory;
    }
    //
}