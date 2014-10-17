package com.sicco.erp.database;

import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.utils.Constant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotificationDBController extends SQLiteOpenHelper {

	private static String DB_NAME = "notification.db";
	private static int DB_VERSION = 1;
	private static String DB_PATH = "/data/data/com.sicco.erp/databases/";
	
	private SQLiteDatabase mDatabase;
	private static NotificationDBController sInstance;
	private Context mContext;
	public NotificationDBController(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mDatabase = getWritableDatabase();
		mContext = context;
	}
	public static NotificationDBController getInstance(Context context){
		if(sInstance == null) {
			sInstance = new NotificationDBController(context);
		}
		
		return sInstance;
	}

	
	public static String TABLE_NAME = "notification_tbl";
	public static String ID_COL = "_id";
	public static String USER_COL = "user";
	public static String NOTIFI_TYPE_COL = "notifi_type";
	public static String MSG_TYPE_COL = "msg_type";
	public static String URL_COL = "url";
	public static String STATE_COL = "state";
	
	private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "("
			+ ID_COL + " integer primary key autoincrement,"
			+ USER_COL + " text,"
			+ NOTIFI_TYPE_COL + " text,"
			+ MSG_TYPE_COL + " text,"
			+ URL_COL + " text,"
			+ STATE_COL + " text);";
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void openDB() throws SQLException{
		String myPath = DB_PATH+ DB_NAME;
		try {
			mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having, String orderBy){
		if (mDatabase == null) openDB();
		
		Cursor cursor = mDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return cursor;
	}
	public long insert(String table, String nullColumnHack, ContentValues values){
		if (mDatabase == null) openDB();
		
		SessionManager session = SessionManager.getInstance(mContext);
		String user = session.getUserDetails().get(SessionManager.KEY_NAME);
		values.put(NotificationDBController.USER_COL, user);
		
		long insertRet = mDatabase.insert(table, nullColumnHack, values);
		return insertRet;
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
		if( mDatabase == null) openDB();
		
		int ret = mDatabase.update(table, values, whereClause, whereArgs);
		
		return ret;
	}
	
	public void checkedNotification(NotificationModel item){
		ContentValues values = new ContentValues();
		values.put(STATE_COL, Constant.NOTIFICATION_STATE_CHECKED);
		SessionManager session = SessionManager.getInstance(mContext);
		String user = session.getUserDetails().get(SessionManager.KEY_NAME);


		String where = NotificationDBController.USER_COL
				+ "=? AND " + NotificationDBController.NOTIFI_TYPE_COL
				+ "=? AND " + NotificationDBController.MSG_TYPE_COL
				+ "=? AND " + NotificationDBController.URL_COL + "=?";
		String[] whereArgs = new String[] { user, item.getNotify(),
				item.getMsg(), item.getUrl()};
		update(TABLE_NAME, values, where, whereArgs);
	}
	
	public void deleteAllData()
	{
	    SQLiteDatabase sdb= this.getWritableDatabase();
	    sdb.delete(TABLE_NAME, null, null);

	}
}
