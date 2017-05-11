package com.example.marni.registerapp.Presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.marni.registerapp.R;

import java.util.ArrayList;
import java.util.Date;

public class OrderHistoryActivity extends AppCompatActivity {

    ListView mListViewOrders;
    CostumAdapter mCostumAdapter;
    ArrayList mOrderList = new ArrayList();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        Order o = new Order();

        o.id = 5;
        o.date = "25-3-2017";
        o.price = 3;
        o.status = "Paid";

        mOrderList.add(o);

        o = new Order();

        o.id = 6;
        o.date = "25-3-2017";
        o.price = 27;
        o.status = "Paid";

        mOrderList.add(o);

        mListViewOrders = (ListView) findViewById(R.id.listViewOrders);

        mCostumAdapter = new CostumAdapter(this, getLayoutInflater(), mOrderList);
        mListViewOrders.setAdapter(mCostumAdapter);
        mCostumAdapter.notifyDataSetChanged();
    }

}

