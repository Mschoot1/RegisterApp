package com.example.marni.registerapp.Presentation.Presentation.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.marni.registerapp.Presentation.AsyncKlassen.AccountGetTask;
import com.example.marni.registerapp.Presentation.AsyncKlassen.DeviceInformationGetTask;
import com.example.marni.registerapp.Presentation.Domain.Customer;
import com.example.marni.registerapp.Presentation.Domain.Deviceinformation;
import com.example.marni.registerapp.R;

import java.util.ArrayList;

/**
 * Created by Wallaard on 16-5-2017.
 */

public class DeviceInformationActivity extends AppCompatActivity implements DeviceInformationGetTask.OnDeviceInformationAvailable, AccountGetTask.OnAccountAvailable {
    private TextView email,hardware,type,model,brand,device,manufacturer,user,serial,host,id,bootloader,board,display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_information);

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
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/customer/284/device/"};

        DeviceInformationGetTask g = new DeviceInformationGetTask(this);
        g.execute(urls);
    }

    public void OnAccountAvailable (Customer customer){
        email.setText(customer.getEmail());
    }

    public void getAccount(){
        String[] urls = new String[]{"https://mysql-test-p4.herokuapp.com/account/284"};

        AccountGetTask e = new AccountGetTask(this);
        e.execute(urls);
    }
}
