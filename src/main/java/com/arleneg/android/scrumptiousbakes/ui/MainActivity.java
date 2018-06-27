package com.arleneg.android.scrumptiousbakes.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.network.RecipeService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private TextView mTempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTempTextView = findViewById(R.id.temp_text_view);
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
                        for (Recipe recipe: recipes) {
                            mTempTextView.append(recipe.getName() + "\n");

                        }
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
}
