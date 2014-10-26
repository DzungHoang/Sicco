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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.sicco.erp.adapter.CongViecDaGiaoAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongViecDaGiaoListener;
import com.sicco.erp.model.CongViecDaGiao;

public class CongViecDaGiaoActivity extends Activity {

	ProgressDialog pDialog;
	ArrayList<CongViecDaGiao> mCongViecDaGiao;
	ListView mListView;
	CongViecDaGiaoAdapter mAdapter;
	String idCongViec;
	static int pnumberCviecDaGiao = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cong_viec_da_giao);

		pDialog = new ProgressDialog(CongViecDaGiaoActivity.this);
		pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
		pDialog.setCancelable(true);
		mListView = (ListView) findViewById(R.id.lv_cong_viec_da_giao);
		final DBController controller = DBController
				.getInstance(getApplicationContext());
		mCongViecDaGiao = controller.getCongViecDaGiao(1,
				new LoadCongViecDaGiaoListener() {

					@Override
					public void onFinished(ArrayList<CongViecDaGiao> data) {
						if (pDialog.isShowing())
							pDialog.dismiss();
						mCongViecDaGiao.clear();
						mCongViecDaGiao.addAll(data);
						mAdapter.notifyDataSetChanged();

					}
				});
		if (mCongViecDaGiao == null) {
			pDialog.show();
			mCongViecDaGiao = new ArrayList<CongViecDaGiao>();
		}
		mAdapter = new CongViecDaGiaoAdapter(getApplicationContext(),
				R.layout.item_lv_cong_viec_da_giao, mCongViecDaGiao);
		mListView.setAdapter(mAdapter);
		((LoadMoreListView) mListView)
				.setOnLoadMoreListener(new OnLoadMoreListener() {
					public void onLoadMore() {
						pnumberCviecDaGiao = pnumberCviecDaGiao + 1;
						controller.getCongViecDaGiao(pnumberCviecDaGiao,
								new LoadCongViecDaGiaoListener() {

									@Override
									public void onFinished(
											ArrayList<CongViecDaGiao> data) {
										mCongViecDaGiao.clear();
										mCongViecDaGiao.addAll(data);
										mAdapter.notifyDataSetChanged();
										Toast.makeText(getApplicationContext(),
												"onLoad " + pnumberCviecDaGiao,
												0).show();
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
