package com.example.marni.registerapp.Presentation.Domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Wallaard on 11-5-2017.
 */

public class Order implements Serializable {

    private int orderId;
    private int customerid;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }
}

