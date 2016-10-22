package com.compass.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "ilogistics.db";
	private static final int DB_VERSION = 1;
	private final String ROUTING_INFO_TABLE = "ROUTING_INFORMATION";
	private Context mContext;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DB_VERSION);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		String createTableUserInfo = "CREATE TABLE IF NOT EXISTS "+ROUTING_INFO_TABLE +
				" (COMPANY VARCHAR,"+
					"INV_TYPE VARCHAR,"+
					"INV_BOOK VARCHAR,"+
					"INV_NO VARCHAR,"+
					"INV_BOOK_NO VARCHAR,"+
					"INV_SEQ VARCHAR,"+
					"DELIVER_STAFF VARCHAR,"+
					"CUST_CODE VARCHAR,"+
					"CUST_NAME VARCHAR,"+
					"PRODUCT_CODE VARCHAR,"+
					"PRODUCT_NAME VARCHAR,"+
					"UNIT VARCHAR,"+
					"WHSE VARCHAR,"+
					"QTY_ORDER VARCHAR,"+
					"QTY_PICKUP VARCHAR,"+
					"QTY_PENDING VARCHAR,"+
					"ROUTE_CODE VARCHAR,"+
					"DELIVERY_DATE VARCHAR,"+
					"CUST_SEQ VARCHAR,"+
					"EXPECTD_TIME VARCHAR);";

		database.execSQL(createTableUserInfo);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int arg1, int arg2) {
		String dropUserTable = "DROP TABLE IF EXISTS " + ROUTING_INFO_TABLE;
		database.execSQL(dropUserTable);
	}
	

	public String getDiviceId() throws Exception {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = null;
		String query = "SELECT * FROM " + ROUTING_INFO_TABLE; 
		try {
			cursor = db.rawQuery(query, null);
			if(cursor.getCount() > 0) {
				if (cursor.moveToFirst()) {
					return cursor.getString(cursor.getColumnIndex("COMPANY"));
					//return cursor.getString("DEVICE_ID").toString().trim();
				}
			}	
		}catch(Exception e) {
			e.printStackTrace();
			db.close();
			throw e;
		}finally {
			Log.i(Constants.LOGI,"finally");
			cursor.close();
			db.close();
		}
		return null;
	}
	

	public void insertUserInfo(String deviceID, String userEmail) throws Exception {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			ContentValues contentValues = new ContentValues();
			contentValues.put("USER_EMAIL", userEmail);
			contentValues.put("DEVICE_ID", deviceID);
			contentValues.put("OTP_STATUS", "false");
			contentValues.put("INFO_STATUS", "false");
			db.insert(ROUTING_INFO_TABLE, null, contentValues);
			db.close();
		}catch(Exception e) {
			e.printStackTrace();
			db.close();
			throw e;
		}finally {
			db.close();
		}
	}
	
	
	public void deleteUserInfo(String deviceID, String userEmail) throws Exception {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			long rows =  db.delete(ROUTING_INFO_TABLE, null, null);
			db.close();
		}catch (Exception e) {
			e.printStackTrace();
			db.close();
		}finally {
			db.close();
		}
	}
	
}