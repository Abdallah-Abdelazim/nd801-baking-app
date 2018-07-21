package com.abdallah.bakingapp.models.recipe;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Arrays;

@Parcel
public class Recipe {

    long id;
    String name;
    int servings;
    @SerializedName("image") String imageUrl;
    Ingredient [] ingredients;
    Step [] steps;

    public Recipe() {
        id = -1;
    }

    public Recipe(long id, String name, int servings, String imageUrl, Ingredient[] ingredients, Step[] steps) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public Step[] getSteps() {
        return steps;
    }

    public void setSteps(Step[] steps) {
        this.steps = steps;
    }

    public boolean hasImage() {
        return (imageUrl != null && !imageUrl.equals(""));
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", servings=" + servings +
                ", imageUrl='" + imageUrl + '\'' +
                ", ingredients=" + Arrays.toString(ingredients) +
                ", steps=" + Arrays.toString(steps) +
                '}';
    }
}
