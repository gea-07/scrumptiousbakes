package com.arleneg.android.scrumptiousbakes.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Ingredient;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.data.Step;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsAndStepsFragment extends Fragment implements RecipeStepsAdapter.StepItemClickListener{

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


    // TODO: need to implement onSaveInstanceState
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients_and_steps, container, false);

        ButterKnife.bind(this, view);

        for (int i= 0; i < mRecipe.getIngredients().size(); ++i) {

            Ingredient ingredient = mRecipe.getIngredients().get(i);
            String ingredientStr =  String.format(Locale.getDefault(), "%s: %.1f %s",
                    ingredient.getIngredient(),ingredient.getQuantity(), ingredient.getMeasure());
            mIngredientsTextView.append(ingredientStr + "\n");
        }

        mStepsRecyclerView = (RecyclerView) view.findViewById(R.id.steps_recycler_view);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(mStepsRecyclerView.getContext()));
        mStepsRecyclerView.setAdapter(new RecipeStepsAdapter(getRecipe().getSteps(), this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        mStepsRecyclerView.addItemDecoration(dividerItemDecoration);

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
}
