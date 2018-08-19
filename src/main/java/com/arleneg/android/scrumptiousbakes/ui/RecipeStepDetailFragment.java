package com.arleneg.android.scrumptiousbakes.ui;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepDetailFragment extends Fragment {
    private static final String TAG = "RecipeStepDetail";
    private static final String STEP_ID = "stepID";
    private static final String PLAYBACK_POSITION = "playbackPosition";
    private static final String PLAY_WHEN_READY = "playWhenReady";
    private static final String CURRENT_WINDOW = "currentWindow";

    @BindView(R.id.short_description_tv)
    TextView mShortDescription;

    @BindView(R.id.step_description_tv)
    TextView mStepDescription;

    @BindView(R.id.player_view)
    PlayerView mPlayerView;

    @BindView(R.id.no_image_view)
    ImageView mNoImageView;

    private Step mStep = null;
    private SimpleExoPlayer mExoPlayer = null;
    private View mFragmentRecipeStepView = null;

    // the following variables were copied from https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
    private long mPlaybackPosition = 0;
    private int mCurrentWindow = 0;
    private boolean mPlayWhenReady = true;

    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentRecipeStepView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, mFragmentRecipeStepView);

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(STEP_ID);
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        }

        Log.i(TAG, String.format("In onCreateView %h, %d, %d, %b"
                , this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));

        if (mStep != null && mStep.getVideoURL() != null && mStep.getVideoURL().isEmpty()) {
            mPlayerView.setVisibility(View.GONE);
            mNoImageView.setVisibility(View.VISIBLE);
            mNoImageView.setImageResource(R.drawable.gaelle_marcel_421585_unsplash);
        }

        if (mStep != null) {
            mShortDescription.setText(mStep.getShortDescription());
            mStepDescription.setText(mStep.getDescription());
        }

        return mFragmentRecipeStepView;
    }

    private void initializeExoPlayer() {

        if (mExoPlayer == null) {
            Log.i(TAG, String.format("In initializeExoPlayer %h, %d, %d, %b"
                    , this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
            // This ExoPlayer code is copied from Udacity's video from Lesson 6 Media Playback
            // of Advanced Android track and from from https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(mFragmentRecipeStepView.getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayerView.setPlayer(mExoPlayer);

            // media source
            String userAgent = Util.getUserAgent(mFragmentRecipeStepView.getContext(), "scrumptiousbakes");
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(mFragmentRecipeStepView.getContext(),
                    userAgent);

            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(mStep.getVideoURL()));

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        }
    }

    // onStart, onResume, onPause, onStop, hideSystemUi, and releasePlayer
    // were copied from https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
    @Override
    public void onStart() {
        Log.i(TAG, "In onStart");
        super.onStart();
        if (Util.SDK_INT > 23) {
            Log.i(TAG, String.format("In onStart--about to call initializeExoPlayer %h, %d, %d, %b"
                    , this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
            initializeExoPlayer();
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "In onResume");
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            Log.i(TAG, String.format("In onResume--about to call initializeExoPlayer %h, %d, %d, %b"
                    , this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
            initializeExoPlayer();
        }
    }

    @Override
    public void onPause() {
        Log.i(TAG, "In onPause");
        super.onPause();
        if (Util.SDK_INT <= 23) {
            Log.i(TAG, String.format("In onPause--about to call releasePlayer %h, %d, %d, %b"
                    , this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        Log.i(TAG, "In onStop");
        super.onStop();

        if (Util.SDK_INT > 23) {
            Log.i(TAG, String.format("In onStop--about to call releasePlayer %h, %d, %d, %b"
                    , this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            Log.i(TAG, String.format("In releasePlayer %h, %d, %d, %b"
                    , this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
//        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public void setStep(Step step) {
        this.mStep = step;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);

        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();

            Log.i(TAG, String.format("In onSaveInstanceState %h, %d, %d, %b"
                    , this, mCurrentWindow, mPlaybackPosition, mPlayWhenReady));
            outState.putLong(PLAYBACK_POSITION, mPlaybackPosition);
            outState.putBoolean(PLAY_WHEN_READY, mPlayWhenReady);
            outState.putInt(CURRENT_WINDOW, mCurrentWindow);
        }

        if (mStep != null) {
            outState.putParcelable(STEP_ID, mStep);
        }
    }
}
