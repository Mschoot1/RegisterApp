package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.marni.registerapp.Presentation.Presentation.Adapters.CostumAdapter;
import com.example.marni.registerapp.Presentation.AsyncKlassen.HttpHandler;
import com.example.marni.registerapp.Presentation.Order;
import com.example.marni.registerapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryActivity extends AppCompatActivity implements HttpHandler.OnRandomOrderAvailable {

    ListView mListViewOrders;
    CostumAdapter mCostumAdapter;
    ArrayList mOrderArrayList = new ArrayList();

    ArrayList<HashMap<String, String>> orderList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        getData();

        mListViewOrders = (ListView) findViewById(R.id.listViewOrders);

        mCostumAdapter = new CostumAdapter(this, getLayoutInflater(), mOrderArrayList);
        mListViewOrders.setAdapter(mCostumAdapter);

    }
    @Override
    public void onRandomOrderAvailable(Order order) {
        mOrderArrayList.add(order);
        mCostumAdapter.notifyDataSetChanged();
    }

    public void getData() {
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/orders/284"};

        HttpHandler g = new HttpHandler(this);
        g.execute(urls);
    }


}

