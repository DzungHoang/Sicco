package com.sicco.erp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import com.sicco.erp.R;
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
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(
					android.R.layout.simple_spinner_dropdown_item, parent,
					false);
		}
		PhongBan phongBan = getItem(position);
		((TextView) convertView).setText(phongBan.getTenPhongBan());
		return convertView;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = (TextView) View.inflate(mContext,
				android.R.layout.simple_spinner_item, null);
		PhongBan phongBan = getItem(position);
		textView.setText(phongBan.getTenPhongBan());
		
		HashMap<String, String> hashMap = new HashMap();
		hashMap.put("id", phongBan.getId());
		hashMap.put("ten_phong_ban", phongBan.getTenPhongBan());
		textView.setTag(hashMap);
		
		return textView;
	}

}
