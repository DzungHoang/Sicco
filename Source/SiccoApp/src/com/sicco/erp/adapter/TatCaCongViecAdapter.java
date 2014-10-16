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

public class TatCaCongViecAdapter extends ArrayAdapter<TatCaCongViec> {

	Context mContext;
	int mResourceID;
	List<TatCaCongViec> mTatCaCongViec;

	public TatCaCongViecAdapter(Context context, int resource,
			ArrayList<TatCaCongViec> objects) {
		super(context, resource, objects);
		mContext = context;
		mResourceID = resource;
		mTatCaCongViec = objects;

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(mResourceID, parent,false);
		TatCaCongViec tatCaCongViec = getItem(position);
		TextView tenCongViec = (TextView) mView.findViewById(R.id.item_lv_ten_cong_viec);
		tenCongViec.setText(tatCaCongViec.getTenCongViec());
		TextView hanCuoi = (TextView) mView.findViewById(R.id.item_lv_han_cuoi);
		hanCuoi.setText(tatCaCongViec.getHanCuoi());
		return mView;
	}

}
