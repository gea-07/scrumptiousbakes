package com.arleneg.android.scrumptiousbakes.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ToxicBakery.viewpager.transforms.FlipHorizontalTransformer;
import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.data.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepDetailActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_ID = "stepId";
    public static final String EXTRA_CURRENT_POSITION_ID = "positionId";

    private List<Step> mSteps;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Intent intent = getIntent();
        mRecipe = intent.getParcelableExtra(EXTRA_RECIPE_ID);
        int currentPosition = intent.getIntExtra(EXTRA_CURRENT_POSITION_ID, 0);
        mSteps = mRecipe.getSteps();

        RecipeStepPagerAdapter pagerAdapter = new RecipeStepPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.step_view_pager);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(currentPosition);
        pager.setPageTransformer(true, new FlipHorizontalTransformer());
        pager.setOffscreenPageLimit(1);
    }

    private class RecipeStepPagerAdapter extends FragmentStatePagerAdapter {

        public RecipeStepPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();
            stepDetailFragment.setStep(mRecipe, position);
            return stepDetailFragment;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mSteps.get(position).getShortDescription();
        }

        @Override
        public int getCount() {
            return (mSteps == null) ? 0 : mSteps.size();
        }
    }
}
