package com.yumnote.yumnote.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jen on 2/15/16.
 */
public class Recipe {
    private String key;
    private String name;
    private int minNumServed;
    private int maxNumServed;
    private List<Ingredient> ingredients;
    private List<String> instructions;

    public Recipe() { // Default Constructor for JSON
    }

    /** Returns null if recipe not yet saved to the server. */
    @Nullable
    public String getKey() {
        return key;
    }

    /** Only accessible within this package. Should only be used by server code to update key. */
    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public int getMinNumServed() {
        return minNumServed;
    }

    public void setMinNumServed(int minNumServed) {
        this.minNumServed = minNumServed;
    }

    public int getMaxNumServed() {
        return maxNumServed;
    }

    public void setMaxNumServed(int maxNumServed) {
        this.maxNumServed = maxNumServed;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
}
