package com.compass.dbhelper;

import com.google.common.collect.Lists;

import com.compass.activity.CheckInOutType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class CheckInCheckOutRepo {
    private DBHelper dbHelper;

    public CheckInCheckOutRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(CheckInCheckOut record) {
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CheckInCheckOut.KEY_USERNAME, record.username);
        values.put(CheckInCheckOut.KEY_DEVICE_ID,record.deviceId);
        values.put(CheckInCheckOut.KEY_COMPANY_CODE, record.companyCode);
        values.put(CheckInCheckOut.KEY_CUSTOMER_CODE, record.customerCode);
        values.put(CheckInCheckOut.KEY_BRANCH_CODE, record.branchCode);
        values.put(CheckInCheckOut.KEY_ROUTE_CODE, record.routeCode);
        values.put(CheckInCheckOut.KEY_DELIVERY_SEQ, record.deliverySequence);
        values.put(CheckInCheckOut.KEY_DELIVERY_DATE, record.deliveryDate);
        values.put(CheckInCheckOut.KEY_LATITUDE, record.latitude);
        values.put(CheckInCheckOut.KEY_LONGITUDE, record.longitude);
        values.put(CheckInCheckOut.KEY_CHECK_IN_TIME, record.checkInTime);
        values.put(CheckInCheckOut.KEY_CHECK_OUT_TIME, record.checkOutTime);

        // Inserting Row
        long recId = db.insert(CheckInCheckOut.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) recId;
    }

    public void delete(CheckInCheckOut record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(CheckInCheckOut.TABLE,
                CheckInCheckOut.KEY_COMPANY_CODE + "= ? AND "+
                CheckInCheckOut.KEY_CUSTOMER_CODE + "= ? AND "+
                CheckInCheckOut.KEY_BRANCH_CODE + "= ? AND "+
                CheckInCheckOut.KEY_ROUTE_CODE + "= ? AND "+
                CheckInCheckOut.KEY_DELIVERY_SEQ + "= ? AND "+
                CheckInCheckOut.KEY_DELIVERY_DATE + "= ? ",
                new String[] {
                        String.valueOf(record.companyCode),
                        String.valueOf(record.customerCode),
                        String.valueOf(record.branchCode),
                        String.valueOf(record.routeCode),
                        String.valueOf(record.deliverySequence),
                        String.valueOf(record.deliveryDate)
                });
        db.close(); // Closing database connection
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CheckInCheckOut.TABLE, "", null);
        db.close(); // Closing database connection
    }

    public void update(CheckInCheckOut record, CheckInOutType checkInOutType) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (checkInOutType == CheckInOutType.CHECK_IN) {
            values.put(CheckInCheckOut.KEY_CHECK_IN_TIME, record.checkInTime);
        } else if (checkInOutType == CheckInOutType.CHECK_OUT) {
            values.put(CheckInCheckOut.KEY_CHECK_OUT_TIME, record.checkOutTime);
        }
        values.put(CheckInCheckOut.KEY_LATITUDE, record.latitude);
        values.put(CheckInCheckOut.KEY_LONGITUDE, record.longitude);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(CheckInCheckOut.TABLE,
                values,
                CheckInCheckOut.KEY_COMPANY_CODE + "= ? AND "+
                CheckInCheckOut.KEY_CUSTOMER_CODE + "= ? AND "+
                CheckInCheckOut.KEY_BRANCH_CODE + "= ? AND "+
                CheckInCheckOut.KEY_ROUTE_CODE + "= ? AND "+
                CheckInCheckOut.KEY_DELIVERY_SEQ + "= ? AND "+
                CheckInCheckOut.KEY_DELIVERY_DATE + "= ? ",
                new String[] {
                        String.valueOf(record.companyCode),
                        String.valueOf(record.customerCode),
                        String.valueOf(record.branchCode),
                        String.valueOf(record.routeCode),
                        String.valueOf(record.deliverySequence),
                        String.valueOf(record.deliveryDate)
                });
        db.close(); // Closing database connection
    }

    public boolean isRecordAvailable(CheckInCheckOut record) {
        if (record == null) return false;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                              CheckInCheckOut.KEY_COMPANY_CODE + "," +
                              CheckInCheckOut.KEY_CUSTOMER_CODE + "," +
                              CheckInCheckOut.KEY_BRANCH_CODE + "," +
                              CheckInCheckOut.KEY_ROUTE_CODE + "," +
                              CheckInCheckOut.KEY_DELIVERY_SEQ + "," +
                              CheckInCheckOut.KEY_DELIVERY_DATE +
                              " FROM " + CheckInCheckOut.TABLE
                              + " WHERE " +
                              CheckInCheckOut.KEY_COMPANY_CODE + "= ? AND "+
                              CheckInCheckOut.KEY_CUSTOMER_CODE + "= ? AND "+
                              CheckInCheckOut.KEY_BRANCH_CODE + "= ? AND "+
                              CheckInCheckOut.KEY_ROUTE_CODE + "= ? AND "+
                              CheckInCheckOut.KEY_DELIVERY_SEQ + "= ? AND "+
                              CheckInCheckOut.KEY_DELIVERY_DATE + "= ? ";

        CheckInCheckOut dbRecord = new CheckInCheckOut();

        Cursor cursor = db.rawQuery(selectQuery,
                new String[] {
                        String.valueOf(record.companyCode),
                        String.valueOf(record.customerCode),
                        String.valueOf(record.branchCode),
                        String.valueOf(record.routeCode),
                        String.valueOf(record.deliverySequence),
                        String.valueOf(record.deliveryDate)
                        });

        if (cursor.moveToFirst()) {
            do {
                dbRecord.companyCode = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_COMPANY_CODE));
                dbRecord.customerCode = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_CUSTOMER_CODE));
                dbRecord.branchCode = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_BRANCH_CODE));
                dbRecord.routeCode = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_ROUTE_CODE));
                dbRecord.deliverySequence = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_DELIVERY_SEQ));
                dbRecord.deliveryDate = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_DELIVERY_DATE));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return record.companyCode.equals(dbRecord.companyCode)
            && record.customerCode.equals(dbRecord.customerCode)
            && record.customerCode.equals(dbRecord.customerCode)
            && record.customerCode.equals(dbRecord.customerCode)
            && record.customerCode.equals(dbRecord.customerCode)
            && record.customerCode.equals(dbRecord.customerCode);
    }

    public List<CheckInCheckOut> getCheckInCheckOutList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT " +
                              CheckInCheckOut.KEY_USERNAME + "," +
                              CheckInCheckOut.KEY_DEVICE_ID + "," +
                              CheckInCheckOut.KEY_COMPANY_CODE + "," +
                              CheckInCheckOut.KEY_CUSTOMER_CODE + "," +
                              CheckInCheckOut.KEY_BRANCH_CODE + "," +
                              CheckInCheckOut.KEY_ROUTE_CODE + "," +
                              CheckInCheckOut.KEY_DELIVERY_SEQ + "," +
                              CheckInCheckOut.KEY_DELIVERY_DATE + "," +
                              CheckInCheckOut.KEY_LATITUDE + "," +
                              CheckInCheckOut.KEY_LONGITUDE + "," +
                              CheckInCheckOut.KEY_CHECK_IN_TIME + "," +
                              CheckInCheckOut.KEY_CHECK_OUT_TIME +
                              " FROM " + CheckInCheckOut.TABLE;

        List<CheckInCheckOut> checkInCheckOutList = Lists.newArrayList();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                CheckInCheckOut dbRecord = new CheckInCheckOut();
                dbRecord.username = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_USERNAME));
                dbRecord.deviceId = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_DEVICE_ID));
                dbRecord.companyCode = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_COMPANY_CODE));
                dbRecord.customerCode = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_CUSTOMER_CODE));
                dbRecord.branchCode = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_BRANCH_CODE));
                dbRecord.routeCode = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_ROUTE_CODE));
                dbRecord.deliverySequence = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_DELIVERY_SEQ));
                dbRecord.deliveryDate = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_DELIVERY_DATE));
                dbRecord.latitude = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_LATITUDE));
                dbRecord.longitude = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_LONGITUDE));
                dbRecord.checkInTime = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_CHECK_IN_TIME));
                dbRecord.checkOutTime = cursor.getString(cursor.getColumnIndex(CheckInCheckOut.KEY_CHECK_OUT_TIME));

                checkInCheckOutList.add(dbRecord);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return checkInCheckOutList;
    }
}
