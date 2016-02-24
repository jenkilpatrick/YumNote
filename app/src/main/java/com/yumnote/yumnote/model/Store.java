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

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by jen on 2/17/16.
 */
public class Store {
    private static final String TAG = "Store";

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

        Firebase plannedRecipeRef = new Firebase(FIREBASE_ROOT_REF).child(PLANNED_RECIPE_REF);
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

        // TODO: Constraining by endAt() seems to exclude the wrong items from the query. Is it a
        // bug?
        Query plannedRecipeQueryRef = rootRef.child(PLANNED_RECIPE_REF)
                .orderByChild("planDateMillis")
                .startAt(startDate.getMillis());
        plannedRecipeQueryRef.addListenerForSingleValueEvent(new DefaultValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "Num planned recipes received: " + dataSnapshot.getChildrenCount());
                CountDownLatch latch = new CountDownLatch((int) dataSnapshot.getChildrenCount());
                for (DataSnapshot prSnapshot : dataSnapshot.getChildren()) {
                    PlannedRecipe plannedRecipe = prSnapshot.getValue(PlannedRecipe.class);
                    plannedRecipe.setKey(prSnapshot.getKey());
                    addIngredientsToShoppingList(shoppingList.getKey(), plannedRecipe, latch);
                }
            }
        });
    }

    private void addIngredientsToShoppingList(String shoppingListKey,
            final PlannedRecipe plannedRecipe, final CountDownLatch recipeLatch) {
        Firebase rootRef = new Firebase(FIREBASE_ROOT_REF);
        Firebase recipeRef = rootRef.child(RECIPE_REF).child(plannedRecipe.getRecipeKey());
        final Firebase shoppingListRef = rootRef.child(SHOPPING_LIST_REF).child(shoppingListKey);

        recipeRef.addListenerForSingleValueEvent(new DefaultValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot recipeSnapshot) {
                        Log.d(TAG, String.format("Recipe %s received for plannedRecipe %s",
                                plannedRecipe.getRecipeKey(), plannedRecipe.getKey()));

                        // TODO: Make this creation and key setting a method on Recipe class to
                        // avoid omitting setting the key elsewhere.
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipe.setKey(recipeSnapshot.getKey());

                        List<Ingredient> ingredientList =
                                recipe.getIngredientsForServingSize(plannedRecipe.getNumServings());

                        if (ingredientList != null) {
                            final CountDownLatch ingredientLatch =
                                    new CountDownLatch(ingredientList.size());
                            for (final Ingredient ingredient : ingredientList) {
                                shoppingListRef.runTransaction(new Handler() {
                                    @Override
                                    public Result doTransaction(MutableData currentData) {
                                        ShoppingList shoppingList =
                                                currentData.getValue(ShoppingList.class);
                                        shoppingList.addIngredient(new ShoppingListIngredient(
                                                ingredient, false, plannedRecipe.getKey(),
                                                plannedRecipe.getRecipeTitle()));
                                        currentData.setValue(shoppingList);
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(FirebaseError firebaseError, boolean b,
                                                           DataSnapshot dataSnapshot) {
                                        ingredientLatch.countDown();
                                        Log.d(TAG, "Ingredient creation is complete");
                                        if (ingredientLatch.getCount() == 0) {
                                            Log.d(TAG, "Recipe creation is complete");
                                            recipeLatch.countDown();
                                            if (recipeLatch.getCount() == 0) {
                                                // TODO: Remove progress bar.
                                                Log.d(TAG, "Shopping list creation is complete");
                                            }
                                        }
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
