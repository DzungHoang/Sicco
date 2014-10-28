package com.sicco.erp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sicco.erp.adapter.ThaoLuanAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.model.ThaoLuan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChiTietCongViecActivity extends Activity {
	
	
	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/congviec_old.php";
	JSONArray thaoluan = null, row = null;
	ArrayList<HashMap<String, String>> thaoLuanList;
	ArrayList<ThaoLuan> mThaoLuan;
	ListView mListView;
	ThaoLuanAdapter mAdapter;
	
	final String cap_nhat_tien_do[] = { "0%", "20%", "40%", "60%", "80%",
			"100%" };
	ImageView imgAnhDaiDien;
	TextView tvTenCongViec;
	TextView tvNguoiGiao;
	TextView tvNguoiThucHien;
	TextView tvNgayKetThuc;
	TextView tvNoiDung;
	TextView tvTongHopBaoCao;
	String id, tenCongViec, tinhTrang, tienDo, nguoiThucHien,
			phongBan, loaiCongViec, ngayKetThuc, duAn, mucUuTien, nguoiDuocXem,
			nguoiGiao, moTa, tongHopBaoCao, Url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chi_tiet_cong_viec);

		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		tenCongViec = intent.getStringExtra("ten_cong_viec");
		tinhTrang = intent.getStringExtra("tinh_trang");
		tienDo = intent.getStringExtra("tien_do");
		nguoiThucHien = intent.getStringExtra("nguoi_thuc_hien");
		phongBan = intent.getStringExtra("phong_ban");
		loaiCongViec = intent.getStringExtra("loai_cong_viec");
		ngayKetThuc = intent.getStringExtra("ngay_ket_thuc");
		duAn = intent.getStringExtra("du_an");
		mucUuTien = intent.getStringExtra("muc_uu_tien");
		nguoiDuocXem = intent.getStringExtra("nguoi_duoc_xem");
		nguoiGiao = intent.getStringExtra("nguoi_giao");
		moTa = intent.getStringExtra("mo_ta");
		tongHopBaoCao = intent.getStringExtra("tong_hop_bao_cao");
		Url = intent.getStringExtra("Url");

		tvTenCongViec = (TextView) findViewById(R.id.tv_chi_tiet_cv_ten_cong_viec);
		tvTenCongViec.setText(tenCongViec);
		tvNguoiGiao = (TextView) findViewById(R.id.tv_chi_tiet_cv_nguoi_giao);
		tvNguoiGiao.setText(nguoiGiao);
		tvNguoiThucHien = (TextView) findViewById(R.id.tv_chi_tiet_cv_nguoi_thuc_hien);
		tvNguoiThucHien.setText(nguoiThucHien);
		tvNgayKetThuc = (TextView) findViewById(R.id.tv_chi_tiet_cv_ket_thuc);
		tvNgayKetThuc.setText(ngayKetThuc);
		tvNoiDung = (TextView) findViewById(R.id.tv_chi_tiet_cv_noi_dung);
		tvNoiDung.setText(moTa);
		tvTongHopBaoCao = (TextView) findViewById(R.id.tv_chi_tiet_cv_tong_hop_bao_cao);
		tvTongHopBaoCao.setText(tongHopBaoCao);
		
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int lvWidth = displayMetrics.widthPixels * 10/10;
		int lvHeight = displayMetrics.heightPixels * 6/10;
		
		
		thaoLuanList = new ArrayList<HashMap<String, String>>();
		new GetThaoLuan().execute();
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
			valuePairs.add(new BasicNameValuePair("id", id));
			valuePairs.add(new BasicNameValuePair("tien_do", tienDo));
			// Making a request to url and getting response
			String ret = handler.makeHTTPRequest(url_congviec,
					HTTPHandler.POST, valuePairs);

			Log.d("LuanDT", "POST : " + valuePairs);

			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pDialog.isShowing())
				pDialog.dismiss();
			if (result != null) {
				try {

					JSONObject jsonObj = new JSONObject(result);
					row = jsonObj.getJSONArray("row");

					for (int i = 0; i < row.length(); i++) {
						JSONObject o = row.getJSONObject(i);
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

						}
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
			final AlertDialog.Builder capNhatTienDo = new AlertDialog.Builder(
					this);
			capNhatTienDo
					.setTitle(R.string.action_update)
					.setSingleChoiceItems(cap_nhat_tien_do, -1,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									tienDo = cap_nhat_tien_do[which];
								}
							})
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setNegativeButton(
							getResources().getString(R.string.action_cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

								}
							});
			AlertDialog alertDialog = capNhatTienDo.create();
			alertDialog.show();
			break;
		case R.id.action_edit:
			Intent intent = getIntent();
			intent.setClass(getApplicationContext(), SuaCongViecActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
