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
		
		HashMap<String, String> hashMap = new HashMap();
		hashMap.put("id", tatCaCongViec.getID());
		hashMap.put("ten_cong_viec", tatCaCongViec.getTenCongViec());
		hashMap.put("ngay_bat_dau", tatCaCongViec.getNgayBatDau());
		hashMap.put("tinh_trang", tatCaCongViec.getTinhTrang());
		hashMap.put("tien_do", tatCaCongViec.getTienDo());
		hashMap.put("nguoi_thuc_hien", tatCaCongViec.getNguoiThucHien());
		hashMap.put("phong_ban", tatCaCongViec.getPhongBan());
		hashMap.put("loai_cong_viec", tatCaCongViec.getLoaiCongViec());
		hashMap.put("ngay_ket_thuc", tatCaCongViec.getHanCuoi());
		hashMap.put("du_an", tatCaCongViec.getDuAn());
		hashMap.put("muc_uu_tien", tatCaCongViec.getMucUuTien());
		hashMap.put("nguoi_duoc_xem", tatCaCongViec.getNguoiDuocXem());
		hashMap.put("nguoi_giao", tatCaCongViec.getNguoiGiao());
		hashMap.put("mo_ta", tatCaCongViec.getMoTa());
		hashMap.put("tong_hop_bao_cao", tatCaCongViec.getTongHopBaoCao());
		hashMap.put("url", tatCaCongViec.getUrl());
		 
		mView.setTag(hashMap);
		
		return mView;
	}

}
