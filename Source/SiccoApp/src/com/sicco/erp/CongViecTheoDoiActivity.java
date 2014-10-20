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
import android.widget.Toast;

import com.sicco.erp.adapter.CongViecDaGiaoAdapter;
import com.sicco.erp.adapter.CongViecTheoDoiAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.CongViecDaGiao;
import com.sicco.erp.model.CongViecTheoDoi;

public class CongViecTheoDoiActivity extends Activity {
	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/congviec.php";
	JSONArray congviec = null;
	ArrayList<HashMap<String, String>> congViecTheoDoiList;
	ArrayList<CongViecTheoDoi> mCongViecTheoDoi;
	ListView mListView;
	CongViecTheoDoiAdapter mAdapter;
	String idCongViec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cong_viec_theo_doi);
		
		SessionManager session = SessionManager.getInstance(getApplicationContext());
		String token = session.getUserDetails().get(SessionManager.KEY_TOKEN);
		String userName = session.getUserDetails().get(SessionManager.KEY_NAME);
		String page = "2";
		
		congViecTheoDoiList = new ArrayList<HashMap<String, String>>();
		new GetCongViec().execute(token,userName,page);
		mCongViecTheoDoi = new ArrayList<CongViecTheoDoi>();
		mListView = (ListView) findViewById(R.id.lv_cong_viec_theo_doi);
		mAdapter = new CongViecTheoDoiAdapter(getApplicationContext(),
				R.layout.item_lv_cong_viec_theo_doi, mCongViecTheoDoi);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("idcongviec", idCongViec);
				intent.setClass(getApplicationContext(), ChiTietCongViecActivity.class);
				startActivity(intent);
				
//				Toast.makeText(getApplicationContext(), "ban vua chon cong viec co id la : " +_ idCongViec, 0);
			}
		});

	}

	private class GetCongViec extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(CongViecTheoDoiActivity.this);
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
						String nguoixuly = c.getString("nguoi_thuc_hien");

						mCongViecTheoDoi.add(new CongViecTheoDoi(idCongViec, tencongviec,
								nguoixuly));
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
		getMenuInflater().inflate(R.menu.menu_cong_viec_theo_doi, menu);
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
