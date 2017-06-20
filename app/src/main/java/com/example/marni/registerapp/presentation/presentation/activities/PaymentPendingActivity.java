package com.example.marni.registerapp.presentation.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.marni.registerapp.presentation.asyncklassen.ConfirmAsync;
import com.example.marni.registerapp.presentation.asyncklassen.ConfirmPostAsync;
import com.example.marni.registerapp.presentation.asyncklassen.OrderPendingPutTask;
import com.example.marni.registerapp.presentation.cardreader.LoyaltyCardReader;
import com.example.marni.registerapp.R;

public class PaymentPendingActivity extends AppCompatActivity implements LoyaltyCardReader.AccountCallback,
        OrderPendingPutTask.PutSuccessListener,ConfirmAsync.SuccessListener, ConfirmPostAsync.SuccessListener {

    private final String tag = getClass().getSimpleName();

    private String orderid;
    private Double priceTotal;
    private int customerId;
    private Boolean cancel;

    private static final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    private LoyaltyCardReader mLoyaltyCardReader;

    public static final String JWT_STR = "jwt_str";
    public static final String USER_KEY = "user";

    String jwt;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_pending);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        jwt = prefs.getString(JWT_STR, "");
        user = prefs.getString(USER_KEY, "");

        cancel = false;
        mLoyaltyCardReader = new LoyaltyCardReader(this);
        enableReaderMode();

        Bundle bundle = getIntent().getExtras();
        orderid = bundle.getString("ORDERID");
        priceTotal = bundle.getDouble("PRICETOTAL");
        customerId = bundle.getInt("CUSTOMERID");


        Button cancelButton = (Button) findViewById(R.id.payment_pending_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel = true;
                Toast.makeText(PaymentPendingActivity.this, "Order canceled", Toast.LENGTH_LONG).show();
                putOrderPendingStatus("https://mysql-test-p4.herokuapp.com/order/pending", "2", orderid);
                Intent intent = new Intent(PaymentPendingActivity.this, RegisterHistoryActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    public void onAccountReceived(final String account) {
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                putOrderPendingStatus("https://mysql-test-p4.herokuapp.com/order/pending", "0", orderid);
                pay();
                cancel = false;
            }
        });
    }

    public void putOrderPendingStatus(String apiUrl, String orderid, String pending) {
        String[] urls = new String[]{apiUrl, orderid, pending, jwt};
        OrderPendingPutTask task = new OrderPendingPutTask(this);
        task.execute(urls);
    }

    @Override
    public void putSuccessful(Boolean successful) {
        if(cancel){
            Log.i(tag, "Pending status has been changed to 2");
            Intent intent = new Intent(this, RegisterHistoryActivity.class);
            startActivity(intent);
        } else {
            Log.i(tag, "Pending status has been changed to 0");
        }
    }

    //PUT methoden hieronder
    public void pay(){
        ConfirmPostAsync confirmPostAsync = new ConfirmPostAsync(this);
        String[] urls2 = new String[]{
                "https://mysql-test-p4.herokuapp.com/order/pay", Double.toString(priceTotal), Integer.toString(customerId), orderid, user, jwt
        };
        confirmPostAsync.execute(urls2);
    }

    @Override
    public void successful(Boolean succesful){
        Log.i(tag,succesful.toString());
        if(succesful){
            Toast.makeText(this, "Payment succesful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RegisterHistoryActivity.class);
            startActivity(intent );
        } else {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        }
    }
}
