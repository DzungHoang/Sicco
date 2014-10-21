package com.sicco.erp.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.TatCaCongViec;
import com.sicco.erp.service.GetNotificationService.NotificationAsync;

public class RemoteFetchService extends Service {

	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private AQuery aquery;
	private String remoteJsonUrl = "http://apis.mobile.vareco.vn/sicco/congviec.php";

	public static ArrayList<ListItem> listItemList;
	JSONArray congviec = null;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
			appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		SessionManager session = SessionManager.getInstance(getApplicationContext());
		String token = session.getUserDetails().get(SessionManager.KEY_TOKEN);
		if (token != null && token.length() > 0) {
			new GetCongViec().execute(token, "aew", "2");
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public class GetCongViec extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			String token = params[0];
			String username = params[1];
			String page = params[2];
			
			Log.d("TuNT", "token " + token);
			HTTPHandler handler = new HTTPHandler();
			List<NameValuePair> nameValue = new ArrayList<NameValuePair>();
			nameValue.add(new BasicNameValuePair("token", token));
			nameValue.add(new BasicNameValuePair("username", username));
			nameValue.add(new BasicNameValuePair("page", page));

			String jsonCode = handler.makeHTTPRequest(remoteJsonUrl,HTTPHandler.POST, nameValue);
			return jsonCode;
		}
		@Override
		protected void onPostExecute(String result) {
			Log.d("TuNT", result);
			listItemList = new ArrayList<ListItem>();
			try {
				JSONObject jsonObj = new JSONObject(result);
				congviec = jsonObj.getJSONArray("row");

				for (int i = 0; i < congviec.length(); i++) {
					JSONObject c = congviec.getJSONObject(i);

					String id = c.getString("id");
					String title = c.getString("ten_cong_viec");
					String han_cuoi = c.getString("ngay_ket_thuc");
					ListItem listItem = new ListItem();
					listItem.id = id;
					listItem.title = title;
					listItem.han_cuoi = han_cuoi;
					listItemList.add(listItem);
				}
				ListItem loadMore = new ListItem();
				loadMore.id = "-1";
				loadMore.title = "Xem them";
				loadMore.han_cuoi = "";
				listItemList.add(loadMore);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			populateWidget();
			super.onPostExecute(result);
		}
		
	}
	private void populateWidget() {

		Intent widgetUpdateIntent = new Intent();
		widgetUpdateIntent.setAction(WidgetCVProvider.DATA_FETCHED);
		widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				appWidgetId);
		sendBroadcast(widgetUpdateIntent);

		this.stopSelf();
	}
}
