package com.sicco.erp.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
import com.sicco.erp.widget.WidgetCBProvider;

public class GetNotificationService extends Service {
	Context context = this;
	JSONObject json;
	private static String url_get_notification = "http://sicco1.tk/sicco_db.php";
	private String notification_type="";
	private String ten="";
	private String content="";
	private String url="";
	private String msg_type="";
	private String congviec="congviec";
	private String congvan="congvan";
	private String lichbieu="lichbieu";
	private String notifi_type="";
	//
	int notification_Count_Type;
	int msn_Count;
	boolean check_Notification_Count=false;
	Cursor cursor;
	NotificationModel temp;
	NotificationDBController db;
	// ArrayList and variable to getCount
	ArrayList<NotificationModel> congVan_list = new ArrayList<NotificationModel>();
	ArrayList<NotificationModel> congViec_list = new ArrayList<NotificationModel>();
	ArrayList<NotificationModel> lichBieu_list = new ArrayList<NotificationModel>();
	static int congVan_count=0;
	static int congViec_count=0;
	static int lichBieu_count=0;
	ArrayList<NotificationModel> notification_list = new ArrayList<NotificationModel>();
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
			try {
				JSONObject all = new JSONObject(ret);
				JSONArray rows = all.getJSONArray("products");
				if(rows != null && rows.length() > 0){
					for (int i = 0; i < rows.length(); i++){
						json = rows.getJSONObject(i);
						/**
						 * notification_type=congvan/congviec/lichbieu/
						 */
						notification_type = json.getString("notification_type");
						// check and get msg_type & notifi_type
						msg_type = json.getString("message_type");
//						origanizeNotification(msg_type);
						//Notification 's name:
						ten = json.getString("ten");
						// noi dung:
						content = json.getString("noi_dung");
						url = json.getString("url");
						
						// ============================================================== \\
//						notification_list.add(new NotificationModel(ten));
//						Log.d("ToanNM", "notification_type_list:"+notification_type_list.size());
//						Log.d("DungHV", "==================Notification==============");
//						Log.d("DungHV", "notification_type = " + notification_type);
//						Log.d("DungHV", "msg_type = " + msg_type);
//						Log.d("DungHV", "url = " + url);
						
						temp = new NotificationModel(
								notification_type, msg_type, ten, content, url, "new");
						// ArrayList:
						notification_list.add(temp);
						db = NotificationDBController
								.getInstance(mContext);
						SessionManager session = SessionManager.getInstance(mContext);
						String user = session.getUserDetails().get(SessionManager.KEY_NAME);

						String selection = NotificationDBController.USER_COL
								+ "=? AND " + NotificationDBController.NOTIFI_TYPE_COL
								+ "=? AND " + NotificationDBController.MSG_TYPE_COL
								+ "=? AND " + NotificationDBController.NAME_COL
								+ "=? AND " + NotificationDBController.CONTENT_COL
								+ "=? AND " + NotificationDBController.URL_COL 
//								+ "=? AND " + NotificationDBController.STATE_COL 
								+ "=?";
						String[] selectionArgs = new String[] { user, notification_type,
								msg_type, ten, content, url };
						cursor = db.query(NotificationDBController.TABLE_NAME,
								null, selection, selectionArgs, null, null, null);
						db.checkedNotification(temp);
						if (cursor != null && cursor.getCount() > 0) {
						} 
						else {
							ContentValues values = new ContentValues();
							values.put(NotificationDBController.NOTIFI_TYPE_COL, notification_type);
							values.put(NotificationDBController.MSG_TYPE_COL, msg_type);
							values.put(NotificationDBController.NAME_COL, ten);
							values.put(NotificationDBController.CONTENT_COL, content);
							values.put(NotificationDBController.URL_COL, url);
							values.put(NotificationDBController.STATE_COL, Constant.NOTIFICATION_STATE_NEW);
							db.insert(NotificationDBController.TABLE_NAME, null, values);
						}
						
					}
					if (cursor != null && cursor.getCount() > 0) {
					} 
					else {
						//TuNT//
						AppWidgetManager manager = AppWidgetManager.getInstance(context);
						int[] appWidgetIds = manager.getAppWidgetIds(new ComponentName(context, WidgetCBProvider.class));
						Intent updateIntent = new  Intent(context, WidgetCBProvider.class);
						updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
						updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
						
						Log.d("TuNT", "update widget");
						context.sendBroadcast(updateIntent);
						//End-TuNT//
						origanizeNoti();
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
	public void origanizeNoti(){
//		int congviecmoi=0;
//		int congvanmoi = 0;
//		int lichcanhan = 0;
//		Log.d("ToanNM", "origanizeNoti():notification_list.size()"+notification_list.size());
		int max = notification_list.size();
		for (int i = 0; i < max; i++) {
			// Set congvan/congviec/lichbieu(count) only
//			if(notification_list.get(i).getMsg().contains("congviecmoi")){
//				congviecmoi++;
//				Log.d("ToanNM", "congviecmoi_count:"+congviecmoi);
//			}
//			if(notification_list.get(i).getMsg().contains("congvanmoi")){
//				congvanmoi++;
//				Log.d("ToanNM", "congvanmoi_count:"+congvanmoi);
//			}
//			if(notification_list.get(i).getMsg().contains("lichcanhan")){
//				lichcanhan++;
//				Log.d("ToanNM", "lichcanhan_count:"+lichcanhan);
//			}
//			Log.d("Count", "------- i =" + i +" -------");
			if(notification_list!=null){
				// ArrayList for CongVan:
				if(notification_list.get(i).getNotify().contains(congvan)){
					//add a new ArrayList
						congVan_list.add(notification_list.get(i));
						congVan_count = congVan_list.size();
						// get data
						String ten = notification_list.get(i).getName();
						String noi_dung = notification_list.get(i).getContent();
						
						Log.d("ToanNM", "run congvan:"+ ten + " " +noi_dung +" " + congVan_count+ " i:" + i + "\n" +"----------------");
						MyNotificationManager.notifyType(context, congVan_list, congVan_count, congvan, ten, noi_dung);
//						Log.d("ToanNM", "notification_list.get(i).getNotify():"+notification_list.get(i).getNotify());
						
				} 
				// ArrayList for CongViec:
				if(notification_list.get(i).getNotify().contains(congviec)){
					//add a new ArrayList
						congViec_list.add(notification_list.get(i));
						congViec_count = congViec_list.size();
						// get data
						String ten = notification_list.get(i).getName();
						String noi_dung = notification_list.get(i).getContent();
						
						Log.d("ToanNM", "run congviec:"+ ten + " " +noi_dung +" " + congViec_count+ " i:"+i + "\n" +"----------------");
						MyNotificationManager.notifyType(context, congViec_list, congViec_count, congviec, ten, noi_dung);
//						Log.d("ToanNM", "notification_list.get(i).getNotify():"+notification_list.get(i).getNotify());
					}
				// ArrayList for LichBieu:
				if(notification_list.get(i).getNotify().contains(lichbieu)){
					//add a new ArrayList
						lichBieu_list.add(notification_list.get(i));
						lichBieu_count = lichBieu_list.size();
						// get data
						String ten = notification_list.get(i).getName();
						String noi_dung = notification_list.get(i).getContent();
						
						Log.d("ToanNM", "run lichbieu:"+ ten + " " +noi_dung +" " + lichBieu_count+ " i:" + i + "\n" +"----------------");
						MyNotificationManager.notifyType(context, lichBieu_list, lichBieu_count, lichbieu, ten, noi_dung);
//						Log.d("ToanNM", "notification_list.get(i).getNotify():"+notification_list.get(i).getNotify());
					}
			}
		}
	}
	public int getCongVanCount(){
		return congVan_count;
	}
	public int getCongViecCount(){
		return congViec_count;
	}
	public int getLichBieuCount(){
		return lichBieu_count;
	}
	public static void resetCount(){
		congVan_count = 0;
		congViec_count = 0;
		lichBieu_count = 0;
	}
//	public void origanizeNotification(String msg_type){
//		
//		//messenger_type: 
//		/** 
//		 * notification_type="congvan"{ messenger_type = congvanmoi/canpheduyet/duocpheduyet.}
//		 * notification_type="congviec"{ messenger_type = congviecmoi/tiendomoi/thaoluanmoi.}
//		 * notification_type="lichbieu"{ messenger_type = lichcanhan/lichcoquan/lichphong.}
//		 * */
//		
//			if(notification_type.contains(congvan)){
//				notification_Count_Type=1;
//				if(msg_type.contains("congvanmoi"))msn_Count=11;
//				if(msg_type.contains("canpheduyet"))msn_Count=12;
//				if(msg_type.contains("duocpheduyet"))msn_Count=13;
//				
//			}else if(notification_type.contains(congviec)){
//				notification_Count_Type=2;
//				if(msg_type.contains("congviecmoi"))msn_Count=21;
//				if(msg_type.contains("tiendomoi"))msn_Count=22;
//				if(msg_type.contains("thaoluanmoi"))msn_Count=23;
//			}else if(notification_type.contains(lichbieu)){
//				notification_Count_Type=3;
//				if(msg_type.contains("lichcanhan"))msn_Count=31;
//				if(msg_type.contains("lichcoquan"))msn_Count=32;
//				if(msg_type.contains("lichphong"))msn_Count=33;
//			}
////			Log.d("ToanNM", "notification_Count:" +notification_Count_Type + ",msn_Count" +msn_Count);
//	}
}
