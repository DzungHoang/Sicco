package com.sicco.erp.widget;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.androidquery.AQuery;
import com.sicco.erp.database.DBController;
import com.sicco.erp.database.DBController.LoadCongViecListener;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.TatCaCongViec;

public class RemoteFetchService extends Service {

	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private AQuery aquery;
	private String remoteJsonUrl = "http://apis.mobile.vareco.vn/sicco/congviec.php";

	public static ArrayList<TatCaCongViec> listItemList;
	JSONArray congviec = null;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("TuNT", "onStartCommand");
		if (intent != null) {
			if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID))
				appWidgetId = intent.getIntExtra(
						AppWidgetManager.EXTRA_APPWIDGET_ID,
						AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		final DBController controller = DBController
				.getInstance(getApplicationContext());

		// controller.getCongViec();
		// Log.e("TuNT", "controller.getCongViec: " + controller.getCongViec());
		SessionManager session = SessionManager
				.getInstance(getApplicationContext());
		String token = session.getUserDetails().get(SessionManager.KEY_TOKEN);

		// if (token != null && token.length() > 0) {
		// Log.d("TuNT", "token = " + token);
		// controller.setLoadingFinishListener(new LoadingFinishListener() {
		//
		// @Override
		// public void onFinished() {
		// listItemList = controller.getCongViec();
		// listItemList.add(new TatCaCongViec("-1", "Xem thêm", ""));
		// listItemList.clone();
		// Log.e("TuNT", "onFinished: " + listItemList);
		// populateWidget();
		// }
		// });
		// }
		Log.d("TuNT", "getCongViec from Fetch");
		listItemList = controller.getCongViec(1, new LoadCongViecListener() {

			@Override
			public void onFinished(ArrayList<TatCaCongViec> data) {

				Log.d("TuNT", "onFinished: " + data);
				listItemList = new ArrayList<TatCaCongViec>();
				listItemList.addAll(data);
				populateWidget();
			}
		});
		if (listItemList != null)
			populateWidget();

		return super.onStartCommand(intent, flags, startId);
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
