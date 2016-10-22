package com.compass.dbhelper;


public class CheckInCheckOut {
    // Labels table name
    public static final String TABLE = "CheckInCheckOut";

    // Labels Table Columns names
    public static final String KEY_USERNAME = "username";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_COMPANY_CODE = "company_code";
    public static final String KEY_CUSTOMER_CODE = "customer_code";
    public static final String KEY_BRANCH_CODE = "branch_code";
    public static final String KEY_ROUTE_CODE = "route_code";
    public static final String KEY_DELIVERY_SEQ = "delivery_seq";
    public static final String KEY_DELIVERY_DATE = "delivery_date";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_CHECK_IN_TIME = "check_in_time";
    public static final String KEY_CHECK_OUT_TIME = "check_out_time";

    // property help us to keep data
    public String username = "";
    public String deviceId = "";
    public String companyCode = "";
    public String customerCode = "";
    public String branchCode = "";
    public String routeCode = "";
    public String deliverySequence = "";
    public String deliveryDate = "";
    public String latitude = "";
    public String longitude = "";
    public String checkInTime = "0";
    public String checkOutTime = "0";
}
