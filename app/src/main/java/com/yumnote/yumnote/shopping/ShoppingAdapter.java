package com.yumnote.yumnote.shopping;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.yumnote.yumnote.R;
import com.yumnote.yumnote.model.DefaultValueEventListener;
import com.yumnote.yumnote.model.ShoppingList;
import com.yumnote.yumnote.model.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter that converts planned recipe data into an aggregated list of ingredients.
 */
public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    private static final String TAG = "ShoppingAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            this.cardView = v;
        }

        public void setShoppingList(ShoppingList shoppingList) {
            // TODO: Fix date formatting.
            ((TextView) cardView.findViewById(R.id.card_title)).setText(
                    "List from " + shoppingList.getStartDateMillis() + " to " + shoppingList.getEndDateMillis());
            LinearLayout linearLayout =
                    (LinearLayout) cardView.findViewById(R.id.card_linear_layout);
            linearLayout.removeViews(1, linearLayout.getChildCount() - 1);

            if (shoppingList.getIngredients() != null) {
                for (ShoppingList.ShoppingListIngredient ingredient :
                        shoppingList.getIngredients().values()) {
                    View listItem = LayoutInflater.from(cardView.getContext())
                            .inflate(R.layout.shopping_item, linearLayout, false);

                    // Set whether the item has been purchased.
                    ((CheckBox) listItem.findViewById(R.id.checkbox))
                            .setChecked(ingredient.getPurchased());
                    ((CheckBox) listItem.findViewById(R.id.checkbox)).setText("");

                    // Set the ingredient name, value, etc.
                    // TODO: Update formatting here.
                    ((TextView) listItem.findViewById(android.R.id.text1)).setText(
                            ingredient.getIngredient().toString());

                    // TODO: Change this to set recipe title instead of key, and allow multiple
                    // recipes.
                    // Set the recipe name(s) that it comes from.
                    ((TextView) listItem.findViewById(android.R.id.text2)).setText(
                            ingredient.getPlannedRecipeKey());

                    linearLayout.addView(listItem);
                }
            }
        }
    }

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
                    shoppingLists.add(shoppingList);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public ShoppingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setShoppingList(shoppingLists.get(position));
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }
}
