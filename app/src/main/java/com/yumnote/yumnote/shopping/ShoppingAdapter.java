package com.yumnote.yumnote.shopping;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.yumnote.yumnote.R;
import com.yumnote.yumnote.model.DefaultValueEventListener;
import com.yumnote.yumnote.model.PlanDate;
import com.yumnote.yumnote.model.ShoppingList;
import com.yumnote.yumnote.model.ShoppingList.ShoppingListIngredient;
import com.yumnote.yumnote.model.ShoppingList.ShoppingListIngredient.PlannedRecipeInfo;
import com.yumnote.yumnote.model.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that converts planned recipe data into an aggregated list of ingredients.
 */
public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    private static final String TAG = "ShoppingAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v, boolean isEmpty) {
            super(v);
            if (isEmpty) {
                v.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlanDate startDate = new PlanDate();
                        PlanDate endDate = new PlanDate().addDays(7);
                        new Store().createShoppingList(startDate, endDate);
                    }
                });
            } else {
                this.cardView = v;
            }

        }

        public void setShoppingList(final ShoppingList shoppingList) {
            if (cardView == null) {
                return;
            }

            // Set shopping list card title.
            TextView cardTitle = (TextView) cardView.findViewById(R.id.card_title);
            String cardTitleText = String.format("%ta, %tb %te", shoppingList.getStartDateMillis(),
                    shoppingList.getStartDateMillis(), shoppingList.getStartDateMillis());
            cardTitle.setText(cardTitleText);

            // Reset the ingredients list.
            LinearLayout listLayout = (LinearLayout) cardView.findViewById(R.id.card_linear_layout);
            listLayout.removeViews(1, listLayout.getChildCount() - 1);

            // Add all the ingredients.
            if (shoppingList.getIngredients() != null) {
                for (ShoppingListIngredient ingredient : shoppingList.getIngredients()) {
                    View listItem = LayoutInflater.from(cardView.getContext())
                            .inflate(R.layout.shopping_item, listLayout, false);

                    // Set whether the item has been purchased.
                    CheckBox checkbox = (CheckBox) listItem.findViewById(R.id.checkbox);
                    checkbox.setChecked(ingredient.getPurchased());
                    checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // TODO: Update the shopping list to set the item as checked.
                        }
                    });

                    // Set the ingredient name, value, etc.
                    TextView ingredientName = (TextView) listItem.findViewById(android.R.id.text1);
                    ingredientName.setText(ingredient.getIngredient().toString());

                    // Set the recipe name(s) that it comes from.
                    TextView recipeName = (TextView) listItem.findViewById(android.R.id.text2);
                    List<String> recipeTitles = new ArrayList<>();
                    for (PlannedRecipeInfo info : ingredient.getPlannedRecipeInfoList()) {
                        recipeTitles.add(info.getTitle());
                    }
                    recipeName.setText("For " + TextUtils.join(", ", recipeTitles));

                    listLayout.addView(listItem);
                }
            }

            // Set up the remove button.
            cardView.findViewById(R.id.card_button).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Store().removeShoppingList(shoppingList);
                }
            });
        }
    }

    private static final int VIEW_TYPE_CARD = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private List<ShoppingList> shoppingLists = new ArrayList<>();

    public ShoppingAdapter() {
        startQueryForIngredients();
    }

    private void startQueryForIngredients() {
        final Firebase ref = new Firebase(Store.FIREBASE_ROOT_REF).child(Store.SHOPPING_LIST_REF);
        ref.addValueEventListener(new DefaultValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shoppingLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShoppingList shoppingList = snapshot.getValue(ShoppingList.class);
                    shoppingList.setKey(snapshot.getKey());
                    shoppingLists.add(shoppingList);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ShoppingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = viewType == VIEW_TYPE_CARD ? R.layout.shopping_card : R.layout.shopping_empty;
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v, viewType == VIEW_TYPE_EMPTY);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (!shoppingLists.isEmpty()) {
            holder.setShoppingList(shoppingLists.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return Math.max(shoppingLists.size(), 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (shoppingLists.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_CARD;
        }
    }
}
