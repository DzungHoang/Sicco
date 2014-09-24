package com.sicco.erp;

import com.sicco.erp.service.GetNotificationService;

import android.app.Application;
import android.content.Intent;

public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Intent intent = new Intent(this, GetNotificationService.class);
		startService(intent);
	}
}
