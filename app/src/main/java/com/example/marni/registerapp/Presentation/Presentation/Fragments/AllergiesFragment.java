package com.example.marni.registerapp.Presentation.Presentation.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AllergiesGetTask;
import com.example.marni.registerapp.Presentation.Domain.Allergy;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.AllergiesFragmentListViewAdapter;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

public class AllergiesFragment extends DialogFragment implements AllergiesGetTask.OnAllergyAvailable, View.OnClickListener {

    private static ArrayList<Allergy> aa;

    private AllergiesFragmentListViewAdapter adapter;
    private ArrayList<Allergy> allergies = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();

    public AllergiesFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AllergiesFragment newInstance(ArrayList<Allergy> allergies) {
        AllergiesFragment frag = new AllergiesFragment();
        Bundle args = new Bundle();
        aa = allergies;
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.allergies_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllergies("https://mysql-test-p4.herokuapp.com/product/allergies");

        Activity activity = getActivity();
        final OnItemsSelected listener = (OnItemsSelected) activity;

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_cancelbutton);
        Button buttonCancel = (Button) view.findViewById(R.id.fragmentAllergiesCancel);
        Button buttonContinue = (Button) view.findViewById(R.id.fragmentAllergiesContinue);
        ListView listView = (ListView) view.findViewById(R.id.allergies_list_view);

        imageView.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Allergy> aa = new ArrayList<>();
                for (Allergy a : allergies) {
                    if (a.checked()) {
                        aa.add(a);
                    }
                }
                listener.onItemsSelected(aa);
                dismiss();
            }
        });

        adapter = new AllergiesFragmentListViewAdapter(getActivity(), this, activity.getLayoutInflater(), allergies);
        listView.setAdapter(adapter);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        listView.requestFocus();
    }

    public void getAllergies(String apiUrl) {
        AllergiesGetTask task = new AllergiesGetTask(this);
        String[] urls = new String[]{apiUrl};
        task.execute(urls);
    }

    @Override
    public void onAllergyAvailable(Allergy allergy) {
        allergies.add(allergy);
        setCurrentAllergiesChecked();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "GELUKT");
        dismiss();
    }

    public interface OnItemsSelected {
        void onItemsSelected(ArrayList<Allergy> allergies);
    }

    private void setCurrentAllergiesChecked() {
        for (Allergy a : aa) {
            for (Allergy b : allergies) {
                if (a.getInformationText().equals(b.getInformationText())) {
                    b.setChecked(true);
                }
            }
        }
    }
}




