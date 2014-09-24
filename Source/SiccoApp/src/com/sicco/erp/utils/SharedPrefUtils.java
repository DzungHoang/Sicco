package com.sicco.erp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefUtils {
	//Ten file
	public static final String PREF_FILE_NAME = "SharedPrefTutorial";
	//Ten key
	public static final String KEY_CB_REMEMBER = "remember";
	
	public static void savePref(Context context, String keyName, Boolean value) {
		SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		
		Editor editor = pref.edit();
		editor.putBoolean(keyName, value);
		
		editor.commit();
	}
	public static void savePref(Context context, String keyName, String value) {
		SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		
		Editor editor = pref.edit();
		editor.putString(keyName, value);
		
		editor.commit();
	}
	public static boolean getPref(Context context, String key, Boolean defaultValue) {
		boolean ret;
		SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		ret = pref.getBoolean(key, defaultValue);
		return ret;
	}
}
