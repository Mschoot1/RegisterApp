package com.example.marni.registerapp.Presentation.BusinessLogic;

import com.example.marni.registerapp.Presentation.Domain.Product;

import java.util.ArrayList;

/**
 * Created by Wallaard on 11-5-2017.
 */

public class CalculatePriceOrder {
    private ArrayList<Product> products;
    private double priceTotal = 0;

    public CalculatePriceOrder(ArrayList products) {
        this.products = products;
    }


}
