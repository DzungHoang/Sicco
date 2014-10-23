package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sicco.erp.adapter.CongViecDaGiaoAdapter;
import com.sicco.erp.adapter.CongViecHoanThanhAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.CongViecDaGiao;
import com.sicco.erp.model.CongViecHoanThanh;

public class CongViecHoanThanhActivity extends Activity {
	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/congviec.php";
	JSONArray congviec = null;
	ArrayList<HashMap<String, String>> congViecHoanThanhList;
	ArrayList<CongViecHoanThanh> mCongViecHoanThanh;
	ListView mListView;
	CongViecHoanThanhAdapter mAdapter;
	String idCongViec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cong_viec_hoan_thanh);
		
		SessionManager session = SessionManager.getInstance(getApplicationContext());
		String token = session.getUserDetails().get(SessionManager.KEY_TOKEN);
		String userName = session.getUserDetails().get(SessionManager.KEY_NAME);
		String page = "2";
		
		congViecHoanThanhList = new ArrayList<HashMap<String, String>>();
		new GetCongViec().execute(token,userName,page);
		mCongViecHoanThanh = new ArrayList<CongViecHoanThanh>();
		mListView = (ListView) findViewById(R.id.lv_cong_viec_hoan_thanh);
		mAdapter = new CongViecHoanThanhAdapter(getApplicationContext(),
				R.layout.item_lv_cong_viec_hoan_thanh, mCongViecHoanThanh);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("idcongviec", idCongViec);
				intent.setClass(getApplicationContext(), ChiTietCongViecActivity.class);
				startActivity(intent);
				Log.d("LuanDT", "ID Công việc = " + idCongViec);
			}
		});

	}

	private class GetCongViec extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(CongViecHoanThanhActivity.this);
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
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("token" , token));
			nameValuePairs.add(new BasicNameValuePair("username", userName));
			nameValuePairs.add(new BasicNameValuePair("page", page));
			
			String jsonStr = sh.makeHTTPRequest(url_congviec, HTTPHandler.POST
					,nameValuePairs);

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
						String ngayhoanthanh = c.getString("ngay_ket_thuc");

						mCongViecHoanThanh.add(new CongViecHoanThanh(idCongViec, tencongviec,
								ngayhoanthanh));
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_cong_viec_hoan_thanh, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), ThemCongViecActivity.class);
		startActivityForResult(intent, 1);
		return true;
	}
}
