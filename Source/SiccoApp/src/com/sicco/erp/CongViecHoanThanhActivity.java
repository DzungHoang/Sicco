package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.androidquery.util.Progress;
import com.sicco.erp.adapter.CongViecHoanThanhAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongViecHoanThanhListener;
import com.sicco.erp.database.DBController.LoadCongViecListener;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.TatCaCongViec;

public class CongViecHoanThanhActivity extends Activity {
	ProgressDialog pDialog;
	ProgressBar pLoadmore;
	ArrayList<TatCaCongViec> mCongViecHoanThanh;
	ListView mListView;
	CongViecHoanThanhAdapter mAdapter;
	String userName;
	
	Button btn_LoadMore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cong_viec_hoan_thanh);
		
		
		mListView = (ListView) findViewById(R.id.lv_cong_viec_hoan_thanh);
		View footerView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.btn_loadmore, null,false);
		btn_LoadMore = (Button)footerView.findViewById(R.id.LoadMore);
		mListView.addFooterView(footerView);
		
		pLoadmore = (ProgressBar)findViewById(R.id.progressBar1);
		
		pDialog = new ProgressDialog(CongViecHoanThanhActivity.this);
		pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
		pDialog.setCancelable(true);

		
		final DBController controller = DBController.getInstance(getApplicationContext());
		mCongViecHoanThanh = controller.getCongViecHoanThanh(1, new LoadCongViecHoanThanhListener() {
			
			@Override
			public void onFinished(ArrayList<TatCaCongViec> data) {
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				
				mCongViecHoanThanh.clear();
				mCongViecHoanThanh.addAll(data);
				mAdapter.notifyDataSetChanged();
			}
		}); 
		if (mCongViecHoanThanh == null) {
			
			pDialog.show();
			mCongViecHoanThanh = new ArrayList<TatCaCongViec>();
		}
		
		btn_LoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pLoadmore.setVisibility(View.VISIBLE);
				TatCaCongViecActivity.pnumberCviec = TatCaCongViecActivity.pnumberCviec+1;
				Toast.makeText(getApplicationContext(), "LoadMore" + TatCaCongViecActivity.pnumberCviec, 0).show();
				controller.getCongViecHoanThanh(TatCaCongViecActivity.pnumberCviec, new LoadCongViecHoanThanhListener() {
					
					@Override
					public void onFinished(ArrayList<TatCaCongViec> data) {
						mCongViecHoanThanh.clear();
						mCongViecHoanThanh.addAll(data);
						mAdapter.notifyDataSetChanged();
						pLoadmore.setVisibility(View.INVISIBLE);
					}
				});
			}
		});
		
		mAdapter = new CongViecHoanThanhAdapter(getApplicationContext(),
				R.layout.item_lv_cong_viec_hoan_thanh, mCongViecHoanThanh);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("thong_tin", ""+view.getTag());
				Log.d("NgaDV", ""+view.getTag());
				Toast.makeText(getApplicationContext(), "gettag: " + view.getTag(), 0).show();
				intent.setClass(getApplicationContext(), ChiTietCongViecActivity.class);
				startActivity(intent);
			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_cong_viec_hoan_thanh, menu);
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
