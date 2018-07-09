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
import com.arleneg.android.scrumptiousbakes.data.Step;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepHolder> {
    private List<Step> mSteps;

    public RecipeStepsAdapter(List<Step> steps) {
        this.mSteps = steps;
    }

    @NonNull
    @Override
    public RecipeStepsAdapter.RecipeStepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new RecipeStepHolder(layoutInflater, parent, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapter.RecipeStepHolder holder, int position) {
        Step step = mSteps.get(position);
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return mSteps.isEmpty() ? 0 : mSteps.size();
    }

    // TODO: Need to implement onClick
    public class RecipeStepHolder extends RecyclerView.ViewHolder {
        private RecipeStepsAdapter mAdapter;

        @BindView(R.id.step_num_tv)
        TextView mStepNumTextView;

        @BindView(R.id.step_short_desc_tv)
        TextView mDescriptionTextView;

        public RecipeStepHolder(View itemView) {
            super(itemView);
        }

        public RecipeStepHolder(LayoutInflater inflater, ViewGroup parent,
                                RecipeStepsAdapter adapter) {
            super(inflater.inflate(R.layout.recipe_step_item, parent, false));

            ButterKnife.bind(this, this.itemView);

            this.mAdapter = adapter;
        }

        public void bind(Step step) {
            mStepNumTextView.setText(String.format(Locale.getDefault(), "%d", step.getId()));
            mDescriptionTextView.setText(step.getShortDescription());
        }
    }
}
