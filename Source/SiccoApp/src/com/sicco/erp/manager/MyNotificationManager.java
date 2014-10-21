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
	public static final int CONGVIEC_NOTIFICATION_ID = 1;
	public static final int CONGVAN_NOTIFICATION_ID = 2;
	public static final int LICHBIEU_NOTIFICATION_ID = 3;
	public static int congVan_Count = 0;
	public static int congViec_Count = 0;
	public static int lichBieu_Count = 0;
	static String congvan = "congvan";
	static String congviec = "congviec";
	static String lichbieu = "lichbieu";
	static NotificationModel notificationModel;
	static String congViec_message;
	static String congVan_message;
	static String lichBieu_message;
	public static void resetCount(){
		congVan_Count = 0;
		congViec_Count = 0;
		lichBieu_Count = 0;
	}
	public static int getcongVan_Count(){
		return congVan_Count;
	}
	public static int getcongViec_Count(){
		return congViec_Count;
	}
	public static int getlichBieu_Count(){
		return lichBieu_Count;
	}
	
	public static void buildNormalNotification(Context context, NotificationModel data){
		//test getCount:
		if(GetNotificationService.notification_Count==1){
			
		}
		
		if(congVan_Count == 1){
//			message = String.format(context.getString(R.string.one_msg), data.getMsg());
			congVan_message = "Ban co "+congVan_Count +"  Cong van";
		}else if(congVan_Count > 1){
//			message = String.format(context.getString(R.string.multi_msg), mCount);
			congVan_message = "Ban co "+congVan_Count +" Cong van";
		}
		//
		
		if(congVan_Count!=0){
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
		builder.setContentTitle(context.getString(R.string.new_noti));
		builder.setContentText(congVan_message);
		builder.setContentInfo(GetNotificationService.ten);
		
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
		manager.notify(CONGVAN_NOTIFICATION_ID, notification);
		
		}
		
		//Congviec:
		if(congViec_Count==1){
//			message = String.format(context.getString(R.string.one_msg), data.getMsg());
			congViec_message = "Ban co "+congViec_Count +" Cong viec";
		}else if(congViec_Count > 1){
//			message = String.format(context.getString(R.string.multi_msg), mCount);
			congViec_message = "Ban co "+congViec_Count +" Cong viec";
		}
		//
		if(congViec_Count!=0){
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
		builder.setContentTitle(context.getString(R.string.new_noti));
		builder.setContentText(congViec_message);
		builder.setContentInfo(GetNotificationService.ten);
		
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
		manager.notify(CONGVIEC_NOTIFICATION_ID, notification);
		}
		
		//lichbieu:
		if(lichBieu_Count == 1){
//					message = String.format(context.getString(R.string.one_msg), data.getMsg());
//			lichBieu_message = R.string.new_noti_mess + lichBieu_Count + "" + R.string.lichbieu;
			lichBieu_message = "Ban co "+lichBieu_Count +" Lich bieu";
		}else if(lichBieu_Count > 1){
//					message = String.format(context.getString(R.string.multi_msg), mCount);
//			lichBieu_message = R.string.new_noti_mess + lichBieu_Count + "" + R.string.lichbieu;
			lichBieu_message = "Ban co "+lichBieu_Count +" Lich bieu";
		}
		//
		if(lichBieu_Count!=0){
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
		builder.setContentTitle(context.getString(R.string.new_noti));
		builder.setContentText(lichBieu_message);
		builder.setContentInfo(GetNotificationService.ten);
		
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
		manager.notify(LICHBIEU_NOTIFICATION_ID, notification);
		}
	}
}
