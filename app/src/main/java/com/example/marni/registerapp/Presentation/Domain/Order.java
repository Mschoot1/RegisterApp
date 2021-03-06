package com.example.marni.registerapp.Presentation.Domain;

import java.util.ArrayList;

/**
 * Created by Wallaard on 11-5-2017.
 */

public class Order {

    private int orderId;
    private String Status;
    private String dateTime;
    private double totalPrice;
    private ArrayList<Product> products;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
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

