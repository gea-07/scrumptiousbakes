package com.arleneg.android.scrumptiousbakes.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

// class adapted from https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/
public class IngredientListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListViewRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
