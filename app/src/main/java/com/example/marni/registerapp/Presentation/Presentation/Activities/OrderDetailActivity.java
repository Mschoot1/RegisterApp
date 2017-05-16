package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmAsync;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmPostAsync;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.ProductsListViewAdapter;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductGenerator;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Wallaard on 9-5-2017.
 */

public class OrderDetailActivity extends AppCompatActivity implements ProductGenerator.OnAvailable,
        ConfirmAsync.SuccessListener, ConfirmPostAsync.SuccessListener
        {
    private final String TAG = getClass().getSimpleName();
    private ProductsListViewAdapter productAdapter;
    private ArrayList<Product> productsList = new ArrayList<>();
    private ListView productListView;
    private TextView textViewTotal;
    private double priceTotal;
    private Button cancelbutton;
    private Button confirmbutton;
            private Button deviceinfobutton;
    DecimalFormat formatter = new DecimalFormat("#0.00");
    //

    //PUT methoden hieronder
    public void changeOrderStatus(){
                ConfirmAsync confirmAsync = new ConfirmAsync(this);
                String[] urls = new String[]{
                        "https://mysql-test-p4.herokuapp.com/order/edit", "1", "4"
                };
                confirmAsync.execute(urls);

                ConfirmPostAsync confirmPostAsync = new ConfirmPostAsync(this);
                String[] urls2 = new String[]{
                        "https://mysql-test-p4.herokuapp.com/order/pay", String.valueOf(priceTotal), "284"
                };
                confirmPostAsync.execute(urls2);
        }

    @Override
    public void successful(Boolean succesful){
        Log.i(TAG,succesful.toString());
        if(succesful){
            Log.i(TAG,"Gelukt");
            Intent intent2 = new Intent(OrderDetailActivity.this,OrderHistoryActivity.class);
            startActivity(intent2);
        } else {
            Log.i(TAG,"Mislukt");
        }
    }
    ///////

    //////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

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
                changeOrderStatus();
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


        getProducts();
        textViewTotal = (TextView) findViewById(R.id.totalprice);
        productListView = (ListView) findViewById(R.id.productdetail_listview);
        productAdapter = new ProductsListViewAdapter(this, getLayoutInflater(), productsList);
        productListView.setAdapter(productAdapter);
    }
    //////


    //GET klassen hieronder
    public void OnAvailable(Product product) {
        productsList.add(product);
        productAdapter.notifyDataSetChanged();
        getPriceTotal(product);

        textViewTotal.setText("€" + formatter.format(priceTotal));

    }

    public void getProducts(){
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/products/order/14"};

        ProductGenerator getProduct = new ProductGenerator(this);
        getProduct.execute(urls);
    }

    public Double getPriceTotal(Product product) {
            priceTotal = priceTotal + (product.getPrice() * product.getSize());

        return priceTotal;
    }
    /////////
}
