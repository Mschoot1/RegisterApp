package com.example.marni.registerapp.presentation.presentation.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marni.registerapp.presentation.domain.Allergy;
import com.example.marni.registerapp.presentation.presentation.fragments.AllergiesFragment;
import com.example.marni.registerapp.R;

import java.util.List;

public class AllergiesFragmentListViewAdapter extends BaseAdapter {

    private final Context context;
    private AllergiesFragment allergiesFragment;
    private LayoutInflater layoutInflater;
    private List<Allergy> allergies;

    public AllergiesFragmentListViewAdapter(Activity activity, AllergiesFragment allergiesFragment, LayoutInflater layoutInflater, List<Allergy> allergies) {
        this.context = activity.getApplicationContext();
        this.allergiesFragment = allergiesFragment;
        this.layoutInflater = layoutInflater;
        this.allergies = allergies;
    }

    @Override
    public int getCount() {
        return allergies.size();
    }

    @Override
    public Object getItem(int position) {
        return allergies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        final Allergy allergy = allergies.get(position);

        if (view == null) {
            view = layoutInflater.inflate(R.layout.allergy_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewCategory = (TextView)view.findViewById(R.id.textView_allergy_description);
            viewHolder.imageViewAllergyIcon = (ImageView) view.findViewById(R.id.imageViewAllergyIcon);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkBoxAllergy);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textViewCategory.setText(allergy.getInformationText());

        String imageName = "@mipmap/" + allergy.getImageurl();
        int imageId = context.getResources().getIdentifier(imageName, null, context.getPackageName());
        viewHolder.imageViewAllergyIcon.setImageResource(imageId);

        viewHolder.checkBox.setChecked(allergy.checked());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allergy.setChecked(!allergy.checked());
            }
        });

        return view;
    }

    private static class ViewHolder {
        TextView textViewCategory;
        ImageView imageViewAllergyIcon;
        CheckBox checkBox;

    }
}