package com.sicco.erp.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sicco.erp.CongViecDaGiaoActivity;
import com.sicco.erp.CongViecDuocGiaoActivity;
import com.sicco.erp.R;
import com.sicco.erp.TatCaCongViecActivity;
import com.sicco.erp.adapter.CongViecAdapter;
import com.sicco.erp.manager.AlertDialogManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.CongViec;

public class FragmentCongViec extends Fragment {
	View rootView;
	SessionManager session;
	HashMap<String, String> user;
	ArrayList<CongViec> mCongViec = new ArrayList<CongViec>();
	CongViecAdapter mAdapter;
	ListView mListView;
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_cong_viec, container,
				false);
//		setHasOptionsMenu(true); //optionMenu
		session = SessionManager.getInstance(getActivity());
		user = session.getUserDetails();
		String token = user.get(SessionManager.KEY_TOKEN);
//		JobAsync job = new JobAsync();
//		job.execute(token);

		///add demo data
		mCongViec.add(new CongViec(R.drawable.cv01, getString(R.string.toan_bo_cong_viec)));
		mCongViec.add(new CongViec(R.drawable.cv01, getString(R.string.cong_viec_da_giao)));
		mCongViec.add(new CongViec(R.drawable.cv01, getString(R.string.cong_viec_duoc_giao)));
		mCongViec.add(new CongViec(R.drawable.cv01, getString(R.string.cong_viec_theo_doi)));
		mCongViec.add(new CongViec(R.drawable.cv01, getString(R.string.cong_viec_hoan_thanh)));
		
		mListView = (ListView) rootView.findViewById(R.id.jop_list);
		mAdapter = new CongViecAdapter(getActivity(), R.layout.item_cong_viec, mCongViec);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				switch (position) {
				case 0:
					intent.setClass(getActivity(), TatCaCongViecActivity.class);
					startActivityForResult(intent, 1);
					break;
				case 1:
					intent.setClass(getActivity(), CongViecDaGiaoActivity.class);
					startActivityForResult(intent, 2);
					break;
				case 2:
					intent.setClass(getActivity(), CongViecDuocGiaoActivity.class);
					startActivityForResult(intent, 3);
					break;

				default:
					break;
				}
			}
		});
		return rootView;
	}
	

//	public class JobAsync extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected String doInBackground(String... arg0) {
//			String token = arg0[0];
//			Log.d("DungHV", "token = " + token);
//			HTTPHandler handler = new HTTPHandler();
//			List<NameValuePair> tokenValue = new ArrayList<NameValuePair>();
//			tokenValue.add(new BasicNameValuePair("token", token));
//
////			String ret = handler.makeHTTPRequest(
////					"http://office.sicco.vn/api/congviec.php",
////					HTTPHandler.POST, tokenValue);
//			String ret = handler.makeHTTPRequest(
//					"http://192.168.56.1/sicco/congviec.php",
//					HTTPHandler.GET);
//			Log.d("TuNT", "ret = " + ret);
//
//			return ret;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			try {
//				JSONObject json = new JSONObject(result);
//				int mTotal = json.getInt("total");
//				JSONArray array = json.getJSONArray("rows");
//				for (int i = 0; i < array.length(); i++) {
//					JSONObject j = array.getJSONObject(i);
//					String mID = j.getString("ID");
//					String mTitle = j.getString("title");
//					String mCode = j.getString("code");
//					String mMota = j.getString("mota");
//					String mStatus = j.getString("status");
//					String mDateStart = j.getString("date_start");
//					String mDateEnd = j.getString("date_end");
//
//					mCongViec.add(new CongViec(mID, mTitle, mCode, mMota,
//							mStatus, mDateStart, mDateEnd));
//					mAdapter.notifyDataSetChanged();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			super.onPostExecute(result);
//		}
//	}
}
