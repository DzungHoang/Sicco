package com.sicco.erp.manager;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.util.Log;
import android.widget.RemoteViews;

import com.sicco.erp.MainActivity;
import com.sicco.erp.R;
import com.sicco.erp.fragment.NavigationDrawerFragment;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.service.GetNotificationService;
import com.sicco.erp.service.HandleNotificationService;

public class MyNotificationManager {
	Context mContext;
	public static int CONGVAN_NOTIFICATION_ID = 1;
	public static int CONGVIEC_NOTIFICATION_ID = 2;
	public static int LICHBIEU_NOTIFICATION_ID = 3;
	public static int NOTIFICATION_ID = 0;
	static String congvan = "congvan";
	static String congviec = "congviec";
	static String lichbieu = "lichbieu";
	NotificationModel notificationModel;
	String message="";
	String contentText="";
	String name="";
	String content="";
	String url="";
	RemoteViews notificationView = null;
	String my_package = "com.sicco.erp";
	NotificationCompat.Builder notificationBuilder = null;
	private GetNotificationService getNotificationService = new GetNotificationService();
	String noti="";
	int noti_count=0;
	PendingIntent pendInt;
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
	public void notifyType(Context context,ArrayList<NotificationModel> arrayList){
		int notification_count = arrayList.size();
		noti_count = notification_count;
		// ==================================== \\
		/**
		 * Test other way to display Notification:
		 */
		for(int i = 0; i < notification_count; i++){
			// get data
			String notification_type = arrayList.get(i).getNotify();
			String ten = arrayList.get(i).getName();
			String noi_dung = arrayList.get(i).getContent();
			String data = arrayList.get(i).getUrl();
			
			if(notification_count==1){
				if(notification_type.equalsIgnoreCase(congvan)){
					NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
					noti = " cong van";
				}
				if(notification_type.equalsIgnoreCase(congviec)){
					NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
					noti = " cong viec";
				}
				if(notification_type.equalsIgnoreCase(lichbieu)){
					NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID; 
					noti = " lich bieu";
				}
				message = "Ban co " + notification_count + " " + noti + "\n" ;
				contentText = noi_dung;
				url = data;
				notify(context, NOTIFICATION_ID);
//				Log.d("ToanNM", "Message:"+message +"\n" + "contentText:"+contentText+"\n"+"NOTIFICATION_ID"+NOTIFICATION_ID
//						+"-----------------------------------------------------------");
				} 
		
			if(notification_count>1){
				if(notification_type.equalsIgnoreCase(congvan)){
					NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
					noti = " cong van";
					name += "" + ten + "\n";
				}
				if(notification_type.equalsIgnoreCase(congviec)){
					NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
					noti = " cong viec";
					name += "" + ten + "\n";
				}
				if(notification_type.equalsIgnoreCase(lichbieu)){
					NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
					noti = " lich bieu";
					name +=  "" + ten + "\n";
				}
				message = "Ban co " + notification_count + " " + noti + " " + "\n" ;
				contentText = name;
				url = data;
				notify(context, NOTIFICATION_ID);
//				Log.d("ToanNM", "Message:"+message +"\n" + "contentText:"+contentText+"\n"+"NOTIFICATION_ID"+NOTIFICATION_ID
//						+"-----------------------------------------------------------");
				}
		}
	}
	public int getNotiCount(){
		return noti_count;
	}
	public String getData(){
		return url;
	}
	public int getNotificationID(){
		return NOTIFICATION_ID;
	}
	// ========================================================================== \\
	public void notify(Context context, int notification_id) {
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
		 */
		
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			HandleNotificationService handleNotificationService = new HandleNotificationService();
			// ==============================
			if(noti_count>1){
				Intent notIntent = new Intent(context, MainActivity.class);
			    notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    pendInt = PendingIntent.getActivity(context, 0,
			      notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//			    handleNotificationService.cancelNotification(context,NOTIFICATION_ID);
			}
			if(noti_count==1){
				Intent notIntent = new Intent();
				notIntent.setData(Uri.parse(url));
			    notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    pendInt = PendingIntent.getActivity(context, 0,
			      notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//			    handleNotificationService.cancelNotification(context,NOTIFICATION_ID);
			}   
			builder.setContentIntent(pendInt);  
		    NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
		    style.bigText(contentText);
		    style.setSummaryText("Swipe Left or Right to dismiss this Notification.");
		    style.build();
		    // ==============================
				    
			builder.setSmallIcon(R.drawable.ic_notification_logo);
			builder.setContentTitle(message);
			builder.setContentText(contentText);
			builder.setStyle(style);
			
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
//			Log.d("Count", "NOTIFICATION_ID:"+notification_id);
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
