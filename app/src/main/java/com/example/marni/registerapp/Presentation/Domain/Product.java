package com.example.marni.registerapp.Presentation.Domain;

import java.util.ArrayList;

/**
 * Created by Wallaard on 9-5-2017.
 */

public class Product {
    private String name;
    private double price;
    private int size;
    private double alcohol_percentage;
    private String category;
    private ArrayList allergies;
    private int categoryid;
    private double totalprice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getAlcohol_percentage() {
        return alcohol_percentage;
    }

    public void setAlcohol_percentage(double alcohol_percentage) {
        this.alcohol_percentage = alcohol_percentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryId() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public ArrayList getAllergies() {
        return allergies;
    }

    public void setAllergies(ArrayList allergies) {
        this.allergies = allergies;
    }

}
