package com.sicco.erp.widget;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.sicco.erp.R;
import com.sicco.erp.model.TatCaCongViec;

public class ListProvider implements RemoteViewsFactory {
	private ArrayList<TatCaCongViec> listItemList = new ArrayList<TatCaCongViec>();
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
		if (RemoteFetchService.listItemList != null){
			listItemList = (ArrayList<TatCaCongViec>) RemoteFetchService.listItemList
					.clone();
			listItemList.add(new TatCaCongViec("-1", context.getResources().getString(R.string.read_more), ""));
		}
		else
			listItemList = new ArrayList<TatCaCongViec>();

		Log.d("TuNT", "data  = " + listItemList);

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
	 * Similar to getView of Adapter where instead of Viewwe return RemoteViews
	 */
	@Override
	public RemoteViews getViewAt(int position) {
		final RemoteViews remoteView = new RemoteViews(
				context.getPackageName(), R.layout.item_lv_tat_ca_cong_viec);
		TatCaCongViec listItem = listItemList.get(position);
		remoteView.setTextViewText(R.id.item_lv_ten_cong_viec,
				listItem.getTenCongViec());
		remoteView
				.setTextViewText(R.id.item_lv_han_cuoi, listItem.getHanCuoi());

		remoteView.setTextColor(R.id.item_lv_ten_cong_viec, mTextColor);
		remoteView.setTextColor(R.id.item_lv_han_cuoi, mTextColor);

		final Intent intent = new Intent();
		final Bundle bundle = new Bundle();
		bundle.putString("idcongviec", listItem.getID());
		
		intent.putExtra("id", listItem.getID());
		intent.putExtra("ten_cong_viec", listItem.getTenCongViec());
		intent.putExtra("tinh_trang", listItem.getTinhTrang());
		intent.putExtra("tien_do", listItem.getTienDo());
		intent.putExtra("nguoi_thuc_hien", listItem.getNguoiThucHien());
		intent.putExtra("phong_ban", listItem.getPhongBan());
		intent.putExtra("loai_cong_viec", listItem.getLoaiCongViec());
		intent.putExtra("ngay_ket_thuc", listItem.getHanCuoi());
		intent.putExtra("du_an", listItem.getDuAn());
		intent.putExtra("muc_uu_tien", listItem.getMucUuTien());
		intent.putExtra("nguoi_duoc_xem", listItem.getNguoiDuocXem());
		intent.putExtra("nguoi_giao", listItem.getNguoiGiao());
		intent.putExtra("mo_ta", listItem.getMoTa());
		intent.putExtra("tong_hop_bao_cao", listItem.getTongHopBaoCao());
		intent.putExtra("Url", listItem.getUrl());
		
		intent.putExtras(bundle);
		remoteView.setOnClickFillInIntent(R.id.item_cong_viec, intent);

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
