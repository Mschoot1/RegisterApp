package com.example.marni.registerapp.Presentation.Presentation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Domain.Category;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Presentation.Fragments.CategoryFragment;
import com.example.marni.registerapp.R;
import java.util.ArrayList;

/**
 * Created by Wallaard on 31-5-2017.
 */

public class CategoryListViewAdapter extends BaseAdapter {
    private CategoryFragment categoryFragment;
    private LayoutInflater layoutInflater;
    private ArrayList<Category> categories;

    public CategoryListViewAdapter(CategoryFragment categoryFragment, LayoutInflater layoutInflater, ArrayList<Category> categories){
        this.categoryFragment = categoryFragment;
        this.layoutInflater = layoutInflater;
        this.categories = categories;
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

        Category category = categories.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.category_single_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewCategory = (TextView) convertView.findViewById(R.id.category_textview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewCategory.setText(category.getCategoryName());
        return convertView;
    }

    private static class ViewHolder {
        TextView textViewCategory;

    }
}