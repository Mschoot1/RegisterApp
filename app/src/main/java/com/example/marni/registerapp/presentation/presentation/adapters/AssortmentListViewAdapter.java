package com.example.marni.registerapp.presentation.presentation.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marni.registerapp.presentation.domain.Product;
import com.example.marni.registerapp.presentation.presentation.fragments.CategoriesFragment;
import com.example.marni.registerapp.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Wallaard on 30-5-2017.
 */

public class AssortmentListViewAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private final String tag = getClass().getSimpleName();

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Product> products;
    private Activity activity;

    public AssortmentListViewAdapter(Activity activity,Context context, LayoutInflater layoutInflater, List<Product> products){
        this.activity = activity;
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
        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null){
            view = layoutInflater.inflate(R.layout.assortment_single_item,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) view.findViewById(R.id.assortment_product);
            viewHolder.textViewPrice = (TextView) view.findViewById(R.id.assortment_price);
            viewHolder.textViewSize = (TextView) view.findViewById(R.id.assortment_size);
            viewHolder.textViewAlcohol = (TextView) view.findViewById(R.id.assortment_alcohol);

            view.setTag(viewHolder);
        } else {
            Log.i(tag, "ViewHolder meegekregen. Position: " + position);
            viewHolder = (ViewHolder) view.getTag();
        }

        Product product = products.get(position);

        DecimalFormat formatter = new DecimalFormat("#0");
        DecimalFormat formatter2 = new DecimalFormat("#0.00");

        if(!product.getImagesrc().equals("")){
            Picasso.with(context).load(product.getImagesrc()).into((ImageView)view.findViewById(R.id.imageView_assortmentimage));
        }

        viewHolder.textViewName.setText(product.getName());
        viewHolder.textViewPrice.setText("€ " +formatter2.format(product.getPrice()));
        viewHolder.textViewSize.setText(product.getSize()+" ML");

        if (Double.compare(product.getAlcoholpercentage(), 0.0) == 1) {
            viewHolder.textViewAlcohol.setText("");
        }else{
            viewHolder.textViewAlcohol.setText(formatter.format(product.getAlcoholpercentage()) + "% Alc.");
        }

        return view;

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        HeaderViewHolder holder;

        if (view == null) {
            holder = new HeaderViewHolder();

            view = layoutInflater.inflate(R.layout.listview_sectionheader_products, parent, false);
            holder.textViewCategoryTitle = (TextView) view.findViewById(R.id.listViewOrders_categoryname);
            holder.imageView = (ImageView) view.findViewById(R.id.imageView_filter);
            view.setTag(holder);
        } else {
            holder = (HeaderViewHolder) view.getTag();
        }
        final Product product = products.get(position);
        holder.imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
        holder.textViewCategoryTitle.setText(product.getCategoryName());

        return view;
    }

    private class HeaderViewHolder {
        TextView textViewCategoryTitle;
        ImageView imageView;
    }

    @Override
    public long getHeaderId(int position) {
        return products.get(position).getCategoryId();
    }

    private static class ViewHolder {
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewSize;
        TextView textViewAlcohol;
        ImageView imageViewProduct;
    }

    public void showEditDialog() {
        FragmentManager fm = activity.getFragmentManager();
        CategoriesFragment alertDialog = CategoriesFragment.newInstance();
        alertDialog.show(fm, "fragment_alert");
    }
}