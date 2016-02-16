package com.yumnote.yumnote.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jen on 2/15/16.
 */
public class Recipe {
    public static final Recipe DEFAULT_RECIPE = new Recipe(
            "Pork Ragu 2", 4, 6, new ArrayList<Ingredient>(), new ArrayList<String>());

    @Nullable
    private String name;
    private int minNumServed;
    private int maxNumServed;

    @Nullable
    private List<Ingredient> ingredients;

    @Nullable
    private List<String> instructions;

    @Nullable
    private String id;

    public Recipe() { // Default Constructor for JSON
    }

    public Recipe(
            String name,
            int minNumServed,
            int maxNumServed,
            List<Ingredient> ingredients,
            List<String> instructions) {
        if (ingredients == null || instructions == null) {
            throw new IllegalArgumentException("Null value when initializing Recipe");
        }

        this.name = name;
        this.minNumServed = minNumServed;
        this.maxNumServed = maxNumServed;
        this.ingredients = ingredients;
        this.instructions = instructions;
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

    /** Returns null if recipe not yet saved to the server. */
    @Nullable
    public String getId() {
        return id;
    }

    /** Only accessible within this package. Should only be used by server code to update id. */
    void setId(String id) {
        this.id = id;
    }
}
