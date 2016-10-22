package com.compass.utils;

public class Constants {
	
	public static final String LOGI = "INFO";
	public static final String LOGD = "DETAIL";
	public static final String LOGW = "WARRING";
	public static final int SWIPE_MIN_DISTANCE = 120;
	public static final int SWIPE_MAX_OFF_PATH = 250;
	public static final int SWIPE_THRESHOLD_VELOCITY = 100;
	public static final String COMP_CODE = "JB";
	public static final String DEVICE_ID = "123456";
	public static final String SHARED_PREF    = "myPref";
	public static final String CONTRACTOR_PHONE_NUMBER    = "phonenumber";
	public static final String CONTRACTOR_OTP    = "otp";
	
	// web service url
	public static final String URL = "http://appiphone.jagota.com:94/mLogistics/WebService.asmx";
	
	// Development Server
	//public static final String URLJSON = "http://hq.compass-softwares.com:83/iQuotation/iLogistic.asmx";
	
	//Production Server
	public static final String URLJSON = "http://appiphone.jagota.com:94/iQuotation/iLogistic.asmx";
	
   //  public static final String URLJSONVERSION = "http://hq.compass-softwares.com:83/JagotaWebIntegration/WebAdmin/ApplicationUDID.asmx";
	
	
	// Production Server
		public static final String URLJSONVERSION = "http://appiphone.jagota.com:94/JagotaWebIntegration/WebAdmin/ApplicationUDID.asmx";	
	
	// web service namespace
	public static final String NAMESPACE ="http://tempuri.org/";
	
	/* Get Incoming Invoice */
	public static final String METHOD_NAME_INCOME_INVOICE = "DetailCustomer";
	public static final String SOAP_ACTION_INCOME_INVOICE = NAMESPACE + METHOD_NAME_INCOME_INVOICE;
}