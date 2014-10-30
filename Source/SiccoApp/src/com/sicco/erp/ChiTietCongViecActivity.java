package com.sicco.erp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.adapter.ThaoLuanAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.ThaoLuan;

public class ChiTietCongViecActivity extends Activity {

	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/congviec.php";
	String url_thaoluan = "http://apis.mobile.vareco.vn/sicco/thaoluan.php";
	JSONArray thaoluan = null, success = null;
	ArrayList<HashMap<String, String>> thaoLuanList;
	ArrayList<ThaoLuan> mThaoLuan;
	ListView mListView;
	ThaoLuanAdapter mAdapter;

	final String cap_nhat_tien_do[] = { "0%", "20%", "40%", "60%", "80%",
			"100%" };
	ImageView imgAnhDaiDien;
	ImageView imgSend;
	EditText edtThaoLuan;
	TextView tvTenCongViec;
	TextView tvNguoiGiao;
	TextView tvNguoiThucHien;
	TextView tvNgayKetThuc;
	TextView tvNoiDung;
	TextView tvTongHopBaoCao;
	String id, tenCongViec, tinhTrang, tienDo, nguoiThucHien, phongBan,
			loaiCongViec, ngayKetThuc, duAn, mucUuTien, nguoiDuocXem,
			nguoiGiao, moTa, tongHopBaoCao, Url, token, username, thoigian,
			noidungthaoluan, updateTienDo, kqUpdateTienDo, id_cong_viec;
	StringBuilder timeThaoLuan;
	String page = "2";

	private int minute;
	private int hour;
	private int date;
	private int months;
	private int years_now;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chi_tiet_cong_viec);

		SessionManager sessionManager = SessionManager
				.getInstance(ChiTietCongViecActivity.this);
		token = sessionManager.getUserDetails().get(SessionManager.KEY_TOKEN);
		username = sessionManager.getUserDetails().get(SessionManager.KEY_NAME);

		final Calendar c = Calendar.getInstance();
		minute = c.get(Calendar.MINUTE);
		hour = c.get(Calendar.HOUR_OF_DAY);
		date = c.get(Calendar.DATE);
		months = c.get(Calendar.MONTH);
		years_now = c.get(Calendar.YEAR);

		timeThaoLuan = new StringBuilder().append(padding_str(hour))
				.append(":").append(padding_str(minute)).append("  ")
				.append(padding_str(date)).append("-")
				.append(padding_str(months)).append("-")
				.append(padding_str(years_now));
		thoigian = new String(timeThaoLuan);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		if (id != null) {
			if (!id.equals("")) {
				if (id.equals("-1")) {
					finish();
					Intent intent2 = new Intent(getApplicationContext(),
							TatCaCongViecActivity.class);
					startActivity(intent2);
				}
			}
		}
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

		// //////////
		imgSend = (ImageView) findViewById(R.id.img_send);
		edtThaoLuan = (EditText) findViewById(R.id.edt_thao_luan);
		imgSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(edtThaoLuan.getText())) {
					edtThaoLuan.setError(getResources().getString(
							R.string.thong_bao_rong));
					return;
				} else {
					noidungthaoluan = edtThaoLuan.getText().toString();
					Log.d("LuanDT", "noidungthaoluan : " + noidungthaoluan);
					new GetThaoLuan().execute(token, username, id, thoigian,
							noidungthaoluan);
				}

			}
		});
		// //////////

		thaoLuanList = new ArrayList<HashMap<String, String>>();
		id_cong_viec = id;
		new GetThaoLuan().execute(token, id_cong_viec, page);
		mThaoLuan = new ArrayList<ThaoLuan>();
		mListView = (ListView) findViewById(R.id.lv_thao_luan);
		mAdapter = new ThaoLuanAdapter(getBaseContext(),
				R.layout.item_lv_thao_luan, mThaoLuan);
		mListView.setAdapter(mAdapter);
	}

	private static String padding_str(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
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
			HTTPHandler handler = new HTTPHandler();
			
			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("token", token));
			valuePairs.add(new BasicNameValuePair("id_cong_viec", id));
			valuePairs.add(new BasicNameValuePair("page", page));
			
			String ret = handler.makeHTTPRequest(url_thaoluan,
					HTTPHandler.POST, valuePairs);

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
					thaoluan = jsonObj.getJSONArray("thao_luan");

					for (int i = 0; i < thaoluan.length(); i++) {
						JSONObject o = thaoluan.getJSONObject(i);
						String nguoithaoluan = o.getString("nguoi_thao_luan");
						String thoigianthaoluan = o.getString("thoi_gian_thao_luan");
						String noidungthaoluan = o.getString("noi_dung_thao_luan");
						String anhdaidien = o.getString("anh_dai_dien");
						mThaoLuan.add(new ThaoLuan(anhdaidien,
								nguoithaoluan, thoigianthaoluan,
								noidungthaoluan));
						
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
									updateTienDo = cap_nhat_tien_do[which];
								}
							})
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									new GetTienDoCV().execute(token, username,
											id, updateTienDo);
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

			intent.putExtra("id", id);
			intent.putExtra("ten_cong_viec", tenCongViec);
			intent.putExtra("tinh_trang", tinhTrang);
			intent.putExtra("tien_do", tienDo);
			intent.putExtra("nguoi_thuc_hien", nguoiThucHien);
			intent.putExtra("phong_ban", phongBan);
			intent.putExtra("loai_cong_viec", loaiCongViec);
			intent.putExtra("ngay_ket_thuc", ngayKetThuc);
			intent.putExtra("du_an", duAn);
			intent.putExtra("muc_uu_tien", mucUuTien);
			intent.putExtra("nguoi_duoc_xem", nguoiDuocXem);
			intent.putExtra("nguoi_giao", nguoiGiao);
			intent.putExtra("mo_ta", moTa);
			intent.putExtra("tong_hop_bao_cao", tongHopBaoCao);
			intent.putExtra("Url", Url);

			Toast.makeText(
					getApplicationContext(),
					"id:" + intent.getStringExtra("id").toString()
							+ "ten_cong_viec:"
							+ intent.getStringExtra("ten_cong_viec").toString(),
					0).show();
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// -------------------------cap nhat tien do-------------------//

	private class GetTienDoCV extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChiTietCongViecActivity.this);
			pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			HTTPHandler handler = new HTTPHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("token", token));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("id_cong_viec", id));
			nameValuePairs.add(new BasicNameValuePair("tien_do", updateTienDo));
			String ret = handler.makeHTTPRequest(url_congviec,
					HTTPHandler.POST, nameValuePairs);
			Log.d("LuanDT", "POST-GetTienDoCV : " + nameValuePairs);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (pDialog.isShowing())
				pDialog.dismiss();
			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
//					success = jsonObject.getJSONArray("success");
//					for (int i = 0; i < success.length(); i++) {
//						JSONObject kq = success.getJSONObject(i);
//						kqUpdateTienDo = kq.getString("success");
//					}
//					Log.d("LuanDT", "kqUpdateTienDo : " + kqUpdateTienDo);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("LuanDT", "Error!");
			}
		}
	}
}
