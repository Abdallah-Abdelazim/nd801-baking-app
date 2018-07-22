package com.abdallah.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.fragments.RecipeStepDetailsFragment;
import com.abdallah.bakingapp.models.recipe.Step;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDetailsActivity}.
 */
public class RecipeStepDetailsActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepDetailsActivity.class.getSimpleName();

    public static final String EXTRA_STEPS = "com.abdallah.bakingapp.extras.EXTRA_STEPS";
    public static final String EXTRA_CURRENT_STEP_INDEX = "com.abdallah.bakingapp.extras.EXTRA_CURRENT_STEP_INDEX";

    private Step [] steps;
    private int currentStepIndex;

    /**
     * Factory method to facilitate creating explicit intents to be used to start this activity.
     * @param context intent context.
     * @param steps intent extra.
     * @return intent instance that can be used to start this activity.
     */
    public static Intent getStartIntent(Context context, Step [] steps, int currentStepIndex) {
        Intent intent = new Intent(context, RecipeStepDetailsActivity.class);
        intent.putExtra(EXTRA_STEPS, steps);
        intent.putExtra(EXTRA_CURRENT_STEP_INDEX, currentStepIndex);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_details);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        steps = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_STEPS));
        currentStepIndex = getIntent().getIntExtra(EXTRA_CURRENT_STEP_INDEX, -1);

        if (steps != null && currentStepIndex != -1) {
            // savedInstanceState is non-null when there is fragment state
            // saved from previous configurations of this activity
            // (e.g. when rotating the screen from portrait to landscape).
            // In this case, the fragment will automatically be re-added
            // to its container so we don't need to manually add it.
            // For more information, see the Fragments API guide at:
            //
            // http://developer.android.com/guide/components/fragments.html
            //
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
//                Bundle arguments = new Bundle();
//                arguments.putString(RecipeStepDetailsFragment.ARG_ITEM_ID,
//                        getIntent().getStringExtra(RecipeStepDetailsFragment.ARG_ITEM_ID));
//                RecipeStepDetailsFragment fragment = new RecipeStepDetailsFragment();
//                fragment.setArguments(arguments);
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.step_details_fragment_container, fragment)
//                        .commit();
            }
        }
        else {
            throw new IllegalStateException("No 'steps' or 'currentStepIndex' were found in the intent extras!");
        }

    }

    @OnClick(R.id.btn_prev_step)
    void openPreviousStep() {
        // TODO
    }

    @OnClick(R.id.btn_next_step)
    void openNextStep() {
        // TODO
    }

}
