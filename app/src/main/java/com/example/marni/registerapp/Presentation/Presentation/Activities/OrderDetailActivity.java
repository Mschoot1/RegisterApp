package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AccountGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmAsync;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ConfirmPostAsync;
import com.example.marni.registerapp.Presentation.AsyncKlassen.GetCustomerfromOrderTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.OrderPendingPutTask;
import com.example.marni.registerapp.Presentation.Domain.Customer;
import com.example.marni.registerapp.Presentation.Domain.Order;
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

public class OrderDetailActivity extends AppCompatActivity implements ProductGenerator.OnAvailable, AccountGetTask.OnAccountAvailable,
        GetCustomerfromOrderTask.OnCustomerIdAvailable, OrderPendingPutTask.PutSuccessListener {

    private final String TAG = getClass().getSimpleName();

    private ProductsListViewAdapter productAdapter;
    private ArrayList<Product> productsList = new ArrayList<>();
    private TextView textViewTotal;
    private double priceTotal;
    private Button cancelbutton;
    private Button confirmbutton;
    private Button deviceinfobutton;
    private double current_balance;
    private String orderid;
    private int customerId;
    DecimalFormat formatter = new DecimalFormat("#0.00");
    private StickyListHeadersListView stickyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Order");

        getBalance();

        Bundle bundle = getIntent().getExtras();
        orderid = bundle.getString("ACCOUNT");

        cancelbutton = (Button) findViewById(R.id.cancelbutton1);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putOrderPendingStatus("https://mysql-test-p4.herokuapp.com/order/pending", "2", orderid);
            }
        });

        confirmbutton = (Button) findViewById(R.id.confirmbutton1);
        confirmbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v2) {
                Intent intent = new Intent(OrderDetailActivity.this,PaymentPendingActivity.class);
                intent.putExtra("ORDERID", orderid);
                intent.putExtra("PRICETOTAL", priceTotal);
                intent.putExtra("CUSTOMERID", customerId);
                startActivity(intent);
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
        getCustomerId();

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


    ///ONACCOUNT AVAILABLE
    public void OnAccountAvailable (Customer customer){
        current_balance = customer.getBalance();
    }

    public void getBalance(){
        AccountGetTask accounttask = new AccountGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/account/284"};
        accounttask.execute(urls3);
    }

    public void getCustomerId(){
        GetCustomerfromOrderTask customer = new GetCustomerfromOrderTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/order/" + orderid};
        customer.execute(urls3);
    }



    @Override
    public void onCustomerIdAvailable(Order order) {
        customerId = order.getCustomerid();
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
}
