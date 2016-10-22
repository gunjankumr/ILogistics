package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.utils.ConnectionDetector;
import com.compass.utils.ValueHolder;

public class HomeActivity extends Activity {
    /** Called when the activity is first created. */
	
	private ConnectionDetector cd;
	private ValueHolder valueHolder;
	
	private Boolean isInternetPresent = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String number = getMobileNumber();
//        Toast.makeText(getApplicationContext(), number, Toast.LENGTH_SHORT).show();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home);       
        //setHeader(getString(R.string.app_name), false, false);
        valueHolder = ValueHolder.getSingletonObject();
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        // check for Internet status
        if (!isInternetPresent) {
        	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
    			alertDialogBuilder.setTitle("Internet Connection Fail");
    			alertDialogBuilder
    				.setMessage("This application require internet connection.")
    				.setCancelable(false)
    				.setIcon(R.drawable.fail)
    				.setNegativeButton("OK",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						dialog.cancel();
    						HomeActivity.this.finish();
    					}
    				});
    				AlertDialog alertDialog = alertDialogBuilder.create();
    				alertDialog.show();
        }
        
        /*
        MediaPlayer mp  = null;
        if (mp != null) {
            mp.reset();
            mp.release();
        }
        mp = MediaPlayer.create(this, R.raw.button);
        mp.start();
        */
    }
    
    
//    public String getMobileNumber(){
//    	 
//    	  TelephonyManager telephonyManager  =        (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//    	 
//    	String strMobileNumber = telephonyManager.getLine1Number();  
//    	 
//    	// Note : If the phone is dual sim, get the second number using :
//    	 
//    	String strSimSerialNumber =  telephonyManager.getSimSerialNumber();
//    	String strSimOperator =  telephonyManager.getSimOperator();
//    	String strSimOperatorName =  telephonyManager.getSimOperatorName();
//    	String strSimCountryIso =  telephonyManager.getSimCountryIso();
//    	String strNetworkCountryIso =  telephonyManager.getNetworkCountryIso();
//    	int strPhoneType =  telephonyManager.getPhoneType();
//    	
//    	     //return String.valueOf(strPhoneType);
//    	return strMobileNumber;
//    	   }
// 
    
    /**
     * Button click handler on Main activity
     * @param v
     */
    
    
    
    public void onButtonClicker(View v)
    {
    	Intent intent;
    	
    	switch (v.getId()) {
		case R.id.menu_admin:
			valueHolder.setMenuID("menu_admin");
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			//intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			//overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			//overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			break;
			
		case R.id.menu_contractor:
			valueHolder.setMenuID("menu_contractor");
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			break;
		/*	
		case R.id.menu_map:
			valueHolder.setMenuID("menu_contractor");
			intent = new Intent(this, CurrentMapActivity.class);
			startActivity(intent);
			break;
		*/	
		default:
			break;
		}
    }
}

