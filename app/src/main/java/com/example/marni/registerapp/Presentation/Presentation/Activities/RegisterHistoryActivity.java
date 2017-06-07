package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AccountGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.OrderPendingPutTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.PendingGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductGenerator;
import com.example.marni.registerapp.Presentation.AsyncKlassen.RegisterGetTask;
import com.example.marni.registerapp.Presentation.BusinessLogic.DrawerMenu;
import com.example.marni.registerapp.Presentation.Domain.Customer;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Domain.Order;
import com.example.marni.registerapp.Presentation.Domain.Register;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.RegisterHistoryAdapter;
import com.example.marni.registerapp.Presentation.cardreader.LoyaltyCardReader;
import com.example.marni.registerapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoyaltyCardReader.AccountCallback,
        RegisterGetTask.OnRandomRegisterAvailable,AdapterView.OnItemClickListener, AccountGetTask.OnAccountAvailable, PendingGetTask.OnPendingAvailable, OrderPendingPutTask.PutSuccessListener {


    private final String TAG = getClass().getSimpleName();
    public static final String ORDER = "ORDER";
    ListView mListViewOrders;
    RegisterHistoryAdapter mCostumAdapter;
    private ArrayList<Register> mOrderArrayList = new ArrayList<>();

    public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public LoyaltyCardReader mLoyaltyCardReader;
    private TextView account_email;

    private int pending;
    private String orderId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        getData();
        getEmail();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order History");

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_order_history);

        View headerView = navigationView.getHeaderView(0);
        account_email = (TextView)headerView.findViewById(R.id.nav_email);

        mLoyaltyCardReader = new LoyaltyCardReader(this);
        enableReaderMode();

        mListViewOrders = (ListView) findViewById(R.id.listViewOrders);

        mCostumAdapter = new RegisterHistoryAdapter(this, getLayoutInflater(), mOrderArrayList);
        mListViewOrders.setAdapter(mCostumAdapter);
        mListViewOrders.setOnItemClickListener(this);

    }

    public void OnAccountAvailable (Customer customer){
        account_email.setText("284");
    }

    public void getEmail(){
        AccountGetTask accounttask = new AccountGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/account/284"};
        accounttask.execute(urls3);
    }

    /////// MENU dingetjes
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.i(TAG, item.toString() + " clicked.");

        int id = item.getItemId();

        new DrawerMenu(getApplicationContext(), id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /////////

    public void getData() {
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/orders/284"};

        RegisterGetTask g = new RegisterGetTask(this);
        g.execute(urls);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("item",position+"");

        Register register = mOrderArrayList.get(position);

        Intent intent = new Intent(getApplicationContext(), RegisterHistoryDetailActivity.class);
        intent.putExtra(ORDER, register);
        startActivity(intent);
    }

    @Override
    public void onRandomRegisterAvailable(Register register) {
        mOrderArrayList.add(register);
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

    @Override
    public void onStop(){
        super.onStop();
        disableReaderMode();
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


    public void getPending(String account){
        Log.i(TAG, account);

        PendingGetTask customer = new PendingGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/order/" + account};
        customer.execute(urls3);
    }

    @Override
    public void onAccountReceived(final String account) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getPending(account);
                orderId = account;
            }
        });
    }

    public void putOrderPendingStatus(String apiUrl, String orderId, String pending) {
        String[] urls = new String[]{apiUrl, orderId, pending};
        OrderPendingPutTask task = new OrderPendingPutTask(this);
        task.execute(urls);
    }

    @Override
    public void onPendingAvailable(Order order) {
        pending = order.getPending();
        Log.i(TAG, pending + "");

        if(pending == 2){
            putOrderPendingStatus("https://mysql-test-p4.herokuapp.com/order/pending", "0", orderId);
            Toast.makeText(this, "The customer has cancelled his order.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
            intent.putExtra("ACCOUNT", orderId);
            startActivity(intent);
        }
    }
    @Override
    public void putSuccessful(Boolean successful) {

    }
}

