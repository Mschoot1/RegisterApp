package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Domain.Order;
import com.example.marni.registerapp.R;

/**
 * Created by Wallaard on 17-5-2017.
 */

public class OrderHistoryDetailActivity extends AppCompatActivity {
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
        Order order = (Order) intent.getSerializableExtra("ORDER");

        idView.setText(String.valueOf(order.getOrderId()));
        if(order.getStatus() == 1) {
            statusView.setText("Paid");
        } else {
            statusView.setText("Open");
        }
        customeridView.setText(String.valueOf(order.getCustomerid()));
        timestampView.setText(order.getDateTime());
        totalpriceView.setText(String.valueOf(order.getTotalPrice()));
    }
}
