package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AccountGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.AssortmentGetTask;
import com.example.marni.registerapp.Presentation.BusinessLogic.DrawerMenu;
import com.example.marni.registerapp.Presentation.Domain.Customer;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.AssortmentListViewAdapter;
import com.example.marni.registerapp.Presentation.Presentation.Fragments.CategoriesFragment;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Wallaard on 30-5-2017.
 */

public class AssortmentActivity extends AppCompatActivity implements
        CategoriesFragment.OnItemSelected, NavigationView.OnNavigationItemSelectedListener,
        AccountGetTask.OnAccountAvailable, AssortmentGetTask.OnProductAvailable,
        AdapterView.OnItemClickListener {
    private TextView account_email;
    private Button CategoryButton;
    private StickyListHeadersListView stickyList;

    private final String TAG = getClass().getSimpleName();

    private int j;

    public static final String PRODUCT = "PRODUCT";

    AssortmentListViewAdapter assortmentListViewAdapter;
    private ArrayList<Product> mProductArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);

        getEmail();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assortment");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_assortment);

        account_email = (TextView)headerView.findViewById(R.id.nav_email);

        getAssortment();
        stickyList = (StickyListHeadersListView) findViewById(R.id.listview_assortment);
        stickyList.setAreHeadersSticky(true);
        stickyList.setOnItemClickListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;

        switch (id){
            case R.id.menu_item_add_product:
                intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu_assortment, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        new DrawerMenu(getApplicationContext(), id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OnAccountAvailable (Customer customer){
        account_email.setText("284");
    }

    public void getEmail(){
        AccountGetTask accounttask = new AccountGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/account/284"};
        accounttask.execute(urls3);
    }

    public void getAssortment(){
        AssortmentGetTask assortmentGetTask = new AssortmentGetTask(this);
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/products"};
        assortmentGetTask.execute(urls);

    }

    @Override
    public void OnProductAvailable(Product product) {
        mProductArrayList.add(product);

        assortmentListViewAdapter = new AssortmentListViewAdapter(this,this, getLayoutInflater(), mProductArrayList);
        stickyList.setAdapter(assortmentListViewAdapter);
        assortmentListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product product = mProductArrayList.get(position);

        Intent intent = new Intent(getApplicationContext(), EditProductActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCT, product);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onItemSelected(int i) {
        j = 0;

        for(Product p : mProductArrayList){
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
