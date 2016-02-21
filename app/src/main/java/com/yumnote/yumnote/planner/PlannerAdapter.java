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
import com.yumnote.yumnote.model.PlanQuery;
import com.yumnote.yumnote.model.PlannedRecipe;
import com.yumnote.yumnote.model.Recipe;
import com.yumnote.yumnote.model.RecipeQuery;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jen on 2/15/16.
 */
public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.ViewHolder> {
    public interface MenuPlannerListener {
        void onAddRecipeToDate(Date date);
        void onRemoveRecipeFromDate(String plannedRecipeKey);
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

        public void setRecipes(
                final PlanQuery recipeQuery, final MenuPlannerListener menuPlannerListener) {
            LinearLayout linearLayout =
                    (LinearLayout) mCardView.findViewById(R.id.card_linear_layout);
            linearLayout.removeViews(1, linearLayout.getChildCount() - 1);

            for (final PlannedRecipe plannedRecipe : recipeQuery.getPlannedRecipes()) {
                View menuItem = LayoutInflater.from(mCardView.getContext())
                        .inflate(R.layout.menu_planner_item, linearLayout, false);
                TextView menuItemTextView = (TextView) menuItem.findViewById(R.id.menu_item_name);

                // TODO: Instead of concatenating, move to string resource.
                menuItemTextView.setText(
                        plannedRecipe.getRecipeTitle() + " for " + plannedRecipe.getNumServings());

                ImageButton removeButton =
                        (ImageButton) menuItem.findViewById(R.id.menu_item_remove);
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuPlannerListener.onRemoveRecipeFromDate(plannedRecipe.getKey());
                    }
                });

                linearLayout.addView(menuItem);
            }
        }
    }

    private final Context context;
    private final MenuPlannerListener menuPlannerListener;
    private final Date[] dateArray;
    private final PlanQuery[] planQueryArray;

    private static final int N_DAYS_TO_SHOW = 3;

    public PlannerAdapter(Context context, MenuPlannerListener menuPlannerListener) {
        this.context = context;
        this.menuPlannerListener = menuPlannerListener;
        this.dateArray = new Date[N_DAYS_TO_SHOW];

        // TODO: This is error-prone. Use custom object to hold day/month/year since that's all I
        // want.
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        for (int i = 0; i < dateArray.length; i++) {
            dateArray[i] = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
        }

        this.planQueryArray = new PlanQuery[dateArray.length];
        for (int i = 0; i < planQueryArray.length; i++) {
            final int position = i;
            planQueryArray[i] = new PlanQuery(dateArray[i], new PlanQuery.RecipeChangeListener() {
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

        Calendar c = Calendar.getInstance();
        c.setTime(dateArray[position]);

        // UGH. Ugly. Change this to a for loop or move somewhere else.
        int dayIndex = 0;
        switch(c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                dayIndex = 0;
                break;
            case Calendar.MONDAY:
                dayIndex = 1;
                break;
            case Calendar.TUESDAY:
                dayIndex = 2;
                break;
            case Calendar.WEDNESDAY:
                dayIndex = 3;
                break;
            case Calendar.THURSDAY:
                dayIndex = 4;
                break;
            case Calendar.FRIDAY:
                dayIndex = 5;
                break;
            case Calendar.SATURDAY:
                dayIndex = 6;
                break;
        }
        String dayName = context.getResources().getStringArray(R.array.days_of_week)[dayIndex];

        holder.setDayName(dayName);
        holder.setRecipes(planQueryArray[position], menuPlannerListener);

        // TODO: Move into holder
        // TODO: Unregister when view is recycled
        ImageButton addRecipeButton =
                (ImageButton) holder.mCardView.findViewById(R.id.card_add_recipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Update to use correct date.
                menuPlannerListener.onAddRecipeToDate(dateArray[position]);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dateArray.length;
    }
}
