package com.example.marni.registerapp.presentation.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.example.marni.registerapp.presentation.asyncklassen.GetCustomerfromOrderTask;
import com.example.marni.registerapp.presentation.asyncklassen.OrderPendingPutTask;
import com.example.marni.registerapp.presentation.asyncklassen.PendingGetTask;
import com.example.marni.registerapp.presentation.domain.Order;
import com.example.marni.registerapp.presentation.presentation.adapters.ProductsListViewAdapter;
import com.example.marni.registerapp.presentation.domain.Product;
import com.example.marni.registerapp.presentation.asyncklassen.ProductGenerator;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.marni.registerapp.presentation.presentation.activities.DeviceInformationActivity.CUSTOMERID;

/**
 * Created by Wallaard on 9-5-2017.
 */

public class OrderDetailActivity extends AppCompatActivity implements ProductGenerator.OnAvailable,
        GetCustomerfromOrderTask.OnCustomerIdAvailable, OrderPendingPutTask.PutSuccessListener, PendingGetTask.OnPendingAvailable {

    private ArrayList<Product> productsList = new ArrayList<>();
    private TextView textViewTotal;
    private double priceTotal;
    private String orderid;
    private int customerId;
    DecimalFormat formatter = new DecimalFormat("#0.00");
    private StickyListHeadersListView stickyList;

    public static final String JWT_STR = "jwt_str";
    public static final String USER_KEY = "user";
    String jwt;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        jwt = prefs.getString(JWT_STR, "");
        user = prefs.getString(USER_KEY, "");

        Bundle bundle = getIntent().getExtras();
        orderid = bundle.getString("ACCOUNT");

        Button cancelbutton = (Button) findViewById(R.id.cancelbutton1);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderDetailActivity.this, "Order canceled", Toast.LENGTH_LONG).show();
                putOrderPendingStatus("https://mysql-test-p4.herokuapp.com/order/pending", "2", orderid);
            }
        });

        Button confirmbutton = (Button) findViewById(R.id.confirmbutton1);
        confirmbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v2) {
                getPending();
            }
        });

        getProducts(orderid);
        getCustomerId();

        Button deviceinfobutton = (Button) findViewById(R.id.deviceinformationbutton);
        deviceinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                Intent intent3 = new Intent(OrderDetailActivity.this,DeviceInformationActivity.class);
                intent3.putExtra("flag","O");
                intent3.putExtra(CUSTOMERID,customerId);
                moveTaskToBack(true);
                startActivity(intent3);
            }
        });

        textViewTotal = (TextView) findViewById(R.id.totalprice);

        stickyList = (StickyListHeadersListView) findViewById(R.id.productdetail_listview);
        stickyList.setAreHeadersSticky(true);
    }

    //GET klassen product hieronder
    public void onAvailable(Product product) {
        productsList.add(product);
        getPriceTotal(product);

        ProductsListViewAdapter productAdapter = new ProductsListViewAdapter(this,this, getLayoutInflater(), productsList);
        stickyList.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();

        textViewTotal.setText("€ " + formatter.format(priceTotal));
    }

    public void getProducts(String orderid){
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/products/order/" + orderid, jwt};

        ProductGenerator getProduct = new ProductGenerator(this);
        getProduct.execute(urls);
    }

    public Double getPriceTotal(Product product) {
            priceTotal = priceTotal + (product.getPrice() * product.getQuantity());

        return priceTotal;
    }


    public void getCustomerId(){
        GetCustomerfromOrderTask customer = new GetCustomerfromOrderTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/order/" + orderid, jwt};
        customer.execute(urls3);
    }

    public void putOrderPendingStatus(String apiUrl, String pending, String orderId) {
        String[] urls = new String[]{apiUrl, pending, orderId, jwt};
        OrderPendingPutTask task = new OrderPendingPutTask(this);
        task.execute(urls);
    }

    public void getPending(){
        PendingGetTask customer = new PendingGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/order/" + orderid, jwt};
        customer.execute(urls3);
    }

    @Override
    public void onCustomerIdAvailable(Order order) {
        customerId = order.getCustomerid();
    }

    @Override
    public void putSuccessful(Boolean successful) {
        Intent intent = new Intent(this, RegisterHistoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPendingAvailable(Order order) {
        int pending = order.getPending();
        Log.i("OrderDetailActivity", Integer.toString(pending));
        if(pending == 1){
            Intent intent = new Intent(OrderDetailActivity.this,PaymentPendingActivity.class);
            intent.putExtra("ORDERID", orderid);
            intent.putExtra("PRICETOTAL", priceTotal);
            intent.putExtra("CUSTOMERID", customerId);
            startActivity(intent);
        } else{
            Toast.makeText(getApplication(), "The customer has cancelled the order.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(OrderDetailActivity.this,RegisterHistoryActivity.class);
            startActivity(intent);
        }
    }
}
