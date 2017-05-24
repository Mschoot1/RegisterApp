package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AccountGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmAsync;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmPostAsync;
import com.example.marni.registerapp.Presentation.Domain.Balance;
import com.example.marni.registerapp.Presentation.Domain.Customer;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.ProductsListViewAdapter;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductGenerator;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.String.valueOf;

/**
 * Created by Wallaard on 9-5-2017.
 */

public class OrderDetailActivity extends AppCompatActivity implements ProductGenerator.OnAvailable,
        ConfirmAsync.SuccessListener, ConfirmPostAsync.SuccessListener, AccountGetTask.OnAccountAvailable {

    private final String TAG = getClass().getSimpleName();

    private ProductsListViewAdapter productAdapter;
    private ArrayList<Product> productsList = new ArrayList<>();
    private TextView textViewTotal;
    private double priceTotal;
    private Button cancelbutton;
    private Button confirmbutton;
    private Button deviceinfobutton;
    private double current_balance, d;
    private String orderid;
    DecimalFormat formatter = new DecimalFormat("#0.00");
    private StickyListHeadersListView stickyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getBalance();

        Bundle bundle = getIntent().getExtras();
        orderid = bundle.getString("ACCOUNT");

        cancelbutton = (Button) findViewById(R.id.cancelbutton1);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this,OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        confirmbutton = (Button) findViewById(R.id.confirmbutton1);
        confirmbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v2) {
                Log.i(TAG,current_balance + " huidig balans");
                Log.i(TAG,priceTotal + " totale prijs");
                if(priceTotal < current_balance){
                    changeOrderStatus();
                    Log.i(TAG,"");
                } else {
                    Log.i(TAG,"Het bedrag is te laag");
                    Toast.makeText(OrderDetailActivity.this, "Customer's balance too low. € "+(priceTotal-current_balance), Toast.LENGTH_SHORT).show();
                }
            }
        });



        deviceinfobutton = (Button) findViewById(R.id.deviceinformationbutton);
        deviceinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                Intent intent3 = new Intent(OrderDetailActivity.this,DeviceInformationActivity.class);
                startActivity(intent3);
            }
        });

        getProducts(orderid);

        textViewTotal = (TextView) findViewById(R.id.totalprice);

        stickyList = (StickyListHeadersListView) findViewById(R.id.productdetail_listview);
        stickyList.setAreHeadersSticky(true);
    }

    //GET klassen product hieronder
    public void OnAvailable(Product product) {
        productsList.add(product);
        getPriceTotal(product);

        productAdapter = new ProductsListViewAdapter(this, getLayoutInflater(), productsList);
        stickyList.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();

        textViewTotal.setText("€ " + formatter.format(priceTotal));

    }

    public void getProducts(String orderid){
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/products/order/" + orderid};

        ProductGenerator getProduct = new ProductGenerator(this);
        getProduct.execute(urls);
    }

    public Double getPriceTotal(Product product) {
            priceTotal = priceTotal + (product.getPrice() * product.getQuantity());

        return priceTotal;
    }


    ///ONACCOUNT AVAILABLE DINGEN
    public void OnAccountAvailable (Customer customer){
        current_balance = customer.getBalance();
    }

    public void getBalance(){
        AccountGetTask accounttask = new AccountGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/account/284"};
        accounttask.execute(urls3);
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
                "https://mysql-test-p4.herokuapp.com/order/pay", Double.toString(priceTotal), "284"
        };
        confirmPostAsync.execute(urls2);
    }

    @Override
    public void successful(Boolean succesful){
        Log.i(TAG,succesful.toString());
        if(succesful){
            Log.i(TAG,"Gelukt");
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent );
        } else {
            Log.i(TAG,"Mislukt");
        }
    }
}
