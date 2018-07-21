package com.abdallah.bakingapp.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.api.RecipeAPI;
import com.abdallah.bakingapp.models.recipe.Recipe;
import com.abdallah.bakingapp.utils.LogUtils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecipesFragment extends Fragment {

    private static final String TAG = RecipesFragment.class.getSimpleName();

    private Unbinder unbinder;

    @BindView(R.id.rv_recipes) RecyclerView recipesRecyclerView;
    @BindInt(R.integer.recipes_grid_span_count) int gridSpanCount;

    private List<Recipe> recipes;


    public RecipesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recipes, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        configureRecipesRecyclerView();

        return fragmentView;
    }

    private void configureRecipesRecyclerView() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipeAPI.fetchRecipes(getContext(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                LogUtils.d(TAG, response);

                Gson gson = new Gson();
                Recipe [] recipesArray = gson.fromJson(response.toString(), Recipe[].class);
                recipes = Arrays.asList(recipesArray);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.d(TAG, error);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        RecipeAPI.cancelOngoingRequests(getContext());
    }
}
