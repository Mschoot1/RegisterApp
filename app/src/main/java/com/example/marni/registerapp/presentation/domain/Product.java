package com.example.marni.registerapp.presentation.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {

    private int id;
    private String name;
    private double price;
    private int size;
    private double alcoholpercentage;
    private List<Allergy> allergies = new ArrayList<>();
    private int categoryId;
    private int quantity;
    private String categoryName;
    private String imagesrc;

    public String getImagesrc() {return imagesrc;}

    public void setImagesrc(String imagesrc) {this.imagesrc = imagesrc;}

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

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

    public double getAlcoholpercentage() {
        return alcoholpercentage;
    }

    public void setAlcoholpercentage(double alcoholpercentage) {
        this.alcoholpercentage = alcoholpercentage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryid) {
        this.categoryId = categoryid;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergy> allergies) {
        this.allergies = allergies;
    }

    public void addAllergy(Allergy allergy) {
        allergies.add(allergy);
    }
}
