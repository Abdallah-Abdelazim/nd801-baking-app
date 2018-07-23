package com.abdallah.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.fragments.RecipeStepDetailsFragment;
import com.abdallah.bakingapp.models.recipe.Step;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
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

    private static final String STATE_NEXT_STEP_BUTTON_ENABLED = "STATE_NEXT_STEP_BUTTON_ENABLED";
    private static final String STATE_PREV_STEP_BUTTON_ENABLED = "STATE_PREV_STEP_BUTTON_ENABLED";

    @BindView(R.id.btn_prev_step) Button prevStepButton;
    @BindView(R.id.btn_next_step) Button nextStepButton;

    private List<Step> steps;
    private int currentStepIndex;

    /**
     * Factory method to facilitate creating explicit intents to be used to start this activity.
     * @param context intent context.
     * @param steps intent extra.
     * @return intent instance that can be used to start this activity.
     */
    public static Intent getStartIntent(Context context, List<Step> steps, int currentStepIndex) {
        Intent intent = new Intent(context, RecipeStepDetailsActivity.class);
        intent.putExtra(EXTRA_STEPS, Parcels.wrap(steps));
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
                RecipeStepDetailsFragment recipeStepDetailsFragment =
                        RecipeStepDetailsFragment.newInstance(steps.get(currentStepIndex));
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_details_fragment_container, recipeStepDetailsFragment)
                        .commit();
                if (currentStepIndex == 0) prevStepButton.setEnabled(false);  // end has been reached
                if (currentStepIndex == steps.size()-1) nextStepButton.setEnabled(false); // end has been reached also
            }
        }
        else {
            throw new RuntimeException("No 'steps' or 'currentStepIndex' were found in the intent extras!");
        }

    }

    @OnClick(R.id.btn_prev_step)
    void openPreviousStep() {
            currentStepIndex--;
            changeDisplayedStep();
            if (currentStepIndex == 0) prevStepButton.setEnabled(false);  // end has been reached
            if (!nextStepButton.isEnabled()) nextStepButton.setEnabled(true);
    }

    @OnClick(R.id.btn_next_step)
    void openNextStep() {
            currentStepIndex++;
            changeDisplayedStep();
            if (currentStepIndex == steps.size()-1) nextStepButton.setEnabled(false); // end has been reached
            if (!prevStepButton.isEnabled()) prevStepButton.setEnabled(true);
    }

    private void changeDisplayedStep() {
        RecipeStepDetailsFragment recipeStepDetailsFragment =
                RecipeStepDetailsFragment.newInstance(steps.get(currentStepIndex));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_details_fragment_container, recipeStepDetailsFragment)
                .commit();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        nextStepButton.setEnabled(savedInstanceState.getBoolean(STATE_NEXT_STEP_BUTTON_ENABLED));
        prevStepButton.setEnabled(savedInstanceState.getBoolean(STATE_PREV_STEP_BUTTON_ENABLED));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_NEXT_STEP_BUTTON_ENABLED, nextStepButton.isEnabled());
        outState.putBoolean(STATE_PREV_STEP_BUTTON_ENABLED, prevStepButton.isEnabled());
    }
}
