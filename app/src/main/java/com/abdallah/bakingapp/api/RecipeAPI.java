package com.abdallah.bakingapp.api;

import android.content.Context;

import com.abdallah.bakingapp.utils.network.RequestQueueSingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;


public final class RecipeAPI {

    private static final String RECIPE_LISTING_URL = "http://go.udacity.com/android-baking-app-json";

    private RecipeAPI() {/* prevent instantiation */}

    public static void fetchRecipes(Context ctx, Response.Listener<JSONArray> listener
            , Response.ErrorListener errorListener) {

        JsonArrayRequest recipeListingRequest = new JsonArrayRequest(Request.Method.GET, RECIPE_LISTING_URL
                , null, listener, errorListener);
        recipeListingRequest.setTag(ctx);
        RequestQueueSingleton.getInstance(ctx).addToRequestQueue(recipeListingRequest);
    }

    /**
     * Cancels all requests associated with the passed context.
     * @param ctx
     */
    public static void cancelOngoingRequests(Context ctx) {
        RequestQueueSingleton.getInstance(ctx).getRequestQueue().cancelAll(ctx);
    }

}
