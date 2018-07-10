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
    private RecipeItemClickListener mItemClickListener;

    public RecipeListAdapter(RecipeItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    public List<Recipe> getRecipeList() {
        return mRecipeList;
    }

    @NonNull
    @Override
    public RecipeListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new RecipeListHolder(layoutInflater, parent, this);
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


    public class RecipeListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mRecipeImageView;
        private TextView mRecipeNameTextView;
        private RecipeListAdapter mAdapter;

        public RecipeListHolder(LayoutInflater inflater, ViewGroup parent,
                                RecipeListAdapter adapter) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));

            // TODO: use butterknife
            mRecipeImageView = (ImageView) itemView.findViewById(R.id.recipe_imageview);
            mRecipeNameTextView = (TextView) itemView.findViewById(R.id.recipe_name_text_view);
            this.mAdapter = adapter;
            this.itemView.setOnClickListener(this);
        }

        //TODO: add placeholder image
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
            int clickedItem = getAdapterPosition();
            mAdapter.mItemClickListener.onItemClick(clickedItem);
        }
    }

    public interface RecipeItemClickListener {
        void onItemClick(int item);
    }
}
