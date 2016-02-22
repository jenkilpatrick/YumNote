package com.yumnote.yumnote.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jen on 2/15/16.
 */
public class ShoppingList {
    public static class ShoppingListIngredient {
        public static class PlannedRecipeInfo {
            private String key;
            private String title;

            PlannedRecipeInfo() { } // For JSON

            PlannedRecipeInfo(String key, String title) {
                this.key = key;
                this.title = title;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        private Ingredient ingredient;
        private boolean purchased;
        private List<PlannedRecipeInfo> plannedRecipeInfoList = new ArrayList<>();

        ShoppingListIngredient() { } // Default constructor for JSON

        ShoppingListIngredient(
                Ingredient ingredient,
                boolean purchased,
                String plannedRecipeKey,
                String plannedRecipeTitle) {
            this.ingredient = ingredient;
            this.purchased = purchased;
            plannedRecipeInfoList.add(new PlannedRecipeInfo(plannedRecipeKey, plannedRecipeTitle));
        }

        public Ingredient getIngredient() {
            return ingredient;
        }

        public boolean getPurchased() {
            return purchased;
        }

        public void setPurchased(boolean purchased) {
            this.purchased = purchased;
        }

        public List<PlannedRecipeInfo> getPlannedRecipeInfoList() {
            return plannedRecipeInfoList;
        }

        public void setPlannedRecipeInfoList(List<PlannedRecipeInfo> plannedRecipeInfoList) {
            this.plannedRecipeInfoList = plannedRecipeInfoList;
        }

        public boolean canAdd(ShoppingListIngredient other) {
            return ingredient.canAdd(other.getIngredient());
        }

        public void add(ShoppingListIngredient other) {
            ingredient.add(other.getIngredient());
            plannedRecipeInfoList.addAll(other.getPlannedRecipeInfoList());
        }
    }

    private long startDateMillis;
    private long endDateMillis;
    private List<ShoppingListIngredient> ingredients;
    private Map<String, ShoppingListIngredient> itemIngredientMap;
    private String key;

    public ShoppingList() { // Default Constructor for JSON
        this.ingredients = new ArrayList<>();
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

    public List<ShoppingListIngredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(ShoppingListIngredient newIngredient) {
        if (itemIngredientMap == null) {
            itemIngredientMap = new HashMap<>();
            for (ShoppingListIngredient ingredient : ingredients) {
                itemIngredientMap.put(ingredient.getIngredient().getItem(), ingredient);
            }
        }

        String item = newIngredient.getIngredient().getItem();
        ShoppingListIngredient existingIngredient = itemIngredientMap.get(item);
        if (existingIngredient != null && existingIngredient.canAdd(newIngredient)) {
            existingIngredient.add(newIngredient);
        } else {
            ingredients.add(newIngredient);
            itemIngredientMap.put(item, newIngredient);
            sortIngredients();
        }
    }

    private void sortIngredients() {
        Collections.sort(ingredients, new Comparator<ShoppingListIngredient>() {
            @Override
            public int compare(ShoppingListIngredient lhs, ShoppingListIngredient rhs) {
                return lhs.getIngredient().getItem().compareTo(rhs.getIngredient().getItem());
            }
        });
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ShoppingList: " + getStartDateMillis();
    }
}
