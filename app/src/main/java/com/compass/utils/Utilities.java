package com.compass.utils;

import com.google.common.base.Strings;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

	public static boolean isEmailValid(String email) {
	    boolean isValid = false;
	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;
	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	public static Bitmap getBitmapFromAsset(String strName, Context context){
        AssetManager assetManager = context.getAssets();
        try{
        	InputStream istr = assetManager.open(strName);
        	Bitmap bitmap = BitmapFactory.decodeStream(istr);
        	return bitmap;
        }catch(Exception exception){
        	return null;
        }
    }
	
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (wifi.isConnected()) {
	        return true;
	    } else if (mobile.isConnected()) {
	        return true;
	    }else{
	    	return false;
	    }
	}

	/*
	public static boolean fileCheck(String fileName){
    	File file = new File(Constants.fileRootPath + fileName);
    	if(file.exists()){
    		return false;
    	}else{
    		return true;
    	}
    }
	*/
	
	public static void showToast(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	
	public static String getServiceString(String txt){
		if(txt.equals("anyType{}")){
			return "";
		}else { 
			return txt;
		}
	}
	
	public static String getSpaceString(int num) {
		/* 100 space */
		String space = "                                                                                         ";
		String str;
		str = space.substring(0,num).toString();
		return str;
	}
	
	public static boolean isValidDate(String inDate) {

	    if (inDate == null)
	      return false;
	    //set the format to use as a constructor argument
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    if (inDate.trim().length() != dateFormat.toPattern().length())
	      return false;
	    dateFormat.setLenient(false);	    
	    try {
	      //parse the inDate parameter
	      dateFormat.parse(inDate.trim());
	    }
	    catch (ParseException pe) {
	      return false;
	    }
	    return true;
	}
	
	public static String addDate(String inDate,int addDays) {
		String outDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        Calendar c = Calendar.getInstance();
	        c.setTime(sdf.parse(inDate));
	        c.add(Calendar.DATE, addDays);  
	        sdf = new SimpleDateFormat("dd/MM/yyyy");
	        Date resultdate = new Date(c.getTimeInMillis());
	        outDate = sdf.format(resultdate);
		}catch (ParseException pe) {
	    	
	    }
		return outDate;
	}
	
	public static String getCurrenceDate() {
		String outDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        Calendar c = Calendar.getInstance();
	        Date resultdate = new Date(c.getTimeInMillis());
	        outDate = sdf.format(resultdate);
		}catch (Exception e) {}
		return outDate;
	}

	//NTP server list: http://tf.nist.gov/tf-cgi/servers.cgi
	public static final String TIME_SERVER = "time-a.nist.gov";

	public static long getCurrentNetworkTime() {
		/*
		NTPUDPClient timeClient = new NTPUDPClient();
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(TIME_SERVER);

			TimeInfo timeInfo = timeClient.getTime(inetAddress);
			//long returnTime = timeInfo.getReturnTime();   //local device time
			long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();   //server time

			Date time = new Date(returnTime);
			Log.d("Network time", "Time from " + TIME_SERVER + ": " + time);

			return time.getTime();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		// return current system date
		Date currentDateTime = Calendar.getInstance().getTime();
		return currentDateTime.getTime();
	}

	public static String getFormattedDateTimeInYyyyMmDdHh24Mi(String dateStr) {
		Date dateToBeParsed = null;
		if (!Strings.isNullOrEmpty(dateStr) && Long.parseLong(dateStr) > 0) {
			dateToBeParsed = new Date(Long.parseLong(dateStr));
		}

		// if still date object is null then initialise it with current system date
		if (dateToBeParsed == null) {
			dateToBeParsed = Calendar.getInstance().getTime();
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
		String formattedDated = dateFormat.format(dateToBeParsed);
		return formattedDated;
	}

}
