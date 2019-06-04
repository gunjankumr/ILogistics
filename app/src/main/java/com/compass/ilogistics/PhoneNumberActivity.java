package com.compass.ilogistics;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.compass.activity.LoginActivity;
import com.compass.utils.AlertDialogManager;
import com.compass.utils.Constants;
import com.compass.utils.SessionManager;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneNumberActivity extends Activity {
	AlertDialogManager alert = new AlertDialogManager();
	EditText phoneNumber;
	//private GetRouteByPhoneNoTask getRouteByPhoneNoTask; 

	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String TIMEOUT = "TIMEOUT";
	private static final String SUCCESS = "SUCCESS";
	private static final String APPVERSION = "2.0"; // Modify every time to change version of application
	private ProgressDialog dialog;
	private SessionManager session;
	private String serviceResponse = null;
	String ph;
	private ArrayList<HashMap<String, String>> serviceResponseList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_phone_number);
        session = new SessionManager(getApplicationContext());
		TextView appVersion = (TextView)findViewById(R.id.appVersion2);
		appVersion.setText(getString(R.string.appVersiontxt) +" " + APPVERSION);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phone_number, menu);
		return true;
	}
	
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create(); 
        // Setting Dialog Title
        alertDialog.setTitle(title); 
        // Setting Dialog Message
        alertDialog.setMessage(message); 
        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail); 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

	/** Called when the user clicks the save phone number button */
	public void savePhoneNumber(View view) {
	    System.out.println("In savePhoneNumber click");
	    phoneNumber = (EditText) findViewById(R.id.phoneNumberTxt);
	    ph  = phoneNumber.getText().toString();
	    if (! ph.matches("")) 
	    {
	    	SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
	    	
	    	SharedPreferences.Editor editor = sharedPref.edit();
	    	editor.putString(Constants.CONTRACTOR_PHONE_NUMBER, ph);
	    	editor.commit();
	    	
	    	
	    	Intent intent;
	    	intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
	    	
	    	finish();
	    	
	    	//sharedPref.edit().putString(Constants.CONTRACTOR_PHONE_NUMBER, ph);
	    	//((Editor) sharedPref).commit();
	    	//valueHolder.setMenuID("menu_contractor");
	    	
//	    	Intent intent = new Intent(this, LoginActivity.class);
//			startActivity(intent);
//			finish();
//	    	
//	    	new GetRouteByPhoneNoTask().execute();
	    }
	    else
	    {
	    	 showAlertDialog(this, "Error", "Please enter your registered phone number.", true);
	    }
	   
	}
	
	
	private class GetRouteByPhoneNoTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PhoneNumberActivity.this, "", getString(R.string.wait));
			dialog.setCancelable(false);
		}		
		@Override
		protected String doInBackground(String... params) {
				WebService service = new WebService();
				try {
					if (isCancelled()) {
	                    publishProgress(CANCELLED); //Notify your activity that you had canceled the task
	                    return (null); // don't forget to terminate this method
	                }
					SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
		        	String strPhone = sharedPref.getString(Constants.CONTRACTOR_PHONE_NUMBER, null);
					//serviceResponse = service.GetRouteByPhoneNo(valueHolder.getUsername(), Constants.DEVICE_ID, Constants.COMP_CODE, valueHolder.getDeliveryDate(), strPhone);
		        	serviceResponseList = service.GetRouteByPhoneNo("JBT04", Constants.DEVICE_ID, Constants.COMP_CODE, "22/08/2014", strPhone);
					
		        	publishProgress(SUCCESS); //if everything is Okay then publish this message, you may also use onPostExecute() method  
				} catch (UnknownHostException e) {
	                e.printStackTrace();
	                publishProgress(NETWORK_ERROR);
				} catch (TimeoutException e) {
					e.printStackTrace();
					publishProgress(TIMEOUT);
	            } catch (Exception e) {
	                e.printStackTrace();
	                publishProgress(SERVER_ERROR);
	            }
				return null;
		}
		@Override
		protected void onProgressUpdate(String... errorCode) {
			if(errorCode[0].toString().equalsIgnoreCase(CANCELLED)) {
				dialog.dismiss();
			}else if(errorCode[0].toString().equalsIgnoreCase(NETWORK_ERROR)) {	
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Network connection error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SERVER_ERROR)) {	
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Server error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(TIMEOUT)) {	
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Service time out",Toast.LENGTH_SHORT).show();	
			}else if(errorCode[0].toString().equalsIgnoreCase(SUCCESS)) {
				dialog.dismiss();
				 
//				SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
//	        	String strPhone = sharedPref.getString(Constants.CONTRACTOR_PHONE_NUMBER, null);
//	        	if(strPhone == null){
//	        		SharedPreferences.Editor editor = sharedPref.edit();
//	        		editor.putString(Constants.CONTRACTOR_PHONE_NUMBER, ph);
//	        		editor.commit();
//	        	}
				
				String roundVal = "1";
				String route_code = null;
				String deliver_name = null;
				String route_desc = null;
				String plate_no = null;
				String curr_dt = null;
				if(serviceResponseList.size() > 0)
				{
				for(int i=0;i<serviceResponseList.size();i++) {
					
					route_code = serviceResponseList.get(i).get("ROUTE_CODE");
					deliver_name = serviceResponseList.get(i).get("DELIVER_NAME");
					route_desc = serviceResponseList.get(i).get("ROUTE_DESCRIPTION");
					plate_no = serviceResponseList.get(i).get("PLATE_NO");
					curr_dt = serviceResponseList.get(i).get("CURRENT_DATE");
		
				}
				
				DateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
				DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date date = null;
				try {
					date = inputFormat.parse(curr_dt);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String outputDateStr = outputFormat.format(date);
				System.out.println(outputDateStr);		
				curr_dt = outputDateStr;
				
				session.setPreferenceVal("ROUTE_CODE", route_code.toUpperCase());
				session.setPreferenceVal("ROUND", roundVal.toUpperCase());
				session.setPreferenceVal("ASSIGN_DATE", curr_dt);
				session.setPreferenceVal("CHECKIN", "");
				}
				else
				{
					SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
		        	
	        		SharedPreferences.Editor editor = sharedPref.edit();
	        		editor.putString(Constants.CONTRACTOR_PHONE_NUMBER, null);
	        		editor.commit();
				}
		       	Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				finish();
				}else if(serviceResponse.equalsIgnoreCase("false")) {
					Toast.makeText(getApplicationContext(), R.string.loginfail,Toast.LENGTH_SHORT).show();
				}else {
					
				}
		}		
		protected void onPostExecute(String result) {
		}
	}
	
}


