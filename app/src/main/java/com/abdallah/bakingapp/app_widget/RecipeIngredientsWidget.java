package com.abdallah.bakingapp.app_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.activities.RecipesActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientsWidget extends AppWidgetProvider {

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_ingredients);

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_widget_file_key), Context.MODE_PRIVATE);
        String recipeName = sharedPreferences.getString(context.getString(R.string.saved_recipe_name_key)
                , context.getString(R.string.default_recipe_name_ingredients_value));
        String recipeIngredients = sharedPreferences.getString(context.getString(R.string.saved_recipe_ingredients_key)
                , context.getString(R.string.default_recipe_name_ingredients_value));

        if (!recipeName.equals(context.getString(R.string.default_recipe_name_ingredients_value))
                || !recipeIngredients.equals(context.getString(R.string.default_recipe_name_ingredients_value))) {
            remoteViews.setTextViewText(R.id.tv_recipe_name, recipeName);
            remoteViews.setTextViewText(R.id.tv_recipe_ingredients, recipeIngredients);
            remoteViews.setViewVisibility(R.id.ll_widget_content, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.tv_empty_widget, View.GONE);
        }
        else {
            remoteViews.setViewVisibility(R.id.ll_widget_content, View.GONE);
            remoteViews.setViewVisibility(R.id.tv_empty_widget, View.VISIBLE);
        }

        Intent intent = new Intent(context, RecipesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget_frame, pendingIntent);

        // Instruct the widget manager to update all the widgets
        ComponentName thisWidget = new ComponentName(context, RecipeIngredientsWidget.class);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateAppWidgets(context, appWidgetManager);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

