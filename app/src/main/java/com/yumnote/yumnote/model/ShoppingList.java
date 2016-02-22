package com.yumnote.yumnote.model;

import java.util.Date;
import java.util.Map;

/**
 * Created by jen on 2/15/16.
 */
public class ShoppingList {
    public static class ShoppingListIngredient {
        private Ingredient ingredient;
        private boolean purchased;
        private String plannedRecipeKey;

        ShoppingListIngredient() { } // Default constructor for JSON

        ShoppingListIngredient(
                Ingredient ingredient,
                boolean purchased,
                String plannedRecipeKey) {
            this.ingredient = ingredient;
            this.purchased = purchased;
            this.plannedRecipeKey = plannedRecipeKey;
        }

        public Ingredient getIngredient() {
            return ingredient;
        }

        public void setIngredient(Ingredient ingredient) {
            this.ingredient = ingredient;
        }

        public boolean getPurchased() {
            return purchased;
        }

        public void setPurchased(boolean purchased) {
            this.purchased = purchased;
        }

        public String getPlannedRecipeKey() {
            return plannedRecipeKey;
        }

        public void setPlannedRecipeKey(String plannedRecipeKey) {
            this.plannedRecipeKey = plannedRecipeKey;
        }
    }

    private long startDateMillis;
    private long endDateMillis;
    private Map<String, ShoppingListIngredient> ingredients;

    public ShoppingList() { // Default Constructor for JSON
    }

    public long getStartDateMillis() {
        return startDateMillis;
    }

    public void setStartDate(PlanDate startDate) {
        this.startDateMillis = startDate.getMillis();
    }

    public long getEndDateMillis() {
        return endDateMillis;
    }

    public void setEndDate(PlanDate endDate) {
        this.endDateMillis = endDate.getMillis();
    }

    public Map<String, ShoppingListIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, ShoppingListIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "ShoppingList: " + getStartDateMillis();
    }
}
