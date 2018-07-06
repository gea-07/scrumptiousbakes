package com.arleneg.android.scrumptiousbakes.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.network.RecipeService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.RecipeItemClickListener {
    private final String TAG = MainActivity.class.getSimpleName();
    private RecipeListAdapter mAdapter;

    @BindView(R.id.main_activity_recycler_view)
    RecyclerView mRecipeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // setup the recycler view
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipeListAdapter(this);
        mRecipeRecyclerView.setAdapter(mAdapter);

        // get recipe list from network
        getRecipeData();
    }

    private void getRecipeData() {
//        mLoadDataProgressBar.setVisibility(View.VISIBLE);
        RecipeService recipeService = RecipeService.retrofit.create(RecipeService.class);
        Log.d(TAG, "Loading recipe data from the network.");
        Call<List<Recipe>> call = recipeService.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
//                mLoadDataProgressBar.setVisibility(View.INVISIBLE);
                Log.d(TAG, "Inside onResponse for Retrofit call.");
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Recipe> recipes = (ArrayList<Recipe>) response.body();
                        mAdapter.setRecipeList(recipes);
//                        mMovieImagesAdapter.setMovieData(mMovieData);
//                        showMovies();
                    }
                } else {
//                    mErrorTextView.setText("Network error loading movie database API: " + response.code());
//                    showErrorMessage();
                    Log.d(TAG, "Inside onResponse...response not successful.");
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
//                mLoadDataProgressBar.setVisibility(View.INVISIBLE);
//                mErrorTextView.setText("Error loading from the Movie DB Api. Your internet connection may be down. Please try again later.");
//                showErrorMessage();
                Log.d(TAG, "Inside onFailure.");
            }
        });
    }

    @Override
    public void onItemClick(int item) {
        Log.d(TAG, "Clicked row " + item);
    }
}
