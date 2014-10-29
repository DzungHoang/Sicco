package com.sicco.erp.manager;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.sicco.erp.MainActivity;
import com.sicco.erp.R;
import com.sicco.erp.fragment.NavigationDrawerFragment;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.service.GetNotificationService;
import com.sicco.erp.service.HandleNotificationService;

public class MyNotificationManager {
	static Context mContext;
	public static int CONGVAN_NOTIFICATION_ID = 1;
	public static int CONGVIEC_NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID + 1;
	public static int LICHBIEU_NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID + 1;
	public static int NOTIFICATION_ID = 0;
	public static int congVan_Count = 0;
	public static int congViec_Count = 0;
	public static int lichBieu_Count = 0;
	static String congvan = "congvan";
	static String congviec = "congviec";
	static String lichbieu = "lichbieu";
	static NotificationModel notificationModel;
	static String message;
	static String contentText;
	static String _ten="";
	static String congvan_name="";
	static String congviec_name="";
	static String lichbieu_name="";
	static RemoteViews notificationView = null;
	static String my_package = "com.sicco.erp";
	static NotificationCompat.Builder notificationBuilder = null;
	GetNotificationService getNotificationService;
	private static ArrayList<NotificationModel> notification_list;
	static String noti="";
	static int noti_count=0;
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
	// ============================================== \\
		
//		getNotificationType();
//		getNotificationCount();
//		getMessage();
//		Log.d("Count", "message:"+ message + "\n" +"notification_count_local:" + notification_count_local );
//		notify(context, message, notification_count_local);
//		test();
	// ============================================== \\
		
//		if(congVan_Count == 1){
////			message = String.format(context.getString(R.string.one_msg), data.getMsg());
//			congVan_message = "Ban co " +congVan_Count +"  cong van" + "\n" + GetNotificationService.ten + "\n" 
//					+ GetNotificationService.notification_type + "\n";
//		}else if(congVan_Count > 1){
////			message = String.format(context.getString(R.string.multi_msg), mCount);
//			congVan_message = "Ban co "+congVan_Count +" cong van" + "\r\n\r";
//		}
	}
	public static void notifyType(Context context,ArrayList<NotificationModel> arrayList,
			int notification_count,String notification_type, String ten, String noi_dung){
		
		congVan_Count = GetNotificationService.getCongVanCount();
		congViec_Count = GetNotificationService.getCongViecCount();
		lichBieu_Count = GetNotificationService.getLichBieuCount();
//		ten = notification_list.;
//		content = notificationModel.getContent();
//		Log.d("ToanNM", "ten:"+ ten +"content:"+content);
		if(notification_count==1){
			if(notification_type.equalsIgnoreCase(congvan)){
				NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
				noti = " cong van";
				noti_count = congVan_Count;
//				Log.d("ToanNM", "count:="+ noti_count +" " +notification_type);
			}
			if(notification_type.equalsIgnoreCase(congviec)){
				NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
				noti = " cong viec";
				noti_count = congViec_Count;
//				Log.d("ToanNM", "count:="+ noti_count +" " +notification_type);
			}
			if(notification_type.equalsIgnoreCase(lichbieu)){
				NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
				noti = " lich bieu";
				noti_count = lichBieu_Count;
//				Log.d("ToanNM", "count:="+ noti_count +" " +notification_type);
			}
			message = "Ban co " + noti_count + " " + noti + "\n" ;
			contentText = " " + noi_dung;
//			Log.d("ToanNM", "At notification_count==1,message: "+message);
			notify(context,NOTIFICATION_ID);
		}
		if(notification_count>1){
			if(notification_type.equalsIgnoreCase(congvan)){
				NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
				noti = " cong van";
				noti_count = congVan_Count;
				congvan_name += "\n" + ten + "\n";
				_ten += "\n" + ten + "\n";
//				Log.d("ToanNM", "count:="+ noti_count +" " +notification_type);
			}
			if(notification_type.equalsIgnoreCase(congviec)){
				NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
				noti = " cong viec";
				noti_count = congViec_Count;
				congviec_name += "\n" + ten + "\n";
				_ten += "\n" + ten + "\n";
//				Log.d("ToanNM", "count:="+ noti_count +" " +notification_type);
			}
			if(notification_type.equalsIgnoreCase(lichbieu)){
				NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
				noti = " lich bieu";
				noti_count = lichBieu_Count;
				lichbieu_name += "\n" + ten + "\n";
				_ten += "\n" + ten + "\n";
//				Log.d("ToanNM", "count:="+ noti_count +" " +notification_type);
			}
			message = "Ban co " + noti_count + " " + noti + " " + "\n" ;
			if(notification_type.equalsIgnoreCase(congvan)){
				contentText = congvan_name;
			}
			if(notification_type.equalsIgnoreCase(congviec)){
				contentText = congviec_name;
			}
			if(notification_type.equalsIgnoreCase(lichbieu)){
				contentText = lichbieu_name;
			}
//			contentText = _ten;
			Log.d("ToanNM", "name:---------- "+contentText+" ----------");
//			Log.d("ToanNM", "At notification_count>1,message: "+message);
			notify(context,NOTIFICATION_ID);
		}
//		Log.d("ToanNM", "message:"+message);
	}
	// ======================================================= \\
	public static void notify(Context context,int notification_id) {
		/**
		 * Notification use RemoteViews:
		 */
//			Intent notifyIntent = new Intent(context, MainActivity.class);
//			notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			PendingIntent pendingIntent = PendingIntent.getActivity
//					(context,
//					 0,
//					 notifyIntent,
//					 PendingIntent.FLAG_UPDATE_CURRENT);
//			notificationView = new RemoteViews(my_package, R.layout.notification_layout);
//			notificationView.setImageViewResource(R.id.notification_logo, R.drawable.ic_notification_logo);
//			notificationView.setTextViewText(R.id.notification_title, message);
//			notificationView.setTextViewText(R.id.notification_content, contentText);
//			notificationBuilder = new NotificationCompat.Builder(context);
//
//			notificationBuilder.setContentIntent(pendingIntent)
//			                   .setSmallIcon(R.drawable.ic_launcher)
////			                   .setTicker("Ban co" +  notification_count_local + "' cong van/cong viec/lich bieu '" + "put noi dung here" + "'")
////			                   .setOngoing(true)
//			                   .setContentTitle(message)
//			                   .setContentText(contentText)
////			                   .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
//			                   .setContent(notificationView);
////			notificationView.setOnClickPendingIntent(viewId, pendingIntent)
//			Notification notification = notificationBuilder.getNotification();
//			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//			notificationManager.notify(3, notification);
		/**
		 * normal Notification:
		 * @return 
		 */
		
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//			builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
			// ==============================
					Intent notIntent = new Intent(context, MainActivity.class);
				    notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    PendingIntent pendInt = PendingIntent.getActivity(context, 0,
				      notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				    NavigationDrawerFragment.mCurrentSelectedPosition = 1;
				    // ==============================
				    builder.setContentIntent(pendInt);
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setContentTitle(message);
			builder.setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));
			builder.setContentText(contentText);
			
