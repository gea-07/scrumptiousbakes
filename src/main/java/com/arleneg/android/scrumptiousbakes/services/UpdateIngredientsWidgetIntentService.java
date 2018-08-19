package com.arleneg.android.scrumptiousbakes.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.Context;
import android.util.Log;


public class UpdateIngredientsWidgetIntentService extends IntentService {
    private final String TAG = "UpdateWidgetService";
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SET_INGREDIENTS = "com.arleneg.android.scrumptiousbakes.services.action.DISPLAY_CURRENT_INGREDIENTS";

    public static final String EXTRA_PARAM_RECIPE_NAME = "com.arleneg.android.scrumptiousbakes.services.extra.PARAM_RECIPE_NAME";
    public static final String EXTRA_PARAM_INGREDIENT_LIST = "com.arleneg.android.scrumptiousbakes.services.extra.PARAM_INGREDIENT_LIST";

    public UpdateIngredientsWidgetIntentService() {
        super("UpdateIngredientsWidgetIntentService");
    }

    public static void startActionSetIngredients(Context context, String recipeName, String[] ingredientList) {
        Intent intent = new Intent(context, UpdateIngredientsWidgetIntentService.class);
        intent.setAction(ACTION_SET_INGREDIENTS);
        intent.putExtra(EXTRA_PARAM_RECIPE_NAME, recipeName);
        intent.putExtra(EXTRA_PARAM_INGREDIENT_LIST, ingredientList);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SET_INGREDIENTS.equals(action)) {
                final String recipeName = intent.getStringExtra(EXTRA_PARAM_RECIPE_NAME);
                final String[] ingredientList = intent.getStringArrayExtra(EXTRA_PARAM_INGREDIENT_LIST);
                handleDisplayIngredients(recipeName, ingredientList);
            }
        }
    }

    private void handleDisplayIngredients(String recipeName, String[] ingredientList) {
        Log.i(TAG, "In handleDisplayIngredients: " + recipeName);
        if (recipeName != null && ingredientList != null) {
            Intent intent = new Intent();
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(EXTRA_PARAM_RECIPE_NAME, recipeName);
            intent.putExtra(EXTRA_PARAM_INGREDIENT_LIST, ingredientList);
            sendBroadcast(intent);
        }
    }
}


