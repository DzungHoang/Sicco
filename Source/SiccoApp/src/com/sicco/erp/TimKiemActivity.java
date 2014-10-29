package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import afzkl.development.colorpickerview.R.string;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.sicco.erp.adapter.CongViecHoanThanhAdapter;
import com.sicco.erp.adapter.TimKiemAdapter;
import com.sicco.erp.database.DBController.LoadCongViecListener;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.TatCaCongViec;
import com.sicco.erp.model.ThaoLuan;
import com.sicco.erp.model.TimKiem;

public class TimKiemActivity  extends Activity{
	ProgressDialog pDialog;
	String url_timkiemcongviec = "http://apis.mobile.vareco.vn/sicco/timkiem.php";
	JSONArray congviec = null, thaoluan = null;
	ArrayList<TimKiem> mTimKiemCongViec;
	ListView mListView;
	TimKiemAdapter mAdapter;
	String idCongViec;
	
	ActionBar mActionBar;
	EditText edt_KeyWord;
	Spinner spn_TieuChi;
	
	//Cac tieu chi trong spinner
	 String[] cacTieuChi;
	 
	 String tieuChiDuocChon,keyWord;
	 String tieuchi;
	 
	 int pNumberTimkiem = 1;

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tim_kiem);
		
		SessionManager session = SessionManager.getInstance(getApplicationContext());
		 
		final String token = session.getUserDetails().get(SessionManager.KEY_TOKEN);
		final String userName = session.getUserDetails().get(SessionManager.KEY_NAME);
		
		cacTieuChi = getResources().getStringArray(R.array.cac_tieu_chi); 

//-------------------------Action bar -----------------------------------------------------------//
		
		mActionBar = getActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar_customview_timkiem, null);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		edt_KeyWord = (EditText)findViewById(R.id.editText1);
		spn_TieuChi = (Spinner)findViewById(R.id.spinner1);
		
		ArrayAdapter<String> mSpnAdapter=new ArrayAdapter<String>
		 (
		 this,
		 android.R.layout.simple_spinner_item,
		 cacTieuChi
		 );
		 //pháº£i gá»�i lá»‡nh nĂ y Ä‘á»ƒ hiá»ƒn thá»‹ danh sĂ¡ch cho Spinner
		mSpnAdapter.setDropDownViewResource
		 (android.R.layout.simple_list_item_1);
		 //Thiáº¿t láº­p adapter cho Spinner
		 spn_TieuChi.setAdapter(mSpnAdapter);
		 
		 //--- first show TimKiemActivity-----------------------
//		 new GetCongViec().execute(token,userName, page, tieuChiDuocChon, keyWord);
		 //////////////////////////////////////////////////////////////////
		 
		 edt_KeyWord.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				tieuChiDuocChon = spn_TieuChi.getSelectedItem().toString();
				keyWord = edt_KeyWord.getText().toString();
				
				Toast.makeText(getApplicationContext(), "keywork: "+ edt_KeyWord.getText() + "---tieu chi: " +tieuChiDuocChon, 0).show();
				Log.d("NgaDV", "onEditor : " + keyWord);
				
				tieuchi = "";
				if (tieuChiDuocChon.equals(cacTieuChi[0]) && !keyWord.equals("")) {
					tieuchi = "all";
				}
				else if (tieuChiDuocChon.equals(cacTieuChi[1]) && !keyWord.equals("")) {
					tieuchi = "ten_cong_viec";
				}
				else if (tieuChiDuocChon.equals(cacTieuChi[2]) && !keyWord.equals("")) {
					tieuchi = "nguoi_giao";
				}
				else if (tieuChiDuocChon.equals(cacTieuChi[3]) && !keyWord.equals("")) {
					tieuchi = "nguoi_thuc_hien";
				}
				mTimKiemCongViec.clear();
				pNumberTimkiem = 1;
				new GetCongViec().execute(token,userName, Integer.toString(pNumberTimkiem), tieuchi, keyWord);
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edt_KeyWord.getWindowToken(), 0); 
				return true;
			}
		});
		
