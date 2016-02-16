package com.yumnote.yumnote;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by jen on 2/15/16.
 */
public class MenuPlannerMealAdapter extends RecyclerView.Adapter<MenuPlannerMealAdapter.ViewHolder> {
    // TODO: Replace with the real thing...
    private static final String[] FOOD_ITEMS = {"Other", "Pork Ragu"};

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private LinearLayout mItemView;

        public ViewHolder(LinearLayout v) {
            super(v);
            mItemView = v;
        }

        public void setFoodName(String dayName) {
//            TextView nameView = (TextView) mItemView.findViewById(R.id.card_meal_item_name);
//            nameView.setText(dayName);
        }
    }

    private Context mContext;
    private String[] mDataset;

    public MenuPlannerMealAdapter(Context context) {
        mContext = context;
        mDataset = FOOD_ITEMS;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MenuPlannerMealAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_planner_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setFoodName(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
