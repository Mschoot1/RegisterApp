package com.example.marni.registerapp.Presentation;

import java.util.Date;

public class Order {
    public String price_total;
    public String timestamp;
    public int customer_id;
    public int id;
    public String status;

    public String getPrice_total() { return price_total; }
    public void setPrice_total(String price_total) { this.price_total = price_total; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public int getCustomer_id() { return customer_id; }
    public void setCustomer_id(int customer_id) { this.customer_id = customer_id; }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) { this.status = status; }
}