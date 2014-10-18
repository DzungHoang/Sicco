package com.sicco.erp.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.sicco.erp.MainActivity;
import com.sicco.erp.R;
import com.sicco.erp.fragment.NavigationDrawerFragment;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.service.GetNotificationService;
import com.sicco.erp.service.HandleNotificationService;

public class MyNotificationManager {
	static Context mContext;
	public static final int CongViecNOTIFICATION_ID = 1;
	public static final int CongVanNOTIFICATION_ID = 1;
	public static final int LichBieuNOTIFICATION_ID = 1;
	static int CongVanCount = 0;
	static int CongViecCount = 0;
	static int LichBieuCount = 0;
	static String congvan = "congvan";
	static String congviec = "congviec";
	static String lichbieu = "lichbieu";
	static NotificationModel notificationModel;
	static String CongViecmessage;
	static String CongVanmessage;
	static String LichBieumessage;
	public static void resetCount(){
		CongVanCount = 0;
		CongViecCount = 0;
		LichBieuCount = 0;
	}
	public static int getCongVanCount(){
		return CongVanCount;
	}
	public static int getCongViecCount(){
		return CongViecCount;
	}
	public static int getLichbieuCount(){
		return LichBieuCount;
	}
	
	public static void buildNormalNotification(Context context, NotificationModel data){
		//test getCount:
		if (
				GetNotificationService.notification_type.contains(congviec) 
//				|| 
//				notificationModel.getNotify().equalsIgnoreCase(congviec)
				)
			CongViecCount++;
		
		if (
				GetNotificationService.notification_type.contains(congvan)
//				||
//				notificationModel.getNotify().equalsIgnoreCase(congvan)
				)
			CongVanCount++;
		if (
				GetNotificationService.notification_type.contains(lichbieu)
//				|| 
//				notificationModel.getNotify().equalsIgnoreCase(lichbieu)
				)
			LichBieuCount++;
		// ============================================================= //
		
		
		
		if(CongVanCount == 1){
//			message = String.format(context.getString(R.string.one_msg), data.getMsg());
			CongVanmessage = "Ban co "+CongVanCount +"  Cong van";
		}else if(CongVanCount > 1){
//			message = String.format(context.getString(R.string.multi_msg), mCount);
			CongVanmessage = "Ban co "+CongVanCount +" Cong van";
		}
		//
		
		if(CongVanCount!=0){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		// ==============================
		Intent notIntent = new Intent(context, MainActivity.class);
	    notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    PendingIntent pendInt = PendingIntent.getActivity(context, 0,
	      notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    NavigationDrawerFragment.mCurrentSelectedPosition = 1;
	    // ==============================
	    builder.setContentIntent(pendInt);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(context.getString(R.string.app_name));
		builder.setContentText(CongVanmessage);
		builder.setContentInfo("");
		
		Intent resultIntent = new Intent();
		resultIntent.setClass(context, HandleNotificationService.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", data);
		resultIntent.putExtra("data", bundle);
//		resultIntent.setAction(Intent.ACTION_VIEW);
//		resultIntent.setData(Uri.parse("http://google.com.vn"));
//		TaskStackBuilder stack = TaskStackBuilder.create(context);
//		stack.addParentStack(MainActivity.class);
//		stack.addNextIntent(resultIntent);
//		PendingIntent pendingIntent = PendingIntent.getService(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////		PendingIntent resultPendingIntent = stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//		builder.setContentIntent(pendingIntent);

		//mo rong
		long[] pattern = {(long)100, (long)100, (long) 100, (long) 100, (long) 100};
		builder.setVibrate(pattern);
		builder.setLights(0xFF0000FF, 500, 500);
//		builder.setAutoCancel(true);
		Notification notification = builder.getNotification();
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(CongVanNOTIFICATION_ID, notification);
		}
		
		//Congviec:
		if(CongViecCount==1){
//			message = String.format(context.getString(R.string.one_msg), data.getMsg());
			CongViecmessage = "Ban co "+CongViecCount +" Cong viec";
		}else if(CongViecCount > 1){
//			message = String.format(context.getString(R.string.multi_msg), mCount);
			CongViecmessage = "Ban co "+CongViecCount +" Cong viec";
		}
		//
		if(CongViecCount!=0){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		// ==============================
				Intent notIntent = new Intent(context, MainActivity.class);
			    notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    PendingIntent pendInt = PendingIntent.getActivity(context, 0,
			      notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			    NavigationDrawerFragment.mCurrentSelectedPosition = 1;
			    // ==============================
			    builder.setContentIntent(pendInt);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(context.getString(R.string.app_name));
		builder.setContentText(CongViecmessage);
		builder.setContentInfo("");
		
		Intent resultIntent = new Intent();
		resultIntent.setClass(context, HandleNotificationService.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", data);
		resultIntent.putExtra("data", bundle);
//		resultIntent.setAction(Intent.ACTION_VIEW);
//		resultIntent.setData(Uri.parse("http://google.com.vn"));
//		TaskStackBuilder stack = TaskStackBuilder.create(context);
//		stack.addParentStack(MainActivity.class);
//		stack.addNextIntent(resultIntent);
//		PendingIntent pendingIntent = PendingIntent.getService(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////		PendingIntent resultPendingIntent = stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//		builder.setContentIntent(pendingIntent);

		//mo rong
		long[] pattern = {(long)100, (long)100, (long) 100, (long) 100, (long) 100};
		builder.setVibrate(pattern);
		builder.setLights(0xFF0000FF, 500, 500);
//		builder.setAutoCancel(true);
		Notification notification = builder.getNotification();
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(CongViecNOTIFICATION_ID, notification);
		}
		
		//lichbieu:
		if(LichBieuCount == 1){
//					message = String.format(context.getString(R.string.one_msg), data.getMsg());
			LichBieumessage = "Ban co "+LichBieuCount +" Lich Bieu";
		}else if(LichBieuCount > 1){
//					message = String.format(context.getString(R.string.multi_msg), mCount);
			LichBieumessage = "Ban co "+LichBieuCount +" Lich Bieu";
		}
		//
		if(LichBieuCount!=0){
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		// ==============================
				Intent notIntent = new Intent(context, MainActivity.class);
			    notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    PendingIntent pendInt = PendingIntent.getActivity(context, 0,
			      notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			    NavigationDrawerFragment.mCurrentSelectedPosition = 1;
			    // ==============================
			    builder.setContentIntent(pendInt);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(context.getString(R.string.app_name));
		builder.setContentText(LichBieumessage);
		builder.setContentInfo("");
		
		Intent resultIntent = new Intent();
		resultIntent.setClass(context, HandleNotificationService.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", data);
		resultIntent.putExtra("data", bundle);
//				resultIntent.setAction(Intent.ACTION_VIEW);
//				resultIntent.setData(Uri.parse("http://google.com.vn"));
//				TaskStackBuilder stack = TaskStackBuilder.create(context);
//				stack.addParentStack(MainActivity.class);
//				stack.addNextIntent(resultIntent);
//		PendingIntent pendingIntent = PendingIntent.getService(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////				PendingIntent resultPendingIntent = stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//		builder.setContentIntent(pendingIntent);

		//mo rong
		long[] pattern = {(long)100, (long)100, (long) 100, (long) 100, (long) 100};
		builder.setVibrate(pattern);
		builder.setLights(0xFF0000FF, 500, 500);
//				builder.setAutoCancel(true);
		Notification notification = builder.getNotification();
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(LichBieuNOTIFICATION_ID, notification);
		}
	}
}
