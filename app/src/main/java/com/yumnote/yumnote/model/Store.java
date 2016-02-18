package com.yumnote.yumnote.model;

import com.firebase.client.Firebase;

import java.util.Date;
import java.util.Map;

/**
 * Created by jen on 2/17/16.
 */
public class Store {
    public static final String FIREBASE_ROOT_REF = "https://popping-heat-7515.firebaseio.com/";
    public static final String RECIPE_REF = "recipe";
    public static final String PLANNED_RECIPE_REF = "planned_recipe";

    public Recipe createNewRecipe(Recipe recipe) {
        Firebase recipeRef = new Firebase(FIREBASE_ROOT_REF + RECIPE_REF);
        Firebase newRef = recipeRef.push();
        newRef.setValue(recipe);
        recipe.setKey(newRef.getKey());
        return recipe;
    }

    public PlannedRecipe addRecipeToPlan(
            Date planDate, String recipeKey, String recipeTitle, int recipeNumServings) {
        PlannedRecipe plannedRecipe = new PlannedRecipe();
        plannedRecipe.setPlanDate(planDate);
        plannedRecipe.setRecipeKey(recipeKey);
        plannedRecipe.setRecipeTitle(recipeTitle);
        plannedRecipe.setNumServings(recipeNumServings);

        Firebase plannedRecipeRef = new Firebase(FIREBASE_ROOT_REF + PLANNED_RECIPE_REF);
        Firebase newRef = plannedRecipeRef.push();
        newRef.setValue(plannedRecipe);
        plannedRecipe.setKey(newRef.getKey());
        return plannedRecipe;
    }

    public void removeRecipeFromPlan(String plannedRecipeKey) {
        Firebase plannedRecipeRef =
                new Firebase(FIREBASE_ROOT_REF).child(PLANNED_RECIPE_REF).child(plannedRecipeKey);
        plannedRecipeRef.removeValue();
    }
}
