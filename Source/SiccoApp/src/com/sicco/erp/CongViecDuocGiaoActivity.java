package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;

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

import com.sicco.erp.adapter.CongViecDuocGiaoAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.model.CongViecDuocGiao;

public class CongViecDuocGiaoActivity extends Activity {
	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/congviec.php";
	JSONArray congviec = null;
	ArrayList<HashMap<String, String>> congViecDuocGiaoList;
	ArrayList<CongViecDuocGiao> mCongViecDuocGiao;
	ListView mListView;
	CongViecDuocGiaoAdapter mAdapter;
	String idCongViec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cong_viec_duoc_giao);
		congViecDuocGiaoList = new ArrayList<HashMap<String, String>>();
		new GetCongViec().execute();
		mCongViecDuocGiao = new ArrayList<CongViecDuocGiao>();
		mListView = (ListView) findViewById(R.id.lv_cong_viec_duoc_giao);
		mAdapter = new CongViecDuocGiaoAdapter(getApplicationContext(),
				R.layout.item_lv_cong_viec_duoc_giao, mCongViecDuocGiao);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						ChiTietCongViecActivity.class);
				startActivityForResult(intent, 1);
				Log.d("LuanDT", "ID Công việc = " + idCongViec);
			}
		});

	}

	private class GetCongViec extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(CongViecDuocGiaoActivity.this);
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

						idCongViec = c.getString("id");
						String tencongviec = c.getString("ten_cong_viec");
						String nguoigiao = c.getString("nguoi_giao");

						mCongViecDuocGiao.add(new CongViecDuocGiao(idCongViec,
								tencongviec, nguoigiao));
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
		getMenuInflater().inflate(R.menu.menu_cong_viec_da_giao, menu);
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
