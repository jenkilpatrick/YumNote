package com.yumnote.yumnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.yumnote.yumnote.choose.ChooseRecipeActivity;
import com.yumnote.yumnote.model.Ingredient;
import com.yumnote.yumnote.model.PlanDate;
import com.yumnote.yumnote.model.Recipe;
import com.yumnote.yumnote.model.Store;
import com.yumnote.yumnote.planner.PlannerAdapter;
import com.yumnote.yumnote.planner.PlannerFragment;
import com.yumnote.yumnote.shopping.ShoppingFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements PlannerAdapter.MenuPlannerListener{
    private static final String TAG = "MainActivity";

    public static final String EXTRA_RECIPE_KEY = "recipeKey";
    public static final String EXTRA_RECIPE_TITLE = "recipeTitle";
    public static final String EXTRA_RECIPE_NUM_SERVINGS = "recipeNumServings";

    private static final int REQUEST_CODE_CHOOSE_RECIPE = 0;

    private PlanDate addRecipeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter sectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        final ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        // Set up tabs.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabsFromPagerAdapter(sectionsPagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Ignore
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Ignore
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        } else if (id == R.id.action_create_recipe) {
            Recipe recipe = new Recipe();
            recipe.setName("New recipe " + new Random().nextInt(10));
            recipe.setMinNumServed(2);
            recipe.setMinNumServed(6);

            List<String> instructionList = new ArrayList<>();
            instructionList.add("instruction1");
            instructionList.add("instruction2");
            recipe.setInstructions(instructionList);

            List<Ingredient> ingredientList = new ArrayList<>();
            ingredientList.add(new Ingredient(5, "cups", "flour"));
            ingredientList.add(new Ingredient(2, "cups", "sugar"));
            recipe.setIngredients(ingredientList);

            new Store().createNewRecipe(recipe);
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
    public void onAddRecipeToDate(PlanDate date) {
        addRecipeDate = date;
        Intent intent = new Intent(this, ChooseRecipeActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_RECIPE);
    }

    @Override
    public void onRemoveRecipeFromDate(String plannedRecipeKey) {
        new Store().removeRecipeFromPlan(plannedRecipeKey);
    }

    // TODO: Look at memory usage and switch to FragmentStatePagerAdapter if necessary.
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PlannerFragment.newInstance();
                case 1:
                    return ShoppingFragment.newInstance();
            }
            return null;
        }

        /** Returns the number of pages to show. */
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Plan";
                case 1:
                    return "Shop";
            }
            return null;
        }
    }
}
