package com.example.marni.registerapp.Presentation;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.marni.registerapp.R;

public class AddItemActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        EditText ed = (EditText) findViewById(R.id.name);
        EditText ed2 = (EditText) findViewById(R.id.price);
        EditText ed3 = (EditText) findViewById(R.id.size);
        EditText ed4 = (EditText) findViewById(R.id.alcohol_percentage);

        String ed_text = ed.getText().toString().trim();
        String ed2_text = ed2.getText().toString().trim();
        String ed3_text = ed3.getText().toString().trim();
        String ed4_text = ed4.getText().toString().trim();

        if(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
        {
            ed.setError("Enter name");
        }

        if(ed2_text.isEmpty() || ed2_text.length() == 0 || ed2_text.equals("") || ed2_text == null)
        {
            ed2.setError("Enter price");
        }
        if(ed3_text.isEmpty() || ed3_text.length() == 0 || ed3_text.equals("") || ed3_text == null)
        {
            ed3.setError("Enter size");
        }

        if(ed4_text.isEmpty() || ed4_text.length() == 0 || ed4_text.equals("") || ed4_text == null)
        {
            ed4.setError("Enter alcohol percentage");
        }

    }
}
