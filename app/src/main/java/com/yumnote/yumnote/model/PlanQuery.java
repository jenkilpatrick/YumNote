package com.yumnote.yumnote.model;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jen on 2/15/16.
 */
public class PlanQuery {
    private static final String TAG = "RecipeQuery";

    public interface RecipeChangeListener {
        void onRecipeUpdated();
    }

    private final List<PlannedRecipe> plannedRecipeList = new ArrayList<>();

    private final Date planDate;

    public PlanQuery(Date planDate, final RecipeChangeListener recipeChangeListener) {
        this.planDate = planDate;

        Firebase ref = new Firebase(Store.FIREBASE_ROOT_REF + Store.PLANNED_RECIPE_REF);

        Log.e(TAG, "Plan date: " + planDate.getTime());
        Query queryRef = ref.orderByChild("planDate").equalTo(planDate.getTime());
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "There are " + snapshot.getChildrenCount() + " planned recipes");
                plannedRecipeList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    PlannedRecipe plannedRecipe = postSnapshot.getValue(PlannedRecipe.class);
                    plannedRecipe.setKey(postSnapshot.getKey());
                    plannedRecipeList.add(plannedRecipe);
                    Log.d(TAG, "Updated recipe:" + plannedRecipe.getRecipeTitle());
                }
                recipeChangeListener.onRecipeUpdated();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, "The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public Date getPlanDate() {
        return planDate;
    }

    public List<PlannedRecipe> getPlannedRecipes() {
        return plannedRecipeList;
    }
}
