package com.sicco.erp.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import com.sicco.erp.R;
import com.sicco.erp.R.id;
import com.sicco.erp.model.CongViec;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CongViecAdapter extends ArrayAdapter<CongViec> {
	Context mContext;
	int mResourceID;
	List<CongViec> mCongViec;

	public CongViecAdapter(Context context, int resource, List<CongViec> objects) {
		super(context, resource, objects);
		mContext = context;
		mResourceID = resource;
		mCongViec = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);

		CongViec congViec = getItem(position);
		TextView jobTitle = (TextView) mView.findViewById(R.id.jobTitle);
		jobTitle.setText(congViec.getTitle());
		Drawable image = mContext.getResources().getDrawable(congViec.getImage());
		jobTitle.setCompoundDrawablesWithIntrinsicBounds(image, null, null, null);
//		HashMap<String, String> tag = new HashMap<String, String>();
//		tag.put("tag_id", congViec.getID());
//		tag.put("tag_title", congViec.getTitle());
//		tag.put("tag_code", congViec.getCode());
//		tag.put("tag_mota", congViec.getMota());
//		tag.put("tag_status", congViec.getStatus());
//		tag.put("tag_date_start", congViec.getDateStart());
//		tag.put("tag_date_end", congViec.getDateEnd());
//		mView.setTag(tag);

		return mView;
	}

}
