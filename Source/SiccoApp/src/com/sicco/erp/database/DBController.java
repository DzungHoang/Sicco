package com.sicco.erp.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.CongVan;
import com.sicco.erp.model.TatCaCongViec;
import com.sicco.erp.model.ThaoLuan;

public class DBController {
	private static DBController instance;
	public static ArrayList<TatCaCongViec> DataCongViec;
	private static ArrayList<CongVan> DataCongVan;

	public static final int TYPE_CONG_VIEC = 0;
	public static final int TYPE_CONG_VAN = TYPE_CONG_VIEC + 1;
	public static final int PAGE_SIZE = 20;

	Context mContext;

	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/congviec.php";
	String url_congvan = "http://apis.mobile.vareco.vn/sicco/congvan.php";
	JSONArray congviec = null, thaoluan = null, congvan = null;

	String token, username;

	public DBController(Context context) {
		mContext = context;
		SessionManager sessionManager = SessionManager.getInstance(mContext);
		token = sessionManager.getUserDetails().get(SessionManager.KEY_TOKEN);
		username = sessionManager.getUserDetails().get(SessionManager.KEY_NAME);
		DataCongViec = new ArrayList<TatCaCongViec>();
		DataCongVan = new ArrayList<CongVan>();
	}

	public static DBController getInstance(Context context) {
		if (instance == null && context != null) {
			Log.d("TuNT", "create DB");
			instance = new DBController(context);
		}
		return instance;
	}

	// -------------------------Cong Viec -----------------------------------------------------------------//
	GetCongViec mGetCongViecAsync;
	ArrayList<LoadCongViecListener> mCViecCallback = new ArrayList<DBController.LoadCongViecListener>();
	int mCViecRunningPage = -1;

	public ArrayList<TatCaCongViec> getCongViec(int page,
			LoadCongViecListener callback) {
		Log.d("TuNT",
				"getCongViec: DataCongViec.size() = " + DataCongViec.size());

		if ((DataCongViec != null && DataCongViec.size() < page * PAGE_SIZE - 1)
				|| DataCongViec == null) {
			Log.d("TuNT", "start loading: page = " + page);
			mCViecCallback.add(callback);
			if (mCViecRunningPage == -1) {
				GetCongViec congViec = new GetCongViec();
				congViec.execute(token, username, Integer.toString(page));
				mCViecRunningPage = page;
			}

			return null;
		} else {
			ArrayList<TatCaCongViec> temp = new ArrayList<TatCaCongViec>();
			temp.addAll(DataCongViec.subList(0, page * PAGE_SIZE));
			return temp;
		}

	}

	public static interface LoadCongViecListener {
		public void onFinished(ArrayList<TatCaCongViec> data);
	}

	// ----------------------------------------- Cong Van --------------------------------------------------------//
	GetCongVan mGetCongVan;
	ArrayList<LoadCongVanListener> mCVanCallback = new ArrayList<DBController.LoadCongVanListener>();
	int mCVanRunningPage = -1;

	public ArrayList<CongVan> getCongVan(int page, LoadCongVanListener callback) {
		if ((DataCongVan != null && DataCongVan.size() < page * PAGE_SIZE - 1)
				|| DataCongVan == null) {
			mCVanCallback.add(callback);
			if (mCVanRunningPage == -1) {
				GetCongVan congvan = new GetCongVan();
				congvan.execute(token, username, Integer.toString(page));
				mCVanRunningPage = page;
			}
			return null;

		} else {
			ArrayList<CongVan> temp = new ArrayList<CongVan>();
			temp.addAll(DataCongVan.subList(0, page * PAGE_SIZE));

			return temp;
		}
	}

	public static interface LoadCongVanListener {
		public void onFinished(ArrayList<CongVan> data);
	}

	private class GetCongViec extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("TuNT", "GetCongViec.onPreExecute");
		}

		@Override
		protected String doInBackground(String... arg0) {
			Log.d("TuNT", "GetCongViec.doInBackground");
			HTTPHandler handler = new HTTPHandler();

			String token = arg0[0];
			String username = arg0[1];
			String page = arg0[2];

			List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
			valuePairs.add(new BasicNameValuePair("token", token));
			valuePairs.add(new BasicNameValuePair("username", username));
			valuePairs.add(new BasicNameValuePair("page", page));
			// Making a request to url and getting response
			String ret = handler.makeHTTPRequest(url_congviec,
					HTTPHandler.POST, valuePairs);
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d("TuNT", "GetCongViec.onPostExecute");
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);
					// Getting JSON Array node
					congviec = jsonObj.getJSONArray("row");

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

						DataCongViec.add(new TatCaCongViec(id, tencongviec,
								ngaybatdau, tinhtrang, tiendo, nguoithuchien,
								phongban, loaicongviec, hancuoi, duan,
								mucuutien, nguoiduocxem, nguoigiao, mota,
								tonghopbaocao, tepdinhkem, url, datathaoluan));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mCViecRunningPage = -1;
				if (mCViecCallback != null && mCViecCallback.size() > 0) {
					Log.d("TuNT",
							"GetCongViec.onPostExecute: mCallback.onFinished: "
									+ DataCongViec);
					for (int i = 0; i < mCViecCallback.size(); i++) {
						mCViecCallback.get(i).onFinished(DataCongViec);
					}
				}
			} else {
				Log.e("TuNT", "ERROR!");
			}
		}

	}

	private class GetCongVan extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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
			nameValuePairs.add(new BasicNameValuePair("token", token));
			nameValuePairs.add(new BasicNameValuePair("username", userName));
			nameValuePairs.add(new BasicNameValuePair("page", page));
			String jsonStr = sh.makeHTTPRequest(url_congvan, HTTPHandler.POST,
					nameValuePairs);

			return jsonStr;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);

					// Getting JSON Array node
					congvan = jsonObj.getJSONArray("row");

					// looping through All Contacts
					for (int i = 0; i < congvan.length(); i++) {
						JSONObject c = congvan.getJSONObject(i);

						String id = c.getString("id");
						String title = c.getString("loai_cong_van");
						String detail = c.getString("trich_yeu");
						String url = c.getString("url");

						DataCongVan.add(new CongVan(title, detail, url, id));

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				mCVanRunningPage = -1;
				if (mCVanCallback != null && mCVanCallback.size() > 0) {
					Log.d("TuNT",
							"GetCongVan.onPostExecute: mCallback.onFinished: "
									+ DataCongVan);
					for (int i = 0; i < mCVanCallback.size(); i++) {
						mCVanCallback.get(i).onFinished(DataCongVan);
					}
				}
			} else {
				Log.e("TuNT", "Khong the lay data tu url nay");
			}
		}
	}

	// public LoadingFinishListener mInterface;
	//
	// public void setLoadingFinishListener(LoadingFinishListener listener) {
	// mInterface = listener;
	// }
	//
	// public static interface LoadingFinishListener {
	// public void onFinished();
	// }
}