package com.example.marni.registerapp.presentation.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.marni.registerapp.presentation.asyncklassen.AccountGetTask;
import com.example.marni.registerapp.presentation.asyncklassen.DeviceInformationGetTask;
import com.example.marni.registerapp.presentation.domain.Customer;
import com.example.marni.registerapp.presentation.domain.Deviceinformation;
import com.example.marni.registerapp.presentation.domain.Register;
import com.example.marni.registerapp.R;


/**
 * Created by Wallaard on 16-5-2017.
 */

public class DeviceInformationActivity extends AppCompatActivity implements DeviceInformationGetTask.OnDeviceInformationAvailable,
        AccountGetTask.OnAccountAvailable, NavigationView.OnNavigationItemSelectedListener {
    private TextView email;
    private TextView hardware;
    private TextView type;
    private TextView model;
    private TextView brand;
    private TextView device;
    private TextView manufacturer;
    private TextView user;
    private TextView serial;
    private TextView host;
    private TextView id;
    private TextView bootloader;
    private TextView board;
    private TextView display;

    private final String tag = getClass().getSimpleName();
    private String customerid;
    public static final String CUSTOMERID = "CUSTOMERID";
    public static final String REGISTER = "REGISTER";

    public static final String JWT_STR = "jwt_str";
    String jwt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        jwt = prefs.getString(JWT_STR, "");

        Intent intent = getIntent();
        String checkFlag = intent.getStringExtra("flag");

        Register register;
        int order;

        if(checkFlag.equals("H")) {
            register = (Register) intent.getSerializableExtra("REGISTER");
            customerid = String.valueOf(register.getCustomerId());
        } else if (checkFlag.equals("O")){
            order = (int) intent.getSerializableExtra("CUSTOMERID");
            customerid = String.valueOf(order);
        }

        getDeviceInformation();
        getAccount();

        email = (TextView)findViewById(R.id.device_customer);
        hardware = (TextView)findViewById(R.id.device_hardware);
        type = (TextView)findViewById(R.id.device_type);
        model = (TextView)findViewById(R.id.device_model);
        brand = (TextView)findViewById(R.id.device_brand);
        device = (TextView)findViewById(R.id.device_device);
        manufacturer = (TextView)findViewById(R.id.device_manufacturer);
        user = (TextView)findViewById(R.id.device_user);
        serial = (TextView)findViewById(R.id.device_serial);
        host = (TextView)findViewById(R.id.device_host);
        id = (TextView)findViewById(R.id.device_id);
        bootloader = (TextView)findViewById(R.id.device_bootloader);
        board = (TextView)findViewById(R.id.device_board);
        display = (TextView)findViewById(R.id.device_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Device Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDeviceInformationAvailable(Deviceinformation deviceinformation) {
        hardware.setText(deviceinformation.getHardware());
        type.setText(deviceinformation.getType());
        model.setText(deviceinformation.getModel());
        brand.setText(deviceinformation.getBrand());
        device.setText(deviceinformation.getDevice());
        manufacturer.setText(deviceinformation.getManufacturer());
        user.setText(deviceinformation.getUser());
        serial.setText(deviceinformation.getSerial());
        host.setText(deviceinformation.getHost());
        id.setText(deviceinformation.getId());
        bootloader.setText(deviceinformation.getBootloader());
        board.setText(deviceinformation.getBoard());
        display.setText(deviceinformation.getDisplay());
    }

    public void getDeviceInformation(){
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/customer/"+customerid+"/device/", jwt};
        Log.i(tag,customerid+"customerid");

        DeviceInformationGetTask g = new DeviceInformationGetTask(this);
        g.execute(urls);
    }

    public void onAccountAvailable (Customer customer){
        email.setText(customer.getEmail());
    }

    public void getAccount(){
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/account/"+customerid, jwt};
        Log.i(tag,customerid+"customerid");

        AccountGetTask e = new AccountGetTask(this);
        e.execute(urls);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if (itemid == android.R.id.home) {
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }


}
