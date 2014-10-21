package com.sicco.erp.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
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
	Context context = this;
	JSONObject json;
	private static String url_get_notification = "http://sicco.tk/sicco_db.php";
	public static String notification_type="";
	public static String ten="";
	public static String content="";
	static String msg_type="";
	String congviec="congviec";
	String congvan="congvan";
	String lichbieu="lichbieu";
	public static int notification_Count;
	public static int msn_Count;
	public static boolean check_Notification_Count=false;
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
//			Log.d("DungHV", "token = " + token);
			HTTPHandler handler = new HTTPHandler();
			List<NameValuePair> nameValue = new ArrayList<NameValuePair>();
			nameValue.add(new BasicNameValuePair("token", token));

			String ret = handler.makeHTTPRequest(url_get_notification,HTTPHandler.POST, nameValue);
//			ret =  "{" + "\"notification_type\"" + ":\"congviec\"" + ",\"message_type\"" + ":\"C\u00f4ng vi\u1ec7c m\u1edbi\"" + ",\"url\"" + ":\"" + mCount +"\"" + "}";
//			
//				NotificationDBController a = new NotificationDBController(context);
//				a.deleteAllData();
//				Log.d("ToanNM", "already deleted all data from NotificationDBController + \r\n\r ");
//				if (loadNotification_Db_hasbeencompleted) {
			try {
				JSONObject all = new JSONObject(ret);
				JSONArray rows = all.getJSONArray("products");
				if(rows != null && rows.length() > 0){
					for (int i = 0; i < rows.length(); i++){
						json = rows.getJSONObject(i);
						//notification_type=congvan/congviec/lichbieu/						
						notification_type = json.getString("notification_type");
						//Notification 's name:
						ten = json.getString("ten");
						
						// ============================================================== \\
						String url = json.getString("url");
						// check and get msg_type & notifi_type
						//messenger_type: 
						/** 
						 * notification_type="congvan"{ messenger_type = congvanmoi/canpheduyet/duocpheduyet.}
						 * notification_type="congviec"{ messenger_type = congviecmoi/tiendomoi/thaoluanmoi.}
						 * notification_type="lichbieu"{ messenger_type = lichcanhan/lichcoquan/lichphong.}
						 * */
						msg_type = json.getString("message_type");
							if(notification_type.contains(congvan)){
								notification_Count=1;
								if(msg_type.contains("congvanmoi"))msn_Count=11;
								if(msg_type.contains("canpheduyet"))msn_Count=12;
								if(msg_type.contains("duocpheduyet"))msn_Count=13;
								
							}else if(notification_type.contains(congviec)){
								notification_Count=2;
								if(msg_type.contains("congviecmoi"))msn_Count=21;
								if(msg_type.contains("tiendomoi"))msn_Count=22;
								if(msg_type.contains("thaoluanmoi"))msn_Count=23;
							}else if(notification_type.contains(lichbieu)){
								notification_Count=3;
								if(msg_type.contains("lichcanhan"))msn_Count=31;
								if(msg_type.contains("lichcoquan"))msn_Count=32;
								if(msg_type.contains("lichphong"))msn_Count=33;
							}
							Log.d("ToanNM", "notification_Count:" +notification_Count + ",msn_Count" +msn_Count);
						// noi dung:
						content = json.getString("noi_dung");
//						Log.d("DungHV", "==================Notification==============");
//						Log.d("DungHV", "notification_type = " + notification_type);
//						Log.d("DungHV", "msg_type = " + msg_type);
//						Log.d("DungHV", "url = " + url);
						
						NotificationModel temp = new NotificationModel(
								notification_type, msg_type, content, url, "new");
						NotificationDBController db = NotificationDBController
								.getInstance(mContext);
						
						
						SessionManager session = SessionManager.getInstance(mContext);
						String user = session.getUserDetails().get(SessionManager.KEY_NAME);

						String selection = NotificationDBController.USER_COL
								+ "=? AND " + NotificationDBController.NOTIFI_TYPE_COL
								+ "=? AND " + NotificationDBController.MSG_TYPE_COL
								+ "=? AND " + NotificationDBController.CONTENT_COL
								+ "=? AND " + NotificationDBController.URL_COL 
//								+ "=? AND " + NotificationDBController.STATE_COL 
								+ "=?";
						String[] selectionArgs = new String[] { user, notification_type,
								msg_type, content, url };
						Cursor cursor = db.query(NotificationDBController.TABLE_NAME,
								null, selection, selectionArgs, null, null, null);
						db.checkedNotification(temp);
						if (cursor != null && cursor.getCount() > 0) {
//							Log.d("DungHV", "already in db");
						} 
						else {
//							Log.d("DungHV", "not in db");
							ContentValues values = new ContentValues();
							values.put(NotificationDBController.NOTIFI_TYPE_COL, notification_type);
							values.put(NotificationDBController.MSG_TYPE_COL, msg_type);
							values.put(NotificationDBController.CONTENT_COL, content);
							values.put(NotificationDBController.URL_COL, url);
							values.put(NotificationDBController.STATE_COL, Constant.NOTIFICATION_STATE_NEW);
							
							db.insert(NotificationDBController.TABLE_NAME, null, values);
							check_Notification_Count = true;
							MyNotificationManager.buildNormalNotification(mContext, temp);
						}
					}
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
			return ret;
//			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
		}
	}
//	public boolean hasConnection() {
//	    ConnectivityManager cm = (ConnectivityManager)context.getSystemService(
//	        Context.CONNECTIVITY_SERVICE);
//
//	    NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//	    if (wifiNetwork != null && wifiNetwork.isConnected()) {
//	      return true;
//	    }
//
//	    NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//	    if (mobileNetwork != null && mobileNetwork.isConnected()) {
//	      return true;
//	    }
//
//	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//	    if (activeNetwork != null && activeNetwork.isConnected()) {
//	      return true;
//	    }
//
//	    return false;
//	  }
}
