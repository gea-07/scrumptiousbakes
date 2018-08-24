package com.arleneg.android.scrumptiousbakes.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Recipe;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailActivity extends AppCompatActivity {

    @BindView(R.id.btn_next)
    Button mNextButton;

    @BindView(R.id.btn_previous)
    Button mPreviousButton;

    public static final String EXTRA_RECIPE_ID = "stepId";
    public static final String EXTRA_CURRENT_POSITION_ID = "positionId";

    private int mStepPosition;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mRecipe = intent.getParcelableExtra(EXTRA_RECIPE_ID);
        mStepPosition = intent.getIntExtra(EXTRA_CURRENT_POSITION_ID, 0);

        if (savedInstanceState == null) {
            initializeRecipeStepFragment();
        }

        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mStepPosition < mRecipe.getSteps().size() - 1) {
                    mStepPosition++;
                }
                else {
                    mStepPosition = 0;
                }
                initializeRecipeStepFragment();
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mStepPosition > 0) {
                    mStepPosition--;
                }
                initializeRecipeStepFragment();
            }
        });

    }

    private void initializeRecipeStepFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();

        // Set the list of image id's for the head fragment and set the position to the first image in the list
        stepDetailFragment.setStep(mRecipe, mStepPosition);

        // stepDetailFragment.setRetainInstance(true);

        // Add the fragment to its container using a FragmentManager and a Transaction
        fragmentManager.beginTransaction()
                .replace(R.id.step_fragment, stepDetailFragment)
                .commit();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .commit();
    }
}
