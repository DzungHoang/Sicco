package com.sicco.erp.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.utils.Constant;
import com.sicco.erp.utils.Utils;

public class GetNotificationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("DungHV", "onStartCommand");
		SessionManager session = SessionManager.getInstance(getApplicationContext());
		String token = session.getUserDetails().get(SessionManager.KEY_TOKEN);
		if (token != null && token.length() > 0) {
			NotificationAsync async = new NotificationAsync(
					getApplicationContext());
			async.execute(token);
		}
		Utils.scheduleNext(getApplicationContext());
		stopSelf();
		return START_STICKY;
	}
	//dummy
	static int mCount = 0;
	public class NotificationAsync extends AsyncTask<String, Void, String> {
		Context mContext;

		public NotificationAsync(Context context) {
			mContext = context;
			//dummy
			mCount++;
		}

		@Override
		protected String doInBackground(String... arg0) {
			String token = arg0[0];
			Log.d("DungHV", "token = " + token);
			HTTPHandler handler = new HTTPHandler();
			List<NameValuePair> nameValue = new ArrayList<NameValuePair>();
			nameValue.add(new BasicNameValuePair("token", token));

			String ret = handler.makeHTTPRequest(
					"http://office.sicco.vn/api/get_notifications.php",
					HTTPHandler.POST, nameValue);
//			ret =  "{" + "\"notification_type\"" + ":\"congviec\"" + ",\"message_type\"" + ":\"C\u00f4ng vi\u1ec7c m\u1edbi\"" + ",\"url\"" + ":\"" + mCount +"\"" + "}";
			Log.d("DungHV", "ret = " + ret);

			try {
				JSONObject all = new JSONObject(ret);
				JSONArray rows = all.getJSONArray("rows");
				if(rows != null && rows.length() > 0){
					for (int i = 0; i < rows.length(); i++){
						JSONObject json = rows.getJSONObject(i);
						String notification_type = json.getString("notification_type");
						String msg_type = json.getString("message_type");
						String url = json.getString("url");
						Log.d("DungHV", "================================");
						Log.d("DungHV", "notification_type = " + notification_type);
						Log.d("DungHV", "msg_type = " + msg_type);
						Log.d("DungHV", "url = " + url);

						NotificationModel temp = new NotificationModel(
								notification_type, msg_type, url, "new");
						NotificationDBController db = NotificationDBController
								.getInstance(mContext);
						
						SessionManager session = SessionManager.getInstance(mContext);
						String user = session.getUserDetails().get(SessionManager.KEY_NAME);

						String selection = NotificationDBController.USER_COL
								+ "=? AND " + NotificationDBController.NOTIFI_TYPE_COL
								+ "=? AND " + NotificationDBController.MSG_TYPE_COL
								+ "=? AND " + NotificationDBController.URL_COL + "=?";
						String[] selectionArgs = new String[] { user, notification_type,
								msg_type, url };
						Cursor cursor = db.query(NotificationDBController.TABLE_NAME,
								null, selection, selectionArgs, null, null, null);
						if (cursor != null && cursor.getCount() > 0) {
							Log.d("DungHV", "already in db");
						} else {
							Log.d("DungHV", "not in db");
							ContentValues values = new ContentValues();
							values.put(NotificationDBController.NOTIFI_TYPE_COL,
									notification_type);
							values.put(NotificationDBController.MSG_TYPE_COL, msg_type);
							values.put(NotificationDBController.URL_COL, url);
							values.put(NotificationDBController.STATE_COL, Constant.NOTIFICATION_STATE_NEW);
							
							db.insert(NotificationDBController.TABLE_NAME, null, values);

							MyNotificationManager.buildNormalNotification(mContext, temp);
						}
					}
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}

			return ret;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
		}

	}
}
