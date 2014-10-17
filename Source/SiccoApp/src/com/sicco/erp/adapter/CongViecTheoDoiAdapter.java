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
import com.sicco.erp.model.CongViecTheoDoi;

public class CongViecTheoDoiAdapter extends ArrayAdapter<CongViecTheoDoi> {

	Context mContext;
	int mResourceID;
	List<CongViecTheoDoi> mCongViecTheoDoi;

	public CongViecTheoDoiAdapter(Context context, int resource,
			ArrayList<CongViecTheoDoi> objects) {
		super(context, resource, objects);
		mContext = context;
		mResourceID = resource;
		mCongViecTheoDoi = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);
		CongViecTheoDoi congViecTheoDoi = getItem(position);
		TextView tenCongViec = (TextView) mView
				.findViewById(R.id.item_lv_ten_cong_viec);
		tenCongViec.setText(congViecTheoDoi.getTenCongViec());
		TextView nguoiXuLy = (TextView) mView
				.findViewById(R.id.item_lv_nguoi_xu_ly);
		nguoiXuLy.setText(congViecTheoDoi.getNguoiXuLy());
		return mView;
	}
}
