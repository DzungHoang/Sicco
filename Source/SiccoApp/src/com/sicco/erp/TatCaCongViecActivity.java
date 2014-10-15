package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sicco.erp.adapter.TatCaCongViecAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.TatCaCongViec;

public class TatCaCongViecActivity extends Activity {
	ProgressDialog pDialog;
	String url_congviec = "http://192.168.1.109:8080/sicco/congviec.php";
	JSONArray congviec = null;
	ArrayList<HashMap<String, String>> congViecList;
	ArrayList<TatCaCongViec> mTatCaCongViec;
	ListView mListView;
	TatCaCongViecAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tat_ca_cong_viec);

		congViecList = new ArrayList<HashMap<String, String>>();
		new GetCongViec().execute();
		mTatCaCongViec = new ArrayList<TatCaCongViec>();

		// mTatCaCongViec.add(new TatCaCongViec("1",
		// getString(R.string.toan_bo_cong_viec), "14/10/2014"));

		mListView = (ListView) findViewById(R.id.lv_tat_ca_cong_viec);
		mAdapter = new TatCaCongViecAdapter(getApplicationContext(),
				R.layout.item_lv_tat_ca_cong_viec, mTatCaCongViec);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("LuanDT", "" + id);
			}
		});

	}

	private class GetCongViec extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(TatCaCongViecActivity.this);
			pDialog.setMessage("Vui lòng đợi !...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(Void... arg0) {
			// Creating service handler class instance
			HTTPHandler sh = new HTTPHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeHTTPRequest(url_congviec, HTTPHandler.GET);

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

						String id = c.getString("id");
						String title = c.getString("ten_cong_viec");
						String han_cuoi = c.getString("ngay_ket_thuc");

						mTatCaCongViec.add(new TatCaCongViec(id, title,
								han_cuoi));
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
		getMenuInflater().inflate(R.menu.menu_toan_bo_cong_viec, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
}
