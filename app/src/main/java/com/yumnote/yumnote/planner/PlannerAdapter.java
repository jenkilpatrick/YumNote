package com.yumnote.yumnote.planner;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yumnote.yumnote.R;
import com.yumnote.yumnote.model.Recipe;
import com.yumnote.yumnote.model.RecipeList;

import java.util.Date;

/**
 * Created by jen on 2/15/16.
 */
public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.ViewHolder> {
    public interface MenuPlannerListener {
        void onAddRecipeToDate(Date date);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private CardView mCardView;

        public ViewHolder(CardView v) {
            super(v);
            mCardView = v;
        }

        public void setDayName(String dayName) {
            TextView dayNameView = (TextView) mCardView.findViewById(R.id.card_header);
            dayNameView.setText(dayName);
        }

        public void setRecipes(RecipeList recipeList) {
            LinearLayout linearLayout =
                    (LinearLayout) mCardView.findViewById(R.id.card_linear_layout);
            linearLayout.removeViews(1, linearLayout.getChildCount() - 1);
            for (Recipe recipe : recipeList.getRecipes()) {
                addRecipeViewForName(recipe.getName(), linearLayout);
            }
        }

        private void addRecipeViewForName(final String recipeName, ViewGroup viewGroup) {
            View menuItem = LayoutInflater.from(mCardView.getContext())
                    .inflate(R.layout.menu_planner_item, viewGroup, false);
            TextView menuItemTextView = (TextView) menuItem.findViewById(R.id.menu_item_name);
            menuItemTextView.setText(recipeName);

            ImageButton removeButton = (ImageButton) menuItem.findViewById(R.id.menu_item_remove);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("PlannerAdapter", "I was clicked! " + recipeName);
                }
            });

            viewGroup.addView(menuItem);
        }
    }


    private final Context context;
    private final MenuPlannerListener menuPlannerListener;
    private final String[] dayNameArray;
    private final RecipeList[] recipeListArray;

    public PlannerAdapter(Context context, MenuPlannerListener menuPlannerListener) {
        this.context = context;
        this.menuPlannerListener = menuPlannerListener;
        this.dayNameArray = context.getResources().getStringArray(R.array.days_of_week);
        this.recipeListArray = new RecipeList[dayNameArray.length];
        for (int i = 0; i < recipeListArray.length; i++) {
            final int position = i;
            recipeListArray[i] = new RecipeList(new RecipeList.RecipeListListener() {
                @Override
                public void onRecipeUpdated() {
                    notifyItemChanged(position);
                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlannerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_planner_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setDayName(dayNameArray[position]);
        holder.setRecipes(recipeListArray[position]);

        // TODO: Move into holder
        // TODO: Unregister when view is recycled
        ImageButton addRecipeButton =
                (ImageButton) holder.mCardView.findViewById(R.id.card_add_recipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Recipe recipe = new Recipe(
//                        "Pork Ragu 2", 4, 6, new ArrayList<Ingredient>(), new ArrayList<String>());
//                recipeListArray[position].addNewRecipe(recipe);
                // TODO: Update to use correct date.
                menuPlannerListener.onAddRecipeToDate(new Date());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dayNameArray.length;
    }
}
