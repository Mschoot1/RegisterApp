package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Domain.Register;
import com.example.marni.registerapp.R;
import com.squareup.picasso.Picasso;

public class AddItemActivity extends AppCompatActivity {
    private Context context;

    ///
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Product product;
        product = (Product) getIntent().getExtras().getSerializable("PRODUCT");

        final ImageView iv = (ImageView) findViewById(R.id.image);
        final EditText ed = (EditText) findViewById(R.id.name);
        final EditText ed2 = (EditText) findViewById(R.id.price);
        final EditText ed3 = (EditText) findViewById(R.id.size);
        final EditText ed4 = (EditText) findViewById(R.id.alcohol_percentage);

        TextView textviewproduct = (TextView) findViewById(R.id.textView_productname);
        Picasso.with(context).load(product.getImagesrc()).into(iv);
        ed.setText(product.getName());
        ed2.setText(String.valueOf(product.getPrice()));
        ed3.setText(String.valueOf(product.getSize()));
        ed4.setText(String.valueOf(product.getAlcohol_percentage()));

        textviewproduct.setText(product.getName());


        Button button = (Button) findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                String ed_text = ed.getText().toString().trim();
                String ed2_text = ed2.getText().toString().trim();
                String ed3_text = ed3.getText().toString().trim();
                String ed4_text = ed4.getText().toString().trim();

                if (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null) {
                    ed.setError("Enter name");
                }

                if (ed2_text.isEmpty() || ed2_text.length() == 0 || ed2_text.equals("") || ed2_text == null) {
                    ed2.setError("Enter price");
                }
                if (ed3_text.isEmpty() || ed3_text.length() == 0 || ed3_text.equals("") || ed3_text == null) {
                    ed3.setError("Enter size");
                }

                if (ed4_text.isEmpty() || ed4_text.length() == 0 || ed4_text.equals("") || ed4_text == null) {
                    ed4.setError("Enter alcohol percentage");
                }
            }
        });
    }
}