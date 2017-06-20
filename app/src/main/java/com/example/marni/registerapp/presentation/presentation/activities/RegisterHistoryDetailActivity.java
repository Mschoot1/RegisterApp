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

import com.example.marni.registerapp.presentation.asyncklassen.ProductGenerator;
import com.example.marni.registerapp.presentation.domain.Product;
import com.example.marni.registerapp.presentation.domain.Register;
import com.example.marni.registerapp.presentation.presentation.adapters.ProductsListViewAdapter;
import com.example.marni.registerapp.presentation.presentation.fragments.CategoriesFragment;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.example.marni.registerapp.presentation.presentation.activities.DeviceInformationActivity.REGISTER;

public class RegisterHistoryDetailActivity extends AppCompatActivity implements CategoriesFragment.OnItemSelected, ProductGenerator.OnAvailable {
    private final String tag = getClass().getSimpleName();
    DecimalFormat formatter = new DecimalFormat("#0.00");
    private TextView textViewTotal;
    private ArrayList<Product> productsList = new ArrayList<>();
    private double priceTotal;
    private StickyListHeadersListView stickyList;


    public static final String JWT_STR = "jwt_str";
    String jwt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_history_single_item);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        jwt = prefs.getString(JWT_STR, "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final Register register = (Register) intent.getSerializableExtra("ORDER");

        String orderid = String.valueOf(register.getOrderId());

        Button deviceinfobutton = (Button) findViewById(R.id.deviceinformationbutton2);
        deviceinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterHistoryDetailActivity.this,DeviceInformationActivity.class);
                intent.putExtra("flag","H");
                intent.putExtra(REGISTER, register);
                moveTaskToBack(true);
                startActivity(intent);
            }
        });

        getProducts(orderid);
        textViewTotal = (TextView) findViewById(R.id.totalprice);

        stickyList = (StickyListHeadersListView) findViewById(R.id.productdetail_listview);
        stickyList.setAreHeadersSticky(true);
    }

    //GET klassen producten hieronder
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
    ////////

    public Double getPriceTotal(Product product) {
        priceTotal = priceTotal + (product.getPrice() * product.getQuantity());

        return priceTotal;
    }

    @Override
    public void onItemSelected(int i) {
        int j = 0;

        for(Product p : productsList){
            j++;
            Log.i(tag, "j: " + j);
            Log.i(tag, "i: " + i);
            if(p.getCategoryId()==i){
                stickyList.setSelection(j);
                break;
            }
        }

    }
}
