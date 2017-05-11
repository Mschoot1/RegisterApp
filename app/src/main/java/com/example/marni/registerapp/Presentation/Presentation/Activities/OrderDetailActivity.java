package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.Presentation.Adapters.ProductsListViewAdapter;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.AsyncKlassen.ProductGenerator;
import com.example.marni.registerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Wallaard on 9-5-2017.
 */

public class OrderDetailActivity extends AppCompatActivity implements ProductGenerator.OnAvailable {
    private ProductsListViewAdapter productAdapter;
    private ArrayList<Product> productsList = new ArrayList<>();
    private ListView productListView;
    private TextView textViewTotal;
    private double priceTotal;
    DecimalFormat formatter = new DecimalFormat("#0.00");
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        getProducts();

        textViewTotal = (TextView) findViewById(R.id.totalprice);

        productListView = (ListView) findViewById(R.id.productdetail_listview);
        productAdapter = new ProductsListViewAdapter(this, getLayoutInflater(), productsList);
        productListView.setAdapter(productAdapter);
    }

    public void OnAvailable(Product product) {
        productsList.add(product);
        productAdapter.notifyDataSetChanged();
        getPriceTotal(product);

        textViewTotal.setText("€" + formatter.format(priceTotal));

    }

    public void getProducts(){
        String[] urls = new String[] {"http://10.0.2.2:3000/"};

        ProductGenerator getProduct = new ProductGenerator(this);
        getProduct.execute(urls);
    }

    public Double getPriceTotal(Product product) {
            priceTotal = priceTotal + (product.getPrice() * product.getSize());

        return priceTotal;
    }
}
