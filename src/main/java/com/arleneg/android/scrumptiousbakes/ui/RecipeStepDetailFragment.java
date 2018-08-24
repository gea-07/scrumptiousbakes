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
import com.arleneg.android.scrumptiousbakes.data.Recipe;
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
    private static final String TAG = "StepDetailFragment";
    private static final String RECIPE_ID = "recipeID";
    private static final String STEP_POSITION_ID = "stepPositionID";
    private static final String PLAYBACK_POSITION = "playbackPosition";
    private static final String PLAY_WHEN_READY = "playWhenReady";
    private static final String CURRENT_WINDOW = "currentWindow";


    @BindView(R.id.step_recipe_name_tv)
    TextView mRecipeName;

    @BindView(R.id.step_description_tv)
    TextView mStepDescription;

    @BindView(R.id.player_view)
    PlayerView mPlayerView;

    @BindView(R.id.no_image_view)
    ImageView mNoImageView;

    private Recipe mRecipe = null;
    private int mStepPosition = 0;
    private Step mStep =  null;
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
            Log.i(TAG, "In onCreateView -- restoring data from saved Instance");
            mRecipe = savedInstanceState.getParcelable(RECIPE_ID);
            mStepPosition = savedInstanceState.getInt(STEP_POSITION_ID);
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
            mStep = mRecipe.getSteps().get(mStepPosition);
        }

        if (mRecipe != null) {
        Log.i(TAG, String.format("In onCreateView %s %s %h",
                mRecipe.getName(),  mRecipe.getSteps().get(mStepPosition).getShortDescription(),
                this ));
        }

        if (mStep != null && mStep.getVideoURL() != null && mStep.getVideoURL().isEmpty()) {
            mPlayerView.setVisibility(View.GONE);
            mNoImageView.setVisibility(View.VISIBLE);
            mNoImageView.setImageResource(R.drawable.gaelle_marcel_421585_unsplash);
        }

        if (mStep != null) {
            mRecipeName.setText(mRecipe.getName());
            mStepDescription.setText(mStep.getDescription());
        }

        return mFragmentRecipeStepView;
    }


    private void initializeExoPlayer() {

        if (mStep == null) {
            return;
        }

        if (mExoPlayer == null && !mStep.getVideoURL().isEmpty()) {
            Log.i(TAG, String.format("In initializeExoPlayer %s %s %h, %d, %d, %b",
                    mRecipe.getName(), mRecipe.getSteps().get(mStepPosition).getShortDescription(),
                    this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
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
        if (mRecipe == null) {
            return;
        }
        Log.i(TAG, String.format("In onStart %s %s %h, %d, %d, %b",
                mRecipe.getName(),  mRecipe.getSteps().get(mStepPosition).getShortDescription(),
                this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializeExoPlayer();
        }
    }

    @Override
    public void onResume() {
        if (mRecipe == null) {
            return;
        }
        Log.i(TAG, String.format("In onResume %s %s %h, %d, %d, %b",
                mRecipe.getName(),  mRecipe.getSteps().get(mStepPosition).getShortDescription(),
                this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
        super.onResume();
        if ((Util.SDK_INT <= 23)) {
            initializeExoPlayer();
        }
    }

    @Override
    public void onPause() {
        if (mRecipe == null) {
            return;
        }
        Log.i(TAG, String.format("In onPause %s %s %h, %d, %d, %b",
                mRecipe.getName(),  mRecipe.getSteps().get(mStepPosition).getShortDescription(),
                this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        if (mRecipe == null) {
            return;
        }
        Log.i(TAG, String.format("In onStop %s %s %h, %d, %d, %b",
                mRecipe.getName(),  mRecipe.getSteps().get(mStepPosition).getShortDescription(),
                this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
        super.onStop();

        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mRecipe == null) {
            return;
        }
        if (mExoPlayer != null && !mStep.getVideoURL().isEmpty()) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            Log.i(TAG, String.format("In releasePlayer %s %s %h, %d, %d, %b",
                    mRecipe.getName(),  mRecipe.getSteps().get(mStepPosition).getShortDescription(),
                    this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setStep(Recipe recipe, int position) {
        if (recipe != null) {
            mRecipe = recipe;
            mStepPosition = position;
            mStep = mRecipe.getSteps().get(mStepPosition);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);

        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();

            outState.putLong(PLAYBACK_POSITION, mPlaybackPosition);
            outState.putBoolean(PLAY_WHEN_READY, mPlayWhenReady);
            outState.putInt(CURRENT_WINDOW, mCurrentWindow);
        }

        if (mRecipe != null) {
            outState.putParcelable(RECIPE_ID, mRecipe);
            outState.putInt(STEP_POSITION_ID, mStepPosition);
            Log.i(TAG, String.format("In onSaveInstanceState %s %s %h, %d, %d, %b",
                    mRecipe.getName(),  mRecipe.getSteps().get(mStepPosition).getShortDescription(),
                    this , mCurrentWindow , mPlaybackPosition , mPlayWhenReady));
        }
    }
}
