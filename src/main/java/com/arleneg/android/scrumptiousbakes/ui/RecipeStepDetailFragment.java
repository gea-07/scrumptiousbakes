package com.arleneg.android.scrumptiousbakes.ui;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Step;
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

    @BindView(R.id.step_description_tv)
    TextView mStepDescription;

    @BindView(R.id.player_view)
    PlayerView mPlayerView;

    private Step mStep;
    private SimpleExoPlayer mPlayer;

    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, view);

        mStepDescription.setText(mStep.getDescription());

        // section on ExoPlayer is inspired and copied from https://www.youtube.com/watch?v=svdq1BWl4r8
        // Building feature-rich media apps with ExoPlayer (Google I/O '18)
        if (mStep.getVideoURL().isEmpty()) {
            mPlayerView.setVisibility(View.INVISIBLE);
        }
        else {
            mPlayerView.setVisibility(View.VISIBLE);
            mPlayer = ExoPlayerFactory.newSimpleInstance(view.getContext(), new DefaultTrackSelector());
            mPlayerView.setPlayer(mPlayer);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(view.getContext(),
                    Util.getUserAgent(view.getContext(), "scrumptiousbakes"));
            Uri videoUri = Uri.parse(mStep.getVideoURL());
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(true);
        }

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!mStep.getVideoURL().isEmpty()) {
            mPlayerView.setPlayer(null);
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void setStep(Step step) {
        this.mStep = step;
    }
}
