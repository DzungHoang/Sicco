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
	GetNotificationService getNotificationService;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("DungHV", "HandleNotificationService.onStartCommand");
		int congVanCount= GetNotificationService.getCongVanCount();
		int congViecCount = GetNotificationService.getCongViecCount();
		int LichBieuCount = GetNotificationService.getLichBieuCount();
		Log.d("Count","CongVanCount = " + congVanCount);
		Log.d("Count","CongViecCount = " + congViecCount);
		Log.d("Count","LichBieuCount = " + LichBieuCount);
		if (congVanCount == 1) {
			if (intent != null) {
				Bundle bundle = intent.getBundleExtra("data");
				if (bundle != null) {
					NotificationModel data = (NotificationModel) bundle
							.getSerializable("data");
					if (data != null) {
						Log.d("DungHV","show URL");
//						Intent resultIntent = new Intent(this,MainActivity.class);
//						resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						resultIntent.setAction(Intent.ACTION_VIEW);
//						resultIntent.setData(Uri.parse(data.getUrl()));
						// 
						Intent resultIntent = new Intent(this, MainActivity.class);
						resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					    resultIntent.setData(Uri.parse(data.getUrl()));
						NavigationDrawerFragment.mCurrentSelectedPosition = 1;
//						startActivity(resultIntent);
						// remove noti on statusbar
						CongVanCancelNotification();
						}
					NotificationDBController db = NotificationDBController.getInstance(getApplicationContext());
					db.checkedNotification(data);
				}
			}
		}else if(congVanCount > 1){
			if (intent != null) {
			Log.d("DungHV","show activity");
			if (intent != null) {
				Bundle bundle = intent.getBundleExtra("data");
				if (bundle != null) {
				Intent resultIntent = new Intent(this, MainActivity.class);
				resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				NavigationDrawerFragment.mCurrentSelectedPosition = 1;
//				startActivity(resultIntent);
				// remove noti on statusbar
				CongVanCancelNotification();
				}
			}
		}
		//congviec:
		if (congViecCount == 1) {
			if (intent != null) {
				Bundle bundle = intent.getBundleExtra("data");
				if (bundle != null) {
					NotificationModel data = (NotificationModel) bundle
							.getSerializable("data");
					if (data != null) {
						Log.d("DungHV","show URL");
						Intent resultIntent = new Intent(this, MainActivity.class);
						resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					    resultIntent.setData(Uri.parse(data.getUrl()));
						NavigationDrawerFragment.mCurrentSelectedPosition = 1;
						// remove noti on statusbar
						CongViecCancelNotification();
						}
					NotificationDBController db = NotificationDBController.getInstance(getApplicationContext());
					db.checkedNotification(data);
				}
			}
		}else if(congViecCount > 1){
			Log.d("DungHV","show activity");
			if (intent != null) {
				Bundle bundle = intent.getBundleExtra("data");
				if (bundle != null) {
						Log.d("DungHV","show URL");
						CongViecCancelNotification();
						Intent resultIntent = new Intent(this, MainActivity.class);
						resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						NavigationDrawerFragment.mCurrentSelectedPosition = 1;
						// remove noti on statusbar
						CongViecCancelNotification();
				}
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
						Log.d("DungHV","show URL");
						Intent resultIntent = new Intent(this, MainActivity.class);
						resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					    resultIntent.setData(Uri.parse(data.getUrl()));
						NavigationDrawerFragment.mCurrentSelectedPosition = 1;
						// remove noti on statusbar
						LichBieuCancelNotification();
						}
					NotificationDBController db = NotificationDBController.getInstance(getApplicationContext());
					db.checkedNotification(data);
				}
			}
		}else if(LichBieuCount > 1){
			Log.d("DungHV","show activity");
			if (intent != null) {
				Bundle bundle = intent.getBundleExtra("data");
				if (bundle != null) {
				Intent resultIntent = new Intent(this, MainActivity.class);
				resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				NavigationDrawerFragment.mCurrentSelectedPosition = 1;
				LichBieuCancelNotification();
				}
			}
		}
	}
//		MyNotificationManager.resetCount();
//		CongVancancelNotification();
//		CongVieccancelNotification();
//		LichBieucancelNotification();
		stopSelf();
		return START_NOT_STICKY;
	}
	public void CongVanCancelNotification() {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(notificationServiceStr);
		mNotificationManager.cancel(MyNotificationManager.CONGVAN_NOTIFICATION_ID);
	}
	public void CongViecCancelNotification() {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(notificationServiceStr);
		mNotificationManager.cancel(MyNotificationManager.CONGVIEC_NOTIFICATION_ID);
	}
	public void LichBieuCancelNotification() {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(notificationServiceStr);
		mNotificationManager.cancel(MyNotificationManager.LICHBIEU_NOTIFICATION_ID);
	}

}
