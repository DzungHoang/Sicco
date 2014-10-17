package com.sicco.erp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.sicco.erp.MainActivity;
import com.sicco.erp.R;
import com.sicco.erp.utils.SharedPrefUtils;

public class WidgetCVProvider extends AppWidgetProvider{
	public static final String DATA_FETCHED = "com.tunt.RELOAD_DATA";
	public static final String TEXT_COLOR_EXTRA = "text_color";
	int bgColor;
	int textColor;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			Intent serviceIntent = new Intent(context, RemoteFetchService.class);
			serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetIds[i]);
			context.startService(serviceIntent);
		}
		bgColor = SharedPrefUtils.getPref(context,
				SharedPrefUtils.KEY_BG_WIDGET_COLOR, 0x80E5E5E5);
		textColor = SharedPrefUtils.getPref(context,
				SharedPrefUtils.KEY_TEXT_WIDGET_COLOR, 0xFFFFFFFF);
		Log.d("DungHV", "onUpdate: textColor = " + textColor);

		RemoteViews views = new RemoteViews(context
				.getPackageName(), R.layout.widget_cong_viec);
		views.setInt(R.id.widget_cv, "setBackgroundColor", bgColor);

		Intent intent = new Intent(context, WidgetCVConfigure.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.textView1, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, views);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

		// which layout to show on widget
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_cong_viec);

		// RemoteViews Service needed to provide adapter for ListView
		Intent svcIntent = new Intent(context, WidgetService.class);
		// passing app widget id to that RemoteViews Service
		svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		//DungHV
		svcIntent.putExtra(TEXT_COLOR_EXTRA, textColor);
		
		// setting a unique Uri to the intent
		// don't know its purpose to me right now
		svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
		// setting adapter to listview of the widget
		remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget,
				svcIntent);
		// setting an empty view in case of no data
		remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
		
		final Intent intent = new Intent(context, MainActivity.class);
		intent.setAction("dzunghoang.WIDGET_ACTION");
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		
		
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setPendingIntentTemplate(R.id.listViewWidget, pendingIntent);
		return remoteViews;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.d("DungHV","onReceive: " + intent.getAction());
		
		bgColor = SharedPrefUtils.getPref(context,
				SharedPrefUtils.KEY_BG_WIDGET_COLOR, 0x80E5E5E5);
		textColor = SharedPrefUtils.getPref(context,
				SharedPrefUtils.KEY_TEXT_WIDGET_COLOR, 0xFFFFFFFF);
		
		if (intent.getAction().equals(DATA_FETCHED)) {
			int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		}
		super.onReceive(context, intent);

	}
}
