package com.arleneg.android.scrumptiousbakes.ui;


import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.data.Recipe;
import com.arleneg.android.scrumptiousbakes.services.UpdateIngredientsWidgetIntentService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsAndStepsFragment extends Fragment implements RecipeStepsAdapter.StepItemClickListener {

    private static final String RECIPE_ID = "Recipe_ID";
    public static final String PREF_RECIPE_TITLE = "recipeTitle";
    public static final String PREF_INGREDIENT_LIST = "ingredientList";

    @BindView(R.id.recipe_tv)
    TextView mRecipeName;

    @BindView(R.id.ingredients_title_tv)
    TextView mIngredientsTitleTextView;

    @BindView(R.id.ingredient_lv)
    ListView mIngredientListView;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients_and_steps, container, false);

        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_ID);
        }

        mRecipeName.setText(mRecipe.getName());

        String[] ingredientList = mRecipe.constructIngredientsAsStringArray();

        mIngredientListView.setAdapter(new ArrayAdapter<String>(getActivity(),
                R.layout.ingredient_list_item,
                ingredientList));

        setListViewHeightBasedOnChildren(mIngredientListView);

        //Lines 97 to 105 to allow scrolling in the Listview was copied from
        // https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
        mIngredientListView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //String ingredientStr = mRecipe.constructIngredientsAsString();

        //mIngredientsTextView.setText(ingredientStr);

        mStepsRecyclerView = view.findViewById(R.id.steps_recycler_view);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(mStepsRecyclerView.getContext()));
        mStepsRecyclerView.setAdapter(new RecipeStepsAdapter(getRecipe().getSteps(), this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mStepsRecyclerView.addItemDecoration(dividerItemDecoration);

        // Lines 123 to 126 to store a string array in shared preferences was copied from
        // https://stackoverflow.com/questions/7965290/put-and-get-string-array-from-shared-preferences
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ingredientList.length; i++) {
            sb.append(ingredientList[i]).append(",");
        }

         //store recipe name and ingredients in shared preference
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putString(PREF_RECIPE_TITLE, mRecipe.getName())
                .putString(PREF_INGREDIENT_LIST, sb.toString())
                .apply();

          UpdateIngredientsWidgetIntentService.startActionSetIngredients(getActivity(),
                mRecipe.getName(), ingredientList);


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

    // The following method was copied from
    // https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
    // This method computes the height of the listview dynamically to show
    // all items in the list view
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
