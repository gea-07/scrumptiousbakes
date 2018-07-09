package com.arleneg.android.scrumptiousbakes.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Ingredient;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.data.Step;

import java.util.List;

public class IngredientsAndStepsActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_ID = "recipeId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_and_steps);

        Intent intent = getIntent();
        final Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE_ID);

        IngredientsAndStepsFragment ingredientsAndStepsFragment = new IngredientsAndStepsFragment();

        // Set the list of image id's for the head fragment and set the position to the second image in the list
       ingredientsAndStepsFragment.setRecipe(recipe);

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.ingredients_and_steps_container, ingredientsAndStepsFragment)
                .commit();

        setTitle(recipe.getName());
    }
}
