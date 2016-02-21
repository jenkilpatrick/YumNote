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

import com.yumnote.yumnote.R;
import com.yumnote.yumnote.model.Recipe;
import com.yumnote.yumnote.planner.PlannerFragment;

public class ChooseRecipeActivity extends AppCompatActivity
        implements ChooseRecipeAdapter.RecipeSelectionListener{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private String selectedRecipeId;
    private String selectedRecipeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        findViewById(R.id.button_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PlannerFragment.EXTRA_RECIPE_KEY, selectedRecipeId);
                intent.putExtra(PlannerFragment.EXTRA_RECIPE_TITLE, selectedRecipeTitle);

                // TODO: Add ability to select number of servings
                intent.putExtra(PlannerFragment.EXTRA_RECIPE_NUM_SERVINGS, 3);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

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
        selectedRecipeId = recipe.getKey();
        selectedRecipeTitle = recipe.getName();

        ((FloatingActionButton) findViewById(R.id.fab)).hide();
        // TODO: Animate button in
        findViewById(R.id.button_select).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectionRemoved() {
        selectedRecipeId = null;
        selectedRecipeTitle = null;

        // TODO: Animate button out
        findViewById(R.id.button_select).setVisibility(View.GONE);
        ((FloatingActionButton) findViewById(R.id.fab)).show();
    }
}
