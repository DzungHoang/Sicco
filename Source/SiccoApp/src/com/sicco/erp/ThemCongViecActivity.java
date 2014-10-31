package com.sicco.erp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.ExFilePickerParcelObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.adapter.DuAnAdapter;
import com.sicco.erp.adapter.ExpandableListUserAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.DuAn;
import com.sicco.erp.model.NguoiDung;
import com.sicco.erp.model.PhongBan;

public class ThemCongViecActivity extends Activity implements OnClickListener {
	private static final int EX_FILE_PICKER_RESULT = 0;
	LinearLayout mLayoutNgayHoanThanh;
	LinearLayout mLayoutDuAn;
	LinearLayout mLayoutNguoiXuLy;
	LinearLayout mLayoutNguoiXem;
	LinearLayout mLayoutTepDinhKem;
	TextView tvTepDinhKem;
	TextView tvNgayhoanThanh;
	Dialog mDialog;
	String path;

	ProgressDialog pDialog;
	SessionManager sessionManager;
	String url_duan = "http://apis.mobile.vareco.vn/sicco/duan.php";
	String url_phongban = "http://apis.mobile.vareco.vn/sicco/phongban.php";
	String url_nguoidung = "http://apis.mobile.vareco.vn/sicco/nguoidung.php";
	String url_themcongviec = "http://apis.mobile.vareco.vn/sicco/themcongviec.php";
	JSONArray duan = null, phongBan = null, nguoiDung = null;
	ArrayList<HashMap<String, String>> duAnList;
	ArrayList<DuAn> mDuAn;
	ListView mLvDuAn;
	DuAnAdapter mDuAnAdapter;

	EditText edTenCongViec;
	EditText edNDCongViec;
	TextView tvChonDuAn;
	TextView tvChonNguoiXuLy;
	TextView tvChonNguoiXem;

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
	// /////////////String, int data//////////
	int positionDuAn = -1;
	String nguoiXuLy;
	String nguoiXem;
	// ////////////Post data//////////////
	String token;
	StringBuilder ngayHoanThanh;
	String idDuAn;
	String idNguoiXuLy;
	String idNguoiXem;
	String stTepDinhKem;

	private static final int FILE_SELECT_CODE = 0;

