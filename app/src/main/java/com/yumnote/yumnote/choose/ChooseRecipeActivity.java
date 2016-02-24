package com.yumnote.yumnote.choose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yumnote.yumnote.R;
import com.yumnote.yumnote.create.CreateRecipeActivity;
import com.yumnote.yumnote.model.Recipe;
import com.yumnote.yumnote.planner.PlannerFragment;

public class ChooseRecipeActivity extends AppCompatActivity
        implements ChooseRecipeAdapter.RecipeSelectionListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private String selectedRecipeId;
    private String selectedRecipeTitle;
    private int selectedRecipeNumServings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseRecipeActivity.this, CreateRecipeActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.button_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PlannerFragment.EXTRA_RECIPE_KEY, selectedRecipeId);
                intent.putExtra(PlannerFragment.EXTRA_RECIPE_TITLE, selectedRecipeTitle);
                intent.putExtra(
                        PlannerFragment.EXTRA_RECIPE_NUM_SERVINGS, selectedRecipeNumServings);
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
    public void onRecipeSelected(Recipe recipe, int numServings) {
        selectedRecipeId = recipe.getKey();
        selectedRecipeTitle = recipe.getName();
        selectedRecipeNumServings = numServings;

        ((FloatingActionButton) findViewById(R.id.fab)).hide();
        // TODO: Animate button in
        findViewById(R.id.button_select).setVisibility(View.VISIBLE);
    }

    @Override
    public void onNumServingsUpdated(int numServings) {
        selectedRecipeNumServings = numServings;
    }

    @Override
    public void onSelectionRemoved() {
        selectedRecipeId = null;
        selectedRecipeTitle = null;
        selectedRecipeNumServings = 0;

        // TODO: Animate button out
        findViewById(R.id.button_select).setVisibility(View.GONE);
        ((FloatingActionButton) findViewById(R.id.fab)).show();
    }
}
