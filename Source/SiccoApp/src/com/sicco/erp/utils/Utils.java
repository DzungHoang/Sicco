package com.sicco.erp.utils;

import com.sicco.erp.service.GetNotificationService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class Utils {
	public static void scheduleNext(Context context){
		Intent intent = new Intent(context, GetNotificationService.class);
		long time = SystemClock.elapsedRealtime();
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, time + 30 * 1000, PendingIntent.getService(context, 0, intent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
	}
}
