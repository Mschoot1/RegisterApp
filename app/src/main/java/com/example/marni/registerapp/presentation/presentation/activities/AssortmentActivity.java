package com.example.marni.registerapp.presentation.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;

import com.example.marni.registerapp.presentation.asyncklassen.AccountGetTask;
import com.example.marni.registerapp.presentation.asyncklassen.AssortmentGetTask;
import com.example.marni.registerapp.presentation.businesslogic.DrawerMenu;
import com.example.marni.registerapp.presentation.domain.Customer;
import com.example.marni.registerapp.presentation.domain.Product;
import com.example.marni.registerapp.presentation.presentation.adapters.AssortmentListViewAdapter;
import com.example.marni.registerapp.presentation.presentation.fragments.CategoriesFragment;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AssortmentActivity extends AppCompatActivity implements
        CategoriesFragment.OnItemSelected, NavigationView.OnNavigationItemSelectedListener,
        AccountGetTask.OnAccountAvailable, AssortmentGetTask.OnProductAvailable,
        AdapterView.OnItemClickListener {
    private TextView accountemail;
    private StickyListHeadersListView stickyList;

    private final String tag = getClass().getSimpleName();

    public static final String PRODUCT = "PRODUCT";

    AssortmentListViewAdapter assortmentListViewAdapter;
    private ArrayList<Product> mProductArrayList = new ArrayList<>();

    public static final String JWT_STR = "jwt_str";
    public static final String USER_KEY = "user";
    String jwt;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        jwt = prefs.getString(JWT_STR, "");
        user = prefs.getString(USER_KEY, "");

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

        accountemail = (TextView)headerView.findViewById(R.id.nav_email);

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

        if(id == R.id.menu_item_add_product){
                intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(intent);
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

    public void onAccountAvailable (Customer customer){
        accountemail.setText(user);
    }

    public void getEmail(){
        AccountGetTask accounttask = new AccountGetTask(this);
        String[] urls3 = new String[]{"https://mysql-test-p4.herokuapp.com/account/" + user, jwt};
        accounttask.execute(urls3);
    }

    public void getAssortment(){
        AssortmentGetTask assortmentGetTask = new AssortmentGetTask(this);
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/products", jwt};
        assortmentGetTask.execute(urls);

    }

    @Override
    public void onProductAvailable(Product product) {
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
        int j = 0;

        for(Product p : mProductArrayList){
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
