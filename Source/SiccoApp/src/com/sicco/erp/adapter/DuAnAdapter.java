package com.sicco.erp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.sicco.erp.R;
import com.sicco.erp.model.DuAn;

public class DuAnAdapter extends ArrayAdapter<DuAn> {
	Context mContext;
	int mResourceID;
	List<DuAn> mDuAn;

	public DuAnAdapter(Context context, int resource, ArrayList<DuAn> mDuAn) {
		super(context, resource, mDuAn);
		mContext = context;
		mResourceID = resource;
		this.mDuAn = mDuAn;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);
		CheckedTextView item = (CheckedTextView) mView
				.findViewById(R.id.lvItem);
		DuAn duAn = getItem(position);
		item.setText(duAn.getTenDuAn());

		return mView;
	}

}
