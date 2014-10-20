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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.adapter.ThaoLuanAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.model.ThaoLuan;

public class ChiTietCongViecActivity extends Activity {

	public static final String urlAvatar = "http://dantri21.vcmedia.vn/zoom/130_100/yT0YJzvK8t63z214dHr/Image/2014/10/russian-president-2a373.jpg";

	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/congviec_old.php";
	JSONArray thaoluan = null, row = null;
	ArrayList<HashMap<String, String>> thaoLuanList;
	ArrayList<ThaoLuan> mThaoLuan;
	ListView mListView;
	ThaoLuanAdapter mAdapter;
	String idCongViec;
	ImageView imgAnhDaiDien;
	TextView tvTenCongViec;
	TextView tvNguoiGiao;
	TextView tvNguoiThucHien;
	TextView tvNgayKetThuc;
	TextView tvNoiDung;
	TextView tvTongHopBaoCao;
	String tenCongViec;
	String nguoiGiao;
	String nguoiThucHien;
	String ngayKetThuc;
	String noiDung;
	String tongHopBaoCao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chi_tiet_cong_viec);
		
		// TuNT
		Intent intent = getIntent();
		idCongViec = intent.getStringExtra("idcongviec");
		if (!intent.getStringExtra("idcongviec").equals("")
				|| intent.getStringExtra("idcongviec") != null) {
			Toast.makeText(getApplicationContext(),
					"ID: " + intent.getStringExtra("idcongviec"),
					Toast.LENGTH_LONG).show();
		}
		// End TuNT

		thaoLuanList = new ArrayList<HashMap<String, String>>();
		new GetThaoLuan().execute(idCongViec);
		mThaoLuan = new ArrayList<ThaoLuan>();
		mListView = (ListView) findViewById(R.id.lv_thao_luan);
		mAdapter = new ThaoLuanAdapter(getBaseContext(),
				R.layout.item_lv_thao_luan, mThaoLuan);
		mListView.setAdapter(mAdapter);

	}

	private class GetThaoLuan extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(ChiTietCongViecActivity.this);
			pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... arg0) {
			// Creating service handler class instance
			HTTPHandler handler = new HTTPHandler();

			// Making a request to url and getting response
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("id", idCongViec));
			// Making a request to url and getting response
			String ret = handler.makeHTTPRequest(url_congviec,
					HTTPHandler.POST, valuePairs);

			Log.d("LuanDT", "POST : " + valuePairs);

			return ret;
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
					row = jsonObj.getJSONArray("row");

					for (int i = 0; i < row.length(); i++) {
						JSONObject o = row.getJSONObject(i);
						tenCongViec = o.getString("ten_cong_viec");
						tenCongViec = o.getString("ten_cong_viec");
						nguoiGiao = o.getString("nguoi_giao");
						nguoiThucHien = o.getString("nguoi_thuc_hien");
						ngayKetThuc = o.getString("ngay_ket_thuc");
						noiDung = o.getString("mo_ta");
						tongHopBaoCao = o.getString("tong_hop_bao_cao");

						thaoluan = o.getJSONArray("thao_luan");

						for (int j = 0; j < thaoluan.length(); j++) {
							JSONObject thao_luan = thaoluan.getJSONObject(i);
							String nguoithaoluan = thao_luan
									.getString("nguoi_thao_luan");
							String thoigianthaoluan = thao_luan
									.getString("thoi_gian_thao_luan");
							String noidungthaoluan = thao_luan
									.getString("noi_dung_thao_luan");
							String anhdaidien = thao_luan
									.getString("anh_dai_dien");
							mThaoLuan.add(new ThaoLuan(anhdaidien,
									nguoithaoluan, thoigianthaoluan,
									noidungthaoluan));
							Log.d("LuanDT", "anh_dai_dien: " + anhdaidien);

						}
						mAdapter.notifyDataSetChanged();

						tvTenCongViec = (TextView) findViewById(R.id.tv_chi_tiet_cv_ten_cong_viec);
						tvTenCongViec.setText(tenCongViec);
						tvNguoiGiao = (TextView) findViewById(R.id.tv_chi_tiet_cv_nguoi_giao);
						tvNguoiGiao.setText(nguoiGiao);
						tvNguoiThucHien = (TextView) findViewById(R.id.tv_chi_tiet_cv_nguoi_thuc_hien);
						tvNguoiThucHien.setText(nguoiThucHien);
						tvNgayKetThuc = (TextView) findViewById(R.id.tv_chi_tiet_cv_ket_thuc);
						tvNgayKetThuc.setText(ngayKetThuc);
						tvNoiDung = (TextView) findViewById(R.id.tv_chi_tiet_cv_noi_dung);
						tvNoiDung.setText(noiDung);
						tvTongHopBaoCao = (TextView) findViewById(R.id.tv_chi_tiet_cv_tong_hop_bao_cao);
						tvTongHopBaoCao.setText(tongHopBaoCao);

						// Log.d("LuanDT", "tenCongViec :" + ten CongViec);

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
			Toast.makeText(getApplicationContext(), "Cáº­p Nháº­t",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.action_edit:
			Toast.makeText(getApplicationContext(), "Sá»­a", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
