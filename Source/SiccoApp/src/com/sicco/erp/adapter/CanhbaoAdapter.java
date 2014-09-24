package com.sicco.erp.adapter;

import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.utils.Constant;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CanhbaoAdapter extends CursorAdapter {


	public CanhbaoAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (view != null) {
			TextView msgTxt = (TextView) view.findViewById(R.id.msg_canhbao);
			TextView urlTxt = (TextView) view.findViewById(R.id.url_canhbao);
			
			String msg = cursor.getString(cursor.getColumnIndex(NotificationDBController.MSG_TYPE_COL));
			String url = cursor.getString(cursor.getColumnIndex(NotificationDBController.URL_COL));
			
			msgTxt.setText(msg);
			urlTxt.setText(url);
			
			String state = cursor.getString(cursor.getColumnIndex(NotificationDBController.STATE_COL));
			if(state.equals(Constant.NOTIFICATION_STATE_NEW)){
				view.setBackgroundColor(Color.CYAN);
			}
		}

	}

	@Override
	public View newView(Context context, Cursor arg1, ViewGroup arg2) {
		View view = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.item_canhbao, arg2, false);
		return view;
	}

}
