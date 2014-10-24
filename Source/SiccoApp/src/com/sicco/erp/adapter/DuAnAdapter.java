package com.sicco.erp.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sicco.erp.R;
import com.sicco.erp.model.DuAn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DuAnAdapter extends ArrayAdapter<DuAn> {
	Context mContext;
	int mResourceID;
	List<DuAn> mDuAn;

	public DuAnAdapter(Context context, int resource, ArrayList<DuAn> mDuAn) {
		super(context, resource, mDuAn);
		mContext = context;
		mResourceID = resource;
		mDuAn = mDuAn;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);
		DuAn duAn = getItem(position);
		TextView tenDuAn = (TextView) mView
				.findViewById(android.R.id.text1);
		tenDuAn.setText(duAn.getTenDuAn());
		return mView;
	}

}
