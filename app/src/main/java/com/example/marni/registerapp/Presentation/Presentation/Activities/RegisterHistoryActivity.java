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

import com.example.marni.registerapp.Presentation.Domain.Register;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.RegisterHistoryAdapter;
import com.example.marni.registerapp.Presentation.AsyncKlassen.RegisterGetTask;
import com.example.marni.registerapp.Presentation.cardreader.LoyaltyCardReader;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

public class RegisterHistoryActivity extends AppCompatActivity implements LoyaltyCardReader.AccountCallback,
        RegisterGetTask.OnRandomRegisterAvailable,AdapterView.OnItemClickListener {


    private final String TAG = getClass().getSimpleName();
    public static final String REGISTER = "REGISTER";
    ListView mListViewRegisters;
    RegisterHistoryAdapter mRegisterHistoryAdapter;
    private ArrayList<Register> mRegisterArrayList = new ArrayList<>();

    public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public LoyaltyCardReader mLoyaltyCardReader;
    private TextView text;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        getData();

        mLoyaltyCardReader = new LoyaltyCardReader(this);
        enableReaderMode();

        mListViewRegisters = (ListView) findViewById(R.id.listViewOrders);

        mRegisterHistoryAdapter = new RegisterHistoryAdapter(this, getLayoutInflater(), mRegisterArrayList);
        mListViewRegisters.setAdapter(mRegisterHistoryAdapter);
        mListViewRegisters.setOnItemClickListener(this);

    }

    public void getData() {
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/register/284"};

        RegisterGetTask g = new RegisterGetTask(this);
        g.execute(urls);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e("item",position+"");

        Register register = mRegisterArrayList.get(position);

        Intent intent = new Intent(getApplicationContext(), RegisterHistoryDetailActivity.class);
        intent.putExtra(REGISTER, register);
        startActivity(intent);
    }

    @Override
    public void onRandomRegisterAvailable(Register register) {

        mRegisterArrayList.add(register);
        mRegisterHistoryAdapter.notifyDataSetChanged();
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

