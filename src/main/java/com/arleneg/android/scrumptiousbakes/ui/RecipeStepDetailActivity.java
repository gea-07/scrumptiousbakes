package com.arleneg.android.scrumptiousbakes.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.data.Step;

import java.util.List;

// TODO Add snack bar with previous and next buttons
public class RecipeStepDetailActivity extends AppCompatActivity {
    public static final String EXTRA_STEP_ID = "stepId";
    public static final String EXTRA_CURRENT_POSITION_ID = "positionId";

    private List<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Intent intent = getIntent();
        mSteps = intent.getParcelableArrayListExtra(EXTRA_STEP_ID);
        int currentPosition = intent.getIntExtra(EXTRA_CURRENT_POSITION_ID, 0);

        RecipeStepPagerAdapter pagerAdapter = new RecipeStepPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.step_view_pager);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(currentPosition);
    }

    private class RecipeStepPagerAdapter extends FragmentPagerAdapter {

        public RecipeStepPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();
            stepDetailFragment.setStep(mSteps.get(position));
            return stepDetailFragment;
        }

        @Override
        public int getCount() {
            return (mSteps == null) ? 0 : mSteps.size();
        }
    }
}
