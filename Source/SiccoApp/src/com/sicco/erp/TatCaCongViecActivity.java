package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.sicco.erp.adapter.PhongBanAdapter;
import com.sicco.erp.adapter.TatCaCongViecAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongViecHoanThanhListener;
import com.sicco.erp.database.DBController.LoadCongViecListener;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.model.PhongBan;
import com.sicco.erp.model.TatCaCongViec;

public class TatCaCongViecActivity extends Activity {
	ProgressDialog pDialog;
	ArrayList<TatCaCongViec> mCongViecPhongBan; // L
	ArrayList<TatCaCongViec> mTatCaCongViec;
	ListView mListView;
	TatCaCongViecAdapter mAdapter;
	ActionBar mActionBar;
	ProgressBar pLoadmore;
	Button btn_LoadMore;
	Spinner sPhongBan;
	String url_phongban = "http://apis.mobile.vareco.vn/sicco/phongban.php";
	// String url_phongban = "http://thuchutcoi.tk/sicco/phongban.php";
	JSONArray phongban = null;
	ArrayList<PhongBan> mPhongBan;
	PhongBanAdapter mPhongBanAdapter;
	String idPhongBan;
	static int pnumberCviec = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tat_ca_cong_viec);

		mActionBar = getActionBar();
		mActionBar.setDisplayShowTitleEnabled(true);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(
				R.layout.layout_spinner_tatcacongviec, null);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

		new GetPhongBan().execute();
		mPhongBan = new ArrayList<PhongBan>();
		mPhongBan.add(new PhongBan("0", getResources().getString(
				R.string.tat_ca)));
		sPhongBan = (Spinner) findViewById(R.id.spinnerphongban);
		mPhongBanAdapter = new PhongBanAdapter(TatCaCongViecActivity.this,
				android.R.layout.simple_list_item_1, mPhongBan);
		sPhongBan.setAdapter(mPhongBanAdapter);
		sPhongBan.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String, String> getArray = (HashMap) view.getTag();
				idPhongBan = getArray.get("id");
				String TenPhongBan = getArray.get("ten_phong_ban");

