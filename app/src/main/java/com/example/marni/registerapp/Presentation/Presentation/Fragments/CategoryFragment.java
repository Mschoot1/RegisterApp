package com.example.marni.registerapp.Presentation.Presentation.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class CategoryFragment extends DialogFragment implements AdapterView.OnItemClickListener, CategoriesGetTask.OnCategoryAvailable {
    ListView listviewcategories;
    CategoryListViewAdapter categoryListViewAdapter;
    private ArrayList<Category> categoriesArrayList = new ArrayList<>();

    public CategoryFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CategoryFragment newInstance(String title) {
        CategoryFragment frag = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
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

        Bundle bundle = savedInstanceState;

        Activity activity = getActivity();

        listviewcategories = (ListView) view.findViewById(R.id.category_listview);

        categoryListViewAdapter = new CategoryListViewAdapter(activity.getApplicationContext(),activity.getLayoutInflater(),categoriesArrayList);
        listviewcategories.setAdapter(categoryListViewAdapter);
        listviewcategories.setOnItemClickListener(this);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        listviewcategories.requestFocus();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public void getCategory(){
        CategoriesGetTask categoriesGetTask = new CategoriesGetTask(this);
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/product/categories"};
        categoriesGetTask.execute(urls);

    }

    @Override
    public void OnCategoryAvailable(Category category) {
        categoriesArrayList.add(category);
        categoryListViewAdapter.notifyDataSetChanged();
    }
}




