package com.arleneg.android.scrumptiousbakes.ui;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.network.RecipeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.RecipeItemClickListener {
    private final String TAG = MainActivity.class.getSimpleName();
    private static final String RECIPE_LIST_STATE = "Recipes";

    private RecipeListAdapter mAdapter;

    @BindView(R.id.main_activity_recycler_view)
    RecyclerView mRecipeRecyclerView;

    @BindView(R.id.error_results_not_found_text_view)
    TextView mErrorTextView;

    @BindView(R.id.data_loading_progress_bar)
    ProgressBar mLoadDataProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // setup the recycler view
        int span = calculateBestSpanCount(1000);
        mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this, (span < 1) ? 1 : span));
        mAdapter = new RecipeListAdapter(this);
        mRecipeRecyclerView.setAdapter(mAdapter);
    }

    private void initializeWidget(String widgetTitle, String[] widgetIngredientList) {
        RecipeWidgetProvider.sendRefreshBroadcast(this);
    }

    // calculateBestSpanCount was copied from fellow classmate's blog:
    // http://programmingbackwards.blogspot.com/2018/06/when-popularmoviesapp-has-worn-you-down.html
    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    private void showErrorMessage (Boolean showError) {
        mRecipeRecyclerView.setVisibility(showError ? View.INVISIBLE : View.VISIBLE);
        mErrorTextView.setVisibility(showError ? View.VISIBLE : View.INVISIBLE);
    }

    private void getRecipeData() {
        mLoadDataProgressBar.setVisibility(View.VISIBLE);
        RecipeService recipeService = RecipeService.retrofit.create(RecipeService.class);
        Log.d(TAG, "Loading recipe data from the network.");
        Call<List<Recipe>> call = recipeService.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mLoadDataProgressBar.setVisibility(View.INVISIBLE);
                showErrorMessage(false);
                Log.d(TAG, "Inside onResponse for Retrofit call.");
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Recipe> recipes = (ArrayList<Recipe>) response.body();
                        mAdapter.setRecipeList(recipes);
                    }
                } else {
                    mErrorTextView.setText(String.format(Locale.getDefault(),"%s %d", getString(R.string.network_error_msg), response.code()));
                    showErrorMessage(true);
                    Log.d(TAG, "Inside onResponse...response not successful.");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                mLoadDataProgressBar.setVisibility(View.INVISIBLE);
                mErrorTextView.setText(getString(R.string.network_not_available));
                showErrorMessage(true);
                Log.d(TAG, "Inside onFailure.");
            }
        });
    }

    @Override
    public void onItemClick(int item) {
        Toast.makeText(this, mAdapter.getRecipeList().get(item).getName(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Clicked row " + item);

        Intent intent = new Intent(this, IngredientsAndStepsActivity.class);
        intent.putExtra(IngredientsAndStepsActivity.EXTRA_RECIPE_ID, mAdapter.getRecipeList().get(item));
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "In onSaveInstanceState. Saving recipe data");
        outState.putParcelableArrayList(RECIPE_LIST_STATE, (ArrayList)mAdapter.getRecipeList());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "In onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            List<Recipe> recipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_STATE);
            mAdapter.setRecipeList(recipeList);
        }
        else {
            // get recipe list from network
            getRecipeData();
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "In onResume");
        super.onResume();
        // if the movie data got loaded from restoreInstanceState, no need to call DB again
        if (mAdapter.getRecipeList() == null) {
            Log.d(TAG, "In onResume--getRecipeData()");
            getRecipeData();
        }

        String ingredientStr = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(IngredientsAndStepsFragment.PREF_INGREDIENT_LIST, null);
        if (ingredientStr != null) {
            String[] ingredientList = ingredientStr.split(",");
            // initialize widget with value from shared preference
            initializeWidget(PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(IngredientsAndStepsFragment.PREF_RECIPE_TITLE, null), ingredientList);
        }
    }
}

