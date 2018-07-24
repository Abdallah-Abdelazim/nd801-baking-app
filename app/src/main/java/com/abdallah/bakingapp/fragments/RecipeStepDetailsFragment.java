package com.abdallah.bakingapp.fragments;

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
    public static final String ARG_STEP = "item_id";

    private static final String STATE_POSITION_EXO_PLAYER = "STATE_POSITION_EXO_PLAYER";
    private static final String STATE_IS_PLAY_WHEN_READY_EXO_PLAYER = "STATE_IS_PLAY_WHEN_READY_EXO_PLAYER";

    @BindView(R.id.player_view) PlayerView playerView;
    @BindView(R.id.tv_step_description) TextView stepDescriptionTextView;
    private Unbinder unbinder;

    private Step step;

    private SimpleExoPlayer exoPlayer;
    private long positionExoPlayer = 0L;
    private boolean isPlayWhenReadyExoPlayer = false;


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
    public static RecipeStepDetailsFragment newInstance(Step step) {
        RecipeStepDetailsFragment fragment = new RecipeStepDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STEP, Parcels.wrap(step));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP)) {
            step = Parcels.unwrap(getArguments().getParcelable(ARG_STEP));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recipe_step_details, container
                , false);
        unbinder = ButterKnife.bind(this, fragmentView);

        if (!(step.hasVideo() || step.hasThumbnail())) {
            playerView.setVisibility(View.GONE);
        }

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            positionExoPlayer = savedInstanceState.getLong(STATE_POSITION_EXO_PLAYER);
            isPlayWhenReadyExoPlayer = savedInstanceState.getBoolean(STATE_IS_PLAY_WHEN_READY_EXO_PLAYER);
        }

        if (step.hasVideo()) {
            exoPlayer = ExoPlayerUtils.initializePlayer(getContext(), playerView
                    , Uri.parse(step.getVideoUrl()), positionExoPlayer, isPlayWhenReadyExoPlayer);
        }
        else if (step.hasThumbnail()) {
            exoPlayer = ExoPlayerUtils.initializePlayer(getContext(), playerView
                    , Uri.parse(step.getThumbnailUrl()), positionExoPlayer, isPlayWhenReadyExoPlayer);
        }

        stepDescriptionTextView.setText(step.getDescription());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        releaseExoPlayer();
    }

    private void releaseExoPlayer() {
        if (exoPlayer != null) {
            ExoPlayerUtils.releasePlayer(exoPlayer);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(STATE_POSITION_EXO_PLAYER, exoPlayer.getCurrentPosition());
        outState.putBoolean(STATE_IS_PLAY_WHEN_READY_EXO_PLAYER, exoPlayer.getPlayWhenReady());
    }

}
