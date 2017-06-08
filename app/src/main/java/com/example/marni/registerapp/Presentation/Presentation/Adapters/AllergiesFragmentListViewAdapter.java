package com.example.marni.registerapp.Presentation.Presentation.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Domain.Allergy;
import com.example.marni.registerapp.Presentation.Presentation.Fragments.AllergiesFragment;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

public class AllergiesFragmentListViewAdapter extends BaseAdapter {

    private final Context context;
    private AllergiesFragment allergiesFragment;
    private LayoutInflater layoutInflater;
    private ArrayList<Allergy> allergies;

    public AllergiesFragmentListViewAdapter(Activity activity, AllergiesFragment allergiesFragment, LayoutInflater layoutInflater, ArrayList<Allergy> allergies) {
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
        final ViewHolder viewHolder;

        final Allergy allergy = allergies.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.allergy_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewCategory = (TextView) convertView.findViewById(R.id.textView_allergy_description);
            viewHolder.imageViewAllergyIcon = (ImageView) convertView.findViewById(R.id.imageViewAllergyIcon);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBoxAllergy);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewCategory.setText(allergy.getInformationText());

        String imageName = "@mipmap/" + String.valueOf(allergy.getImage_url());
        int imageId = context.getResources().getIdentifier(imageName, null, context.getPackageName());
        viewHolder.imageViewAllergyIcon.setImageResource(imageId);

        viewHolder.checkBox.setChecked(allergy.checked());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allergy.setChecked(!allergy.checked());
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewCategory;
        ImageView imageViewAllergyIcon;
        CheckBox checkBox;

    }
}