//			builder.setContentInfo(noidung);
			
//			Intent resultIntent = new Intent();
//			resultIntent.setClass(context, HandleNotificationService.class);
//			resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					resultIntent.setAction(Intent.ACTION_VIEW);
//					resultIntent.setData(Uri.parse("http://google.com.vn"));
//					TaskStackBuilder stack = TaskStackBuilder.create(context);
//					stack.addParentStack(MainActivity.class);
//					stack.addNextIntent(resultIntent);
//			PendingIntent pendingIntent = PendingIntent.getService(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////					PendingIntent resultPendingIntent = stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//			builder.setContentIntent(pendingIntent);

			//mo rong
			long[] pattern = {(long)100, (long)100, (long) 100, (long) 100, (long) 100};
			builder.setVibrate(pattern);
			builder.setLights(0xFF0000FF, 500, 500);
//					builder.setAutoCancel(true);
			Notification notification = builder.getNotification();
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			
			NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Log.d("Count", "NOTIFICATION_ID:"+notification_id);
			manager.notify(notification_id, notification);
		
		}
//	}
//	public static int getNotificationType(){
//		//test getCount:
//			if(GetNotificationService.notification_Count_Type==1){
//				NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
//				notification_count_type_local = 1;
//				return notification_count_type_local;
//			}
//			if(GetNotificationService.notification_Count_Type==2){
//				NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
//				notification_count_type_local = 2;
//				return notification_count_type_local;
//			}
//			if(GetNotificationService.notification_Count_Type==3){
//				NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
//				notification_count_type_local = 3;
//				return notification_count_type_local;
//			}
//			Log.d("ToanNM", "notification_count_local:" +notification_count_type_local);
//			return notification_count_type_local;
//	}
//	public static int getNotificationCount(){
//		//test getCount:
//			for (int i = 0; i < GetNotificationService.notification_type_list.size(); i++) {
//				getNotificationType();
//				if(GetNotificationService.notification_Count_Type==1){
//					congVan_Count++;
//					notification_count_local = congVan_Count;
//					return notification_count_local;
//				}
//				if(GetNotificationService.notification_Count_Type==2){
//					congViec_Count++;
//					notification_count_local = congViec_Count;
//					return notification_count_type_local;
//				}
//				if(GetNotificationService.notification_Count_Type==3){
//					lichBieu_Count++;
//					notification_count_local = lichBieu_Count;
//					return notification_count_type_local;
//				}
//				Log.d("Count", "notification_count_local:" +notification_count_type_local);
//			}			
//			return notification_count_type_local;
//	}
//	public static String getMessage(){
//		if(GetNotificationService.notification_Count_Type==1){
//			getNotificationType();
//			if(notification_count_type_local == 1){
//				message = "Ban co" + congVan_Count + " cong van" + "\n" 
//						+ GetNotificationService.ten + "\n" 
//						+ GetNotificationService.notification_type + "\n"
//						+ GetNotificationService.content + "\n";
//			}else if(notification_count_type_local > 1){
//				message = "Ban co "+congVan_Count +" cong van" + "\r\n\r"
//						+ GetNotificationService.ten + "\n" ;
//			}
//			return message;
//		}
//		if(GetNotificationService.notification_Count_Type==2){
//			getNotificationType();
//			if(notification_count_type_local == 1){
//				message = "Ban co " +notification_count_type_local +"  cong viec" + "\n" 
//						+ GetNotificationService.ten + "\n" 
//						+ GetNotificationService.notification_type + "\n"
//						+ GetNotificationService.content + "\n";
//			}else if(notification_count_type_local > 1){
//				message = "Ban co "+notification_count_type_local +" cong viec" + "\r\n\r"
//						+ GetNotificationService.ten + "\n" ;
//			}
//			Log.d("ToanNM", "message:"+message);
//			return message;
//		}
//		if(GetNotificationService.notification_Count_Type==3){
//			getNotificationType();
//			if(notification_count_type_local == 1){
//				message = "Ban co " +notification_count_type_local +" lich bieu" + "\n" 
//						+ GetNotificationService.ten + "\n" 
//						+ GetNotificationService.notification_type + "\n"
//						+ GetNotificationService.content + "\n"
//						+ GetNotificationService.ten + "\n" ;
//			}else if(notification_count_type_local > 1){
//				message = "Ban co "+notification_count_type_local +" lich bieu" + "\r\n\r";
//			}
//			return message;
//		}
//		Log.d("ToanNM", "message:" +message);
//		return message;
//	}
//	public static void test(){
//		for (int i = 0; i < GetNotificationService.notification_type_list.size(); i++) {
//			if(GetNotificationService.notification_Count_Type==1){
//				NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
////				notification_count_type_local = 1;
//				congVan_Count++;
//				congVan_message = "Ban co" + congVan_Count + " cong van" + "\n" 
//						+ GetNotificationService.ten + "\n" 
//						+ GetNotificationService.notification_type + "\n"
//						+ GetNotificationService.content + "\n";
//			}
//			if(GetNotificationService.notification_Count_Type==2){
//				NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
////				notification_count_type_local = 2;
//				congViec_Count++;
//				congViec_message = "Ban co" + congViec_Count + " cong viec" + "\n" 
//						+ GetNotificationService.ten + "\n" 
//						+ GetNotificationService.notification_type + "\n"
//						+ GetNotificationService.content + "\n";
//			}
//			if(GetNotificationService.notification_Count_Type==3){
//				NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
////				notification_count_type_local = 3;
//				lichBieu_Count++;
//				lichBieu_message = "Ban co" + lichBieu_Count + " lich bieu" + "\n" 
//						+ GetNotificationService.ten + "\n" 
//						+ GetNotificationService.notification_type + "\n"
//						+ GetNotificationService.content + "\n";
//			}
//		Log.d("ToanNM", "test()" +"congVan_Count:"+congVan_Count+"\n"
//				+"congViec_Count:"+congViec_Count+"\n"
//				+"lichBieu_Count:"+lichBieu_Count+"\n"
//				+congVan_message+"\n"
//				+congViec_message+"\n"
//				+lichBieu_message+"\n");
//		}
//	}

}
