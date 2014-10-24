package com.sicco.erp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sicco.erp.R;
import com.sicco.erp.adapter.CanhbaoAdapter;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.NotificationModel;

public class FragmentCanhBao extends Fragment {
	View rootView;
	CanhbaoAdapter mAdapter;
	ListView mListView;

	Cursor mCursor;
	NotificationDBController mDB;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_cong_viec, container,
				false);

		mListView = (ListView) rootView.findViewById(R.id.jop_list);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mCursor != null) {
					mCursor.moveToFirst();
					for (int i = 0; i < position; i++) {
						Log.d("DungHV", "touch on " + i);
						mCursor.moveToNext();
					}
					String notify = mCursor.getString(mCursor
							.getColumnIndex(NotificationDBController.NOTIFI_TYPE_COL));
					String msg = mCursor.getString(mCursor
							.getColumnIndex(NotificationDBController.MSG_TYPE_COL));
					String ten = mCursor.getString(mCursor
							.getColumnIndex(NotificationDBController.NAME_COL));
					String content = mCursor.getString(mCursor
							.getColumnIndex(NotificationDBController.CONTENT_COL));
					String url = mCursor.getString(mCursor
							.getColumnIndex(NotificationDBController.URL_COL));
					String state = mCursor.getString(mCursor
							.getColumnIndex(NotificationDBController.STATE_COL));
					NotificationModel temp = new NotificationModel(notify, msg, ten,content, url, state);

					mDB.checkedNotification(temp);

					Intent resultIntent = new Intent();
					resultIntent.setAction(Intent.ACTION_VIEW);
					resultIntent.setData(Uri.parse(url));
					startActivity(resultIntent);
				}
			}
		});
		return rootView;
	}

	@Override
	public void onResume() {
		mDB = NotificationDBController.getInstance(getActivity());
		SessionManager session = SessionManager.getInstance(getActivity());
		String user = session.getUserDetails().get(SessionManager.KEY_NAME);
		String selection = NotificationDBController.USER_COL + "=?";
		String[] selectionArgs = new String[] { user };
		mCursor = mDB.query(NotificationDBController.TABLE_NAME, null,
				selection, selectionArgs, null, null, null);

		mAdapter = new CanhbaoAdapter(getActivity(), mCursor);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		super.onResume();
	}

}
