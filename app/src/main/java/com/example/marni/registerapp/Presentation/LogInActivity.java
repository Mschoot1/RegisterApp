package com.example.marni.registerapp.Presentation;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.marni.registerapp.R;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button button = (Button) findViewById(R.id.buttonSignIn);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            public void onClick(View V) {
                EditText ed = (EditText) findViewById(R.id.editTextEmailaddress);
                EditText ed2 = (EditText) findViewById(R.id.editTextPassword);

                String ed_text = ed.getText().toString().trim();
                String ed2_text = ed2.getText().toString().trim();

                if(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
                {
                    ed.setError("Enter ID");
                }

                if(ed2_text.isEmpty() || ed2_text.length() == 0 || ed2_text.equals("") || ed2_text == null)
                {
                    ed2.setError("Enter password");
                }
            }
        });
    }
}

