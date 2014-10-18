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
import android.widget.ListView;
import android.widget.Toast;

import com.sicco.erp.adapter.ThaoLuanAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.model.ThaoLuan;

public class ChiTietCongViecActivity extends Activity {

	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/chitietcongviec.php";
	JSONArray thaoluan = null;
	ArrayList<HashMap<String, String>> thaoLuanList;
	ArrayList<ThaoLuan> mThaoLuan;
	ListView mListView;
	ThaoLuanAdapter mAdapter;
	String idCongViec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chi_tiet_cong_viec);

		//TuNT
		Intent intent = getIntent();
		if (!intent.getStringExtra("item_id").equals("")
				|| intent.getStringExtra("item_id") != null)
		{
			Toast.makeText(getApplicationContext(), "ID: "+intent.getStringExtra("item_id"), Toast.LENGTH_SHORT).show();
		}
		//End TuNT
		thaoLuanList = new ArrayList<HashMap<String, String>>();
		new GetThaoLuan().execute();
		mThaoLuan = new ArrayList<ThaoLuan>();

		mListView = (ListView) findViewById(R.id.lv_thao_luan);
		mAdapter = new ThaoLuanAdapter(getBaseContext(),
				R.layout.item_lv_thao_luan, mThaoLuan);
		mListView.setAdapter(mAdapter);

	}

	private class GetThaoLuan extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ChiTietCongViecActivity.this);
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
					thaoluan = jsonObj.getJSONArray("row");

					for (int i = 0; i < thaoluan.length(); i++) {

						JSONObject object = thaoluan.getJSONObject(i);

						int anhdaidien = R.drawable.default_avatar;
						String urlImages = object.getString("anh_dai_dien");
						String nguoithaoluan = object
								.getString("nguoi_thao_luan");
						String thoigianthaoluan = object
								.getString("thoi_gian_thao_luan");
						String noidungthaoluan = object
								.getString("noi_dung_thao_luan");

						Log.d("LuanDT", "anh dai dien :" + urlImages);

						mThaoLuan.add(new ThaoLuan(anhdaidien, nguoithaoluan,
								thoigianthaoluan, noidungthaoluan));
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
		getMenuInflater().inflate(R.menu.menu_chi_tiet_cong_viec, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_update:
			Toast.makeText(getApplicationContext(), "Cập Nhật",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_edit:
			Toast.makeText(getApplicationContext(), "Sửa", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
