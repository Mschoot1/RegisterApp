package com.example.marni.registerapp.presentation.domain;

import java.io.Serializable;

/**
 * Created by MSI-PC on 26-5-2017.
 */

public class Register implements Serializable {
    private int orderId;
    private int customerId;
    private String dateTime;
    private double totalPrice;

    public Register(int orderId, String dateTime, double totalPrice, int customerId) {
        this.orderId = orderId;
        this.dateTime = dateTime;
        this.totalPrice = totalPrice;
        this.customerId = customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
