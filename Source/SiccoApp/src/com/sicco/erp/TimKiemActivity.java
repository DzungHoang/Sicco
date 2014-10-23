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

import com.sicco.erp.adapter.TimKiemAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.TimKiem;

public class TimKiemActivity  extends Activity{
	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/congviec.php";
	JSONArray congviec = null;
	ArrayList<HashMap<String, String>> timKiemList;
	ArrayList<TimKiem> mTimKiemCongViec;
	ListView mListView;
	TimKiemAdapter mAdapter;
	String idCongViec;
	
	ActionBar mActionBar;
	EditText edt_KeyWork;
	Spinner spn_TieuChi;
	
	//Cac tieu chi trong spinner
	 String[] cacTieuChi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tim_kiem);
		
		cacTieuChi = getResources().getStringArray(R.array.cac_tieu_chi); 

//-------------------------Action bar -----------------------------------------------------------//
		
		mActionBar = getActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.actionbar_customview_timkiem, null);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		edt_KeyWork = (EditText)findViewById(R.id.editText1);
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
		 
		 edt_KeyWork.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				
				Toast.makeText(getApplicationContext(), edt_KeyWork.getText(), 0).show();
				Log.d("NgaDV", "onEditor : " + edt_KeyWork.getText());
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edt_KeyWork.getWindowToken(), 0);
				return false;
			}
		});
		 
		 

		
		
		
//-------------------------And Action bar -----------------------------------------------------------//
		
		SessionManager session = SessionManager.getInstance(getApplicationContext());
		 
		String token = session.getUserDetails().get(SessionManager.KEY_TOKEN);
		String userName = session.getUserDetails().get(SessionManager.KEY_NAME);
		String page = "1";
		 
		timKiemList = new ArrayList<HashMap<String, String>>();
		new GetCongViec().execute(token,userName, page);
		mTimKiemCongViec = new ArrayList<TimKiem>();

		mListView = (ListView) findViewById(R.id.lv_tim_kiem);
		mAdapter = new TimKiemAdapter(getApplicationContext(),
				R.layout.item_lv_tim_kiem, mTimKiemCongViec);
		mListView.setAdapter(mAdapter);
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
			pDialog.setCancelable(false);
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
			
			List<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("token" , token));
			nameValuePairs.add(new BasicNameValuePair("username", userName));
			nameValuePairs.add(new BasicNameValuePair("page" , page));
			
			String jsonStr = sh.makeHTTPRequest(url_congviec, HTTPHandler.POST,nameValuePairs);
			Log.d("LuanDT", "json" + jsonStr);

			return jsonStr;
		}

		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
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

						idCongViec = c.getString("id");
						String tencongviec = c.getString("ten_cong_viec");
						String hancuoi = c.getString("ngay_ket_thuc");

						mTimKiemCongViec.add(new TimKiem(idCongViec, tencongviec,
								hancuoi));
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
