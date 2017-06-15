package com.example.marni.registerapp.presentation.domain;

/**
 * Created by Wallaard on 17-5-2017.
 */

public class Customer {
    private String email;
    private int balance;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