//-------------------------And Action bar -----------------------------------------------------------//
		 
		mTimKiemCongViec = new ArrayList<TimKiem>();

		mListView = (ListView) findViewById(R.id.lv_tim_kiem);
		mAdapter = new TimKiemAdapter(getApplicationContext(),
				R.layout.item_lv_tim_kiem, mTimKiemCongViec);
		mListView.setAdapter(mAdapter);
		((LoadMoreListView) mListView)
		.setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				pNumberTimkiem = pNumberTimkiem+1;
				
				String page = Integer.toString(pNumberTimkiem);
				Toast.makeText(getApplicationContext(), "pnumber timkiem: " + page, 0).show();
				new GetCongViec().execute(token,userName, page, tieuchi, keyWord);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				mAdapter.notifyDataSetChanged();
				((LoadMoreListView) mListView).onLoadMoreComplete();
				
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ChiTietCongViecActivity.class);
				startActivityForResult(intent, 1);
				Log.d("LuanDT", "ID CĂ´ng viá»‡c = " + idCongViec);
			}
		});

	}

	private class GetCongViec extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(TimKiemActivity.this);
			pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// Creating service handler class instance
			HTTPHandler sh = new HTTPHandler();
			
			
			// Making a request to url and getting response
			String token = arg0[0];
			String userName = arg0[1];
			String page = arg0[2];
			String tieuchi = arg0[3];
			String keywork = arg0[4];
			
			List<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("token" , token));
			nameValuePairs.add(new BasicNameValuePair("username", userName));
			nameValuePairs.add(new BasicNameValuePair("page" , page));
			nameValuePairs.add(new BasicNameValuePair("tieu_chi" , tieuchi));
			nameValuePairs.add(new BasicNameValuePair("keyword" , keywork));
			
			String jsonStr = sh.makeHTTPRequest(url_timkiemcongviec, HTTPHandler.GET, nameValuePairs);
			Log.d("LuanDT", "json" + jsonStr);

			return jsonStr;
		}

		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			Toast.makeText(getApplicationContext(), "onpostexecute cho tu", 0).show();
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					congviec = jsonObj.getJSONArray("row");

					// looping through All Contacts
					for (int i = 0; i < congviec.length(); i++) {
						JSONObject c = congviec.getJSONObject(i);

						String id = c.getString("id");
						String tencongviec = c.getString("ten_cong_viec");
						String ngaybatdau = c.getString("ngay_bat_dau");
						String tinhtrang = c.getString("tinh_trang");
						String tiendo = c.getString("tien_do");
						String nguoithuchien = c.getString("nguoi_thuc_hien");
						String phongban = c.getString("phong_ban");
						String loaicongviec = c.getString("loai_cong_viec");
						String hancuoi = c.getString("ngay_ket_thuc");
						String duan = c.getString("du_an");
						String mucuutien = c.getString("muc_uu_tien");
						String nguoiduocxem = c.getString("nguoi_duoc_xem");
						String nguoigiao = c.getString("nguoi_giao");
						String mota = c.getString("mo_ta");
						String tonghopbaocao = c.getString("tong_hop_bao_cao");
						String tepdinhkem = c.getString("tep_dinh_kem");
						String url = c.getString("url");
						ArrayList<ThaoLuan> datathaoluan = new ArrayList<ThaoLuan>();

						thaoluan = c.getJSONArray("thao_luan");

						for (int j = 0; j < thaoluan.length(); j++) {
							JSONObject thao_luan = thaoluan.getJSONObject(j);
							String nguoithaoluan = thao_luan
									.getString("nguoi_thao_luan");
							String thoigianthaoluan = thao_luan
									.getString("thoi_gian_thao_luan");
							String noidungthaoluan = thao_luan
									.getString("noi_dung_thao_luan");
							String anhdaidien = thao_luan
									.getString("anh_dai_dien");
							datathaoluan.add(new ThaoLuan(anhdaidien,
									nguoithaoluan, thoigianthaoluan,
									noidungthaoluan));
						}

						mTimKiemCongViec.add(new TimKiem(id, tencongviec,
								ngaybatdau, tinhtrang, tiendo, nguoithuchien,
								phongban, loaicongviec, hancuoi, duan,
								mucuutien, nguoiduocxem, nguoigiao, mota,
								tonghopbaocao, tepdinhkem, url, datathaoluan));
						mAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("LuanDT", "Khong the lay data tu url nay");
			}
		}

	}
	
	
	
}
