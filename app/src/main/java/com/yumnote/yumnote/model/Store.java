package com.yumnote.yumnote.model;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.Date;

/**
 * Created by jen on 2/17/16.
 */
public class Store {
    public static final String FIREBASE_ROOT_REF = "https://popping-heat-7515.firebaseio.com/";
    public static final String RECIPE_REF = "recipe";
    public static final String PLANNED_RECIPE_REF = "planned_recipe";
    public static final String SHOPPING_LIST_REF = "shopping_list";

    public Recipe createNewRecipe(Recipe recipe) {
        Firebase recipeRef = new Firebase(FIREBASE_ROOT_REF + RECIPE_REF);
        Firebase newRef = recipeRef.push();
        newRef.setValue(recipe);
        recipe.setKey(newRef.getKey());
        return recipe;
    }

    public void addRecipeToPlan(
            PlanDate planDate, String recipeKey, String recipeTitle, int recipeNumServings) {
        PlannedRecipe plannedRecipe = new PlannedRecipe();
        plannedRecipe.setPlanDate(planDate);
        plannedRecipe.setRecipeKey(recipeKey);
        plannedRecipe.setRecipeTitle(recipeTitle);
        plannedRecipe.setNumServings(recipeNumServings);

        Firebase plannedRecipeRef = new Firebase(FIREBASE_ROOT_REF + PLANNED_RECIPE_REF);
        Firebase newRef = plannedRecipeRef.push();
        newRef.setValue(plannedRecipe);
        plannedRecipe.setKey(newRef.getKey());
    }

    public void removeRecipeFromPlan(String plannedRecipeKey) {
        Firebase plannedRecipeRef =
                new Firebase(FIREBASE_ROOT_REF).child(PLANNED_RECIPE_REF).child(plannedRecipeKey);
        plannedRecipeRef.removeValue();
    }

    public void createShoppingList(PlanDate startDate, PlanDate endDate) {
        final Firebase rootRef = new Firebase(FIREBASE_ROOT_REF);

        // Create new shopping list item
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setStartDate(startDate);
        shoppingList.setEndDate(endDate);
        final Firebase newRef = rootRef.child(SHOPPING_LIST_REF).push();
        newRef.setValue(shoppingList);

        // Look up all relevant ingredients and create shopping list ingredients.
        Query plannedRecipeQueryRef = rootRef.child(PLANNED_RECIPE_REF).orderByChild("planDateMillis")
                .startAt(startDate.getMillis()).endAt(endDate.getMillis());
        plannedRecipeQueryRef.addListenerForSingleValueEvent(new DefaultValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot plannedRecipeSnapshot : dataSnapshot.getChildren()) {
                    final PlannedRecipe plannedRecipe =
                            plannedRecipeSnapshot.getValue(PlannedRecipe.class);
                    final String plannedRecipeKey = plannedRecipeSnapshot.getKey();
                    rootRef.child(RECIPE_REF).child(plannedRecipe.getRecipeKey())
                            .addListenerForSingleValueEvent(new DefaultValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot recipeSnapshot) {
                                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                                    if (recipe.getIngredients() != null) {
                                        for (Ingredient ingredient : recipe.getIngredients()) {
                                            Firebase ingredientRef =
                                                    newRef.child("ingredients").push();
                                            ingredientRef.setValue(
                                                    new ShoppingList.ShoppingListIngredient(
                                                            ingredient, false, plannedRecipeKey));

                                        }
                                    }
                                }
                            });
                }
            }
        });
    }
}
