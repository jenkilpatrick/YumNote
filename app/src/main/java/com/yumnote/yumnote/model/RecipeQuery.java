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
public class RecipeQuery {
    private static final String TAG = "RecipeQuery";

    public interface RecipeChangeListener {
        void onRecipeUpdated();
    }

    private final RecipeChangeListener recipeChangeListener;
    private final List<Recipe> recipeList = new ArrayList<>();
    private final Firebase recipeRef;

    public RecipeQuery(RecipeChangeListener recipeChangeListener) {
        this.recipeChangeListener = recipeChangeListener;
        recipeRef = new Firebase(Store.FIREBASE_ROOT_REF + Store.RECIPE_REF);
        startQueryForDay();
    }

    public void startQueryForDay() {
        recipeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "There are " + snapshot.getChildrenCount() + " recipes");
                recipeList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    // TODO: Key isn't getting set here
                    Recipe newRecipe = postSnapshot.getValue(Recipe.class);
                    newRecipe.setKey(postSnapshot.getKey());
                    recipeList.add(newRecipe);
                    Log.d(TAG, "Updated recipe:" + newRecipe.getName());
                }
                recipeChangeListener.onRecipeUpdated();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, "The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public List<Recipe> getRecipes() {
        return recipeList;
    }
}
