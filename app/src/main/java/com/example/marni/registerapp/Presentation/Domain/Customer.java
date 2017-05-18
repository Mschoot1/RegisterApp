package com.example.marni.registerapp.Presentation.Domain;

/**
 * Created by Wallaard on 17-5-2017.
 */

public class Customer {
    private String email;

    public Customer(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
