package com.example.marni.registerapp.Presentation.Presentation.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.CategoriesGetTask;
import com.example.marni.registerapp.Presentation.Domain.Category;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.R;
import java.util.ArrayList;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AssortmentGetTask;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.CategoryListViewAdapter;
import com.example.marni.registerapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wallaard on 31-5-2017.
 */

public class CategoryFragment extends DialogFragment implements CategoriesGetTask.OnCategoryAvailable {
    CategoryListViewAdapter adapter;
    private ArrayList<Category> categories = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();

    public CategoryFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CategoryFragment newInstance() {
        CategoryFragment frag = new CategoryFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_selection, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCategory();


        Activity activity = getActivity();
        final OnItemSelected listener = (OnItemSelected) activity;

        ImageView iv = (ImageView) view.findViewById(R.id.imageView_cancelbuttoncategories);
        ListView listView = (ListView) view.findViewById(R.id.category_listview);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "GELUKT");
                dismiss();
            }
        });

        adapter = new CategoryListViewAdapter(this, activity.getLayoutInflater(), categories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemSelected(categories.get(position).getCategoryId());
                dismiss();
            }
        });

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        listView.requestFocus();
    }

    public void getCategory(){
        CategoriesGetTask categoriesGetTask = new CategoriesGetTask(this);
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/product/categories"};
        categoriesGetTask.execute(urls);
    }

    @Override
    public void onCategoryAvailable(Category category) {
        categories.add(category);
        adapter.notifyDataSetChanged();
    }

    public interface OnItemSelected {
        void onItemSelected(int i);
    }
}




