package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import com.compass.activity.map.MapsActivity;
import com.compass.ilogistics.R;
import com.compass.utils.ConnectionDetector;
import com.compass.utils.WebService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class SplashActivity extends Activity {
    /** Called when the activity is first created. */
	
	private volatile boolean running = true;
	private ConnectionDetector cd;
	private Boolean isInternetPresent = false;
	private ProgressDialog dialog;
	private InitialTask initialTask;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String TIMEOUT = "TIMEOUT";
	private static final String SUCCESS = "SUCCESS";
	
	private String serviceResponse = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();        
        // check for Internet status
        if (!isInternetPresent) {
        	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
    			alertDialogBuilder.setTitle("Internet Connection Fail");
    			alertDialogBuilder
    				.setMessage("This application require internet connection.")
    				.setCancelable(false)
    				.setIcon(R.drawable.fail)
    				.setNegativeButton("OK",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						dialog.cancel();
    						SplashActivity.this.finish();
    					}
    				});
    				AlertDialog alertDialog = alertDialogBuilder.create();
    				alertDialog.show();
        }
        
        initialTask = new InitialTask();
        initialTask.execute();
    }
    
    
    
    private class InitialTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			//dialog = ProgressDialog.show(SplashActivity.this, "", getString(R.string.wait));
			//dialog.setCancelable(false);
		}
		
		@Override
		protected String doInBackground(String... params) {
				WebService service = new WebService();
				String versionName = "";
				try {
					if (isCancelled()) {
	                    publishProgress(CANCELLED); //Notify your activity that you had canceled the task
	                    return (null); // don't forget to terminate this method
	                }
					      
			        PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			        versionName = pinfo.versionName;
					serviceResponse = service.checkVersion(versionName);
					publishProgress(SUCCESS);  
				} catch (UnknownHostException e) {
	                e.printStackTrace();
	                publishProgress(NETWORK_ERROR);
				} catch (TimeoutException e) {
					e.printStackTrace();
					publishProgress(TIMEOUT);
				} catch (NameNotFoundException e) {
		            e.printStackTrace();
		            publishProgress(NETWORK_ERROR);
	            } catch (Exception e) {
	                e.printStackTrace();
	                publishProgress(SERVER_ERROR);
	            }
				return null;
		}
		@Override
		protected void onProgressUpdate(String... errorCode) {
			if(errorCode[0].toString().equalsIgnoreCase(CANCELLED)) {
				//dialog.dismiss();
			}else if(errorCode[0].toString().equalsIgnoreCase(NETWORK_ERROR)) {	
				//dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Network connection error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SERVER_ERROR)) {	
				//dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Server error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(TIMEOUT)) {	
				//dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Service time out",Toast.LENGTH_SHORT).show();	
			}else if(errorCode[0].toString().equalsIgnoreCase(SUCCESS)) {
				//dialog.dismiss();
				if(serviceResponse.equalsIgnoreCase("true")) {
					if (running) {
					//	startActivity(new Intent(getApplicationContext(), HomeActivity.class));
						startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//						startActivity(new Intent(getApplicationContext(), MapsActivity.class));
						
						finish();
					}
				}else if(serviceResponse.equalsIgnoreCase("false")) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
	    			alertDialogBuilder.setTitle("Application Fail");
	    			alertDialogBuilder
	    				.setMessage("This application out to date please update new version.")
	    				.setCancelable(false)
	    				.setIcon(R.drawable.fail)
	    				.setNegativeButton("OK",new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog,int id) {
	    						dialog.cancel();
	    						SplashActivity.this.finish();
	    					}
	    				});
	    				AlertDialog alertDialog = alertDialogBuilder.create();
	    				alertDialog.show();
				}
			}
		}		
		protected void onPostExecute(String result) {
		}
		@Override
	    protected void onCancelled() {
	        running = false;
	    }
	}
    
    
    @Override
    protected void onDestroy() {
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (initialTask != null && initialTask.getStatus() != AsyncTask.Status.FINISHED)
        	initialTask.cancel(true);
        super.onDestroy();
    }
    
}

