package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sicco.erp.adapter.CongViecDuocGiaoAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongViecDuocGiaoListener;
import com.sicco.erp.database.DBController.LoadCongViecHoanThanhListener;
import com.sicco.erp.model.CongViecDuocGiao;
import com.sicco.erp.model.TatCaCongViec;

public class CongViecDuocGiaoActivity extends Activity {

	ProgressDialog pDialog;
	ProgressBar pLoadmore;
	ArrayList<TatCaCongViec> mCongViecDuocGiao;
	ListView mListView;
	CongViecDuocGiaoAdapter mAdapter;
	String userName;
	
	Button btn_LoadMore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cong_viec_duoc_giao);


		mListView = (ListView) findViewById(R.id.lv_cong_viec_duoc_giao);
		View footerView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.btn_loadmore, null,false);
		btn_LoadMore = (Button)footerView.findViewById(R.id.LoadMore);
		mListView.addFooterView(footerView);
		
		pLoadmore = (ProgressBar)findViewById(R.id.progressBar1);
		
		pDialog = new ProgressDialog(CongViecDuocGiaoActivity.this);
		pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
		pDialog.setCancelable(true);

		
		final DBController controller = DBController.getInstance(getApplicationContext());
		mCongViecDuocGiao = controller.getCongViecDuocGiao(1, new LoadCongViecDuocGiaoListener() {
			
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
		
		btn_LoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pLoadmore.setVisibility(View.VISIBLE);
				TatCaCongViecActivity.pnumberCviec = TatCaCongViecActivity.pnumberCviec+1;
				Toast.makeText(getApplicationContext(), "LoadMore" + TatCaCongViecActivity.pnumberCviec, 0).show();
				controller.getCongViecDuocGiao(TatCaCongViecActivity.pnumberCviec, new LoadCongViecDuocGiaoListener() {
					
					@Override
					public void onFinished(ArrayList<TatCaCongViec> data) {
						mCongViecDuocGiao.clear();
						mCongViecDuocGiao.addAll(data);
						mAdapter.notifyDataSetChanged();
						pLoadmore.setVisibility(View.INVISIBLE);
					}
				});
			}
		});
		mAdapter = new CongViecDuocGiaoAdapter(getApplicationContext(),
				R.layout.item_lv_cong_viec_duoc_giao, mCongViecDuocGiao);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
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
