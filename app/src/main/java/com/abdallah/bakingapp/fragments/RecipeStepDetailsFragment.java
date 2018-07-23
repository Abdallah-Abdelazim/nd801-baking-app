package com.abdallah.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdallah.bakingapp.R;
import com.abdallah.bakingapp.activities.RecipeDetailsActivity;
import com.abdallah.bakingapp.activities.RecipeStepDetailsActivity;
import com.abdallah.bakingapp.models.recipe.Step;
import com.abdallah.bakingapp.utils.GlideApp;
import com.abdallah.bakingapp.utils.LogUtils;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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

    @BindView(R.id.player_view) PlayerView playerView;
    @BindView(R.id.iv_thumbnail) ImageView thumbnailImageView;
    @BindView(R.id.tv_step_description) TextView stepDescriptionTextView;
    private Unbinder unbinder;

    private Step step;

    private SimpleExoPlayer player;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recipe_step_details, container
                , false);
        unbinder = ButterKnife.bind(this, fragmentView);

        if (!step.hasVideo()) {
            playerView.setVisibility(View.GONE);
        }
        if (!step.hasThumbnail()) {
            thumbnailImageView.setVisibility(View.GONE);
        }

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (step.hasVideo()) {
            initializePlayer(Uri.parse(step.getVideoUrl()));
        }

        if (step.hasThumbnail()) {
            GlideApp.with(this)
                    .asGif()
                    .load(Uri.parse(step.getThumbnailUrl()))
                    .placeholder(R.drawable.loading_img_placeholder)
                    .into(thumbnailImageView);
        }

        stepDescriptionTextView.setText(step.getDescription());
    }

    /**
     * Initializes ExoPlayer;
     * @param mediaUri the URI of the video/audio to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            // Create a default TrackSelector
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            // Bind the player to the view.
            playerView.setPlayer(player);

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)));
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaUri);
            // Prepare the player with the source.
            player.prepare(videoSource);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        releasePlayer();
    }
}
