package com.example.marni.registerapp.Presentation.Presentation.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.CategoriesGetTask;
import com.example.marni.registerapp.Presentation.Domain.Category;
import com.example.marni.registerapp.R;
import java.util.ArrayList;

import com.example.marni.registerapp.Presentation.Presentation.Adapters.CategoriesFragmentListViewAdapter;

public class CategoriesFragment extends DialogFragment implements CategoriesGetTask.OnCategoryAvailable {
    CategoriesFragmentListViewAdapter adapter;
    private ArrayList<Category> categories = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();

    public static final String JWT_STR = "jwt_str";
    String jwt;

    public CategoriesFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CategoriesFragment newInstance() {
        CategoriesFragment frag = new CategoriesFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.categories_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        jwt = prefs.getString(JWT_STR, "");
        getCategories("https://mysql-test-p4.herokuapp.com/product/categories");

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

        adapter = new CategoriesFragmentListViewAdapter(this, activity.getLayoutInflater(), categories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemSelected(categories.get(position).getCategoryId());
                dismiss();
            }
        });

        listView.requestFocus();
    }

    public void getCategories(String apiUrl){
        CategoriesGetTask categoriesGetTask = new CategoriesGetTask(this);
        String[] urls = new String[]{apiUrl, jwt};
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




