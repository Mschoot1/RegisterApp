package com.example.marni.registerapp.Presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.marni.registerapp.R;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        EditText nameEditText = (EditText) findViewById(R.id.editTextEmailaddress);
        String name = nameEditText.getText().toString();

        if(TextUtils.isEmpty(name)) {
            nameEditText.setError("Empty field");
            return;
        }
    }
}
