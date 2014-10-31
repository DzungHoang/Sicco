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

public class CongViecDuocGiaoAdapter extends ArrayAdapter<TatCaCongViec> {
	Context mContext;
	int mResourceID;
	List<TatCaCongViec> mCongViecDuocGiao;

	public CongViecDuocGiaoAdapter(Context context, int resource,
			ArrayList<TatCaCongViec> objects) {
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
		TatCaCongViec congViecDuocGiao = getItem(position);
		TextView tenCongViec = (TextView) mView
				.findViewById(R.id.item_lv_ten_cong_viec);
		tenCongViec.setText(congViecDuocGiao.getTenCongViec());
		TextView nguoigiao = (TextView) mView
				.findViewById(R.id.item_lv_nguoi_giao);
		nguoigiao.setText(congViecDuocGiao.getNguoiGiao());
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("id", congViecDuocGiao.getID());
		hashMap.put("ten_cong_viec", congViecDuocGiao.getTenCongViec());
		hashMap.put("ngay_bat_dau", congViecDuocGiao.getNgayBatDau());
		hashMap.put("tinh_trang", congViecDuocGiao.getTinhTrang());
		hashMap.put("tien_do", congViecDuocGiao.getTienDo());
		hashMap.put("nguoi_thuc_hien", congViecDuocGiao.getNguoiThucHien());
		hashMap.put("phong_ban", congViecDuocGiao.getPhongBan());
		hashMap.put("loai_cong_viec", congViecDuocGiao.getLoaiCongViec());
		hashMap.put("ngay_ket_thuc", congViecDuocGiao.getHanCuoi());
		hashMap.put("du_an", congViecDuocGiao.getDuAn());
		hashMap.put("muc_uu_tien", congViecDuocGiao.getMucUuTien());
		hashMap.put("nguoi_duoc_xem", congViecDuocGiao.getNguoiDuocXem());
		hashMap.put("nguoi_giao", congViecDuocGiao.getNguoiGiao());
		hashMap.put("mo_ta", congViecDuocGiao.getMoTa());
		hashMap.put("tong_hop_bao_cao", congViecDuocGiao.getTongHopBaoCao());
		hashMap.put("url", congViecDuocGiao.getUrl());
		
		mView.setTag(hashMap);
		return mView;
	}
}
