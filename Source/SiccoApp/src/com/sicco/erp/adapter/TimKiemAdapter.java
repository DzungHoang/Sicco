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
import com.sicco.erp.model.CongViec;
import com.sicco.erp.model.TatCaCongViec;
import com.sicco.erp.model.TimKiem;

public class TimKiemAdapter extends ArrayAdapter<TimKiem> {

	
	Context mContext;
	int mResourceID;
	List<TimKiem> mCongViecTheoDoi;

	public TimKiemAdapter(Context context, int resource,
			ArrayList<TimKiem> objects) {
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
		TimKiem timKiemCongViec = getItem(position);
		TextView tenCongViec = (TextView) mView
				.findViewById(R.id.item_lv_ten_cong_viec);
		tenCongViec.setText(timKiemCongViec.getTenCongViec());
		TextView tiendo = (TextView) mView
				.findViewById(R.id.item_lv_han_cuoi);
		tiendo.setText(timKiemCongViec.getHanCuoi());
		return mView;
	}
}
