package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.sicco.erp.adapter.CongViecDuocGiaoAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongViecDuocGiaoListener;
import com.sicco.erp.model.CongViecDuocGiao;
import com.sicco.erp.model.TatCaCongViec;

public class CongViecDuocGiaoActivity extends Activity {
	ProgressDialog pDialog;
	ArrayList<TatCaCongViec> mCongViecDuocGiao;
	CongViecDuocGiaoAdapter mAdapter;
	ListView mListView;
	String idCongViec;
	public int pnumberCViecDuocGiao = TatCaCongViecActivity.pnumberCviec;

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
					public void onFinished(ArrayList<TatCaCongViec> data) {
						if (pDialog.isShowing())
							pDialog.dismiss();
						mCongViecDuocGiao.clear();
						mCongViecDuocGiao.addAll(data);
						mAdapter.notifyDataSetChanged();

					}
				});
		if (mCongViecDuocGiao == null) {
			pDialog.show();
			mCongViecDuocGiao = new ArrayList<TatCaCongViec>();

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
											ArrayList<TatCaCongViec> data) {
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
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				HashMap<String, String> getArray = (HashMap) view.getTag();
				Intent intent = new Intent();
				intent.putExtra("id", getArray.get("id"));
				intent.putExtra("ten_cong_viec", getArray.get("ten_cong_viec"));
				intent.putExtra("tinh_trang", getArray.get("tinh_trang"));
				intent.putExtra("tien_do", getArray.get("tien_do"));
				intent.putExtra("nguoi_thuc_hien", getArray.get("nguoi_thuc_hien"));
				intent.putExtra("phong_ban", getArray.get("phong_ban"));
				intent.putExtra("loai_cong_viec", getArray.get("loai_cong_viec"));
				intent.putExtra("ngay_ket_thuc", getArray.get("ngay_ket_thuc"));
				intent.putExtra("du_an", getArray.get("du_an"));
				intent.putExtra("muc_uu_tien", getArray.get("muc_uu_tien"));
				intent.putExtra("nguoi_duoc_xem", getArray.get("nguoi_duoc_xem"));
				intent.putExtra("nguoi_giao", getArray.get("nguoi_giao"));
				intent.putExtra("mo_ta", getArray.get("mo_ta"));
				intent.putExtra("tong_hop_bao_cao", getArray.get("tong_hop_bao_cao"));
				intent.putExtra("Url", getArray.get("Url"));
				
				
				intent.setClass(getApplicationContext(),
						ChiTietCongViecActivity.class);
				startActivity(intent);
				
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
