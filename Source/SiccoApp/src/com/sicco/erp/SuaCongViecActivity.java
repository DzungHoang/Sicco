package com.sicco.erp;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.sicco.erp.ThemCongViecActivity.LoadingDuAnFinishListener;
import com.sicco.erp.ThemCongViecActivity.LoadingFinishListener;
import com.sicco.erp.ThemCongViecActivity.LoadingNguoiDungFinishListener;
import com.sicco.erp.adapter.DuAnAdapter;
import com.sicco.erp.adapter.ExpandableListUserAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.DuAn;
import com.sicco.erp.model.NguoiDung;
import com.sicco.erp.model.PhongBan;
import com.sicco.erp.utils.FileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.appcompat.R.string;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class SuaCongViecActivity extends Activity implements OnClickListener {
	LinearLayout mLayoutNgayHoanThanh;
	LinearLayout mLayoutDuAn;
	LinearLayout mLayoutNguoiXuLy;
	LinearLayout mLayoutNguoiXem;
	LinearLayout mLayoutTepDinhKem;
	TextView tvChonDuAn;
	TextView tvTepDinhKem;
	TextView tvNgayhoanThanh;
	TextView tvChonNguoiXuLy;
	TextView tvChonNguoiXem;
	EditText edtTenCongViec;
	EditText edtNoiDungCongViec;
	
	String  postTenCongViec,postNoiDung,postNgayHoanThanh,postIdDuAn,postNguoiXuLy,postNguoiXem,postPathTepDinhKem;

	
	Dialog mDialog;
	String path;

	ProgressDialog pDialog;
	String url_duan = "http://apis.mobile.vareco.vn/sicco/duan.php";
	String url_phongban = "http://apis.mobile.vareco.vn/sicco/phongban.php";
	String url_nguoidung = "http://apis.mobile.vareco.vn/sicco/nguoidung.php";
	JSONArray DuAn = null, PhongBan = null, NguoiDung = null;
	ArrayList<HashMap<String, String>> duAnList;
	ArrayList<DuAn> mDuAn;
	ListView mLvDuAn;
	DuAnAdapter mDuAnAdapter;

	
	

	ExpandableListView listView;
	ExpandableListUserAdapter adapter;
	LinearLayout loading;
	ArrayList<NguoiDung> listNXLChecked = new ArrayList<NguoiDung>();
	ArrayList<NguoiDung> listNXChecked = new ArrayList<NguoiDung>();
	
	List<PhongBan> dataPB;
	HashMap<String, List<NguoiDung>> dataND;
	HashMap<String, ArrayList<NguoiDung>> hashMap;
	ArrayList<NguoiDung> dsNguoiDung;
	
	private int date;
	private int months;
	private int years_now;
	// /////////////Positon data//////////
	int positionDuAn;
	int positionPBNXL;
	int positionNXL;
	String nguoiXuLy;
	String nguoiXem;
	// ////////////Post data//////////////
	String idDuAn;
	String idNguoiXuLy;
	String idNguoiXem;
	
	String id, tenCongViec, tinhTrang, tienDo, nguoiThucHien, phongBan,
	loaiCongViec, ngayKetThuc, duAn, mucUuTien, nguoiDuocXem,
	nguoiGiao, moTa, tongHopBaoCao, Url, token, username;
	private static final String TAG = "SuaCongViecActivity";
	private static final int FILE_SELECT_CODE = 0;

	static final int DATE_DIALOG_ID = 111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_them_cong_viec);
		
		SessionManager sessionManager = SessionManager
				.getInstance(SuaCongViecActivity.this);
		token = sessionManager.getUserDetails().get(SessionManager.KEY_TOKEN);
		username = sessionManager.getUserDetails().get(SessionManager.KEY_NAME);
		
		mLayoutNgayHoanThanh = (LinearLayout) findViewById(R.id.layout_ngay_hoan_thanh);
		mLayoutDuAn = (LinearLayout) findViewById(R.id.layout_du_an);
		mLayoutNguoiXuLy = (LinearLayout) findViewById(R.id.layout_nguoi_xu_ly);
		mLayoutNguoiXem = (LinearLayout) findViewById(R.id.layout_nguoi_xem);
		mLayoutTepDinhKem = (LinearLayout) findViewById(R.id.layout_tep_dinh_kem);
		
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		tenCongViec = intent.getStringExtra("ten_cong_viec");
		tinhTrang = intent.getStringExtra("tinh_trang");
		tienDo = intent.getStringExtra("tien_do");
		nguoiThucHien = intent.getStringExtra("nguoi_thuc_hien");
		phongBan = intent.getStringExtra("phong_ban");
		loaiCongViec = intent.getStringExtra("loai_cong_viec");
		ngayKetThuc = intent.getStringExtra("ngay_ket_thuc");
		duAn = intent.getStringExtra("du_an");
		mucUuTien = intent.getStringExtra("muc_uu_tien");
		nguoiDuocXem = intent.getStringExtra("nguoi_duoc_xem");
		nguoiGiao = intent.getStringExtra("nguoi_giao");
		moTa = intent.getStringExtra("mo_ta");
		tongHopBaoCao = intent.getStringExtra("tong_hop_bao_cao");
		Url = intent.getStringExtra("Url");
		
		final String[] listNguoiThucHien = nguoiThucHien.split(",");
		for (int i = 0; i < listNguoiThucHien.length; i++) {
			listNXLChecked.add(new NguoiDung("", listNguoiThucHien[i], ""));
		}
		final String[] listNguoiXem = nguoiDuocXem.split(",");
		for (int i = 0; i < listNguoiXem.length; i++) {
			listNXChecked.add(new NguoiDung("", listNguoiXem[i], ""));
		}
		Toast.makeText(getApplicationContext(), "nguoithuchien 1:"+listNguoiXem[0].toString()
				+ "nguoithuchien 2:"+listNguoiXem[1].toString(), 0).show();
		
		tvChonDuAn = (TextView) findViewById(R.id.tv_du_an);
		tvChonNguoiXuLy = (TextView) findViewById(R.id.tv_nguoi_xu_ly);
		tvTepDinhKem = (TextView) findViewById(R.id.tv_tep_dinh_kem);
		tvNgayhoanThanh = (TextView) findViewById(R.id.tv_ngay_hoan_thanh);
		tvChonNguoiXem = (TextView) findViewById(R.id.tv_nguoi_xem);
		edtTenCongViec = (EditText) findViewById(R.id.edt_ten_cong_viec);
		edtNoiDungCongViec = (EditText) findViewById(R.id.edt_noi_dung);
		
		tvTepDinhKem.setText(R.string.chon_tep_dinh_kem);
		edtTenCongViec.setText(tenCongViec);
		edtNoiDungCongViec.setText(moTa);
		tvChonDuAn.setText(duAn);
		tvNgayhoanThanh.setText(ngayKetThuc);
		tvChonNguoiXuLy.setText(nguoiThucHien);
		tvChonNguoiXem.setText(nguoiDuocXem);

		final String[] valueNgayHoanThanh = ngayKetThuc.split("/");
		Toast.makeText(getApplicationContext(), valueNgayHoanThanh[0].toString() + "/" +valueNgayHoanThanh[1].toString() + "/" +valueNgayHoanThanh[2].toString(), 0).show();
		date = Integer.parseInt(valueNgayHoanThanh[0].toString());
		months = Integer.parseInt(valueNgayHoanThanh[1].toString());
		years_now = Integer.parseInt(valueNgayHoanThanh[2].toString());
		
		dataPB = new ArrayList<PhongBan>();
		dataND = new HashMap<String, List<NguoiDung>>();
		dsNguoiDung = new ArrayList<NguoiDung>();
	}

	@Override
	protected void onResume() {
		if (path != null) {
			String fileName = path.substring(path.lastIndexOf("/") + 1);
			tvTepDinhKem.setText(fileName);
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ngay_hoan_thanh:
			showDialog(DATE_DIALOG_ID);

			break;
		case R.id.layout_du_an:
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View dialogDuAn = inflater.inflate(R.layout.duan_dialog, null);
			loading = (LinearLayout) dialogDuAn
					.findViewById(R.id.loadingListDuAn);
			duAnList = new ArrayList<HashMap<String, String>>();
			new GetDuAn().execute();
			mDuAn = new ArrayList<DuAn>();
			mLvDuAn = (ListView) dialogDuAn.findViewById(R.id.lv_duan);
			mDuAnAdapter = new DuAnAdapter(SuaCongViecActivity.this,R.layout.item_dialog_du_an, mDuAn);
			
			setLoadingFinishListener(new LoadingDuAnFinishListener() {

				@Override
				public void onFinished() {
					mLvDuAn.setVisibility(View.VISIBLE);
					loading.setVisibility(View.INVISIBLE);
					mLvDuAn.setAdapter(mDuAnAdapter);
					for (int i = 0; i < mDuAn.size(); i++) {
						Toast.makeText(getApplicationContext(), mDuAn.get(i).getTenDuAn()+"/"+duAn, 0).show();
						if (mDuAn.get(i).getTenDuAn().equals(duAn)) {
							mLvDuAn.setItemChecked(i, true);
						}
					}
				}
			});
			mLvDuAn.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					positionDuAn = arg2;
				}
			});
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(R.string.chon_du_an));
			builder.setView(dialogDuAn);
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialogInterface,
								int arg1) {
							dialogInterface.dismiss();

						}
					});
			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							tvChonDuAn.setText(mDuAn.get(positionDuAn)
									.getTenDuAn());
							idDuAn = mDuAn.get(positionDuAn).getId();
							Toast.makeText(getApplicationContext(),
									"idDuAn: " + idDuAn, 0).show();
						}
					});
			AlertDialog alertDialog = builder.create();
			alertDialog.show();

			break;
		case R.id.layout_nguoi_xu_ly:
			new GetNguoiDung().execute();

			LayoutInflater inflaterNXL = LayoutInflater
					.from(getApplicationContext());
			View dialogUserList = inflaterNXL.inflate(
					R.layout.nguoidung_dialog, null);

			listView = (ExpandableListView) dialogUserList
					.findViewById(R.id.ListExpUser);

			loading = (LinearLayout) dialogUserList
					.findViewById(R.id.loadingListUser);

			listView.setVisibility(View.INVISIBLE);
			loading.setVisibility(View.VISIBLE);

			adapter = new ExpandableListUserAdapter(getApplicationContext(),
					dataPB, dataND, listNXLChecked);
			setLoadingFinishListener(new LoadingNguoiDungFinishListener() {

				@Override
				public void onFinished() {
					new GetPhongBan().execute();
				}
			});

			setLoadingFinishListener(new LoadingFinishListener() {

				@Override
				public void onFinished() {
					listView.setVisibility(View.VISIBLE);
					loading.setVisibility(View.INVISIBLE);
					listView.setAdapter(adapter);
				}
			});

			listView.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					return false;
				}
			});

			// Listview Group expanded listener
			listView.setOnGroupExpandListener(new OnGroupExpandListener() {

				@Override
				public void onGroupExpand(int groupPosition) {
					Toast.makeText(
							getApplicationContext(),
							dataPB.get(groupPosition).getTenPhongBan()
									+ " Expanded", Toast.LENGTH_SHORT).show();
				}
			});
			// Listview Group collasped listener
			listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

				@Override
				public void onGroupCollapse(int groupPosition) {
					Toast.makeText(
							getApplicationContext(),
							dataPB.get(groupPosition).getTenPhongBan()
									+ " Collapsed", Toast.LENGTH_SHORT).show();

				}
			});

			// Listview on child click listener
			listView.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					// Toast.makeText(
					// getApplicationContext(),
					// dataPB.get(groupPosition).getTenPhongBan()
					// + " : "
					// + dataND.get(
					// dataPB.get(groupPosition)
					// .getTenPhongBan()).get(
					// childPosition).getId(), Toast.LENGTH_SHORT)
					// .show();
					positionPBNXL = groupPosition;
					positionNXL = childPosition;
					return false;
				}
			});

			AlertDialog.Builder builderNXL = new AlertDialog.Builder(this);
			builderNXL.setTitle(getResources().getString(R.string.chon_nguoi_xu_ly));
			builderNXL.setView(dialogUserList);
			builderNXL.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialogInterface,
								int arg1) {
							dialogInterface.dismiss();

						}
					});
			builderNXL.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							idNguoiXuLy = "";
							nguoiXuLy = "";
							if(listNXLChecked.isEmpty()){
								nguoiXuLy = getResources().getString(R.string.nguoi_xu_ly);
							}else{
								for (int i = 0; i < listNXLChecked.size(); i++) {
									if(i == listNXLChecked.size() - 1){
										nguoiXuLy += listNXLChecked.get(i).getUsername();
									}else{
										nguoiXuLy += listNXLChecked.get(i).getUsername()+", ";
									}
									idNguoiXuLy += listNXLChecked.get(i).getId() + ",";
								}
							}
							Toast.makeText(getApplicationContext(), "id: "+idNguoiXuLy, Toast.LENGTH_SHORT).show();
							tvChonNguoiXuLy.setText(nguoiXuLy);
							Log.d("TuNT", "List active: "+listNXLChecked);
						}
					});
			AlertDialog alertDialogNXL = builderNXL.create();
			alertDialogNXL.show();
			break;
		case R.id.layout_nguoi_xem:
			new GetNguoiDung().execute();

			LayoutInflater inflaterNX = LayoutInflater
					.from(getApplicationContext());
			View dialogUserList2 = inflaterNX.inflate(
					R.layout.nguoidung_dialog, null);

			listView = (ExpandableListView) dialogUserList2
					.findViewById(R.id.ListExpUser);

			loading = (LinearLayout) dialogUserList2
					.findViewById(R.id.loadingListUser);

			listView.setVisibility(View.INVISIBLE);
			loading.setVisibility(View.VISIBLE);

			adapter = new ExpandableListUserAdapter(getApplicationContext(),
					dataPB, dataND, listNXChecked);
			setLoadingFinishListener(new LoadingNguoiDungFinishListener() {

				@Override
				public void onFinished() {
					new GetPhongBan().execute();
				}
			});

			setLoadingFinishListener(new LoadingFinishListener() {

				@Override
				public void onFinished() {
					listView.setVisibility(View.VISIBLE);
					loading.setVisibility(View.INVISIBLE);
					listView.setAdapter(adapter);
				}
			});

			listView.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					return false;
				}
			});

			// Listview Group expanded listener
			listView.setOnGroupExpandListener(new OnGroupExpandListener() {

				@Override
				public void onGroupExpand(int groupPosition) {
//					Toast.makeText(
//							getApplicationContext(),
//							dataPB.get(groupPosition).getTenPhongBan()
//									+ " Expanded", Toast.LENGTH_SHORT).show();
				}
			});
			// Listview Group collasped listener
			listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

				@Override
				public void onGroupCollapse(int groupPosition) {
//					Toast.makeText(
//							getApplicationContext(),
//							dataPB.get(groupPosition).getTenPhongBan()
//									+ " Collapsed", Toast.LENGTH_SHORT).show();
				}
			});

			// Listview on child click listener
			listView.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					return false;
				}
			});

			AlertDialog.Builder builderNX = new AlertDialog.Builder(this);
			builderNX.setTitle(getResources()
					.getString(R.string.chon_nguoi_xem));
			builderNX.setView(dialogUserList2);
			builderNX.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialogInterface,
								int arg1) {
							dialogInterface.dismiss();

						}
					});
			builderNX.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							idNguoiXem = "";
							nguoiXem = "";
							if (listNXChecked.isEmpty()) {
								nguoiXem = getResources().getString(
										R.string.chon_nguoi_xem);
							} else {
								for (int i = 0; i < listNXChecked.size(); i++) {
									if (i == listNXChecked.size() - 1) {
										nguoiXem += listNXChecked.get(i)
												.getUsername();

										idNguoiXem += listNXChecked.get(i)
												.getId();
									} else {
										nguoiXem += listNXChecked.get(i)
												.getUsername() + ", ";

										idNguoiXem += listNXChecked.get(i)
												.getId() + ", ";
									}
								}
							}
