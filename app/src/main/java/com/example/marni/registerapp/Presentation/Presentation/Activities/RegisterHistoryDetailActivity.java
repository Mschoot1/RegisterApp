package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Domain.Register;
import com.example.marni.registerapp.R;

/**
 * Created by Wallaard on 17-5-2017.
 */

public class RegisterHistoryDetailActivity extends AppCompatActivity {
    private TextView customeridView,idView,statusView,timestampView,totalpriceView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_single_item);

        idView = (TextView)findViewById(R.id.history_id);
        statusView = (TextView)findViewById(R.id.history_status);
        customeridView= (TextView)findViewById(R.id.history_customerid);
        timestampView = (TextView)findViewById(R.id.history_timestamp);
        totalpriceView = (TextView)findViewById(R.id.history_totalprice);

        Intent intent = getIntent();
        Register register = (Register) intent.getSerializableExtra("REGISTER");

        idView.setText(String.valueOf(register.getOrderId()));
        customeridView.setText(String.valueOf(register.getCustomerId()));
        timestampView.setText(register.getDateTime());
        totalpriceView.setText(String.valueOf(register.getTotalPrice()));
    }
}
