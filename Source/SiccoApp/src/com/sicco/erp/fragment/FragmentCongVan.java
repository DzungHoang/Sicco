package com.sicco.erp.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sicco.erp.MainActivity;
import com.sicco.erp.R;
import com.sicco.erp.adapter.CongVanAdapter;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.model.CongVan;

public class FragmentCongVan extends Fragment{
	View rootView;
	ProgressDialog pDialog;
	
	String url_congvan = "http://192.168.56.1/sicco/congvan.php";
	JSONArray contacts = null;

	ArrayList<HashMap<String, String>> contactList;
	ArrayList<CongVan> mCongVan;
	ListView mListView;
//	ArrayList<CongVan> mCongVan = new ArrayList<CongVan>();
	CongVanAdapter mAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_cong_van, container, false);
		contactList = new ArrayList<HashMap<String, String>>();
		new GetContacts().execute();
		
		mCongVan = new ArrayList<CongVan>();
		
		
//		Log.d("NgaDV", "oncreat view : " + mCongVan.toString());
		mListView = (ListView) rootView.findViewById(R.id.Congvan_listView);
		mAdapter = new CongVanAdapter(getActivity(), R.layout.cvan_list_item, mCongVan);
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(), "Má»Ÿ trĂ¬nh duyá»‡t vá»›i URL : " + mCongVan.get(position).getUrl(), 0).show();
				
			}
			
		});
		
		return rootView;
	}
	
	private class GetContacts extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Vui long doi !...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected String doInBackground(Void... arg0) {
			// Creating service handler class instance
			HTTPHandler sh = new HTTPHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeHTTPRequest(url_congvan, HTTPHandler.GET);

			Log.d("NgaDV", "json" + jsonStr);

			return jsonStr;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();
			if (result != null) {
				try {
					JSONObject jsonObj = new JSONObject(result);

					
					// Getting JSON Array node
					contacts = jsonObj.getJSONArray("row");
					
					// looping through All Contacts
					for (int i = 0; i < contacts.length(); i++) {
						JSONObject c = contacts.getJSONObject(i);
						
						
						String id = c.getString("id");
						String title = c.getString("loai_cong_van");
						String detail = c.getString("trich_yeu");
						String url = c.getString("url");
						
						
						mCongVan.add(new CongVan(title, detail, url, id));
						mAdapter.notifyDataSetChanged();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("NgaDV", "Khong the lay data tu url nay");
			}
		}
	}
}
