package com.abdallah.bakingapp.models.recipe;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("ingredient") private String name;
    private int quantity;
    private String measure;

    public Ingredient(String name, int quantity, String measure) {
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
