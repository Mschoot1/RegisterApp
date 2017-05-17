package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.marni.registerapp.Presentation.Presentation.Adapters.CostumAdapter;
import com.example.marni.registerapp.Presentation.AsyncKlassen.HttpHandler;
import com.example.marni.registerapp.Presentation.Domain.Order;
import com.example.marni.registerapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryActivity extends AppCompatActivity implements HttpHandler.OnRandomOrderAvailable,AdapterView.OnItemClickListener {

    private final String TAG = getClass().getSimpleName();
    public static final String ORDER = "ORDER";
    ListView mListViewOrders;
    CostumAdapter mCostumAdapter;
    private ArrayList<Order> mOrderArrayList = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        getData();

        mListViewOrders = (ListView) findViewById(R.id.listViewOrders);

        mCostumAdapter = new CostumAdapter(this, getLayoutInflater(), mOrderArrayList);
        mListViewOrders.setAdapter(mCostumAdapter);
        mListViewOrders.setOnItemClickListener(this);

    }

    public void getData() {
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/orders/284"};

        HttpHandler g = new HttpHandler(this);
        g.execute(urls);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("item",position+"");

        Order order = mOrderArrayList.get(position);

        Intent intent = new Intent(getApplicationContext(), OrderHistorySingleActivity.class);
        intent.putExtra(ORDER, order);
        startActivity(intent);
    }

    @Override
    public void onRandomOrderAvailable(Order order) {
        mOrderArrayList.add(order);
        mCostumAdapter.notifyDataSetChanged();
    }
}

