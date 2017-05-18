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

import com.example.marni.registerapp.Presentation.AsyncKlassen.BalanceGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmAsync;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmPostAsync;
import com.example.marni.registerapp.Presentation.AsyncKlassen.EmailGetTask;
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
        ConfirmAsync.SuccessListener, ConfirmPostAsync.SuccessListener, EmailGetTask.OnEmailAvailable, BalanceGetTask.OnBalanceAvailable {
    private final String TAG = getClass().getSimpleName();
    private ProductsListViewAdapter productAdapter;
    private ArrayList<Product> productsList = new ArrayList<>();
    private ListView productListView;
    private TextView textViewTotal;
    private double priceTotal;
    private Button cancelbutton;
    private Button confirmbutton;
    private Button deviceinfobutton;
    private TextView email;
    private double current_balance,d;
    private String orderid;
    DecimalFormat formatter = new DecimalFormat("#0.00");
    //

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
                //String totalstring = textViewTotal.getText().toString();
                Log.i(TAG,current_balance+"huidig balans");
                Log.i(TAG,priceTotal+"totale prijs");
                if(
                        priceTotal < current_balance
                        ){
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

        getEmail();
        getProducts(orderid);

        email = (TextView) findViewById(R.id.customer_email);
        textViewTotal = (TextView) findViewById(R.id.totalprice);
        productListView = (ListView) findViewById(R.id.productdetail_listview);
        productAdapter = new ProductsListViewAdapter(this, getLayoutInflater(), productsList);
        productListView.setAdapter(productAdapter);
    }
    //////

    //GET methoden van balance
    @Override
    public void onBalanceAvailable(Balance balance) {
        current_balance = balance.getBalance();
    }

    public void getBalance(){
        BalanceGetTask balancetask = new BalanceGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/balance/284"};
        balancetask.execute(urls3);
    }

    //GET klassen hieronder
    public void OnAvailable(Product product) {
        productsList.add(product);
        productAdapter.notifyDataSetChanged();
        getPriceTotal(product);

        textViewTotal.setText("€" + formatter.format(priceTotal));

    }

    public void getProducts(String orderid){
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/products/order/" + orderid};

        ProductGenerator getProduct = new ProductGenerator(this);
        getProduct.execute(urls);
    }

    public Double getPriceTotal(Product product) {
            priceTotal = priceTotal + (product.getPrice() * product.getSize());

        return priceTotal;
    }

    public void OnEmailAvailable (Customer customer){
        email.setText(customer.getEmail());
    }

    public void getEmail(){
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/email/284"};

        EmailGetTask e = new EmailGetTask(this);
        e.execute(urls);
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
                "https://mysql-test-p4.herokuapp.com/order/pay", valueOf(priceTotal), "284"
        };
        confirmPostAsync.execute(urls2);
    }

    @Override
    public void successful(Boolean succesful){
        Log.i(TAG,succesful.toString());
        if(succesful){
            Log.i(TAG,"Gelukt");
            Intent intent = new Intent(OrderDetailActivity.this, OrderHistoryActivity.class);
            startActivity(intent );
        } else {
            Log.i(TAG,"Mislukt");
        }
    }
}
