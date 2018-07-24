package com.abdallah.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.fragments.RecipeStepDetailsFragment;
import com.abdallah.bakingapp.models.recipe.Step;
import com.abdallah.bakingapp.utils.ExoPlayerUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

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

    private static final String STATE_STEPS = "STATE_STEPS";
    private static final String STATE_CURRENT_STEP_INDEX = "STATE_CURRENT_STEP_INDEX";

    @Nullable @BindView(R.id.btn_prev_step) Button prevStepButton;
    @Nullable @BindView(R.id.btn_next_step) Button nextStepButton;

    @Nullable @BindView(R.id.player_view) PlayerView playerView;

    private List<Step> steps;
    private int currentStepIndex;

    private SimpleExoPlayer exoPlayer;
    private boolean isFullScreen = false;

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

        if (savedInstanceState == null) {
            // get the 'steps' & 'currentStepIndex' from intent extras
            steps = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_STEPS));
            currentStepIndex = getIntent().getIntExtra(EXTRA_CURRENT_STEP_INDEX, -1);
        }
        else {
            // get the 'steps' & 'currentStepIndex' from savedInstanceState
            steps = Parcels.unwrap(savedInstanceState.getParcelable(STATE_STEPS));
            currentStepIndex = savedInstanceState.getInt(STATE_CURRENT_STEP_INDEX);
        }

        Step currentStep = steps.get(currentStepIndex);

        int layoutId;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && (currentStep.hasVideo() || currentStep.hasThumbnail())) {
            layoutId = R.layout.video_full_screen;
            isFullScreen = true;
        }
        else {
            layoutId = R.layout.activity_recipe_step_details;
        }
        setContentView(layoutId);
        ButterKnife.bind(this);

        if (isFullScreen) {
            /* displaying the activity as full screen video in landscape mode */
            hideSystemUI();
            if (currentStep.hasVideo()) {
                exoPlayer = ExoPlayerUtils.initializePlayer(this, playerView
                        , Uri.parse(currentStep.getVideoUrl()));
            }
            else if (currentStep.hasThumbnail()) {
                exoPlayer = ExoPlayerUtils.initializePlayer(this, playerView
                        , Uri.parse(currentStep.getThumbnailUrl()));
            }
            exoPlayer.setPlayWhenReady(true);
        }
        else {
            /* displaying the normal step details */

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Show the Up button in the action bar.
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            setTitle(steps.get(currentStepIndex).getShortDescription());

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
            }

            if (currentStepIndex == 0) prevStepButton.setEnabled(false);  // end has been reached
            if (currentStepIndex == steps.size() - 1)
                nextStepButton.setEnabled(false); // end has also been reached
        }

    }

    @Optional
    @OnClick(R.id.btn_prev_step)
    void openPreviousStep() {
            currentStepIndex--;
            changeDisplayedStep();
            if (currentStepIndex == 0) prevStepButton.setEnabled(false);  // end has been reached
            if (!nextStepButton.isEnabled()) nextStepButton.setEnabled(true);
    }

    @Optional
    @OnClick(R.id.btn_next_step)
    void openNextStep() {
            currentStepIndex++;
            changeDisplayedStep();
            if (currentStepIndex == steps.size()-1) nextStepButton.setEnabled(false); // end has been reached
            if (!prevStepButton.isEnabled()) prevStepButton.setEnabled(true);
    }

    private void changeDisplayedStep() {
        setTitle(steps.get(currentStepIndex).getShortDescription());

        RecipeStepDetailsFragment recipeStepDetailsFragment =
                RecipeStepDetailsFragment.newInstance(steps.get(currentStepIndex));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_details_fragment_container, recipeStepDetailsFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFullScreen) ExoPlayerUtils.releasePlayer(exoPlayer);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STATE_STEPS, Parcels.wrap(steps));
        outState.putInt(STATE_CURRENT_STEP_INDEX, currentStepIndex);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFullScreen && hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables sticky immersive mode.
        View decorView = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        else {
            decorView.setSystemUiVisibility(
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

}
