package com.yumnote.yumnote.model;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Query;
import com.firebase.client.Transaction;
import com.firebase.client.Transaction.Handler;
import com.firebase.client.Transaction.Result;
import com.yumnote.yumnote.model.ShoppingList.ShoppingListIngredient;

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
        final ShoppingList shoppingList = new ShoppingList();
        shoppingList.setStartDate(startDate);
        shoppingList.setEndDate(endDate);

        Firebase newRef = rootRef.child(SHOPPING_LIST_REF).push();
        newRef.setValue(shoppingList);
        shoppingList.setKey(newRef.getKey());

        // Look up all relevant ingredients and create shopping list ingredients.
        Log.e("Store", "Start millis: " + startDate.getMillis() + " " + "End millis: "
                + endDate.getMillis());

        // TODO: This -1 +1 nonsense is a hack that fixes an issue where startAt() only returns a
        // single item with that planDateMillis value, rather than all items with that value. Fix.
        Query plannedRecipeQueryRef = rootRef.child(PLANNED_RECIPE_REF)
                .orderByChild("planDateMillis")
                .startAt(startDate.getMillis() - 1).endAt(endDate.getMillis() + 1);
        plannedRecipeQueryRef.addListenerForSingleValueEvent(new DefaultValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot prSnapshot : dataSnapshot.getChildren()) {
                    PlannedRecipe plannedRecipe = prSnapshot.getValue(PlannedRecipe.class);
                    String plannedRecipeKey = prSnapshot.getKey();
                    addIngredientsToShoppingList(
                            shoppingList.getKey(), plannedRecipe.getRecipeKey(), plannedRecipeKey,
                            plannedRecipe.getRecipeTitle());
                }
            }
        });
    }

    private void addIngredientsToShoppingList(String shoppingListKey, String recipeKey,
            final String plannedRecipeKey, final String plannedRecipeTitle) {
        Firebase rootRef = new Firebase(FIREBASE_ROOT_REF);
        Firebase recipeRef = rootRef.child(RECIPE_REF).child(recipeKey);
        final Firebase shoppingListRef = rootRef.child(SHOPPING_LIST_REF).child(shoppingListKey);

        recipeRef.addListenerForSingleValueEvent(new DefaultValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot recipeSnapshot) {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        if (recipe.getIngredients() != null) {
                            for (final Ingredient ingredient : recipe.getIngredients()) {
                                shoppingListRef.runTransaction(new Handler() {
                                    @Override
                                    public Result doTransaction(MutableData currentData) {
                                        ShoppingList shoppingList =
                                                currentData.getValue(ShoppingList.class);
                                        shoppingList.addIngredient(new ShoppingListIngredient(
                                                ingredient, false, plannedRecipeKey,
                                                plannedRecipeTitle));
                                        currentData.setValue(shoppingList);
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(FirebaseError firebaseError, boolean b,
                                                           DataSnapshot dataSnapshot) {
                                        // Ignore
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public void removeShoppingList(ShoppingList shoppingList) {
        Firebase ref = new Firebase(FIREBASE_ROOT_REF).child(SHOPPING_LIST_REF)
                .child(shoppingList.getKey());
        ref.removeValue();
    }

    public void setShoppingListIngredientState(
            ShoppingList shoppingList, int position, boolean purchased) {
        Firebase ref = new Firebase(FIREBASE_ROOT_REF).child(SHOPPING_LIST_REF)
                .child(shoppingList.getKey())
                .child("ingredients")
                .child(String.valueOf(position))
                .child("purchased");
        ref.setValue(purchased);
    }
}
