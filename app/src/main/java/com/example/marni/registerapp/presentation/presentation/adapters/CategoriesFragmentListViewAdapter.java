package com.example.marni.registerapp.presentation.presentation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.marni.registerapp.presentation.domain.Category;
import com.example.marni.registerapp.presentation.presentation.fragments.CategoriesFragment;
import com.example.marni.registerapp.R;
import java.util.List;

public class CategoriesFragmentListViewAdapter extends BaseAdapter {

    private CategoriesFragment categoriesFragment;
    private LayoutInflater layoutInflater;
    private List<Category> categories;

    public CategoriesFragmentListViewAdapter(CategoriesFragment categoriesFragment, LayoutInflater layoutInflater, List<Category> categories){
        this.categoriesFragment = categoriesFragment;
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
        View view = convertView;
        final ViewHolder viewHolder;

        Category category = categories.get(position);

        if (view == null) {
            view = layoutInflater.inflate(R.layout.category_single_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewCategory = (TextView) view.findViewById(R.id.category_textview);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textViewCategory.setText(category.getCategoryName());
        return view;
    }

    private static class ViewHolder {
        TextView textViewCategory;

    }
}