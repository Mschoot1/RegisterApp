package com.example.marni.registerapp.Presentation.Presentation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Wallaard on 9-5-2017.
 */

public class ProductsListViewAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> products;

    public ProductsListViewAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Product> products){
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
            convertView = layoutInflater.inflate(R.layout.activity_order,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.textview_product);
            viewHolder.textViewPrice = (TextView) convertView.findViewById(R.id.textview_price);
            viewHolder.textViewQuantity = (TextView) convertView.findViewById(R.id.textview_quantity);
            viewHolder.textViewSize = (TextView) convertView.findViewById(R.id.textview_size);
            viewHolder.textViewAlcohol = (TextView) convertView.findViewById(R.id.textview_alcohol);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product product = products.get(position);

        DecimalFormat formatter = new DecimalFormat("#0");
        DecimalFormat formatter2 = new DecimalFormat("#0.00");

        Picasso.with(context).load(product.getImagesrc()).into((ImageView) convertView.findViewById(R.id.imageView_productimage));
        viewHolder.textViewName.setText(product.getName());
        viewHolder.textViewPrice.setText("€ "+formatter2.format(product.getPrice()));
        viewHolder.textViewSize.setText(product.getSize()+" ML");
        viewHolder.textViewQuantity.setText(product.getQuantity()+"");
        if(product.getAlcohol_percentage()==0) {
            viewHolder.textViewAlcohol.setText("");
        }else{
            viewHolder.textViewAlcohol.setText(formatter.format(product.getAlcohol_percentage()) + "% Alc.");
        }

        return convertView;

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = layoutInflater.inflate(R.layout.listview_sectionheader_products, parent, false);
            holder.textViewCategoryTitle = (TextView) convertView.findViewById(R.id.listViewOrders_categoryname);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        Product product = products.get(position);

        holder.textViewCategoryTitle.setText(product.getCategoryName());

        return convertView;
    }

    private class HeaderViewHolder {
        TextView textViewCategoryTitle;
    }

    @Override
    public long getHeaderId(int position) {
        return products.get(position).getCategoryId();
    }

    private static class ViewHolder{
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewSize;
        TextView textViewQuantity;
        TextView textViewAlcohol;
    }
    //
}
