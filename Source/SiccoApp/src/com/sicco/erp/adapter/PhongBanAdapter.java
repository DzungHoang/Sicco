package com.sicco.erp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.sicco.erp.model.PhongBan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PhongBanAdapter extends ArrayAdapter<PhongBan> {

	Context mContext;
	int mResourceID;
	List<PhongBan> mPhongBan;

	public PhongBanAdapter(Context context, int resource,
			ArrayList<PhongBan> mPhongBan) {
		super(context, resource, mPhongBan);
		mContext = context;
		mResourceID = resource;
		mPhongBan = mPhongBan;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);
		PhongBan phongBan = getItem(position);
		TextView tenPhongBan = (TextView) mView.findViewById(android.R.id.text1);
		tenPhongBan.setText(phongBan.getTenPhongBan());
		return mView;
	}

}
