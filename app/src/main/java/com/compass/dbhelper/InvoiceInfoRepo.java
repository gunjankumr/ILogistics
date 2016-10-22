package com.compass.dbhelper;


import com.google.common.collect.Lists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class InvoiceInfoRepo {
    private DBHelper dbHelper;

    public InvoiceInfoRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(InvoiceInfo record) {
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InvoiceInfo.KEY_ROUTE_CODE, record.routeCode);
        values.put(InvoiceInfo.KEY_CUSTOMER_CODE,record.customerCode);
        values.put(InvoiceInfo.KEY_TRANSACTION_TYPE, record.transactionType);
        values.put(InvoiceInfo.KEY_INVOICE_BOOK, record.invoiceBook);
        values.put(InvoiceInfo.KEY_INVOICE_NUMBER, record.invoiceNumber);
        values.put(InvoiceInfo.KEY_PRODUCT_SEQ, record.productSeq);
        values.put(InvoiceInfo.KEY_PRODUCT_CODE, record.productCode);
        values.put(InvoiceInfo.KEY_WARE_CODE, record.wareCode);
        values.put(InvoiceInfo.KEY_WARE_ZONE, record.wareZone);
        values.put(InvoiceInfo.KEY_QTY_RETURN, record.qtyReturn);
        values.put(InvoiceInfo.KEY_UNIT, record.unit);
        values.put(InvoiceInfo.KEY_COMPLAIN_CODE, record.complainCode);
        values.put(InvoiceInfo.KEY_REMARK_ID, record.remarkId);
        values.put(InvoiceInfo.KEY_REMARKS, record.remarks);
        values.put(InvoiceInfo.KEY_DELIVERY_DATE, record.deliveryDate);
        values.put(InvoiceInfo.KEY_DELIVERY_SEQ, record.deliverySeq);
        values.put(InvoiceInfo.KEY_CASH, record.cash);
        values.put(InvoiceInfo.KEY_COMPLETED, record.isCompleted ? 1 : 0);

        // Inserting Row
        long recId = db.insert(InvoiceInfo.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) recId;
    }

    public void delete(InvoiceInfo record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(InvoiceInfo.TABLE,
                InvoiceInfo.KEY_DELIVERY_DATE + "= ? AND "+
                InvoiceInfo.KEY_DELIVERY_SEQ + "= ? AND "+
                InvoiceInfo.KEY_ROUTE_CODE + "= ? AND "+
                InvoiceInfo.KEY_CUSTOMER_CODE + "= ? AND "+
                InvoiceInfo.KEY_INVOICE_BOOK + "= ? AND "+
                InvoiceInfo.KEY_INVOICE_NUMBER + "= ? AND "+
                InvoiceInfo.KEY_PRODUCT_CODE + "= ? AND "+
                InvoiceInfo.KEY_PRODUCT_SEQ + "= ? AND "+
                InvoiceInfo.KEY_WARE_CODE + "= ? ",
                new String[] {
                        String.valueOf(record.deliveryDate),
                        String.valueOf(record.deliverySeq),
                        String.valueOf(record.routeCode),
                        String.valueOf(record.customerCode),
                        String.valueOf(record.invoiceBook),
                        String.valueOf(record.invoiceNumber),
                        String.valueOf(record.productCode),
                        String.valueOf(record.productSeq),
                        String.valueOf(record.wareCode)
                });
        db.close(); // Closing database connection
    }

    public void update(InvoiceInfo record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(InvoiceInfo.KEY_CASH, record.cash);
        values.put(InvoiceInfo.KEY_COMPLETED, record.isCompleted ? 1 : 0);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(InvoiceInfo.TABLE,
                values,
                InvoiceInfo.KEY_DELIVERY_DATE + "= ? AND "+
                InvoiceInfo.KEY_DELIVERY_SEQ + "= ? AND "+
                InvoiceInfo.KEY_ROUTE_CODE + "= ? AND "+
                InvoiceInfo.KEY_CUSTOMER_CODE + "= ? AND "+
                InvoiceInfo.KEY_INVOICE_BOOK + "= ? AND "+
                InvoiceInfo.KEY_INVOICE_NUMBER + "= ? AND "+
                InvoiceInfo.KEY_PRODUCT_CODE + "= ? AND "+
                InvoiceInfo.KEY_PRODUCT_SEQ + "= ? AND "+
                InvoiceInfo.KEY_WARE_CODE + "= ? ",
                new String[] {
                        String.valueOf(record.deliveryDate),
                        String.valueOf(record.deliverySeq),
                        String.valueOf(record.routeCode),
                        String.valueOf(record.customerCode),
                        String.valueOf(record.invoiceBook),
                        String.valueOf(record.invoiceNumber),
                        String.valueOf(record.productCode),
                        String.valueOf(record.productSeq),
                        String.valueOf(record.wareCode)
                });
        db.close(); // Closing database connection
    }

    public boolean isRecordAvailable(InvoiceInfo record) {
        if (record == null) return false;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                              InvoiceInfo.KEY_DELIVERY_DATE + "," +
                              InvoiceInfo.KEY_DELIVERY_SEQ + "," +
                              InvoiceInfo.KEY_ROUTE_CODE + "," +
                              InvoiceInfo.KEY_CUSTOMER_CODE + "," +
                              InvoiceInfo.KEY_INVOICE_BOOK + "," +
                              InvoiceInfo.KEY_INVOICE_NUMBER + "," +
                              InvoiceInfo.KEY_PRODUCT_CODE + "," +
                              InvoiceInfo.KEY_PRODUCT_SEQ + "," +
                              InvoiceInfo.KEY_WARE_CODE +
                              " FROM " + InvoiceInfo.TABLE
                              + " WHERE " +
                              InvoiceInfo.KEY_DELIVERY_DATE + "= ? AND "+
                              InvoiceInfo.KEY_DELIVERY_SEQ + "= ? AND "+
                              InvoiceInfo.KEY_ROUTE_CODE + "= ? AND "+
                              InvoiceInfo.KEY_CUSTOMER_CODE + "= ? AND "+
                              InvoiceInfo.KEY_INVOICE_BOOK + "= ? AND "+
                              InvoiceInfo.KEY_INVOICE_NUMBER + "= ? AND "+
                              InvoiceInfo.KEY_PRODUCT_CODE + "= ? AND "+
                              InvoiceInfo.KEY_PRODUCT_SEQ + "= ? AND "+
                              InvoiceInfo.KEY_WARE_CODE + "= ? ";

        InvoiceInfo dbRecord = new InvoiceInfo();

        Cursor cursor = db.rawQuery(selectQuery,
                new String[] {
                        String.valueOf(record.deliveryDate),
                        String.valueOf(record.deliverySeq),
                        String.valueOf(record.routeCode),
                        String.valueOf(record.customerCode),
                        String.valueOf(record.invoiceBook),
                        String.valueOf(record.invoiceNumber),
                        String.valueOf(record.productCode),
                        String.valueOf(record.productSeq),
                        String.valueOf(record.wareCode)
                });

        if (cursor.moveToFirst()) {
            do {
                dbRecord.deliveryDate = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_DELIVERY_DATE));
                dbRecord.deliverySeq = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_DELIVERY_SEQ));
                dbRecord.routeCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_ROUTE_CODE));
                dbRecord.customerCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_CUSTOMER_CODE));
                dbRecord.invoiceBook = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_INVOICE_BOOK));
                dbRecord.invoiceNumber = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_INVOICE_NUMBER));
                dbRecord.productCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_PRODUCT_CODE));
                dbRecord.productSeq = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_PRODUCT_SEQ));
                dbRecord.wareCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_WARE_CODE));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return record.deliveryDate.equals(dbRecord.deliveryDate)
               && record.deliverySeq.equals(dbRecord.deliverySeq)
               && record.routeCode.equals(dbRecord.routeCode)
               && record.customerCode.equals(dbRecord.customerCode)
               && record.invoiceBook.equals(dbRecord.invoiceBook)
               && record.invoiceNumber.equals(dbRecord.invoiceNumber)
               && record.productCode.equals(dbRecord.productCode)
               && record.productSeq.equals(dbRecord.productSeq)
               && record.wareCode.equals(dbRecord.wareCode);
    }

    public List<InvoiceInfo> getInvoiceList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT " +
                              InvoiceInfo.KEY_ROUTE_CODE + "," +
                              InvoiceInfo.KEY_CUSTOMER_CODE + "," +
                              InvoiceInfo.KEY_TRANSACTION_TYPE + "," +
                              InvoiceInfo.KEY_INVOICE_BOOK + "," +
                              InvoiceInfo.KEY_INVOICE_NUMBER + "," +
                              InvoiceInfo.KEY_PRODUCT_SEQ + "," +
                              InvoiceInfo.KEY_PRODUCT_CODE + "," +
                              InvoiceInfo.KEY_WARE_CODE + "," +
                              InvoiceInfo.KEY_WARE_ZONE + "," +
                              InvoiceInfo.KEY_QTY_RETURN + "," +
                              InvoiceInfo.KEY_UNIT + "," +
                              InvoiceInfo.KEY_COMPLAIN_CODE + "," +
                              InvoiceInfo.KEY_REMARK_ID + "," +
                              InvoiceInfo.KEY_REMARKS + "," +
                              InvoiceInfo.KEY_DELIVERY_DATE + "," +
                              InvoiceInfo.KEY_DELIVERY_SEQ + "," +
                              InvoiceInfo.KEY_CASH + "," +
                              InvoiceInfo.KEY_COMPLETED +
                              " FROM " + InvoiceInfo.TABLE;

        List<InvoiceInfo> invoiceList = Lists.newArrayList();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                InvoiceInfo dbRecord = new InvoiceInfo();
                dbRecord.routeCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_ROUTE_CODE));
                dbRecord.customerCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_CUSTOMER_CODE));
                dbRecord.transactionType = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_TRANSACTION_TYPE));
                dbRecord.invoiceBook = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_INVOICE_BOOK));
                dbRecord.invoiceNumber = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_INVOICE_NUMBER));
                dbRecord.productSeq = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_PRODUCT_SEQ));
                dbRecord.productCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_PRODUCT_CODE));
                dbRecord.wareCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_WARE_CODE));
                dbRecord.wareZone = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_WARE_ZONE));
                dbRecord.qtyReturn = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_QTY_RETURN));
                dbRecord.unit = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_UNIT));
                dbRecord.complainCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_COMPLAIN_CODE));
                dbRecord.remarkId = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_REMARK_ID));
                dbRecord.remarks = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_REMARKS));
                dbRecord.deliveryDate = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_DELIVERY_DATE));
                dbRecord.deliverySeq = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_DELIVERY_SEQ));
                dbRecord.cash = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_CASH));
                dbRecord.isCompleted = cursor.getInt(cursor.getColumnIndex(InvoiceInfo.KEY_COMPLETED)) == 1;

                invoiceList.add(dbRecord);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return invoiceList;
    }

    public List<InvoiceInfo> selectUniqueInvoiceInfo() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT " +
                              InvoiceInfo.KEY_ROUTE_CODE + "," +
                              InvoiceInfo.KEY_CUSTOMER_CODE + "," +
                              InvoiceInfo.KEY_TRANSACTION_TYPE + "," +
                              InvoiceInfo.KEY_INVOICE_BOOK + "," +
                              InvoiceInfo.KEY_INVOICE_NUMBER +
                              " FROM " + InvoiceInfo.TABLE +
                              " GROUP BY "+InvoiceInfo.KEY_INVOICE_BOOK
                ;

        List<InvoiceInfo> invoiceList = Lists.newArrayList();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                InvoiceInfo dbRecord = new InvoiceInfo();
                dbRecord.routeCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_ROUTE_CODE));
                dbRecord.customerCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_CUSTOMER_CODE));
                dbRecord.transactionType = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_TRANSACTION_TYPE));
                dbRecord.invoiceBook = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_INVOICE_BOOK));
                dbRecord.invoiceNumber = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_INVOICE_NUMBER));
                dbRecord.productSeq = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_PRODUCT_SEQ));
                dbRecord.productCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_PRODUCT_CODE));
                dbRecord.wareCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_WARE_CODE));
                dbRecord.wareZone = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_WARE_ZONE));
                dbRecord.qtyReturn = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_QTY_RETURN));
                dbRecord.unit = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_UNIT));
                dbRecord.complainCode = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_COMPLAIN_CODE));
                dbRecord.remarkId = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_REMARK_ID));
                dbRecord.remarks = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_REMARKS));
                dbRecord.deliveryDate = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_DELIVERY_DATE));
                dbRecord.deliverySeq = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_DELIVERY_SEQ));
                dbRecord.cash = cursor.getString(cursor.getColumnIndex(InvoiceInfo.KEY_CASH));
                dbRecord.isCompleted = cursor.getInt(cursor.getColumnIndex(InvoiceInfo.KEY_COMPLETED)) == 1;

                invoiceList.add(dbRecord);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return invoiceList;
    }
}
