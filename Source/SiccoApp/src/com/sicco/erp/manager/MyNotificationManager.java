package com.sicco.erp.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.sicco.erp.MainActivity;
import com.sicco.erp.R;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.service.HandleNotificationService;

public class MyNotificationManager {
	
	static int mCount = 0;

	public static void resetCount(){
		mCount = 0;
	}
	public static int getCount(){
		return mCount;
	}
	
	public static void buildNormalNotification(Context context, NotificationModel data){
		mCount++;
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(context.getString(R.string.app_name));
		String message = "";
		if(mCount == 1){
			message = String.format(context.getString(R.string.one_msg), data.getMsg());
		}else if(mCount > 1){
			message = String.format(context.getString(R.string.multi_msg), mCount);
		}
		builder.setContentText(message);
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
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		PendingIntent resultPendingIntent = stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);

		//mo rong
		long[] pattern = {(long)100, (long)100, (long) 100, (long) 100, (long) 100};
		builder.setVibrate(pattern);
		builder.setLights(0xFF0000FF, 500, 500);
//		builder.setAutoCancel(true);
		Notification notification = builder.getNotification();
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(0, notification);
	}
}
