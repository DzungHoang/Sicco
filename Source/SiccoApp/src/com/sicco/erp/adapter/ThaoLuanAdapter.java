package com.sicco.erp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.model.ThaoLuan;

public class ThaoLuanAdapter extends ArrayAdapter<ThaoLuan> {

	Context mContext;
	int mResourceID;
	List<ThaoLuan> mThaoLuan;

	public ThaoLuanAdapter(Context context, int resource,
			ArrayList<ThaoLuan> objects) {
		super(context, resource, objects);
		mContext = context;
		mResourceID = resource;
		mThaoLuan = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);
		ThaoLuan thaoLuan = getItem(position);
		ImageView anhDaiDien = (ImageView) mView.findViewById(R.id.avatar_nguoi_thao_luan);
//		anhDaiDien.setImageURI("");
		TextView nguoiThaoLuan = (TextView) mView
				.findViewById(R.id.item_lv_nguoi_thao_luan);
		nguoiThaoLuan.setText(thaoLuan.getNguoiThaoLuan());
		TextView thoiGianThaoLuan = (TextView) mView
				.findViewById(R.id.item_lv_thoi_gian_thao_luan);
		thoiGianThaoLuan.setText(thaoLuan.getThoiGianThaoLuan());
		TextView noiDungThaoLuan = (TextView) mView
				.findViewById(R.id.item_lv_noi_dung_thao_luan);
		noiDungThaoLuan.setText(thaoLuan.getNoiDungThaoLuan());
		return mView;
	}

}
