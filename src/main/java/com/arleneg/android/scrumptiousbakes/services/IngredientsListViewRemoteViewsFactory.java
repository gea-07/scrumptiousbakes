package com.arleneg.android.scrumptiousbakes.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.ui.IngredientsAndStepsFragment;
import com.arleneg.android.scrumptiousbakes.ui.RecipeWidgetProvider;

import java.util.ArrayList;

// class adapted from https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
class IngredientsListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<String> mIngredients;

    public IngredientsListViewRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        mIngredients = new ArrayList<String>();
    }

    @Override
    public void onDataSetChanged() {
        mIngredients.clear();
        mIngredients.add(0, PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(IngredientsAndStepsFragment.PREF_RECIPE_TITLE, null));

        String ingredientListAsStr = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(IngredientsAndStepsFragment.PREF_INGREDIENT_LIST, null);

        String[] ingredients = ingredientListAsStr != null ? ingredientListAsStr.split("\n") : new String[0];
        int i = 1;
        for (String s : ingredients) {
            mIngredients.add(i, s);
            i++;
        }
    }


    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        }

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        remoteViews.setTextViewText(R.id.widgetIngredientItem, mIngredients.get(position));

        return remoteViews;
    }


    public int getCount(){
        return mIngredients.size();
    }

    public int getViewTypeCount(){
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public void onDestroy(){
        mIngredients.clear();
    }

    public boolean hasStableIds() {
        return true;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

}