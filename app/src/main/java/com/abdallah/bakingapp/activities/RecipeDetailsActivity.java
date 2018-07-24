package com.abdallah.bakingapp.activities;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.adapters.RecipeStepsAdapter;
import com.abdallah.bakingapp.app_widget.RecipeIngredientsWidget;
import com.abdallah.bakingapp.fragments.RecipeStepDetailsFragment;
import com.abdallah.bakingapp.models.recipe.Ingredient;
import com.abdallah.bakingapp.models.recipe.Recipe;
import com.abdallah.bakingapp.utils.LogUtils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing the recipe's ingredients and a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailsActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailsActivity extends AppCompatActivity implements RecipeStepsAdapter.ItemClickListener {

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    public static final String EXTRA_RECIPE = "com.abdallah.bakingapp.extras.EXTRA_RECIPE";
    private static final String STATE_RECIPE = "STATE_RECIPE";
    private static final String STATE_CURRENT_SELECTED_STEP_POSITION = "STATE_CURRENT_SELECTED_STEP_POSITION";

    @BindView(R.id.tv_ingredients) TextView ingredientsTextView;
    @BindView(R.id.rv_steps) RecyclerView stepsRecyclerView;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private boolean isTwoPane;
    private Recipe recipe;
    private RecipeStepsAdapter stepsAdapter;

    /**
     * Factory method to facilitate creating explicit intents to be used to start this activity.
     * @param context intent context.
     * @param recipe intent extra.
     * @return intent instance that can be used to start this activity.
     */
    public static Intent getStartIntent(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(EXTRA_RECIPE, Parcels.wrap(recipe));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.step_details_fragment_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true;
        }

        int currentSelectedStepPos;
        if (savedInstanceState == null) {
            recipe = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_RECIPE));
            currentSelectedStepPos = 0;
        }
        else {
            // restore saved instance state
            recipe = Parcels.unwrap(savedInstanceState.getParcelable(STATE_RECIPE));
            currentSelectedStepPos = savedInstanceState.getInt(STATE_CURRENT_SELECTED_STEP_POSITION);
        }


        if (recipe != null) {
            setTitle(recipe.getName());
            setupIngredientsTextView();
            setupStepsRecyclerView(currentSelectedStepPos);

            if (isTwoPane && currentSelectedStepPos == 0) {
                RecipeStepDetailsFragment recipeStepDetailsFragment =
                        RecipeStepDetailsFragment.newInstance(recipe.getSteps().get(currentSelectedStepPos));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_details_fragment_container, recipeStepDetailsFragment)
                        .commit();
            }
        }
        else {
            throw new RuntimeException("No 'recipe' was found in the intent extras!");
        }
    }

    private void setupIngredientsTextView() {
        StringBuilder strBuilder = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            strBuilder.append(
                    getString(R.string.ingredient_item, ingredient.getName()
                            , ingredient.getFormattedQuantity(), ingredient.getMeasure())
            );
        }
        ingredientsTextView.setText(strBuilder);
    }

    private void setupStepsRecyclerView(int currentSelectedStepPos) {

        stepsRecyclerView.setHasFixedSize(false);

        stepsRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        stepsRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        stepsRecyclerView.addItemDecoration(dividerItemDecoration);

        stepsAdapter = new RecipeStepsAdapter(recipe.getSteps(), currentSelectedStepPos, isTwoPane
                , this);
        stepsRecyclerView.setAdapter(stepsAdapter);
    }

    @Override
    public void onRecyclerViewItemClicked(int clickedItemIndex) {
        LogUtils.d(TAG, "Clicked step index = " + clickedItemIndex);

        if (isTwoPane) {
            RecipeStepDetailsFragment recipeStepDetailsFragment =
                    RecipeStepDetailsFragment.newInstance(recipe.getSteps().get(clickedItemIndex));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_details_fragment_container, recipeStepDetailsFragment)
                    .commit();
        }
        else {
            Intent intent = RecipeStepDetailsActivity.getStartIntent(this
                    , recipe.getSteps(), clickedItemIndex);
            startActivity(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STATE_RECIPE, Parcels.wrap(recipe));
        outState.putInt(STATE_CURRENT_SELECTED_STEP_POSITION, stepsAdapter.getSelectedPos());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_details_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_pin_recipe_to_widget:
                pinRecipeToAppWidget();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pinRecipeToAppWidget() {
        StringBuilder strBuilder = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            strBuilder.append(
                    getString(R.string.ingredient_item, ingredient.getName()
                            , ingredient.getFormattedQuantity(), ingredient.getMeasure())
            );
        }
        String ingredients = strBuilder.toString();

        // store the recipe name & ingredients in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.pref_widget_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(getString(R.string.saved_recipe_id_key), recipe.getId());
        editor.putString(getString(R.string.saved_recipe_name_key), recipe.getName());
        editor.putString(getString(R.string.saved_recipe_ingredients_key), ingredients);
        editor.apply();

        RecipeIngredientsWidget.updateAppWidgets(this, AppWidgetManager.getInstance(this));

        Toast.makeText(this, R.string.msg_recipe_pinned_to_widget_successfully
                , Toast.LENGTH_SHORT).show();
    }

}
