package com.sicco.erp.adapter;

import java.util.List;

import com.sicco.erp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sicco.erp.model.CongVan;

public class CongVanAdapter extends ArrayAdapter<CongVan> {
	
	Context mContext;
	int mIdResource;
	List<CongVan> mCongVan;
	
	public CongVanAdapter(Context context, int resource, List<CongVan> congvan) {
		super(context, resource,congvan);
		
		mCongVan = congvan;
		mIdResource = resource;
		mContext = context;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(mIdResource, parent,false);
		
		CongVan congVan = getItem(position);
		
		TextView txt_title = (TextView) mView.findViewById(R.id.cvan_txt_title);
		if(txt_title != null){
			txt_title.setText(mCongVan.get(position).getTitle());
		}
		TextView txt_detail = (TextView) mView.findViewById(R.id.cvan_txt_detail);
		if(txt_detail != null){
			txt_detail.setText(mCongVan.get(position).getDetail());
		}
		return mView;
	}
 
}
