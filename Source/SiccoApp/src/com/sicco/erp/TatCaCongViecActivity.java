package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.sicco.erp.adapter.TatCaCongViecAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongViecListener;
import com.sicco.erp.model.TatCaCongViec;

public class TatCaCongViecActivity extends Activity {
	ProgressDialog pDialog;
	String url_congviec = "http://apis.mobile.vareco.vn/sicco/congviec.php";
	JSONArray congviec = null;
	ArrayList<HashMap<String, String>> congViecList;
	ArrayList<TatCaCongViec> mTatCaCongViec;
	ListView mListView;
	TatCaCongViecAdapter mAdapter;
	String idCongViec;

	static int pnumber = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tat_ca_cong_viec);

//		SessionManager sessionManager = SessionManager
//				.getInstance(getApplicationContext());
//		String token = sessionManager.getUserDetails().get(
//				SessionManager.KEY_TOKEN);
//		String username = sessionManager.getUserDetails().get(
//				SessionManager.KEY_NAME);

		pDialog = new ProgressDialog(TatCaCongViecActivity.this);
		pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
		pDialog.setCancelable(false);
		
		
		mListView = (ListView) findViewById(R.id.lv_tat_ca_cong_viec);
		
		final DBController controller = DBController.getInstance(getApplicationContext());
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
		if(mTatCaCongViec == null) {
			pDialog.show();
			mTatCaCongViec = new ArrayList<TatCaCongViec>();
		}
		mAdapter = new TatCaCongViecAdapter(getApplicationContext(),
				R.layout.item_lv_tat_ca_cong_viec, mTatCaCongViec);
		mListView.setAdapter(mAdapter);
		
		((LoadMoreListView) mListView)
		.setOnLoadMoreListener(new OnLoadMoreListener() {
			public void onLoadMore() {
				pnumber = pnumber+1;
				controller.getCongViec(pnumber, new LoadCongViecListener() {
					
					@Override
					public void onFinished(ArrayList<TatCaCongViec> data) {
						mTatCaCongViec.clear();
						mTatCaCongViec.addAll(data);
						mAdapter.notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "onLoad "+pnumber, 0).show();
						((LoadMoreListView) mListView).onLoadMoreComplete();
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
