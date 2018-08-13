package com.arleneg.android.scrumptiousbakes.ui;


import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Ingredient;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.services.UpdateIngredientsWidgetIntentService;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsAndStepsFragment extends Fragment implements RecipeStepsAdapter.StepItemClickListener{

    private static final String RECIPE_ID = "Recipe_ID";
    public static final String PREF_RECIPE_TITLE = "recipeTitle";
    public static final String PREF_INGREDIENT_LIST = "ingredientList";

    @BindView(R.id.ingredients_title_tv)
    TextView mIngredientsTitleTextView;

    @BindView(R.id.ingredients_tv)
    TextView mIngredientsTextView;

    @BindView(R.id.steps_title_tv)
    TextView mStepsTitleTextView;

    @BindView(R.id.steps_recycler_view)
    RecyclerView mStepsRecyclerView;

    private Recipe mRecipe;

    // Callbacks interface is adapted and modified from Android Programming Big Nerd Ranch book
    private Callbacks mCallbacks;

    public IngredientsAndStepsFragment() {
        // Required empty public constructor
    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void setRecipe(Recipe recipe) {
        this.mRecipe = recipe;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_ID);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients_and_steps, container, false);

        ButterKnife.bind(this, view);

        String ingredientStr = mRecipe.constructIngredientsAsString();

        mIngredientsTextView.setText(ingredientStr);

        mStepsRecyclerView = (RecyclerView) view.findViewById(R.id.steps_recycler_view);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(mStepsRecyclerView.getContext()));
        mStepsRecyclerView.setAdapter(new RecipeStepsAdapter(getRecipe().getSteps(), this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        mStepsRecyclerView.addItemDecoration(dividerItemDecoration);

        // store recipe name and ingredients in shared preference
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putString(PREF_RECIPE_TITLE, mRecipe.getName())
                .putString(PREF_INGREDIENT_LIST, ingredientStr)
                .apply();

        UpdateIngredientsWidgetIntentService.startActionSetIngredients(getActivity(),
                mRecipe.getName(), ingredientStr);

        return view;
    }

    @Override
    public void onItemClick(int position) {
        mCallbacks.onStepSelected(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public interface Callbacks {
        void onStepSelected(int position);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(RECIPE_ID, mRecipe);
    }
}
