package com.yumnote.yumnote.choose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.client.Firebase;
import com.yumnote.yumnote.R;
import com.yumnote.yumnote.model.Recipe;
import com.yumnote.yumnote.planner.PlannerActivity;
import com.yumnote.yumnote.planner.PlannerAdapter;

public class ChooseRecipeActivity extends AppCompatActivity
        implements ChooseRecipeAdapter.RecipeSelectionListener{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private String selectedRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Move this to Application class, or ensure called in every activity.
        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_choose_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: Change FAB icon to "add" symbol
        // TODO: Allow user to create new meals
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);

        // Switch to grid view instead?
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChooseRecipeAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        // TODO: Show a select button
        ((FloatingActionButton) findViewById(R.id.fab)).hide();
    }

    @Override
    public void onSelectionRemoved() {
        // TODO: Hide select button
        ((FloatingActionButton) findViewById(R.id.fab)).show();
    }

    // TODO: Call this method when select button is tapped.
    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra(PlannerActivity.RECIPE_ID, selectedRecipeId);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
