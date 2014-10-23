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
import com.sicco.erp.model.CongViecHoanThanh;

public class CongViecHoanThanhAdapter extends ArrayAdapter<CongViecHoanThanh> {

	Context mContext;
	int mResourceID;
	List<CongViecHoanThanh> mCongViecHoanThanh;

	public CongViecHoanThanhAdapter(Context context, int resource,
			ArrayList<CongViecHoanThanh> objects) {
		super(context, resource, objects);
		mContext = context;
		mResourceID = resource;
		mCongViecHoanThanh = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);
		CongViecHoanThanh congViecHoanThanh = getItem(position);
		TextView tenCongViec = (TextView) mView
				.findViewById(R.id.item_lv_ten_cong_viec);
		tenCongViec.setText(congViecHoanThanh.getTenCongViec());
		TextView ngayhoanthanh = (TextView) mView
				.findViewById(R.id.item_lv_ngay_hoan_thanh);
		ngayhoanthanh.setText(congViecHoanThanh.getNgayHoanThanh());
		return mView;
	}
}
