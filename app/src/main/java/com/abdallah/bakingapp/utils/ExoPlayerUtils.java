package com.abdallah.bakingapp.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.abdallah.bakingapp.R;
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

/**
 * Utility methods for ExoPlayer library.
 * @author Abdallah Abdelazim
 */
public final class ExoPlayerUtils {

    private ExoPlayerUtils() {/* prevent instantiation */}

    /**
     * Initializes ExoPlayer;
     * @param mediaUri the URI of the video/audio to play.
     */
    public static SimpleExoPlayer initializePlayer(@NonNull Context ctx, @NonNull PlayerView playerView
            , @NonNull Uri mediaUri, long playbackPosition, boolean isPlayWhenReady) {
        // Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create the player
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(ctx, trackSelector);

        // Bind the player to the view.
        playerView.setPlayer(player);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(ctx,
                Util.getUserAgent(ctx, ctx.getString(R.string.app_name)));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaUri);
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.seekTo(playbackPosition);
        player.setPlayWhenReady(isPlayWhenReady);

        return player;
    }

    /**
     * Release ExoPlayer.
     */
    public static void releasePlayer(@NonNull SimpleExoPlayer player) {
            player.stop();
            player.release();
            player = null;
    }

}
