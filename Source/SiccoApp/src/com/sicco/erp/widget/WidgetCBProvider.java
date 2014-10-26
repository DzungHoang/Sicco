package com.sicco.erp.widget;

import com.sicco.erp.MainActivity;
import com.sicco.erp.R;
import com.sicco.erp.service.GetNotificationService;
import com.sicco.erp.utils.SharedPrefUtils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetCBProvider extends AppWidgetProvider{
	int bgColor;
	int textColor;
	int numberCongViec = 0;
	int numberCongVan = 0;
	int numberLichBieu = 0;
	
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
		
		numberCongViec = GetNotificationService.getCongViecCount();
		if(numberCongViec == 0){
			views.setViewVisibility(R.id.numberCongViec, View.INVISIBLE);
		}else{
			views.setViewVisibility(R.id.numberCongViec, View.VISIBLE);
			views.setTextViewText(R.id.numberCongViec, Integer.toString(numberCongViec));
		}
		
		numberCongVan = GetNotificationService.getCongVanCount();
		if(numberCongVan == 0){
			views.setViewVisibility(R.id.numberCongVan, View.INVISIBLE);
		}else{
			views.setViewVisibility(R.id.numberCongVan, View.VISIBLE);
			views.setTextViewText(R.id.numberCongVan, Integer.toString(numberCongVan));
		}
		
		numberLichBieu = GetNotificationService.getLichBieuCount();
		if(numberLichBieu == 0){
			views.setViewVisibility(R.id.numberLichBieu, View.INVISIBLE);
		}else{
			views.setViewVisibility(R.id.numberLichBieu, View.VISIBLE);
			views.setTextViewText(R.id.numberLichBieu, Integer.toString(numberLichBieu));
		}

		Log.d("TuNT", numberCongVan+"/"+numberCongViec+"/"+numberLichBieu);
		Intent openCanhBao = new Intent(context, MainActivity.class);
		PendingIntent pendingOpenCanhBao = PendingIntent.getActivity(context, 0,
				openCanhBao, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.widget_content, pendingOpenCanhBao);
		
		Intent openWidgetConfigure = new Intent(context, WidgetCBConfigure.class);
		PendingIntent pendingOpenWidgetConfigure = PendingIntent.getActivity(context, 0,
				openWidgetConfigure, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.imageConfigure, pendingOpenWidgetConfigure);
		
		appWidgetManager.updateAppWidget(appWidgetIds, views);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}
