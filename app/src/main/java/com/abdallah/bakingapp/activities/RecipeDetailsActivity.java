package com.abdallah.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.adapters.RecipeStepsAdapter;
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

    @BindView(R.id.tv_ingredients) TextView ingredientsTextView;
    @BindView(R.id.rv_steps) RecyclerView stepsRecyclerView;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
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
        intent.putExtra(RecipeDetailsActivity.EXTRA_RECIPE, Parcels.wrap(recipe));
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

        recipe = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_RECIPE));

        if (recipe != null) {
            setTitle(recipe.getName());
            setupIngredientsTextView();
            setupStepsRecyclerView();
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

    private void setupStepsRecyclerView() {

        stepsRecyclerView.setHasFixedSize(false);

        stepsRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        stepsRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        stepsRecyclerView.addItemDecoration(dividerItemDecoration);

        stepsAdapter = new RecipeStepsAdapter(recipe.getSteps(), isTwoPane, this);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        // TODO app widget stuff

        return super.onOptionsItemSelected(item);
    }

}
