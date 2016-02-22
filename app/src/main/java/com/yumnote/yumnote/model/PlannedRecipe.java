package com.yumnote.yumnote.model;

import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Created by jen on 2/15/16.
 */
public class PlannedRecipe {
    private String key;
    private long planDateMillis;
    private String recipeKey;
    private String recipeTitle;
    private int numServings;

    public PlannedRecipe() { // Default Constructor for JSON
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

    public long getPlanDateMillis() {
        return planDateMillis;
    }

    public void setPlanDate(PlanDate planDate) {
        this.planDateMillis = planDate.getMillis();
    }

    public String getRecipeKey() {
        return recipeKey;
    }

    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public int getNumServings() {
        return numServings;
    }

    public void setNumServings(int numServings) {
        this.numServings = numServings;
    }
}
