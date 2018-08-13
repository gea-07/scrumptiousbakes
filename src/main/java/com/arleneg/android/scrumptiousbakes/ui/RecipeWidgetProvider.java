package com.arleneg.android.scrumptiousbakes.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.services.UpdateIngredientsWidgetIntentService;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "RecipeWidgetProvider";

    private String mWidgetTitle = null;

    private String mWidgetIngredientList = null;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            mWidgetTitle = intent
                    .getStringExtra(UpdateIngredientsWidgetIntentService.EXTRA_PARAM_RECIPE_NAME);
            mWidgetIngredientList = intent
                    .getStringExtra(UpdateIngredientsWidgetIntentService.EXTRA_PARAM_INGREDIENT_LIST);
        }

        else if (mWidgetTitle == null && mWidgetIngredientList == null){
            mWidgetTitle = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(IngredientsAndStepsFragment.PREF_RECIPE_TITLE, null);

            mWidgetIngredientList = PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(IngredientsAndStepsFragment.PREF_INGREDIENT_LIST, null);
        }

        Log.i(TAG, "In RecipeWidgetProvider onReceive "
                + " "
                + intent.getAction()
                + " "
                + mWidgetTitle
                + " "
                + mWidgetIngredientList + "\n");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), RecipeWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setTextViewText(R.id.appwidget_title, mWidgetTitle/*widgetTitle*/);
            views.setTextViewText(R.id.appwidget_text, mWidgetIngredientList/*widgetIngredientList*/);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

            //updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        // This is inspired and modified from Lynda.com - Manage widget updates with alarm manager video
//        Intent intent = new Intent(context, RecipeWidgetProvider.class);
//        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setExact(AlarmManager.RTC, System.currentTimeMillis() + 60000,
//                pendingIntent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        if (mWidgetTitle != null && mWidgetIngredientList != null) {

            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), RecipeWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            if (appWidgetIds != null && appWidgetIds.length > 0) {
                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }
    }
}

