package com.abdallah.bakingapp.models.recipe;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Ingredient {

    @SerializedName("ingredient") String name;
    float quantity;
    String measure;

    public Ingredient() {
    }

    public Ingredient(String name, float quantity, String measure) {
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

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", measure='" + measure + '\'' +
                '}';
    }
}
