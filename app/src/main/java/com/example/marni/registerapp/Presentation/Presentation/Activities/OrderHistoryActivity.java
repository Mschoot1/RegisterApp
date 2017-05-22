package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductGenerator;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.CostumAdapter;
import com.example.marni.registerapp.Presentation.AsyncKlassen.HttpHandler;
import com.example.marni.registerapp.Presentation.Domain.Order;
import com.example.marni.registerapp.Presentation.cardreader.LoyaltyCardReader;
import com.example.marni.registerapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryActivity extends AppCompatActivity implements LoyaltyCardReader.AccountCallback,
        HttpHandler.OnRandomOrderAvailable,AdapterView.OnItemClickListener {

    private final String TAG = getClass().getSimpleName();
    public static final String ORDER = "ORDER";
    ListView mListViewOrders;
    CostumAdapter mCostumAdapter;
    private ArrayList<Order> mOrderArrayList = new ArrayList<>();

    public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public LoyaltyCardReader mLoyaltyCardReader;
    private TextView text;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        getData();

        mLoyaltyCardReader = new LoyaltyCardReader(this);
        enableReaderMode();

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

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        Activity activity = this;
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.enableReaderMode(activity, mLoyaltyCardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        Activity activity = this;
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.disableReaderMode(activity);
        }
    }

    @Override
    public void onAccountReceived(final String account) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
                intent.putExtra("ACCOUNT", account);
                startActivity(intent);
            }
        });

    }
}

