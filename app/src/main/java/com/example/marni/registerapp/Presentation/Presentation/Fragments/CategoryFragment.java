package com.example.marni.registerapp.Presentation.Presentation.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.marni.registerapp.R;

/**
 * Created by Wallaard on 31-5-2017.
 */

public class CategoryFragment extends DialogFragment {
    private TextView testview;

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

        testview = (TextView) view.findViewById(R.id.testview);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        testview.requestFocus();

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


}




