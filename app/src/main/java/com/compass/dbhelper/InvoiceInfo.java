package com.compass.dbhelper;


public class InvoiceInfo {
    // Labels table name
    public static final String TABLE = "InvoiceInfo";

    // Labels Table Columns names
    public static final String KEY_ROUTE_CODE = "route_code";
    public static final String KEY_CUSTOMER_CODE = "customer_code";
    public static final String KEY_TRANSACTION_TYPE = "transaction_type";
    public static final String KEY_INVOICE_BOOK = "invoice_book";
    public static final String KEY_INVOICE_NUMBER = "invoice_number";
    public static final String KEY_PRODUCT_SEQ = "product_sequence";
    public static final String KEY_PRODUCT_CODE = "product_code";
    public static final String KEY_WARE_CODE = "ware_code";
    public static final String KEY_WARE_ZONE = "ware_zone";
    public static final String KEY_QTY_RETURN = "qty_return";
    public static final String KEY_UNIT = "unit";
    public static final String KEY_COMPLAIN_CODE = "complain_code";
    public static final String KEY_REMARK_ID = "remark_id";
    public static final String KEY_REMARKS = "remarks";
    public static final String KEY_DELIVERY_DATE = "delivery_date";
    public static final String KEY_DELIVERY_SEQ = "delivery_seq";
    public static final String KEY_CASH = "cash";
    public static final String KEY_COMPLETED = "completed";

    // property help us to keep data
    public String routeCode = "";
    public String customerCode = "";
    public String transactionType = "";
    public String invoiceBook = "";
    public String invoiceNumber = "";
    public String productSeq = "";
    public String productCode = "";
    public String wareCode = "";
    public String wareZone = "";
    public String qtyReturn = "";
    public String unit = "";
    public String complainCode = "";
    public String remarkId = "";
    public String remarks = "";
    public String deliveryDate = "";
    public String deliverySeq = "";
    public String cash = "";
    public boolean isCompleted = false;
}