	static final int DATE_DIALOG_ID = 111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_them_cong_viec);

		sessionManager = SessionManager.getInstance(getApplicationContext());
		token = sessionManager.getUserDetails().get(SessionManager.KEY_TOKEN);

		mLayoutNgayHoanThanh = (LinearLayout) findViewById(R.id.layout_ngay_hoan_thanh);
		mLayoutDuAn = (LinearLayout) findViewById(R.id.layout_du_an);
		mLayoutNguoiXuLy = (LinearLayout) findViewById(R.id.layout_nguoi_xu_ly);
		mLayoutNguoiXem = (LinearLayout) findViewById(R.id.layout_nguoi_xem);
		mLayoutTepDinhKem = (LinearLayout) findViewById(R.id.layout_tep_dinh_kem);

		edTenCongViec = (EditText) findViewById(R.id.edt_ten_cong_viec);
		edNDCongViec = (EditText) findViewById(R.id.edt_noi_dung);
		tvChonDuAn = (TextView) findViewById(R.id.tv_du_an);
		tvChonNguoiXuLy = (TextView) findViewById(R.id.tv_nguoi_xu_ly);
		tvChonNguoiXem = (TextView) findViewById(R.id.tv_nguoi_xem);

		tvTepDinhKem = (TextView) findViewById(R.id.tv_tep_dinh_kem);
		tvTepDinhKem.setText(R.string.chon_tep_dinh_kem);
		tvNgayhoanThanh = (TextView) findViewById(R.id.tv_ngay_hoan_thanh);
		final Calendar c = Calendar.getInstance();
		date = c.get(Calendar.DATE);
		months = c.get(Calendar.MONTH);
		years_now = c.get(Calendar.YEAR);
		ngayHoanThanh = new StringBuilder().append(" ")
				.append(padding_str(date)).append("-")
				.append(padding_str(months + 1)).append("-")
				.append(padding_str(years_now));
		tvNgayhoanThanh.setText(ngayHoanThanh);

		dataPB = new ArrayList<PhongBan>();
		dataND = new HashMap<String, List<NguoiDung>>();
		dsNguoiDung = new ArrayList<NguoiDung>();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_ngay_hoan_thanh:
			showDialog(DATE_DIALOG_ID);

			break;
		case R.id.layout_du_an:

			new GetDuAn().execute();
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View dialogDuAn = inflater.inflate(R.layout.duan_dialog, null);

			loading = (LinearLayout) dialogDuAn
					.findViewById(R.id.loadingListDuAn);
			mLvDuAn = (ListView) dialogDuAn.findViewById(R.id.lv_duan);
			mLvDuAn.setVisibility(View.INVISIBLE);
			loading.setVisibility(View.VISIBLE);

			duAnList = new ArrayList<HashMap<String, String>>();
			mDuAn = new ArrayList<DuAn>();
			mDuAnAdapter = new DuAnAdapter(ThemCongViecActivity.this,
					R.layout.item_dialog_du_an, mDuAn);

			setLoadingFinishListener(new LoadingDuAnFinishListener() {

				@Override
				public void onFinished() {
					mLvDuAn.setVisibility(View.VISIBLE);
					loading.setVisibility(View.INVISIBLE);
					mLvDuAn.setAdapter(mDuAnAdapter);
					if (positionDuAn >= 0) {
						mLvDuAn.setItemChecked(positionDuAn, true);
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
							if (positionDuAn >= 0) {
								tvChonDuAn.setText(mDuAn.get(positionDuAn)
										.getTenDuAn());
								idDuAn = mDuAn.get(positionDuAn).getId();
								Toast.makeText(getApplicationContext(),
										"idDuAn: " + idDuAn, 0).show();
								tvChonDuAn.setError(null);
							}
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
					// Toast.makeText(
					// getApplicationContext(),
					// dataPB.get(groupPosition).getTenPhongBan()
					// + " Expanded", Toast.LENGTH_SHORT).show();
				}
			});
			// Listview Group collasped listener
			listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

				@Override
				public void onGroupCollapse(int groupPosition) {
					// Toast.makeText(
					// getApplicationContext(),
					// dataPB.get(groupPosition).getTenPhongBan()
					// + " Collapsed", Toast.LENGTH_SHORT).show();

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

			AlertDialog.Builder builderNXL = new AlertDialog.Builder(this);
			builderNXL.setTitle(getResources().getString(
					R.string.chon_nguoi_xu_ly));
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
							if (listNXLChecked.isEmpty()) {
								nguoiXuLy = getResources().getString(
										R.string.chon_nguoi_xu_ly);
							} else {
								for (int i = 0; i < listNXLChecked.size(); i++) {
									if (i == listNXLChecked.size() - 1) {
										nguoiXuLy += listNXLChecked.get(i)
												.getUsername();

										idNguoiXuLy += listNXLChecked.get(i)
												.getId();
									} else {
										nguoiXuLy += listNXLChecked.get(i)
												.getUsername() + ", ";

										idNguoiXuLy += listNXLChecked.get(i)
												.getId() + ", ";
									}
								}
								tvChonNguoiXuLy.setError(null);
							}
							tvChonNguoiXuLy.setText(nguoiXuLy);
							Toast.makeText(getApplicationContext(),
									"id: " + idNguoiXuLy, Toast.LENGTH_SHORT)
									.show();
							Log.d("TuNT", "List active: " + listNXLChecked);
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
					// Toast.makeText(
					// getApplicationContext(),
					// dataPB.get(groupPosition).getTenPhongBan()
					// + " Expanded", Toast.LENGTH_SHORT).show();
				}
			});
			// Listview Group collasped listener
			listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

				@Override
				public void onGroupCollapse(int groupPosition) {
					// Toast.makeText(
					// getApplicationContext(),
					// dataPB.get(groupPosition).getTenPhongBan()
					// + " Collapsed", Toast.LENGTH_SHORT).show();
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
								tvChonNguoiXem.setError(null);
							}
							Toast.makeText(getApplicationContext(),
									"id: " + idNguoiXem, Toast.LENGTH_SHORT)
									.show();
							tvChonNguoiXem.setText(nguoiXem);
							Log.d("TuNT", "List active: " + listNXChecked);
						}
					});
			AlertDialog alertDialogNX = builderNX.create();
			alertDialogNX.show();
			break;
		case R.id.layout_tep_dinh_kem:
			Intent intent = new Intent(getApplicationContext(), ru.bartwell.exfilepicker.ExFilePickerActivity.class);
			intent.putExtra(ExFilePicker.SET_ONLY_ONE_ITEM, true);
			intent.putExtra(ExFilePicker.SET_CHOICE_TYPE, ExFilePicker.CHOICE_TYPE_FILES);
			startActivityForResult(intent, EX_FILE_PICKER_RESULT);
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
					.append(padding_str(date)).append("-")
					.append(padding_str(months + 1)).append("-")
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
		}

		@Override
		protected String doInBackground(String... arg0) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("token", token));
			HTTPHandler handler = new HTTPHandler();
			String ret = handler.makeHTTPRequest(url_duan, HTTPHandler.POST,
					nameValuePairs);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					duan = jsonObj.getJSONArray("row");
					// looping through All Contacts
					for (int i = 0; i < duan.length(); i++) {
						JSONObject c = duan.getJSONObject(i);

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
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("token", token));
			HTTPHandler handler = new HTTPHandler();
			String ret = handler.makeHTTPRequest(url_phongban,
					HTTPHandler.POST, nameValuePairs);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					phongBan = jsonObj.getJSONArray("row");
					// looping through All Contacts
					for (int i = 0; i < phongBan.length(); i++) {
						JSONObject c = phongBan.getJSONObject(i);

						String id = c.getString("id_phong_ban");
						String tenphongban = c.getString("ten_phong_ban");

						Log.d("TuNT", "tenphongban = " + tenphongban + " - "
								+ id);
						dataPB.add(new PhongBan(id, tenphongban));
						ArrayList<NguoiDung> data = new ArrayList<NguoiDung>();
						for (int j = 0; j < dsNguoiDung.size(); j++) {
							NguoiDung temp = dsNguoiDung.get(j);
							if (temp.getPhongban().equals(id)) {
								data.add(new NguoiDung(temp.getId(), temp
										.getUsername(), temp.getPhongban()));
							}
						}
						Log.d("TuNT", "data " + data);
						dataND.put(dataPB.get(i).getTenPhongBan(), data);
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
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("token", token));
			HTTPHandler handler = new HTTPHandler();
			String ret = handler.makeHTTPRequest(url_nguoidung,
					HTTPHandler.POST, nameValuePairs);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				Log.d("TuNT", "Nguoi dung: " + result);
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					nguoiDung = jsonObj.getJSONArray("row");
					// looping through All Contacts
					dsNguoiDung.clear();
					for (int i = 0; i < nguoiDung.length(); i++) {
						JSONObject c = nguoiDung.getJSONObject(i);

						String id = c.getString("id");
						String username = c.getString("username");
						String phong_ban = c.getString("phong_ban");

						dsNguoiDung.add(new NguoiDung(id, username, phong_ban));
						dsNguoiDung.clone();
					}
					Log.d("TuNT", "Nguoi dung: " + dsNguoiDung);
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

	// ///////////Gui len cong viec////////////////////////
	private class AddCongViec extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String token = arg0[0];
			String tenCongViec = arg0[1];
			String noiDung = arg0[2];
			String ngayHoanThanh = arg0[3];
			String duAn = arg0[4];
			String nguoiXuLy = arg0[5];
			String nguoiXem = arg0[6];
			String tepDinhKem = arg0[7];
			String phongBan = arg0[8];
			String tuNgay = arg0[9];
			String tienDo = arg0[10];
			String mucDo = arg0[11];

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("token", token));
			nameValuePairs.add(new BasicNameValuePair("ten_cong_viec",
					tenCongViec));
			nameValuePairs.add(new BasicNameValuePair("noi_dung", noiDung));
			nameValuePairs.add(new BasicNameValuePair("ngay_hoan_thanh",
					ngayHoanThanh));
			nameValuePairs.add(new BasicNameValuePair("du_an", duAn));
			nameValuePairs
					.add(new BasicNameValuePair("nguoi_xu_ly", nguoiXuLy));
			nameValuePairs.add(new BasicNameValuePair("nguoi_xem", nguoiXem));
			nameValuePairs.add(new BasicNameValuePair("tep_dinh_kem",
					tepDinhKem));
			nameValuePairs.add(new BasicNameValuePair("phong_ban", phongBan));
			nameValuePairs.add(new BasicNameValuePair("tu_ngay", tuNgay));
			nameValuePairs.add(new BasicNameValuePair("tien_do", tienDo));
			nameValuePairs.add(new BasicNameValuePair("muc_do", mucDo));

			HTTPHandler handler = new HTTPHandler();
			String ret = handler.makeHTTPRequest(url_themcongviec,
					HTTPHandler.POST, nameValuePairs);
			Log.d("TuNT", "nameValuePairs: " + nameValuePairs);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			int success = -1;
			if (result != null) {
				Log.d("TuNT", "jsonCode: "+result);
				try {
					JSONObject jsonObj = new JSONObject(result);
					int stSuccess = jsonObj.getInt("success");
					Log.d("TuNT", "success"+stSuccess);
					if (Integer.toString(stSuccess).equals("1")) {
						success = 1;
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
			if (success == 1) {
				Toast.makeText(getApplicationContext(), "Thêm thành công", 0)
						.show();
			}
			super.onPostExecute(result);
		}

	}
	//////////activity result///////////
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EX_FILE_PICKER_RESULT) {
			if (data != null) {
				ExFilePickerParcelObject object = (ExFilePickerParcelObject) data.getParcelableExtra(ExFilePickerParcelObject.class.getCanonicalName());
				if (object.count > 0) {
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < object.count; i++) {
						buffer.append(object.names.get(i));
						if (i < object.count - 1) buffer.append(", ");
					}
					stTepDinhKem = stTepDinhKem+buffer.toString();
					Log.d("TuNT", "Duong dan file:" + stTepDinhKem);
					tvTepDinhKem.setText(buffer.toString());
				}
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
		int id = item.getItemId();
		switch (id) {
		case R.id.action_cancel:
			finish();
			break;
		case R.id.action_done:
			boolean error = false;
			if (TextUtils.isEmpty(edTenCongViec.getText())) {
				edTenCongViec.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			}
			if (TextUtils.isEmpty(edNDCongViec.getText())) {
				edNDCongViec.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			}
			if (getResources().getString(R.string.chon_du_an).equals(
					tvChonDuAn.getText().toString())) {
				tvChonDuAn.setError("");
				error = true;
			}
			if (getResources().getString(R.string.chon_nguoi_xu_ly).equals(
					tvChonNguoiXuLy.getText().toString())) {
				tvChonNguoiXuLy.setError("");
				error = true;
			}
			if (getResources().getString(R.string.chon_nguoi_xem).equals(
					tvChonNguoiXem.getText().toString())) {
				tvChonNguoiXem.setError("");
				error = true;
			}
			if (error == false) {
				new AddCongViec().execute(token, edTenCongViec.getText()
						.toString(), edNDCongViec.getText().toString(),
						ngayHoanThanh.toString(), idDuAn, idNguoiXuLy,
						idNguoiXem, tvTepDinhKem.getText().toString(), "1",
						"2", "3", "4");
			}
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
