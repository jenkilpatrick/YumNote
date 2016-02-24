package com.yumnote.yumnote.choose;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.yumnote.yumnote.R;
import com.yumnote.yumnote.model.Recipe;
import com.yumnote.yumnote.model.RecipeQuery;

/**
 * Created by jen on 2/15/16.
 */
public class ChooseRecipeAdapter extends RecyclerView.Adapter<ChooseRecipeAdapter.ViewHolder> {
    public interface RecipeSelectionListener {
        void onRecipeSelected(Recipe recipe, int numServings);
        void onNumServingsUpdated(int numServings);
        void onSelectionRemoved();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // TODO: Keep this class static?
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private CardView cardView;
        private View servingsChooserView;
        private NumberPicker servingsPicker;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            cardView.setClickable(true);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedItem == getLayoutPosition()) {
                        selectedItem = -1;
                        notifyItemChanged(getLayoutPosition());
                        recipeSelectionListener.onSelectionRemoved();
                    } else {
                        notifyItemChanged(selectedItem);
                        selectedItem = getLayoutPosition();
                        notifyItemChanged(selectedItem);
                        recipeSelectionListener.onRecipeSelected(
                                recipeQuery.getRecipes().get(getLayoutPosition()),
                                servingsPicker.getValue());
                    }
                }
            });
            servingsChooserView = cardView.findViewById(R.id.servings_chooser);
            servingsPicker =
                    (NumberPicker) servingsChooserView.findViewById(R.id.num_servings_picker);
            servingsPicker.setMinValue(1);
            servingsPicker.setMaxValue(50);
            servingsPicker.setOnValueChangedListener(new OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    recipeSelectionListener.onNumServingsUpdated(newVal);
                }
            });
        }

        public void setRecipe(Recipe recipe) {
            TextView recipeNameView = (TextView) cardView.findViewById(R.id.recipe_name);
            recipeNameView.setText(recipe.getName());
            servingsPicker.setValue(recipe.getMinNumServed());
        }

        public void setSelectionState(boolean selected) {
            cardView.setSelected(selected);
            servingsChooserView.setVisibility(selected ? View.VISIBLE : View.GONE);
        }
    }

    private final RecipeSelectionListener recipeSelectionListener;
    private final RecipeQuery recipeQuery;

    private int selectedItem = -1;

    public ChooseRecipeAdapter(RecipeSelectionListener recipeSelectionListener) {
        this.recipeSelectionListener = recipeSelectionListener;
        recipeQuery = new RecipeQuery(new RecipeQuery.RecipeChangeListener() {
            @Override
            public void onRecipeUpdated() {
                // TODO: Notify on particular item?
                notifyDataSetChanged();
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChooseRecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_recipe_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setRecipe(recipeQuery.getRecipes().get(position));
        holder.setSelectionState(selectedItem == position);

        // TODO: Setting the selected color this way destroys the tap animation, and the gray is too
        // dark. Fix it.
        holder.cardView.setCardBackgroundColor(
                selectedItem == position ? Color.parseColor("#eeeeee") : Color.WHITE);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recipeQuery.getRecipes().size();
    }
}
