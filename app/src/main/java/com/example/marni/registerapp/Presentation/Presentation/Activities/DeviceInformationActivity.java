package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AccountGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.DeviceInformationGetTask;
import com.example.marni.registerapp.Presentation.Domain.Customer;
import com.example.marni.registerapp.Presentation.Domain.Deviceinformation;
import com.example.marni.registerapp.Presentation.Domain.Order;
import com.example.marni.registerapp.Presentation.Domain.Register;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

import static com.example.marni.registerapp.R.string.customerid;
import static com.example.marni.registerapp.R.string.orderid;

/**
 * Created by Wallaard on 16-5-2017.
 */

public class DeviceInformationActivity extends AppCompatActivity implements DeviceInformationGetTask.OnDeviceInformationAvailable,
        AccountGetTask.OnAccountAvailable, NavigationView.OnNavigationItemSelectedListener {
    private TextView email,hardware,type,model,brand,device,manufacturer,user,serial,host,id,bootloader,board,display;
    private final String TAG = getClass().getSimpleName();
    private String customerid;
    private Register register;
    private int order;
    public static final String CUSTOMERID = "CUSTOMERID";
    public static final String REGISTER = "REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);

        Intent intent = getIntent();
        String checkFlag = intent.getStringExtra("flag");

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
    public void OnDeviceInformationAvailable(Deviceinformation deviceinformation) {
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
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/customer/"+customerid+"/device/"};
        Log.i(TAG,customerid+"customerid");

        DeviceInformationGetTask g = new DeviceInformationGetTask(this);
        g.execute(urls);
    }

    public void OnAccountAvailable (Customer customer){
        email.setText(customer.getEmail());
    }

    public void getAccount(){
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/account/"+customerid};
        Log.i(TAG,customerid+"customerid");

        AccountGetTask e = new AccountGetTask(this);
        e.execute(urls);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG,"TESTEN HAHA");
        int id = item.getItemId();
        if (id == android.R.id.home) {
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
