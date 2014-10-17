package com.sicco.erp.widget;

import java.util.ArrayList;

import com.sicco.erp.R;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 * here it now takes RemoteFetchService ArrayList<ListItem> for data
 * which is a static ArrayList
 * and this example won't work if there are multiple widgets and 
 * they update at same time i.e they modify RemoteFetchService ArrayList at same
 * time.
 * For that use Database or other techniquest
 */
public class ListProvider implements RemoteViewsFactory {
	private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
	private Context context = null;
	private int appWidgetId;
	private int mTextColor;

	public ListProvider(Context context, Intent intent) {
		this.context = context;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		mTextColor = intent.getIntExtra(WidgetCVProvider.TEXT_COLOR_EXTRA, 0);
		Log.d("DungHV", "ListProvider: textColor = " + mTextColor);
		Log.d("DungHV", "ListProvider: appWidgetId = " + appWidgetId);
		populateListItem();
	}

	private void populateListItem() {
		if(RemoteFetchService.listItemList !=null )
		listItemList = (ArrayList<ListItem>) RemoteFetchService.listItemList
				.clone();
		else
			listItemList = new ArrayList<ListItem>();

	}

	@Override
	public int getCount() {
		return listItemList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 *Similar to getView of Adapter where instead of View
	 *we return RemoteViews 
	 * 
	 */
	@Override
	public RemoteViews getViewAt(int position) {
		final RemoteViews remoteView = new RemoteViews(
				context.getPackageName(), R.layout.item_lv_tat_ca_cong_viec);
		ListItem listItem = listItemList.get(position);
		remoteView.setTextViewText(R.id.item_lv_ten_cong_viec, listItem.title);
		remoteView.setTextViewText(R.id.item_lv_han_cuoi, listItem.han_cuoi);
		
		remoteView.setTextColor(R.id.item_lv_ten_cong_viec, mTextColor);
		remoteView.setTextColor(R.id.item_lv_han_cuoi, mTextColor);
		
		final Intent intent = new Intent();
		final Bundle bundle = new Bundle();
		bundle.putInt("extra_id", position);
		intent.putExtras(bundle);
		remoteView.setOnClickFillInIntent(R.id.item_cong_viec, intent);
//		remoteView.setOnClickFillInIntent(R.id.item_lv_han_cuoi, intent);

		return remoteView;
	}
	

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDataSetChanged() {
	}

	@Override
	public void onDestroy() {
	}

}
