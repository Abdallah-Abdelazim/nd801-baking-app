package com.abdallah.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.activities.RecipeDetailsActivity;
import com.abdallah.bakingapp.activities.RecipeStepDetailsActivity;
import com.abdallah.bakingapp.models.recipe.Step;
import com.abdallah.bakingapp.utils.ExoPlayerUtils;
import com.abdallah.bakingapp.utils.LogUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link RecipeDetailsActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailsActivity}
 * on handsets.
 */
public class RecipeStepDetailsFragment extends Fragment {

    private static final String TAG = RecipeStepDetailsFragment.class.getSimpleName();

    /**
     * The fragment argument representing the Step instance that this fragment
     * represents.
     */
    public static final String ARG_STEP = "ARG_STEP";
    public static final String ARG_IS_TWO_PANE = "ARG_IS_TWO_PANE";

    private static final String STATE_POSITION_EXO_PLAYER = "STATE_POSITION_EXO_PLAYER";
    private static final String STATE_IS_PLAY_WHEN_READY_EXO_PLAYER = "STATE_IS_PLAY_WHEN_READY_EXO_PLAYER";

    @BindView(R.id.player_view) PlayerView playerView;
    @Nullable @BindView(R.id.tv_step_description) TextView stepDescriptionTextView;
    private Unbinder unbinder;

    private Step step;
    private boolean isTwoPane;

    private boolean isFullScreen = false;

    private SimpleExoPlayer exoPlayer;
    private long positionExoPlayer = 0L;
    private boolean isPlayWhenReadyExoPlayer = true;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailsFragment() {
    }

    /**
     * Factory method to facilitate creating instances of this fragment.
     * @param step required fragment argument.
     * @return an instance of RecipeStepDetailsFragment.
     */
    public static RecipeStepDetailsFragment newInstance(Step step, boolean isTwoPane) {
        RecipeStepDetailsFragment fragment = new RecipeStepDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, Parcels.wrap(step));
        args.putBoolean(ARG_IS_TWO_PANE, isTwoPane);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP)) {
            step = Parcels.unwrap(getArguments().getParcelable(ARG_STEP));
        }
        if (getArguments().containsKey(ARG_IS_TWO_PANE)) {
            isTwoPane = getArguments().getBoolean(ARG_IS_TWO_PANE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView;
        int orientation = getResources().getConfiguration().orientation;
        if (step.hasVideo() && !isTwoPane
                && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isFullScreen = true;
            fragmentView = inflater.inflate(R.layout.video_full_screen, container
                    , false);
        }
        else {
            fragmentView = inflater.inflate(R.layout.fragment_recipe_step_details, container
                    , false);
        }
        unbinder = ButterKnife.bind(this, fragmentView);

        if (!step.hasVideo()) {
            playerView.setVisibility(View.GONE);
        }

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_POSITION_EXO_PLAYER)
                && savedInstanceState.containsKey(STATE_IS_PLAY_WHEN_READY_EXO_PLAYER)) {
            // restore video player position & playback status
            positionExoPlayer = savedInstanceState.getLong(STATE_POSITION_EXO_PLAYER);
            isPlayWhenReadyExoPlayer = savedInstanceState.getBoolean(STATE_IS_PLAY_WHEN_READY_EXO_PLAYER);
        }

        if (isFullScreen) {
            ((RecipeStepDetailsActivity) getActivity()).displayFragmentFullScreen();
        }
        else {
            stepDescriptionTextView.setText(step.getDescription());
            // TODO display thumbnail here
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // initialize the video player
        if (step.hasVideo()) {
            exoPlayer = ExoPlayerUtils.initializePlayer(getContext(), playerView
                    , Uri.parse(step.getVideoUrl()), positionExoPlayer, isPlayWhenReadyExoPlayer);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (exoPlayer != null) {
            // this resumes the video player if it was paused in onPause()
            exoPlayer.setPlayWhenReady(isPlayWhenReadyExoPlayer);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (exoPlayer != null) {
            positionExoPlayer = exoPlayer.getCurrentPosition();
            isPlayWhenReadyExoPlayer = exoPlayer.getPlayWhenReady();
            // pause the video player
            exoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (exoPlayer != null) {
            // release the video player
            ExoPlayerUtils.releasePlayer(exoPlayer);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (exoPlayer != null) {
            outState.putLong(STATE_POSITION_EXO_PLAYER, positionExoPlayer);
            outState.putBoolean(STATE_IS_PLAY_WHEN_READY_EXO_PLAYER, isPlayWhenReadyExoPlayer);
        }
    }

}
