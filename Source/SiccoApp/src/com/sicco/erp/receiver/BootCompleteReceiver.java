package com.sicco.erp.receiver;

import com.sicco.erp.service.GetNotificationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, GetNotificationService.class);
		context.startService(service);
	}
}
