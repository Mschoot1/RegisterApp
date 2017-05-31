package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AccountGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.AssortmentGetTask;
import com.example.marni.registerapp.Presentation.BusinessLogic.DrawerMenu;
import com.example.marni.registerapp.Presentation.Domain.Customer;
import com.example.marni.registerapp.Presentation.Domain.Product;
import com.example.marni.registerapp.Presentation.Presentation.Adapters.AssortmentListViewAdapter;
import com.example.marni.registerapp.Presentation.Presentation.Fragments.CategoryFragment;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

/**
 * Created by Wallaard on 30-5-2017.
 */

public class AssortmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AccountGetTask.OnAccountAvailable, AssortmentGetTask.OnProductAvailable, AdapterView.OnItemClickListener {
    private TextView account_email;
    private Button CategoryButton;

    ListView mListViewAssortment;
    AssortmentListViewAdapter assortmentListViewAdapter;
    private ArrayList<Product> mProductArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assortment);

        CategoryButton = (Button) findViewById(R.id.assortment_category_button);
        CategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

        getEmail();
        getAssortment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assortment");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_assortment);

        View headerView = navigationView.getHeaderView(0);
        account_email = (TextView)headerView.findViewById(R.id.nav_email);

        mListViewAssortment = (ListView) findViewById(R.id.listview_assortment);

        assortmentListViewAdapter = new AssortmentListViewAdapter(this, getLayoutInflater(), mProductArrayList);
        mListViewAssortment.setAdapter(assortmentListViewAdapter);
        mListViewAssortment.setOnItemClickListener(this);
    }

    public void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        CategoryFragment alertDialog = CategoryFragment.newInstance("Some title");
        alertDialog.show(fm, "fragment_alert");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
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
        account_email.setText(customer.getEmail());
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
        assortmentListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