//				Toast.makeText(getApplicationContext(),
//						"Phong ban : " + TenPhongBan + "..." + idPhongBan,
//						Toast.LENGTH_LONG).show();
				if (idPhongBan != "0") {
					congViecPhongBan();
				} else {
					tatCaCongViec();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		pDialog = new ProgressDialog(TatCaCongViecActivity.this);
		pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
		pDialog.setCancelable(true);

		mListView = (ListView) findViewById(R.id.lv_tat_ca_cong_viec);
		View footerView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.btn_loadmore, null,false);
		btn_LoadMore = (Button)footerView.findViewById(R.id.LoadMore);
		mListView.addFooterView(footerView);
		pLoadmore = (ProgressBar)findViewById(R.id.progressBar1);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> getArray = (HashMap) view.getTag();
				Intent intent = new Intent();
				intent.putExtra("id", getArray.get("id"));
				intent.putExtra("ten_cong_viec", getArray.get("ten_cong_viec"));
				intent.putExtra("tinh_trang", getArray.get("tinh_trang"));
				intent.putExtra("tien_do", getArray.get("tien_do"));
				intent.putExtra("nguoi_thuc_hien",
						getArray.get("nguoi_thuc_hien"));
				intent.putExtra("phong_ban", getArray.get("phong_ban"));
				intent.putExtra("loai_cong_viec",
						getArray.get("loai_cong_viec"));
				intent.putExtra("ngay_ket_thuc", getArray.get("ngay_ket_thuc"));
				intent.putExtra("du_an", getArray.get("du_an"));
				intent.putExtra("muc_uu_tien", getArray.get("muc_uu_tien"));
				intent.putExtra("nguoi_duoc_xem",
						getArray.get("nguoi_duoc_xem"));
				intent.putExtra("nguoi_giao", getArray.get("nguoi_giao"));
				intent.putExtra("mo_ta", getArray.get("mo_ta"));
				intent.putExtra("tong_hop_bao_cao",
						getArray.get("tong_hop_bao_cao"));
				intent.putExtra("Url", getArray.get("Url"));

				intent.setClass(getApplicationContext(),
						ChiTietCongViecActivity.class);
				startActivity(intent);
			}
		});
		
	}

	private void tatCaCongViec() {
		
		final DBController controller = DBController
				.getInstance(getApplicationContext());
		mTatCaCongViec = controller.getCongViec(1, new LoadCongViecListener() {

			@Override
			public void onFinished(ArrayList<TatCaCongViec> data) {
				if (pDialog.isShowing())
					pDialog.dismiss();
				mTatCaCongViec.clear();
				mTatCaCongViec.addAll(data);
				mAdapter.notifyDataSetChanged();
			}
		});
		if (mTatCaCongViec == null) {
			pDialog.show();
			mTatCaCongViec = new ArrayList<TatCaCongViec>();
		}
		btn_LoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pLoadmore.setVisibility(View.VISIBLE);
				pnumberCviec = pnumberCviec+1;
				controller.getCongViec(pnumberCviec, new LoadCongViecListener() {
					
					@Override
					public void onFinished(ArrayList<TatCaCongViec> data) {
						mTatCaCongViec.clear();
						mTatCaCongViec.addAll(data);
						mAdapter.notifyDataSetChanged();
						pLoadmore.setVisibility(View.INVISIBLE);
					}
				});
			}
		});
		mAdapter = new TatCaCongViecAdapter(getApplicationContext(),
				R.layout.item_lv_tat_ca_cong_viec, mTatCaCongViec);
		mListView.setAdapter(mAdapter);
	}

	private void congViecPhongBan() {
		mCongViecPhongBan = new ArrayList<TatCaCongViec>();
		final DBController controller = DBController
				.getInstance(getApplicationContext());
		mTatCaCongViec = controller.getCongViec(1, new LoadCongViecListener() {

			@Override
			public void onFinished(ArrayList<TatCaCongViec> data) {
				if (pDialog.isShowing())
					pDialog.dismiss();
				Log.d("TuNT", "onFinished: " + data);
				mTatCaCongViec.clear();
				mTatCaCongViec.addAll(data);
				mAdapter.notifyDataSetChanged();
			}
		});
		if (mTatCaCongViec == null) {
			pDialog.show();
			mTatCaCongViec = new ArrayList<TatCaCongViec>();
		}
		showList(mTatCaCongViec);
		btn_LoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pLoadmore.setVisibility(View.VISIBLE);
				pnumberCviec = pnumberCviec+1;
				controller.getCongViec(pnumberCviec, new LoadCongViecListener() {
					
					@Override
					public void onFinished(ArrayList<TatCaCongViec> data) {
						mTatCaCongViec.clear();
						mTatCaCongViec.addAll(data);
						showList(mTatCaCongViec);
						mAdapter.notifyDataSetChanged();
						pLoadmore.setVisibility(View.INVISIBLE);
					}
				});
			}
		});
		mAdapter = new TatCaCongViecAdapter(getApplicationContext(),
				R.layout.item_lv_tat_ca_cong_viec, mCongViecPhongBan);
		mListView.setAdapter(mAdapter);
	}

	// ----------------------phongban---------------------------//

	private class GetPhongBan extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HTTPHandler handler = new HTTPHandler();
			String ret = handler.makeHTTPRequest(url_phongban, HTTPHandler.GET);
			return ret;

		}

		@Override
		protected void onPostExecute(String result) {
			// if (pDialog.isShowing())
			// pDialog.dismiss();
			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					phongban = jsonObject.getJSONArray("row");
					for (int i = 0; i < phongban.length(); i++) {
						JSONObject object = phongban.getJSONObject(i);
						String id = object.getString("id_phong_ban");
						String tenphongban = object.getString("ten_phong_ban");
						mPhongBan.add(new PhongBan(id, tenphongban));
						mPhongBanAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("LuanDT", "Error");
			}
			super.onPostExecute(result);
		}

	}

	public void showList(ArrayList<TatCaCongViec> arrayList) {
		mCongViecPhongBan.clear();
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).getPhongBan().equals(idPhongBan)) {
				mCongViecPhongBan.add(arrayList.get(i));
			}
		}
		Log.d("LuanDT", "showListIDPBan = " + idPhongBan);
	}

}
