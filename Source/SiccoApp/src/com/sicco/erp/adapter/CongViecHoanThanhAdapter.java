package com.sicco.erp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.model.TatCaCongViec;

public class CongViecHoanThanhAdapter extends ArrayAdapter<TatCaCongViec> {

	Context mContext;
	int mResourceID;
	List<TatCaCongViec> mCongViecHoanThanh;

	public CongViecHoanThanhAdapter(Context context, int resource,
			ArrayList<TatCaCongViec> objects) {
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
		TatCaCongViec congViecHoanThanh = getItem(position);
		TextView tenCongViec = (TextView) mView
				.findViewById(R.id.item_lv_ten_cong_viec);
		tenCongViec.setText(congViecHoanThanh.getTenCongViec());
		TextView hancuoi = (TextView) mView
				.findViewById(R.id.item_lv_ngay_hoan_thanh);
		hancuoi.setText(congViecHoanThanh.getHanCuoi());
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("id", congViecHoanThanh.getID());
		hashMap.put("ten_cong_viec", congViecHoanThanh.getTenCongViec());
		hashMap.put("ngay_bat_dau", congViecHoanThanh.getNgayBatDau());
		hashMap.put("tinh_trang", congViecHoanThanh.getTinhTrang());
		hashMap.put("tien_do", congViecHoanThanh.getTienDo());
		hashMap.put("nguoi_thuc_hien", congViecHoanThanh.getNguoiThucHien());
		hashMap.put("phong_ban", congViecHoanThanh.getPhongBan());
		hashMap.put("loai_cong_viec", congViecHoanThanh.getLoaiCongViec());
		hashMap.put("ngay_ket_thuc", congViecHoanThanh.getHanCuoi());
		hashMap.put("du_an", congViecHoanThanh.getDuAn());
		hashMap.put("muc_uu_tien", congViecHoanThanh.getMucUuTien());
		hashMap.put("nguoi_duoc_xem", congViecHoanThanh.getNguoiDuocXem());
		hashMap.put("nguoi_giao", congViecHoanThanh.getNguoiGiao());
		hashMap.put("mo_ta", congViecHoanThanh.getMoTa());
		hashMap.put("tong_hop_bao_cao", congViecHoanThanh.getTongHopBaoCao());
		hashMap.put("url", congViecHoanThanh.getUrl());
		
		mView.setTag(hashMap);
		return mView;
	}
}
