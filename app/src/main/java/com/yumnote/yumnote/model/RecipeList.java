package com.yumnote.yumnote.model;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jen on 2/15/16.
 */
public class RecipeList {
    private static final String TAG = "RecipeList";

    public static final String FIREBASE_ROOT_REF = "https://popping-heat-7515.firebaseio.com/";
    public static final String RECIPE_REF = "recipe";

    public interface RecipeListListener {
        void onRecipeUpdated();
    }

    private final RecipeListListener recipeListListener;
    private final List<Recipe> recipeList = new ArrayList<>();
    private final Firebase recipeRef;

    public RecipeList(RecipeListListener recipeListListener) {
        this.recipeListListener = recipeListListener;
        recipeRef = new Firebase(FIREBASE_ROOT_REF + RECIPE_REF);
        startQueryForDay();
    }

    public void startQueryForDay() {
        recipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "There are " + snapshot.getChildrenCount() + " recipes");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Recipe newRecipe = postSnapshot.getValue(Recipe.class);
                    if (!recipeList.contains(newRecipe)) {
                        recipeList.add(newRecipe);
                        notifyListeners();
                    }
                    Log.d(TAG, "Updated recipe:" + newRecipe.getName());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public List<Recipe> getRecipes() {
        return recipeList;
    }

    public void addNewRecipe(Recipe recipe) {
        Firebase newRecipeRef = recipeRef.push();
        newRecipeRef.setValue(recipe);
        recipe.setId(newRecipeRef.getKey());
        recipeList.add(recipe);
        notifyListeners();
    }

    private void notifyListeners() {
        recipeListListener.onRecipeUpdated();
    }
}
