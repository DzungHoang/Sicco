package com.sicco.erp.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.sicco.erp.R;
import com.sicco.erp.adapter.CongVanAdapter;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongVanListener;
import com.sicco.erp.model.CongVan;

public class FragmentCongVan extends Fragment{
	View rootView;
	ProgressDialog pDialog;
	
	ArrayList<CongVan> mCongVan;
	ListView mListView;
	CongVanAdapter mAdapter;
	static int pnumberCvan = 1;
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
		mAdapter = new CongVanAdapter(getActivity(), R.layout.cvan_list_item, mCongVan);
		mListView.setAdapter(mAdapter);
		
		((LoadMoreListView) mListView).setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				pnumberCvan = pnumberCvan+1;
				Toast.makeText(getActivity(), "pnumber : " + pnumberCvan	, 0).show();
				controller.getCongVan(pnumberCvan, new LoadCongVanListener() {
					
					
					@Override
					public void onFinished(ArrayList<CongVan> data) {
						mCongVan.clear();
						mCongVan.addAll(data);
					
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
