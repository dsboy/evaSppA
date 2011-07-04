package com.loopback.androidapps.saveapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetSaveApp extends AppWidgetProvider {

	public DBManager dbManager;
	public SaveApp saveApp;
	String account;
	RemoteViews views;
	public static String ACTION_WIDGET_CONFIGURE = "ConfigureWidget";
	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, WidgetSaveApp.class);
			intent.setAction(ACTION_WIDGET_RECEIVER);
			intent.putExtra("msg", "New Movement Added");
			PendingIntent actionPendingIntent = PendingIntent.getBroadcast(
					context, 0, intent, 0);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			views = new RemoteViews(context.getPackageName(), R.layout.widget);
			views.setOnClickPendingIntent(R.id.btnWidget, actionPendingIntent);

			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			Log.d("WUP", "Painting..." + String.valueOf(i));
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	public void onReceive(Context context, Intent intent) {
		// v1.5 fix that doesn't call onDelete Action
		final String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
			final int appWidgetId = intent.getExtras().getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				this.onDeleted(context, new int[] { appWidgetId });
			}
		} else {
			// check, if our Action was called
			if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
				String msg = "null";
				try {
					msg = intent.getStringExtra("msg");
				} catch (NullPointerException e) {
					Log.e("Error", "msg = null");
				}
				Toast.makeText(context, msg + account, Toast.LENGTH_SHORT).show();
			}

			super.onReceive(context, intent);
		}
	}

	public void onEnabled(Context context) {
		dbManager.open();
	}

	public void onDisable(Context context) {
		// Delete Alarms
	}

}
