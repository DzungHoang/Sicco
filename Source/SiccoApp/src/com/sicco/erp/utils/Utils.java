package com.sicco.erp.utils;

import com.sicco.erp.service.GetNotificationService;
import com.sicco.erp.service.HandleNotificationService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
/**
 * TimeAlarm for starting GetNotificationService
 */
public class Utils {
	public static void scheduleNext(Context context){
		Intent intent = new Intent(context, GetNotificationService.class);
		long time = SystemClock.elapsedRealtime();
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time + 10 * 1000,
				PendingIntent.getService(context, 0, intent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
		
		Intent notificationOnStatusBar = new Intent(context, HandleNotificationService.class);
		long notificationOnStatusBarTime = SystemClock.elapsedRealtime();
		AlarmManager notificationOnStatusBarManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		notificationOnStatusBarManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, notificationOnStatusBarTime + 10 * 1000,
				PendingIntent.getService(context, 0, notificationOnStatusBar, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
	}
}
