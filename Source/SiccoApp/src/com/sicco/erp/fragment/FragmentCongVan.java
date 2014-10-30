package com.sicco.erp.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.sicco.erp.R;
import com.sicco.erp.TatCaCongViecActivity;
import com.sicco.erp.adapter.CongVanAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongVanListener;
import com.sicco.erp.database.DBController.LoadCongViecHoanThanhListener;
import com.sicco.erp.model.CongVan;
import com.sicco.erp.model.TatCaCongViec;

public class FragmentCongVan extends Fragment{
	View rootView;
	ProgressDialog pDialog;
	ProgressBar pLoadmore;
	ArrayList<CongVan> mCongVan;
	ListView mListView;
	CongVanAdapter mAdapter;
	static int pnumberCvan = 1;
	Button btn_LoadMore;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_cong_van, container, false);
		
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage(getResources().getString(R.string.vui_long_doi));
		pDialog.setCancelable(true);
		
		final DBController controller = DBController.getInstance(getActivity());
		mCongVan = controller.getCongVan(1, new LoadCongVanListener() {
			
			@Override
			public void onFinished(ArrayList<CongVan> data) {
				if (pDialog.isShowing()) {
					pDialog.dismiss();
				}
				mCongVan.clear();
				mCongVan.addAll(data);
				mAdapter.notifyDataSetChanged();
				
			}
		});
		
		if (mCongVan == null) {
			pDialog.show();
			mCongVan = new ArrayList<CongVan>();
		}
		
		mListView = (ListView) rootView.findViewById(R.id.Congvan_listView);
		LayoutInflater inflater2 = LayoutInflater.from(getActivity());
		View footerView = inflater2.inflate(R.layout.btn_loadmore, null);
		
		btn_LoadMore = (Button)footerView.findViewById(R.id.LoadMore);
		pLoadmore = (ProgressBar)footerView.findViewById(R.id.progressBar1);
		
		mListView.addFooterView(footerView);
		mAdapter = new CongVanAdapter(getActivity(), R.layout.cvan_list_item, mCongVan);
		mListView.setAdapter(mAdapter);
		btn_LoadMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pLoadmore.setVisibility(View.VISIBLE);
				pnumberCvan = pnumberCvan+1;
				controller.getCongVan(pnumberCvan, new LoadCongVanListener() {
					
					
					@Override
					public void onFinished(ArrayList<CongVan> data) {
						mCongVan.clear();
						mCongVan.addAll(data);
						pLoadmore.setVisibility(View.INVISIBLE);
						mAdapter.notifyDataSetChanged();
						((LoadMoreListView) mListView).onLoadMoreComplete();
					}
				});
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(), "Mo trinh duyet voi URL : " + mCongVan.get(position).getUrl(), 0).show();
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCongVan.get(position).getUrl()));
				startActivity(browserIntent);
				
			}
			
		});
		
		return rootView;
	}
	
}
