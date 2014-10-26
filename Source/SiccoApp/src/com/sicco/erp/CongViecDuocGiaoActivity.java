package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.sicco.erp.adapter.CongViecDuocGiaoAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongViecDuocGiaoListener;
import com.sicco.erp.model.CongViecDuocGiao;

public class CongViecDuocGiaoActivity extends Activity {
	ProgressDialog pDialog;
	ArrayList<CongViecDuocGiao> mCongViecDuocGiao;
	CongViecDuocGiaoAdapter mAdapter;
	ListView mListView;
	String idCongViec;
	public int pnumberCViecDuocGiao = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cong_viec_duoc_giao);

		pDialog = new ProgressDialog(CongViecDuocGiaoActivity.this);
		pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
		pDialog.setCancelable(true);
		mListView = (ListView) findViewById(R.id.lv_cong_viec_duoc_giao);
		final DBController controller = DBController
				.getInstance(getApplicationContext());
		mCongViecDuocGiao = controller.getCongViecDuocGiao(1,
				new LoadCongViecDuocGiaoListener() {

					@Override
					public void onFinished(ArrayList<CongViecDuocGiao> data) {
						if (pDialog.isShowing())
							pDialog.dismiss();
						mCongViecDuocGiao.clear();
						mCongViecDuocGiao.addAll(data);
						mAdapter.notifyDataSetChanged();

					}
				});
		if (mCongViecDuocGiao == null) {
			pDialog.show();
			mCongViecDuocGiao = new ArrayList<CongViecDuocGiao>();

		}
		mAdapter = new CongViecDuocGiaoAdapter(getApplicationContext(),
				R.layout.item_lv_cong_viec_duoc_giao, mCongViecDuocGiao);
		mListView.setAdapter(mAdapter);
		((LoadMoreListView) mListView)
				.setOnLoadMoreListener(new OnLoadMoreListener() {

					@Override
					public void onLoadMore() {
						pnumberCViecDuocGiao = pnumberCViecDuocGiao + 1;
						controller.getCongViecDuocGiao(pnumberCViecDuocGiao,
								new LoadCongViecDuocGiaoListener() {

									@Override
									public void onFinished(
											ArrayList<CongViecDuocGiao> data) {
										mCongViecDuocGiao.clear();
										mCongViecDuocGiao.addAll(data);
										Toast.makeText(
												getApplicationContext(),
												"OnLoad "
														+ pnumberCViecDuocGiao,
												0).show();
										((LoadMoreListView) mListView)
												.onLoadMoreComplete();

									}
								});

					}
				});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
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