//							Toast.makeText(getApplicationContext(),
//									"id: " + idNguoiXem, Toast.LENGTH_SHORT)
//									.show();
							tvChonNguoiXem.setText(nguoiXem);
							Log.d("TuNT", "List active: " + listNXChecked);
						}
					});
			AlertDialog alertDialogNX = builderNX.create();
			alertDialogNX.show();
			break;
		case R.id.layout_tep_dinh_kem:
			showFileChooser();
			break;

		default:
			break;
		}

	}

	// ------------choose date------------------//

	private DatePickerDialog.OnDateSetListener datePickerListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			date = dayOfMonth;
			months = monthOfYear;
			years_now = year;

			tvNgayhoanThanh.setText(new StringBuilder()
					.append(padding_str(date)).append("/")
					.append(padding_str(months + 1)).append("/")
					.append(padding_str(years_now)));
		}

	};

	@Deprecated
	protected Dialog onCreateDialog(int id) {
		DatePickerDialog datePickerDialog = new DatePickerDialog(this,
				datePickerListener, years_now, months, date);
		datePickerDialog.getDatePicker().setSpinnersShown(false);
		datePickerDialog.getDatePicker().setCalendarViewShown(true);
		return datePickerDialog;
	}

	private static String padding_str(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	// ------------choose du an------------------------//

	private class GetDuAn extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
		}

		@Override
		protected String doInBackground(String... arg0) {
			HTTPHandler handler = new HTTPHandler();
			String ret = handler.makeHTTPRequest(url_duan, HTTPHandler.GET);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			// Dismiss the progress dialog
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					DuAn = jsonObj.getJSONArray("row");
					// looping through All Contacts
					for (int i = 0; i < DuAn.length(); i++) {
						JSONObject c = DuAn.getJSONObject(i);

						String id = c.getString("id_du_an");
						String tenduan = c.getString("ten_du_an");

						Log.d("LuanDT", "tenduan = " + tenduan + " - " + id);

						mDuAn.add(new DuAn(id, tenduan));
						mDuAnAdapter.notifyDataSetChanged();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (mInterfaceDA != null) {
					mInterfaceDA.onFinished();
				}
			} else {
				Log.e("LuanDT", "Khong the lay data tu url nay");
			}

			super.onPostExecute(result);
		}

	}

	private class GetPhongBan extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dataPB.clear();
		}

		@Override
		protected String doInBackground(String... arg0) {
			HTTPHandler handler = new HTTPHandler();
			String ret = handler.makeHTTPRequest(url_phongban, HTTPHandler.GET);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					PhongBan = jsonObj.getJSONArray("row");
					// looping through All Contacts
					for (int i = 0; i < PhongBan.length(); i++) {
						JSONObject c = PhongBan.getJSONObject(i);

						String id = c.getString("id_phong_ban");
						String tenphongban = c.getString("ten_phong_ban");

						Log.d("TuNT", "tenphongban = " + tenphongban + " - "
								+ id);
						dataPB.add(new PhongBan(id, tenphongban));
						ArrayList<NguoiDung> data = new ArrayList<NguoiDung>();
						for (int j = 0; j < dsNguoiDung.size(); j++) {
							if (dsNguoiDung.get(j).getPhongban().equals(id)) {
								data.add(new NguoiDung(id, dsNguoiDung.get(j)
										.getUsername(), dsNguoiDung.get(j)
										.getPhongban()));
							}
						}
						dataND.put(dataPB.get(i).getTenPhongBan(), data);
						// Toast.makeText(getApplicationContext(),
						// ""+dataND.get(dataPB.get(i).getTenPhongBan()),
						// 0).show();

						adapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (mInterface != null) {
					mInterface.onFinished();
				}
			} else {
				Log.e("TuNT", "Khong the lay data tu url nay");
			}
			super.onPostExecute(result);
		}

	}

	private class GetNguoiDung extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			HTTPHandler handler = new HTTPHandler();
			String ret = handler
					.makeHTTPRequest(url_nguoidung, HTTPHandler.GET);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					NguoiDung = jsonObj.getJSONArray("row");
					// looping through All Contacts
					dsNguoiDung.clear();
					for (int i = 0; i < NguoiDung.length(); i++) {
						JSONObject c = NguoiDung.getJSONObject(i);

						String id = c.getString("id");
						String username = c.getString("username");
						String phong_ban = c.getString("phong_ban");

						dsNguoiDung.add(new NguoiDung(id, username, phong_ban));
						dsNguoiDung.clone();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (mInterfaceND != null) {
					mInterfaceND.onFinished();
				}
			} else {
				Log.e("TuNT", "Khong the lay data tu url nay");
			}
			super.onPostExecute(result);
		}

	}

	// ------------choose file-------------------------//

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(Intent.createChooser(intent,
					getString(R.string.chon_tep_dinh_kem)), FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "Please install a File Manager.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				// Get the Uri of the selected file
				Uri uri = data.getData();
				Log.d("LuanDT", "File Uri: " + uri.toString());
				// Get the path
				path = FileUtils.getPath(this, uri);
				Log.d("LuanDT", "File Path: " + path);
				Toast.makeText(this, "File Selected: " + path,
						Toast.LENGTH_SHORT).show();
				// Get the file instance
				// File file = new File(path);
				// Initiate the upload 
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static String getPath(Context context, Uri uri)
			throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

//=========================================POST===========================================================//

		private class postEditCongViec extends AsyncTask<String, Void, String> {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(SuaCongViecActivity.this);
				pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
				pDialog.setCancelable(false);
				pDialog.show();
			}

			@Override
			protected String doInBackground(String... arg0) {
				HTTPHandler handler = new HTTPHandler();
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("token", token));
				nameValuePairs.add(new BasicNameValuePair("username",
						username));
				nameValuePairs.add(new BasicNameValuePair("ten_cong_viec", postTenCongViec));
				nameValuePairs.add(new BasicNameValuePair("noi_dung", postNoiDung));
				nameValuePairs.add(new BasicNameValuePair("ngay_hoan_thanh",postNgayHoanThanh));
				nameValuePairs.add(new BasicNameValuePair("du_an",postIdDuAn));
				nameValuePairs.add(new BasicNameValuePair("nguoi_thuc_hien",postNguoiXuLy));
				nameValuePairs.add(new BasicNameValuePair("nguoi_xem",postNguoiXem));
				nameValuePairs.add(new BasicNameValuePair("tep_dinh_kem",postPathTepDinhKem));
				String ret = handler.makeHTTPRequest(url_duan,
						HTTPHandler.POST, nameValuePairs);
				Log.d("LuanDT", "PostEditCongViec : " + nameValuePairs);
				return ret;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				int successEditCongViec = -1;
				if (result != null) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						String resultEditCongViec = jsonObject.getString("success");
						if (resultEditCongViec.equals("1")) {
							successEditCongViec = 1;
						}
						Log.d("NgaDV", "resultEditCongViec = " + resultEditCongViec);
						Log.d("NgaDV", "successEditCongViec = " + successEditCongViec);
						if (successEditCongViec == 1) {
//							new GetThaoLuan().execute(token, id_cong_viec,
//									Integer.toString(page));
							Toast.makeText(
									getApplicationContext(),
									""
											+ getResources()
													.getString(
															R.string.sua_cong_viec_thanh_cong),
									Toast.LENGTH_LONG).show();}
						else {
							Toast.makeText(
									getApplicationContext(),
									""
											+ getResources()
													.getString(
															R.string.sua_cong_viec_khong_thanh_cong),
									Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Log.e("LuanDT", "Error!");
				}
			}
		}
	// ------------option menu-------------------------//

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_them_cong_viec, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_cancel:
			finish();
			break;
		case R.id.action_done:
			
			if (TextUtils.isEmpty(edtTenCongViec.getText())) {
				edtTenCongViec.setError("ban chua nhap ten");
				return true;
			}
			if (TextUtils.isEmpty(edtNoiDungCongViec.getText())) {
				edtNoiDungCongViec.setError("banj chua nhap noi dung");
				return true;
			}
			
			postTenCongViec = edtTenCongViec.getText().toString();
			postNoiDung = edtNoiDungCongViec.getText().toString();
			postNgayHoanThanh = tvNgayhoanThanh.getText().toString();
			postIdDuAn = idDuAn;
			postNguoiXuLy = tvChonNguoiXuLy.getText().toString();
			postNguoiXem = tvChonNguoiXem.getText().toString();
			postPathTepDinhKem = tvTepDinhKem.getText().toString();
			
			Toast.makeText(getApplicationContext(),
					"idDuAnpost: " + idDuAn
					+ "--postTenCongViec" + postTenCongViec
					+ "--postNoiDung" +postNoiDung
					+ "--postNgayHoanThanh" + postNgayHoanThanh
					+ "--postIdDuAn" + postIdDuAn
					+ "--postNguoiXuLy" + postNguoiXuLy
					+ "--postNguoiXem" + postNguoiXem
					+ "--postPathTepDinhKem" + postPathTepDinhKem, 1).show();
			Log.d("NgaDV", "-==========================================================");
			Log.d("NgaDV", "idDuAnpost: " + idDuAn
					+ "--postTenCongViec" + postTenCongViec
					+ "--postNoiDung" +postNoiDung
					+ "--postNgayHoanThanh" + postNgayHoanThanh
					+ "--postIdDuAn" + postIdDuAn
					+ "--postNguoiXuLy" + postNguoiXuLy
					+ "--postNguoiXem" + postNguoiXem
					+ "--postPathTepDinhKem" + postPathTepDinhKem);
			Log.d("NgaDV", "-==========================================================");
			
			new postEditCongViec().execute(token,username,postTenCongViec,postNoiDung,postNgayHoanThanh,
					postIdDuAn,postNguoiXuLy,postNguoiXem,
					postPathTepDinhKem);
			
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private LoadingFinishListener mInterface;

	public void setLoadingFinishListener(LoadingFinishListener listener) {
		mInterface = listener;
	}

	public static interface LoadingFinishListener {
		public void onFinished();
	}

	private LoadingNguoiDungFinishListener mInterfaceND;

	public void setLoadingFinishListener(LoadingNguoiDungFinishListener listener) {
		mInterfaceND = listener;
	}

	public static interface LoadingNguoiDungFinishListener {
		public void onFinished();
	}
	private LoadingDuAnFinishListener mInterfaceDA;

	public void setLoadingFinishListener(LoadingDuAnFinishListener listener) {
		mInterfaceDA = listener;
	}

	public static interface LoadingDuAnFinishListener {
		public void onFinished();
	}
}
