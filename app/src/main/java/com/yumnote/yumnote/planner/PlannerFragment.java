package com.yumnote.yumnote.planner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yumnote.yumnote.R;

public class PlannerFragment extends Fragment {
    private static final String TAG = "PlannerFragment";

    public static final String EXTRA_RECIPE_KEY = "recipeKey";
    public static final String EXTRA_RECIPE_TITLE = "recipeTitle";
    public static final String EXTRA_RECIPE_NUM_SERVINGS = "recipeNumServings";

    public static PlannerFragment newInstance() {
        PlannerFragment fragment = new PlannerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlannerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_planner, container, false);

        RecyclerView recyclerView =
                (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // TODO: Check safety of MenuPlannerListener
        PlannerAdapter adapter = new PlannerAdapter(getContext(),
                (PlannerAdapter.MenuPlannerListener) getActivity());
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
