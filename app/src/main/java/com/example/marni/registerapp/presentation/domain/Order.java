package com.example.marni.registerapp.presentation.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wallaard on 11-5-2017.
 */

public class Order implements Serializable {

    private int orderId;
    private int customerid;
    private int status;
    private String dateTime;
    private double totalPrice;
    private List<Product> products;
    private int pending;

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

