package com.arleneg.android.scrumptiousbakes.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListHolder>{

    private List<Recipe> mRecipeList;

    public void setRecipeList(List<Recipe> recipeList) {
        this.mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new RecipeListHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        if (recipe.getImage().isEmpty()) {
            switch (recipe.getName()) {
                case "Nutella Pie":
                    recipe.setResourceId(R.drawable.chocolate_2330836_640);
                    break;
                case "Brownies":
                    recipe.setResourceId(R.drawable.brownie_548591_640);
                    break;
                case "Yellow Cake":
                    recipe.setResourceId(R.drawable.cupcake_2646285_640);
                    break;
                case "Cheesecake":
                    recipe.setResourceId(R.drawable.cheesecake_3458117_640);
                    break;
            }

        }
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        if (mRecipeList != null) {
            return mRecipeList.size();
        }
        return 0;
    }


    public class RecipeListHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        private ImageView mRecipeImageView;
        private TextView mRecipeNameTextView;

        public RecipeListHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));

            mRecipeImageView = (ImageView) itemView.findViewById(R.id.recipe_imageview);
            mRecipeNameTextView = (TextView) itemView.findViewById(R.id.recipe_name_text_view);
        }

        public void bind(Recipe recipe) {
            if (!recipe.getImage().isEmpty()) {
                Picasso.with(mRecipeImageView.getContext())
                        .load(recipe.getImage())
                        //.placeholder(R.drawable.placeholder)
                        .into(mRecipeImageView);
            }
            else {
                Picasso.with(mRecipeImageView.getContext())
                        .load(recipe.getResourceId())
                        //.placeholder(R.drawable.placeholder)
                        .into(mRecipeImageView);
            }
            mRecipeNameTextView.setText(recipe.getName());
        }

        @Override
        public void onClick(View v) {

        }
    }
}
