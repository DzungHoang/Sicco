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
import com.sicco.erp.model.CongViecDuocGiao;

public class CongViecDuocGiaoAdapter extends ArrayAdapter<CongViecDuocGiao> {
	Context mContext;
	int mResourceID;
	List<CongViecDuocGiao> mCongViecDuocGiao;

	public CongViecDuocGiaoAdapter(Context context, int resource,
			ArrayList<CongViecDuocGiao> objects) {
		super(context, resource, objects);
		mContext = context;
		mResourceID = resource;
		mCongViecDuocGiao = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);
		CongViecDuocGiao congViecDuocGiao = getItem(position);
		TextView tenCongViec = (TextView) mView
				.findViewById(R.id.item_lv_ten_cong_viec);
		tenCongViec.setText(congViecDuocGiao.getTenCongViec());
		TextView nguoiGiao = (TextView) mView
				.findViewById(R.id.item_lv_nguoi_giao);
		nguoiGiao.setText(congViecDuocGiao.getNguoiGiao());
		return mView;
	}
}
