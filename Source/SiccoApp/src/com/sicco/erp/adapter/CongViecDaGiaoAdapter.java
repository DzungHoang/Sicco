package com.sicco.erp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.model.CongViecDaGiao;

public class CongViecDaGiaoAdapter extends ArrayAdapter<CongViecDaGiao> {

	Context mContext;
	int mResourceID;
	List<CongViecDaGiao> mCongViecDaGiao;

	public CongViecDaGiaoAdapter(Context context, int resource,
			ArrayList<CongViecDaGiao> objects) {
		super(context, resource, objects);
		mContext = context;
		mResourceID = resource;
		mCongViecDaGiao = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);
		CongViecDaGiao congViecDaGiao = getItem(position);
		TextView tenCongViec = (TextView) mView
				.findViewById(R.id.item_lv_ten_cong_viec);
		tenCongViec.setText(congViecDaGiao.getTenCongViec());
		TextView nguoiXuLy = (TextView) mView
				.findViewById(R.id.item_lv_nguoi_xu_ly);
		nguoiXuLy.setText(congViecDaGiao.getNguoiXuLy());
		return mView;
	}
}
