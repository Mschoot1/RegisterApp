package com.example.marni.registerapp.presentation.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.preference.PreferenceManager;
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

import com.example.marni.registerapp.presentation.asyncklassen.AccountGetTask;
import com.example.marni.registerapp.presentation.asyncklassen.PendingGetTask;
import com.example.marni.registerapp.presentation.asyncklassen.RegisterGetTask;
import com.example.marni.registerapp.presentation.businesslogic.DrawerMenu;
import com.example.marni.registerapp.presentation.domain.Customer;
import com.example.marni.registerapp.presentation.domain.Order;
import com.example.marni.registerapp.presentation.domain.Register;
import com.example.marni.registerapp.presentation.presentation.adapters.RegisterHistoryAdapter;
import com.example.marni.registerapp.presentation.cardreader.LoyaltyCardReader;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

public class RegisterHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LoyaltyCardReader.AccountCallback,
        RegisterGetTask.OnRandomRegisterAvailable,AdapterView.OnItemClickListener, AccountGetTask.OnAccountAvailable, PendingGetTask.OnPendingAvailable {

    private final String tag = getClass().getSimpleName();
    public static final String ORDER = "ORDER";
    ListView mListViewOrders;
    RegisterHistoryAdapter mCostumAdapter;
    private ArrayList<Register> mOrderArrayList = new ArrayList<>();

    private static final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private LoyaltyCardReader mLoyaltyCardReader;
    private TextView accountemail;

    private String orderId;

    public static final String JWT_STR = "jwt_str";
    public static final String USER_KEY = "user";
    String jwt;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        jwt = prefs.getString(JWT_STR, "");
        user = prefs.getString(USER_KEY, "");

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
        accountemail = (TextView)headerView.findViewById(R.id.nav_email);

        mLoyaltyCardReader = new LoyaltyCardReader(this);
        enableReaderMode();

        mListViewOrders = (ListView) findViewById(R.id.listViewOrders);

        mCostumAdapter = new RegisterHistoryAdapter(this, getLayoutInflater(), mOrderArrayList);
        mListViewOrders.setAdapter(mCostumAdapter);
        mListViewOrders.setOnItemClickListener(this);

    }

    public void onAccountAvailable (Customer customer){
        accountemail.setText(user);
    }

    public void getEmail(){
        AccountGetTask accounttask = new AccountGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/account/" + user, jwt};
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

        Log.i(tag, item.toString() + " clicked.");

        int id = item.getItemId();

        new DrawerMenu(getApplicationContext(), id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /////////

    public void getData() {
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/orders/" + user, jwt};

        RegisterGetTask g = new RegisterGetTask(this);
        g.execute(urls);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("item", Integer.toString(position));

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
        Log.i(tag, "Enabling reader mode");
        Activity activity = this;
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.enableReaderMode(activity, mLoyaltyCardReader, READER_FLAGS, null);
        }
    }

    private void disableReaderMode() {
        Log.i(tag, "Disabling reader mode");
        Activity activity = this;
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.disableReaderMode(activity);
        }
    }


    public void getPending(String account){
        Log.i(tag, account);

        PendingGetTask customer = new PendingGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/order/" + account, jwt};
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

    @Override
    public void onPendingAvailable(Order order) {
        int pending = order.getPending();
        Log.i(tag, Integer.toString(pending));

        if(pending == 2){
            Toast.makeText(this, "Customer scanned but order was cancelled by register", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
            intent.putExtra("ACCOUNT", orderId);
            startActivity(intent);
        }
    }
}
