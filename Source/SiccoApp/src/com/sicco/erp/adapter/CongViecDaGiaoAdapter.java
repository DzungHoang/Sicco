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
import com.sicco.erp.model.CongViecDaGiao;
import com.sicco.erp.model.TatCaCongViec;

public class CongViecDaGiaoAdapter extends ArrayAdapter<TatCaCongViec> {

	Context mContext;
	int mResourceID;
	List<TatCaCongViec> mCongViecDaGiao;

	public CongViecDaGiaoAdapter(Context context, int resource,
			ArrayList<TatCaCongViec> objects) {
		super(context, resource, objects);
		mContext = context;
		mResourceID = resource;
		mCongViecDaGiao = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		mView = ((LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				mResourceID, parent, false);
		TatCaCongViec congViecDaGiao = getItem(position);
		TextView tenCongViec = (TextView) mView
				.findViewById(R.id.item_lv_ten_cong_viec);
		tenCongViec.setText(congViecDaGiao.getTenCongViec());
		TextView nguoiXuLy = (TextView) mView
				.findViewById(R.id.item_lv_nguoi_xu_ly);
		nguoiXuLy.setText(congViecDaGiao.getNguoiThucHien());
		
		HashMap<String, String> hashMap = new HashMap();
		hashMap.put("id", congViecDaGiao.getID());
		hashMap.put("ten_cong_viec", congViecDaGiao.getTenCongViec());
		hashMap.put("ngay_bat_dau", congViecDaGiao.getNgayBatDau());
		hashMap.put("tinh_trang", congViecDaGiao.getTinhTrang());
		hashMap.put("tien_do", congViecDaGiao.getTienDo());
		hashMap.put("nguoi_thuc_hien", congViecDaGiao.getNguoiThucHien());
		hashMap.put("phong_ban", congViecDaGiao.getPhongBan());
		hashMap.put("loai_cong_viec", congViecDaGiao.getLoaiCongViec());
		hashMap.put("ngay_ket_thuc", congViecDaGiao.getHanCuoi());
		hashMap.put("du_an", congViecDaGiao.getDuAn());
		hashMap.put("muc_uu_tien", congViecDaGiao.getMucUuTien());
		hashMap.put("nguoi_duoc_xem", congViecDaGiao.getNguoiDuocXem());
		hashMap.put("nguoi_giao", congViecDaGiao.getNguoiGiao());
		hashMap.put("mo_ta", congViecDaGiao.getMoTa());
		hashMap.put("tong_hop_bao_cao", congViecDaGiao.getTongHopBaoCao());
		hashMap.put("url", congViecDaGiao.getUrl());
		 
		mView.setTag(hashMap);
		
		return mView;
	}
}
