package com.yumnote.yumnote.planner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.yumnote.yumnote.choose.ChooseRecipeActivity;
import com.yumnote.yumnote.R;
import com.yumnote.yumnote.model.Store;

import java.util.Date;

public class PlannerActivity extends AppCompatActivity
        implements PlannerAdapter.MenuPlannerListener {
    private static final String TAG = "PlannerActivity";

    public static final String EXTRA_RECIPE_KEY = "recipeKey";
    public static final String EXTRA_RECIPE_TITLE = "recipeTitle";
    public static final String EXTRA_RECIPE_NUM_SERVINGS = "recipeNumServings";

    private static final int REQUEST_CODE_CHOOSE_RECIPE = 0;

    private Date addRecipeDate;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_planner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PlannerAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_planner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE_CHOOSE_RECIPE) : {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Returned recipe key: " + data.getStringExtra(EXTRA_RECIPE_KEY));
                    if (data.hasExtra(EXTRA_RECIPE_KEY)) {
                        String recipeKey = data.getStringExtra(EXTRA_RECIPE_KEY);
                        String recipeTitle = data.getStringExtra(EXTRA_RECIPE_TITLE);
                        int numServings = data.getIntExtra(EXTRA_RECIPE_NUM_SERVINGS, 1);
                        new Store().addRecipeToPlan(
                                addRecipeDate, recipeKey, recipeTitle, numServings);
                        addRecipeDate = null;
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onAddRecipeToDate(Date date) {
        addRecipeDate = date;
        Intent intent = new Intent(this, ChooseRecipeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_RECIPE);
    }

    @Override
    public void onRemoveRecipeFromDate(String plannedRecipeKey) {
        new Store().removeRecipeFromPlan(plannedRecipeKey);
    }
}
