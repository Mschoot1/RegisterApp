package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmAsync;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmPostAsync;
import com.example.marni.registerapp.Presentation.AsyncKlassen.OrderPendingPutTask;
import com.example.marni.registerapp.Presentation.cardreader.LoyaltyCardReader;
import com.example.marni.registerapp.R;

public class PaymentPendingActivity extends AppCompatActivity implements LoyaltyCardReader.AccountCallback,
        OrderPendingPutTask.PutSuccessListener,ConfirmAsync.SuccessListener, ConfirmPostAsync.SuccessListener {

    private final String TAG = getClass().getSimpleName();

    private Button cancelButton;
    private String orderid;
    private Double priceTotal;
    private int customerId;

    public static int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public LoyaltyCardReader mLoyaltyCardReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_pending);

        mLoyaltyCardReader = new LoyaltyCardReader(this);
        enableReaderMode();

        Bundle bundle = getIntent().getExtras();
        orderid = bundle.getString("ORDERID");
        priceTotal = bundle.getDouble("PRICETOTAL");
        customerId = bundle.getInt("CUSTOMERID");


        cancelButton = (Button) findViewById(R.id.payment_pending_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putOrderPendingStatus("https://mysql-test-p4.herokuapp.com/order/pending", "0", orderid);
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
                changeOrderStatus();
            }
        });
    }

    public void putOrderPendingStatus(String apiUrl, String orderid, String pending) {
        String[] urls = new String[]{apiUrl, orderid, pending};
        OrderPendingPutTask task = new OrderPendingPutTask(this);
        task.execute(urls);
    }

    @Override
    public void putSuccessful(Boolean successful) {
        Intent intent = new Intent(this, RegisterHistoryActivity.class);
        startActivity(intent);
    }

    //PUT methoden hieronder
    public void changeOrderStatus(){
        ConfirmAsync confirmAsync = new ConfirmAsync(this);
        String[] urls = new String[]{
                "https://mysql-test-p4.herokuapp.com/order/edit", "1", orderid
        };
        confirmAsync.execute(urls);

        ConfirmPostAsync confirmPostAsync = new ConfirmPostAsync(this);
        String[] urls2 = new String[]{
                "https://mysql-test-p4.herokuapp.com/order/pay", Double.toString(priceTotal), Integer.toString(customerId), orderid, "284"
        };
        confirmPostAsync.execute(urls2);
    }

    @Override
    public void successful(Boolean succesful){
        Log.i(TAG,succesful.toString());
        if(succesful){
            Toast.makeText(this, "Payment succesful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RegisterHistoryActivity.class);
            startActivity(intent );
        } else {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        }
    }
}
