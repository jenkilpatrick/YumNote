package com.yumnote.yumnote.create;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.yumnote.yumnote.R;
import com.yumnote.yumnote.model.Ingredient;
import com.yumnote.yumnote.model.Recipe;
import com.yumnote.yumnote.model.Store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity {

    private EditText recipeTitle;
    private NumberPicker numberPickerMin;
    private NumberPicker numberPickerMax;
    private EditText ingredients;
    private EditText instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recipeTitle = (EditText) findViewById(R.id.recipe_name);
        ingredients = (EditText) findViewById(R.id.ingredients);
        instructions = (EditText) findViewById(R.id.instructions);

        numberPickerMin = (NumberPicker) findViewById(R.id.numberPickerMin);
        numberPickerMin.setMinValue(1);
        numberPickerMin.setMaxValue(50);
        numberPickerMin.setValue(4);

        numberPickerMax = (NumberPicker) findViewById(R.id.numberPickerMax);
        numberPickerMax.setMinValue(1);
        numberPickerMax.setMaxValue(50);
        numberPickerMax.setValue(6);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            // TODO: Sanity check and sanitize values
            Recipe recipe = new Recipe();
            recipe.setName(recipeTitle.getText().toString());
            recipe.setMinNumServed(numberPickerMin.getValue());
            recipe.setMaxNumServed(numberPickerMax.getValue());

            // Parse instructions by taking one per line.
            recipe.setInstructions(Arrays.asList(instructions.getText().toString().split("\n")));

            // TODO: Make this much easier to input and parse (dedicated fields in form?).
            // Parse ingredients from text field.
            List<Ingredient> ingredientList = new ArrayList<>();
            for (String ingredientText : ingredients.getText().toString().split("\n")) {
                String[] parts = ingredientText.split(" ");
                ingredientList.add(new Ingredient(Integer.valueOf(parts[0]), parts[1], parts[2]));
            }
            recipe.setIngredients(ingredientList);

            new Store().createNewRecipe(recipe);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
