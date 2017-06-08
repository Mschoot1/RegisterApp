package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AssortmentGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.CategoriesGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductGenerator;
import com.example.marni.registerapp.Presentation.Domain.Category;
import com.example.marni.registerapp.Presentation.Domain.Order;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Domain.Register;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.AssortmentListViewAdapter;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.ProductsListViewAdapter;
import com.example.marni.registerapp.Presentation.Presentation.Fragments.CategoriesFragment;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.marni.registerapp.Presentation.Presentation.Activities.RegisterHistoryActivity.ORDER;

/**
 * Created by Wallaard on 17-5-2017.
 */

public class RegisterHistoryDetailActivity extends AppCompatActivity implements CategoriesFragment.OnItemSelected, ProductGenerator.OnAvailable {
    private final String TAG = getClass().getSimpleName();
    DecimalFormat formatter = new DecimalFormat("#0.00");
    private TextView textViewTotal;
    private ProductsListViewAdapter productAdapter;
    private ArrayList<Product> productsList = new ArrayList<>();
    private double priceTotal;
    private Button deviceinfobutton;
    private StickyListHeadersListView stickyList;
    private String orderid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_history_single_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final Register register = (Register) intent.getSerializableExtra("ORDER");

        orderid = String.valueOf(register.getOrderId());

        deviceinfobutton = (Button) findViewById(R.id.deviceinformationbutton2);
        deviceinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterHistoryDetailActivity.this,DeviceInformationActivity.class);
                intent.putExtra(ORDER, register);
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
    public void OnAvailable(Product product) {
        productsList.add(product);
        getPriceTotal(product);

        productAdapter = new ProductsListViewAdapter(this,this, getLayoutInflater(), productsList);
        stickyList.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();

        textViewTotal.setText("€ " + formatter.format(priceTotal));
    }

    public void getProducts(String orderid){
        String[] urls = new String[] {"http://mysql-test-p4.herokuapp.com/products/order/" + orderid};

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
            Log.i(TAG, "j: " + j);
            Log.i(TAG, "i: " + i);
            if(p.getCategoryId()==i){
                stickyList.setSelection(j);
                break;
            }
        }

    }
}
