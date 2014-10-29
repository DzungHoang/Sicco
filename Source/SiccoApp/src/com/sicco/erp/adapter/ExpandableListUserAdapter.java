package com.sicco.erp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.ThemCongViecActivity;
import com.sicco.erp.model.NguoiDung;
import com.sicco.erp.model.PhongBan;

public class ExpandableListUserAdapter extends BaseExpandableListAdapter {
	Context mContext;
	List<PhongBan> mPhongBan;
	HashMap<String, List<NguoiDung>> mNguoiDung;
//	ArrayList<NguoiDung> list = ThemCongViecActivity.listActive;
	ArrayList<NguoiDung> list;
	
	public ExpandableListUserAdapter(Context context, List<PhongBan> phongBan,
			HashMap<String, List<NguoiDung>> nguoiDung, ArrayList<NguoiDung> listChecked) {
		mContext = context;
		mPhongBan = phongBan;
		mNguoiDung = nguoiDung;
		list = listChecked;
	}

	@Override
	public int getGroupCount() {
		return mPhongBan.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mNguoiDung.get(mPhongBan.get(groupPosition).getTenPhongBan())
				.size();
	}

	@Override
	public PhongBan getGroup(int groupPosition) {
		return mPhongBan.get(groupPosition);
	}

	@Override
	public NguoiDung getChild(int groupPosition, int childPosition) {
		return mNguoiDung.get(mPhongBan.get(groupPosition).getTenPhongBan())
				.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		PhongBan phongBan = getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater
					.inflate(R.layout.group_phong_ban, null);
		}
		TextView tenPhongBan = (TextView) convertView
				.findViewById(R.id.tenPhongBan);
		tenPhongBan.setText(phongBan.getTenPhongBan());
		tenPhongBan.setTag(phongBan.getId());
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		NguoiDung nguoiDung = getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.child_nguoi_dung,
					null);
		}
		CheckBox tenTaiKhoan = (CheckBox) convertView
				.findViewById(R.id.tenTaiKhoan);
//		if(!list.isEmpty()){
//			for (int i = 0; i < list.size(); i++) {
//				Log.d("TuNT", "size = "+list.size());
//				Log.d("TuNT", ""+list);
//				if (list.get(i).getId().contains(mNguoiDung.get(
//						mPhongBan.get(groupPosition).getTenPhongBan()).get(
//						childPosition).getId())) {
//					tenTaiKhoan.setChecked(true);
//				}
//				else{
//					tenTaiKhoan.setChecked(false);
//				}
//			}
//		}
		tenTaiKhoan.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Toast.makeText(
						mContext,
						"G: " + groupPosition + " / C: " + childPosition
								+ " / isChecked: " + arg1, 0).show();
				NguoiDung nguoiDung = mNguoiDung.get(
						mPhongBan.get(groupPosition).getTenPhongBan()).get(
						childPosition);
				if (arg1) {
					if(list.isEmpty()){
						list.add(nguoiDung);
					}else{
						Log.d("TuNT", "else");
						boolean exist = false;
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).getId()
									.equals(nguoiDung.getId())) {
								exist = true;
							}
						}
						if(exist == false){
							list.add(nguoiDung);
						}
					}
				} else {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getId()
								.contains(nguoiDung.getId())) {
							list.remove(list.get(i));
						}
					}
				}
			}
		});
		tenTaiKhoan.setText(nguoiDung.getUsername());
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("id", nguoiDung.getId());
		hashMap.put("username", nguoiDung.getUsername());
		hashMap.put("phong_ban", nguoiDung.getPhongban());
		tenTaiKhoan.setTag(hashMap);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
