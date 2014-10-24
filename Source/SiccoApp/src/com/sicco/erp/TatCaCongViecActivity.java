package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.sicco.erp.adapter.TatCaCongViecAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongViecListener;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.model.TatCaCongViec;

public class TatCaCongViecActivity extends Activity {
	ProgressDialog pDialog;
	ArrayList<TatCaCongViec> mTatCaCongViec;
	ListView mListView;
	TatCaCongViecAdapter mAdapter;
	String idCongViec;
	ActionBar mActionBar;
	Spinner sPhongBan;
	String url_phongban = "http://apis.mobile.vareco.vn/sicco/phongban.php";
	JSONArray phongban = null;
	ArrayAdapter<String> mAdapterPhongBan;
	List<String> mListPhongBan;
	static int pnumberCviec = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tat_ca_cong_viec);

		mActionBar = getActionBar();
		mActionBar.setDisplayShowTitleEnabled(true);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(
				R.layout.layout_spinner_tatcacongviec, null);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);

		new GetPhongBan().execute();
		mListPhongBan = new ArrayList<String>();
		sPhongBan = (Spinner) findViewById(R.id.spinnerphongban);
		mAdapterPhongBan = new ArrayAdapter<String>(TatCaCongViecActivity.this,
				android.R.layout.simple_list_item_1, mListPhongBan);
		sPhongBan.setAdapter(mAdapterPhongBan);

		pDialog = new ProgressDialog(TatCaCongViecActivity.this);
		pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
		pDialog.setCancelable(true);
		mListView = (ListView) findViewById(R.id.lv_tat_ca_cong_viec);
		final DBController controller = DBController
				.getInstance(getApplicationContext());
		mTatCaCongViec = controller.getCongViec(1, new LoadCongViecListener() {

			@Override
			public void onFinished(ArrayList<TatCaCongViec> data) {
				if (pDialog.isShowing())
					pDialog.dismiss();
				Log.d("TuNT", "onFinished: " + data);
				mTatCaCongViec.clear();
				mTatCaCongViec.addAll(data);
				Log.d("TuNT", "onFinished: " + mTatCaCongViec.size());
				mAdapter.notifyDataSetChanged();
			}
		});
		if (mTatCaCongViec == null) {
			pDialog.show();
			mTatCaCongViec = new ArrayList<TatCaCongViec>();
		}
		mAdapter = new TatCaCongViecAdapter(getApplicationContext(),
				R.layout.item_lv_tat_ca_cong_viec, mTatCaCongViec);
		mListView.setAdapter(mAdapter);

		((LoadMoreListView) mListView)
				.setOnLoadMoreListener(new OnLoadMoreListener() {
					public void onLoadMore() {
						pnumberCviec = pnumberCviec + 1;
						controller.getCongViec(pnumberCviec,
								new LoadCongViecListener() {

									@Override
									public void onFinished(
											ArrayList<TatCaCongViec> data) {
										mTatCaCongViec.clear();
										mTatCaCongViec.addAll(data);
										mAdapter.notifyDataSetChanged();
										Toast.makeText(getApplicationContext(),
												"onLoad " + pnumberCviec, 0)
												.show();
										((LoadMoreListView) mListView)
												.onLoadMoreComplete();
									}
								});
					}
				});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("idcongviec", idCongViec);
				intent.setClass(getApplicationContext(),
						ChiTietCongViecActivity.class);
				startActivity(intent);
				Log.d("LuanDT", "ID Công việc = " + idCongViec);
			}
		});

	}

	// ----------------------phongban---------------------------//

	private class GetPhongBan extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HTTPHandler handler = new HTTPHandler();
			String ret = handler.makeHTTPRequest(url_phongban, HTTPHandler.GET);
			return ret;

		}

		@Override
		protected void onPostExecute(String result) {
			if (pDialog.isShowing())
				pDialog.dismiss();
			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					phongban = jsonObject.getJSONArray("row");
					for (int i = 0; i < phongban.length(); i++) {
						JSONObject object = phongban.getJSONObject(i);
						String id = object.getString("id_phong_ban");
						String tenphongban = object.getString("ten_phong_ban");
						mListPhongBan.add(tenphongban);
						mAdapterPhongBan.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("LuanDT", "Error");
			}
			super.onPostExecute(result);
		}

	}

}
