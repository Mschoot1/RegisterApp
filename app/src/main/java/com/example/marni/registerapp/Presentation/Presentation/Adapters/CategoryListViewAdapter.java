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
import java.util.ArrayList;

/**
 * Created by Wallaard on 31-5-2017.
 */

public class CategoryListViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Product> categories;

    public CategoryListViewAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Product> products){
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.categories = products;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.category_single_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewCategory = (TextView) convertView.findViewById(R.id.category_textview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewCategory.setText("HALLO");
        return convertView;
    }

    private static class ViewHolder {
        TextView textViewCategory;

    }
}