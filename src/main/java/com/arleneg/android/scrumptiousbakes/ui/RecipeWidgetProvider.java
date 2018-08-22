package com.arleneg.android.scrumptiousbakes.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.arleneg.android.scrumptiousbakes.R;
import com.arleneg.android.scrumptiousbakes.services.IngredientListViewWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "RecipeWidgetProvider";

    // onReceive is adapted from https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh all your widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, RecipeWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.ingredient_listview);
        }
        super.onReceive(context, intent);
    }

        // onUpdate is adapted from https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; ++i) {
            for (int appWidgetId : appWidgetIds) {
                RemoteViews views = new RemoteViews(
                        context.getPackageName(),
                        R.layout.recipe_widget_provider
                );
                Intent intent = new Intent(context, IngredientListViewWidgetService.class);
                views.setRemoteAdapter(R.id.ingredient_listview, intent);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }

    // sendRefreshBroadcast is adapted from https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, RecipeWidgetProvider.class));
        context.sendBroadcast(intent);
    }



    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

//    @Override
//    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
//        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
//
//        if (mWidgetTitle != null && mWidgetIngredientList != null) {
//
//            ComponentName thisWidget = new ComponentName(context.getApplicationContext(), RecipeWidgetProvider.class);
//            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
//            if (appWidgetIds != null && appWidgetIds.length > 0) {
//                onUpdate(context, appWidgetManager, appWidgetIds);
//            }
//        }
//    }
}

