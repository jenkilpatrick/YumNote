package com.yumnote.yumnote.model;

import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Created by jen on 2/15/16.
 */
public class Plan {
    private Date date;
    private List<PlannedRecipe> plannedRecipeList;
    private String key;

    public Plan() { // Default Constructor for JSON
    }

    /** Returns null if recipe not yet saved to the server. */
    @Nullable
    public String getKey() {
        return key;
    }

    /** Only accessible within this package. Should only be used by server code to update key. */
    void setKey(String key) {
        this.key = key;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<PlannedRecipe> getPlannedRecipeList() {
        return plannedRecipeList;
    }

    public void setPlannedRecipeList(List<PlannedRecipe> plannedRecipeList) {
        this.plannedRecipeList = plannedRecipeList;
    }
}
