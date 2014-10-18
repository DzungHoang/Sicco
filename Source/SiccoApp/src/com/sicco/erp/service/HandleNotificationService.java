package com.sicco.erp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.sicco.erp.MainActivity;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.fragment.NavigationDrawerFragment;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.model.NotificationModel;

public class HandleNotificationService extends Service {
	private NotificationManager mNotificationManager;
	Context mContext;
	NotificationModel notificationModel;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("DungHV", "HandleNotificationService.onStartCommand");
		int CongViecCount = MyNotificationManager.getCongViecCount();
		int CongVanCount = MyNotificationManager.getCongVanCount();
		int LichBieuCount = MyNotificationManager.getLichbieuCount();
		Log.d("DungHV","CongViecCount = " + CongViecCount);
		Log.d("DungHV","CongVanCount = " + CongVanCount);
		Log.d("DungHV","LichBieuCount = " + LichBieuCount);
		if (CongVanCount == 1) {
			if (intent != null) {
				Bundle bundle = intent.getBundleExtra("data");
				if (bundle != null) {
					NotificationModel data = (NotificationModel) bundle
							.getSerializable("data");
					if (data != null) {
						if(GetNotificationService.notification_type.contains("congvan")){
						Log.d("DungHV","show URL");
						Intent resultIntent = new Intent(this,MainActivity.class);
						resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						PendingIntent pendInt = PendingIntent.getActivity(this, 0,
								resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						resultIntent.setAction(Intent.ACTION_VIEW);
						resultIntent.setData(Uri.parse(data.getUrl()));
						// 
						NavigationDrawerFragment.mCurrentSelectedPosition = 1;
						startActivity(resultIntent);
						// remove noti on statusbar
						CongVancancelNotification();
						}
					}
					NotificationDBController db = NotificationDBController.getInstance(getApplicationContext());
					db.checkedNotification(data);
				}
			}
		}else if(CongVanCount > 1){
			Log.d("DungHV","show activity");
			if(GetNotificationService.notification_type.contains("congvan")){
			Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
			activityIntent.putExtra("data", true);
			activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(activityIntent);
			CongVancancelNotification();
			}
		}
		//congviec:
		if (CongViecCount == 1) {
			if (intent != null) {
				Bundle bundle = intent.getBundleExtra("data");
				if (bundle != null) {
					NotificationModel data = (NotificationModel) bundle
							.getSerializable("data");
					if (data != null) {
						if(GetNotificationService.notification_type.contains("congviec")){
						Log.d("DungHV","show URL");
						Intent resultIntent = new Intent(this,MainActivity.class);
						resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						PendingIntent pendInt = PendingIntent.getActivity(this, 0,
								resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						resultIntent.setAction(Intent.ACTION_VIEW);
						resultIntent.setData(Uri.parse(data.getUrl()));
						// 
						NavigationDrawerFragment.mCurrentSelectedPosition = 1;
						startActivity(resultIntent);
						// remove noti on statusbar
						CongVieccancelNotification();
						}
					}
					NotificationDBController db = NotificationDBController.getInstance(getApplicationContext());
					db.checkedNotification(data);
				}
			}
		}else if(CongViecCount > 1){
			Log.d("DungHV","show activity");
			if(GetNotificationService.notification_type.contains("congviec")){
			Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
			activityIntent.putExtra("data", true);
			activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(activityIntent);
			CongVieccancelNotification();
			}
		}
		//LichBieu:
		if (LichBieuCount == 1) {
			if (intent != null) {
				Bundle bundle = intent.getBundleExtra("data");
				if (bundle != null) {
					NotificationModel data = (NotificationModel) bundle
							.getSerializable("data");
					if (data != null) {
						if(GetNotificationService.notification_type.contains("lichbieu")){
						Log.d("DungHV","show URL");
						Intent resultIntent = new Intent(this,MainActivity.class);
						resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						PendingIntent pendInt = PendingIntent.getActivity(this, 0,
								resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						resultIntent.setAction(Intent.ACTION_VIEW);
						resultIntent.setData(Uri.parse(data.getUrl()));
						// 
						NavigationDrawerFragment.mCurrentSelectedPosition = 1;
						startActivity(resultIntent);
						// remove noti on statusbar
						LichBieucancelNotification();
						}
					}
					NotificationDBController db = NotificationDBController.getInstance(getApplicationContext());
					db.checkedNotification(data);
				}
			}
		}else if(LichBieuCount > 1){
			Log.d("DungHV","show activity");
			if(GetNotificationService.notification_type.contains("lichbieu")){
			Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
			activityIntent.putExtra("data", true);
			activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(activityIntent);
			LichBieucancelNotification();
			}
		}
		MyNotificationManager.resetCount();
//		CongVancancelNotification();
//		CongVieccancelNotification();
//		LichBieucancelNotification();
//		stopSelf();
		return START_NOT_STICKY;
	}
	public void CongVancancelNotification() {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(notificationServiceStr);
		mNotificationManager.cancel(MyNotificationManager.CongVanNOTIFICATION_ID);
	}
	public void CongVieccancelNotification() {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(notificationServiceStr);
		mNotificationManager.cancel(MyNotificationManager.CongViecNOTIFICATION_ID);
	}
	public void LichBieucancelNotification() {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(notificationServiceStr);
		mNotificationManager.cancel(MyNotificationManager.LichBieuNOTIFICATION_ID);
	}

}
