package com.arleneg.android.scrumptiousbakes.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Ingredient;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.data.Step;

import java.util.List;
import java.util.Locale;

public class IngredientsAndStepsActivity extends AppCompatActivity
    implements IngredientsAndStepsFragment.Callbacks {

    public static final String EXTRA_RECIPE_ID = "recipeId";
    private String PREF_DETAIL = "prefDetails";
    private static final String PREF_TWO_PANES = "prefTwoPanes";
    // Two pane layout code in this activity is adapted and copied from
    // Udacity's Android Nanodegree lesson on Fragments
    private boolean mTwoPanes;
    private Recipe mRecipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_and_steps);

        Intent intent = getIntent();
        mRecipe = intent.getParcelableExtra(EXTRA_RECIPE_ID);

        if (savedInstanceState == null) {
            IngredientsAndStepsFragment ingredientsAndStepsFragment = new IngredientsAndStepsFragment();

            // Set the list of image id's for the head fragment and set the position to the second image in the list
            ingredientsAndStepsFragment.setRecipe(mRecipe);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.ingredients_and_steps_container, ingredientsAndStepsFragment)
                    .commit();

            if (findViewById(R.id.step_linear_layout) != null) {
                mTwoPanes = true;

                RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();
                stepDetailFragment.setStep(mRecipe.getSteps().get(0));

                fragmentManager.beginTransaction()
                        .add(R.id.detail_container, stepDetailFragment)
                        .commit();

            } else {
                mTwoPanes = false;
            }
            getSharedPreferences(PREF_DETAIL, MODE_PRIVATE)
                    .edit()
                    .putBoolean(PREF_TWO_PANES, mTwoPanes)
                    .apply();
        }

        setTitle(mRecipe.getName());
    }

    @Override
    public void onStepSelected(int position) {

        mTwoPanes = getSharedPreferences(PREF_DETAIL, MODE_PRIVATE).getBoolean(PREF_TWO_PANES, false);

        if (!mTwoPanes) {
            String toastString = String.format(Locale.getDefault(), "Step %d", mRecipe.getSteps().get(position).getId());
            Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putParcelableArrayListExtra(RecipeStepDetailActivity.EXTRA_STEP_ID, mRecipe.getSteps());
            intent.putExtra(RecipeStepDetailActivity.EXTRA_CURRENT_POSITION_ID, position);
            startActivity(intent);
        }
        else {
            RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();
            stepDetailFragment.setStep(mRecipe.getSteps().get(position));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, stepDetailFragment)
                    .commit();
        }
    }
}
