package com.example.marni.registerapp.presentation.domain;

/**
 * Created by Wallaard on 17-5-2017.
 */

public class Balance {
    private double customerbalance;

    public Balance(double balance) {
        this.customerbalance = balance;
    }

    public double getCustomerbalance() {
        return customerbalance;
    }

    public void setCustomerbalance(double customerbalance) {
        this.customerbalance = customerbalance;
    }
}
