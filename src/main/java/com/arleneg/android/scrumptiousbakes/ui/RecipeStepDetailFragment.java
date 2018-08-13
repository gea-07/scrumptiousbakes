package com.arleneg.android.scrumptiousbakes.ui;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepDetailFragment extends Fragment {

    private static final String STEP_ID = "Step_ID";
    @BindView(R.id.short_description_tv)
    TextView mShortDescription;

    @BindView(R.id.step_description_tv)
    TextView mStepDescription;

    @BindView(R.id.player_view)
    PlayerView mPlayerView;

    @BindView(R.id.no_image_view)
    ImageView mNoImageView;

    private Step mStep;
    private SimpleExoPlayer mExoPlayer;

    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(STEP_ID);
        }

        if (mStep.getVideoURL() != null && mStep.getVideoURL().isEmpty()) {
            mPlayerView.setVisibility(View.GONE);
            mNoImageView.setVisibility(View.VISIBLE);
            mNoImageView.setImageResource(R.drawable.gaelle_marcel_421585_unsplash);
        }
        else {
            mPlayerView.setVisibility(View.VISIBLE);
            mNoImageView.setVisibility(View.GONE);
            InitializeExoPlayer(view, Uri.parse(mStep.getVideoURL()));
        }

        mShortDescription.setText(mStep.getShortDescription());
        mStepDescription.setText(mStep.getDescription());

        return view;
    }

    private void InitializeExoPlayer(View view, Uri videoUri) {

        if (mExoPlayer == null) {
            // This ExoPlayer code is copied from Udacity's video from Lesson 6 Media Playback
            // of Advanced Android track

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(view.getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mPlayerView.setPlayer(mExoPlayer);

            // media source
            String userAgent = Util.getUserAgent(view.getContext(), "scrumptiousbakes");
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(view.getContext(),
                    userAgent);

            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        mPlayerView.setPlayer(null);

        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setStep(Step step) {
        this.mStep = step;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STEP_ID, mStep);
    }
}
