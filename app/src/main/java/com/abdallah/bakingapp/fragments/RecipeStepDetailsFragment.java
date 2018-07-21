package com.abdallah.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.activities.RecipeDetailsActivity;
import com.abdallah.bakingapp.activities.RecipeStepDetailsActivity;

import butterknife.ButterKnife;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link RecipeDetailsActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailsActivity}
 * on handsets.
 */
public class RecipeStepDetailsFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recipe_step_details, container
                , false);
        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }
}
