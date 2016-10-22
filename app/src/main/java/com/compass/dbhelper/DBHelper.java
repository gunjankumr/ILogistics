package com.compass.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DBHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;
 
    // Database Name
    private static final String DATABASE_NAME = "iLogistics.db";
 
    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
 
        String CREATE_TABLE_CHECK_IN_CHECK_OUT = "CREATE TABLE " + CheckInCheckOut.TABLE  + "("
                + CheckInCheckOut.KEY_USERNAME  + " TEXT,"
                + CheckInCheckOut.KEY_DEVICE_ID + " TEXT, "
                + CheckInCheckOut.KEY_COMPANY_CODE + " TEXT, "
                + CheckInCheckOut.KEY_CUSTOMER_CODE + " TEXT, "
                + CheckInCheckOut.KEY_BRANCH_CODE + " TEXT, "
                + CheckInCheckOut.KEY_ROUTE_CODE + " TEXT, "
                + CheckInCheckOut.KEY_DELIVERY_SEQ + " TEXT, "
                + CheckInCheckOut.KEY_DELIVERY_DATE + " TEXT, "
                + CheckInCheckOut.KEY_LATITUDE + " TEXT, "
                + CheckInCheckOut.KEY_LONGITUDE + " TEXT, "
                + CheckInCheckOut.KEY_CHECK_IN_TIME + " TEXT, "
                + CheckInCheckOut.KEY_CHECK_OUT_TIME + " TEXT )";

        String CREATE_TABLE_INVOICE_INFO = "CREATE TABLE " + InvoiceInfo.TABLE  + "("
                + InvoiceInfo.KEY_USERNAME  + " TEXT,"
                + InvoiceInfo.KEY_DEVICE_ID + " TEXT, "
                + InvoiceInfo.KEY_COMPANY + " TEXT, "
                + InvoiceInfo.KEY_ROUTE_CODE  + " TEXT,"
                + InvoiceInfo.KEY_CUSTOMER_CODE + " TEXT, "
                + InvoiceInfo.KEY_TRANSACTION_TYPE + " TEXT, "
                + InvoiceInfo.KEY_INVOICE_BOOK + " TEXT, "
                + InvoiceInfo.KEY_INVOICE_NUMBER + " TEXT, "
                + InvoiceInfo.KEY_PRODUCT_SEQ + " TEXT, "
                + InvoiceInfo.KEY_PRODUCT_CODE + " TEXT, "
                + InvoiceInfo.KEY_WARE_CODE + " TEXT, "
                + InvoiceInfo.KEY_WARE_ZONE + " TEXT, "
                + InvoiceInfo.KEY_QTY_RETURN + " TEXT, "
                + InvoiceInfo.KEY_UNIT + " TEXT, "
                + InvoiceInfo.KEY_COMPLAIN_CODE + " TEXT, "
                + InvoiceInfo.KEY_REMARK_ID + " TEXT, "
                + InvoiceInfo.KEY_REMARKS + " TEXT, "
                + InvoiceInfo.KEY_DELIVERY_DATE + " TEXT, "
                + InvoiceInfo.KEY_DELIVERY_SEQ + " TEXT, "
                + InvoiceInfo.KEY_CASH + " TEXT, "
                + InvoiceInfo.KEY_COMPLETED + " INTEGER )";
 
        db.execSQL(CREATE_TABLE_CHECK_IN_CHECK_OUT);
        db.execSQL(CREATE_TABLE_INVOICE_INFO);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + CheckInCheckOut.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + InvoiceInfo.TABLE);
 
        // Create tables again
        onCreate(db);
    }
}