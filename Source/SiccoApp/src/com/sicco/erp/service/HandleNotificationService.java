package com.sicco.erp.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.sicco.erp.MainActivity;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.model.NotificationModel;

public class HandleNotificationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("DungHV", "HandleNotificationService.onStartCommand");
		int count = MyNotificationManager.getCount();
		Log.d("DungHV","count = " + count);
		if (count == 1) {
			if (intent != null) {
				Bundle bundle = intent.getBundleExtra("data");
				if (bundle != null) {
					NotificationModel data = (NotificationModel) bundle
							.getSerializable("data");
					if (data != null) {
						Log.d("DungHV","show URL");
						Intent resultIntent = new Intent();
						resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						resultIntent.setAction(Intent.ACTION_VIEW);
						resultIntent.setData(Uri.parse(data.getUrl()));
						startActivity(resultIntent);
					}
					NotificationDBController db = NotificationDBController.getInstance(getApplicationContext());
					db.checkedNotification(data);
				}
			}
		}else if(count > 1){
			Log.d("DungHV","show activity");
			Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
			activityIntent.putExtra("data", true);
			activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(activityIntent);
		}
		
		MyNotificationManager.resetCount();
//		stopSelf();
		return START_NOT_STICKY;
	}

}
