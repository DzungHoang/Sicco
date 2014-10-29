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
import com.sicco.erp.model.NguoiDung;
import com.sicco.erp.model.PhongBan;

public class ExpandableListUserAdapter extends BaseExpandableListAdapter {
	Context mContext;
	List<PhongBan> mPhongBan;
	HashMap<String, List<NguoiDung>> mNguoiDung;
	// ArrayList<NguoiDung> list = ThemCongViecActivity.listActive;
	ArrayList<NguoiDung> list;

	public ExpandableListUserAdapter(Context context, List<PhongBan> phongBan,
			HashMap<String, List<NguoiDung>> nguoiDung,
			ArrayList<NguoiDung> listChecked) {
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
		final NguoiDung nguoiDung = getChild(groupPosition, childPosition);
		Log.d("TuNT","=====================  getChildView  ============================");
		CheckBox tenTaiKhoan;
		boolean createNew = true;
		//Check if getChildView is recalled with the same groupPos & childPos => ignore create new view
		if(convertView != null){
			tenTaiKhoan = (CheckBox) convertView
					.findViewById(R.id.tenTaiKhoan);
			if(tenTaiKhoan != null){
				NguoiDung tag = (NguoiDung) tenTaiKhoan.getTag();
				if(tag != null){
					if(tag.equal(nguoiDung)) createNew = false;
				}
			}
		}
		//inflate new view
		if (createNew) {
			Log.d("TuNT", "inflate new view");
			LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.child_nguoi_dung,
					null);
		}
		 
		tenTaiKhoan = (CheckBox) convertView
				.findViewById(R.id.tenTaiKhoan);
		
		Log.d("TuNT", "getChildView: groupPosition = " + groupPosition + "; childPosition = " + childPosition);
		Log.d("TuNT", "list.size() = " + list.size());
		Log.d("TuNT", "nguoiDung = " + nguoiDung.toString());
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Log.d("TuNT", "" + list.get(i).toString());
				if (list.get(i).equal(nguoiDung)) {
					Log.d("TuNT","setChecked: true");
					tenTaiKhoan.setChecked(true);
					break;
				} else {
					tenTaiKhoan.setChecked(false);
				}
			}
		}
		tenTaiKhoan.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Log.d("TuNT","============== onCheckedChanged: " + arg1 +" =================");
				Log.d("TuNT", "list.size() = " + list.size());
				Log.d("TuNT", "nguoiDung = " + nguoiDung.toString());
				Log.d("TuNT", "arg0.getTag() = " + arg0.getTag().toString());
				Log.d("TuNT", "mNguoiDung = " + mNguoiDung.get(mPhongBan.get(groupPosition).getTenPhongBan()).get(childPosition).toString());
				if (arg1) {
					if (list.isEmpty()) {
						Log.d("TuNT", "Add: " + nguoiDung.toString());
						list.add(nguoiDung);
					} else {
						boolean exist = false;
						for (int i = 0; i < list.size(); i++) {
							if (list.get(i).equal(nguoiDung)) {
								exist = true;
							}
						}
						if (exist == false) {
							Log.d("TuNT", "Add: " + nguoiDung.toString());
							list.add(nguoiDung);
						}
					}
				} else {
					for (int i = 0; i < list.size(); i++) {
						Log.d("TuNT", "" + list.get(i).toString());
						if (list.get(i).equal(nguoiDung)) {
							list.remove(list.get(i));
						}
					}
				}
				Log.d("TuNT", "list.size() = " + list.size());
			}
		});
		tenTaiKhoan.setText(nguoiDung.getUsername());
		tenTaiKhoan.setTag(nguoiDung);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
