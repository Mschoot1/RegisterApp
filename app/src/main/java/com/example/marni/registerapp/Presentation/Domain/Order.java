package com.example.marni.registerapp.Presentation.Domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Wallaard on 11-5-2017.
 */

public class Order implements Serializable {

    private int orderId;
    private int customerid;
    private int Status;
    private String dateTime;
    private double totalPrice;
    private ArrayList<Product> products;

    public Order(int orderId, int status, String dateTime, double totalPrice, int customerid) {
        this.orderId = orderId;
        Status = status;
        this.dateTime = dateTime;
        this.totalPrice = totalPrice;
        this.customerid = customerid;
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
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
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

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}

