package com.sicco.erp.widget;

import com.sicco.erp.R;
import com.sicco.erp.utils.SharedPrefUtils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetCBProvider extends AppWidgetProvider{
	int bgColor;
	int textColor;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		bgColor = SharedPrefUtils.getPref(context,
				SharedPrefUtils.KEY_BG_WIDGET_CB_COLOR, 0x80E5E5E5);
		textColor = SharedPrefUtils.getPref(context,
				SharedPrefUtils.KEY_TEXT_WIDGET_CB_COLOR, 0xFFFFFFFF);
		
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_canh_bao);
		views.setInt(R.id.widget_cb, "setBackgroundColor", bgColor);
		views.setInt(R.id.textView1, "setTextColor", textColor);
		views.setInt(R.id.textView2, "setTextColor", textColor);
		views.setInt(R.id.textView3, "setTextColor", textColor);
		
		Intent openWidgetConfigure = new Intent(context, WidgetCBConfigure.class);
		PendingIntent pendingOpenWidgetConfigure = PendingIntent.getActivity(context, 0,
				openWidgetConfigure, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.widget_cb, pendingOpenWidgetConfigure);
		
		appWidgetManager.updateAppWidget(appWidgetIds, views);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
