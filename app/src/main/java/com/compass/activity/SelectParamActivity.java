package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.utils.Constants;
import com.compass.utils.SessionManager;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;

public class SelectParamActivity extends Activity {
    
	private ValueHolder valueHolder;
	private SessionManager session;
	private ProgressDialog dialog;
	private Intent intent;
	
	private EditText etDate;
	//private LoginTask loginTask; 
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String TIMEOUT = "TIMEOUT";
	private static final String SUCCESS = "SUCCESS";
	
	private String serviceResponse = null;
	private ArrayList<CustomerModel> serviceResponseList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_param);
        etDate=(EditText)findViewById(R.id.deliveryDate);
        session = new SessionManager(getApplicationContext());
        valueHolder = ValueHolder.getSingletonObject();
        
        etDate = (EditText) findViewById(R.id.usernameTxt);
       
        ScrollView view = (ScrollView)findViewById(R.id.scrollView);
        view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        
        final Drawable x = getResources().getDrawable(R.drawable.ic_input_delete);
		x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
		etDate.setCompoundDrawables(null, null, x, null);
		etDate.setOnTouchListener(new OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
		        if (etDate.getCompoundDrawables()[2] == null) {
		            return false;
		        }
		        if (event.getAction() != MotionEvent.ACTION_UP) {		        	
		            return false;
		        }
		        if (event.getX() > etDate.getWidth() - etDate.getPaddingRight() - x.getIntrinsicWidth()) {
		        	etDate.setText("");        
		        }else {
		        	final Calendar c = Calendar.getInstance();
					int y = c.get(Calendar.YEAR);
					int m = c.get(Calendar.MONTH);
					int d = c.get(Calendar.DAY_OF_MONTH);
					//final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
					DatePickerDialog dp = new DatePickerDialog(SelectParamActivity.this,new DatePickerDialog.OnDateSetListener() {
								public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
									String month = String.valueOf(monthOfYear + 1);
									String erg = "";
									erg = String.valueOf(dayOfMonth);
									month = (month.length() < 2) ? "0".concat(month) : month;
									erg += "/" + month;
									erg += "/" + year;
									((TextView) etDate).setText(erg);									
									//new CustomerStaffThread().execute();
								}
							}, y, m, d);
					dp.setTitle("Calender");
					dp.setMessage("Select Your Delivery Date Please?");
					dp.show();
		        }
		        return false;
		    }
		});
        
    }
    
    
    public void onButtonClicker(View v)
    { 	
    	switch (v.getId()) {
		case R.id.btnLogin:
			/*
            if (loginTask != null && loginTask.getStatus() != AsyncTask.Status.FINISHED) {
                loginTask.cancel(true);
            }   
        	loginTask = new LoginTask(); //every time create new object, as AsynTask will only be executed one time.
        	loginTask.execute();
        	*/
			break;	
		default:
			break;
		}
    }
    
    /*
    private class LoginTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(SelectParamActivity.this, "", getString(R.string.wait));
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
					serviceResponse = service.checkUserLogin(etUsername.getText().toString(), etPassword.getText().toString());
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
				valueHolder.clearLocation();
				if(serviceResponse.equalsIgnoreCase("true")) {
					//startActivity(new Intent(LoginActivity.this, RoutingListActivity.class));
					startActivity(new Intent(SelectParamActivity.this, HomeAdminActivity.class));
					finish();
				}else if(serviceResponse.equalsIgnoreCase("false")) {
					Toast.makeText(getApplicationContext(), R.string.loginfail,Toast.LENGTH_SHORT).show();
				}else {
					
				}
			}
		}		
		protected void onPostExecute(String result) {
		}
	}
    */

    public class GetRoutingTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(SelectParamActivity.this, "", getString(R.string.wait));
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
					serviceResponseList = service.getRoutingDetails1(valueHolder.getUsername(), Constants.DEVICE_ID, valueHolder.getCompanyCode(), valueHolder.getRoutingCode(),valueHolder.getDeliverySeq(),valueHolder.getDeliveryDate());
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
				valueHolder.setCustomerList(serviceResponseList);
				valueHolder.clearLocation();
				if(serviceResponseList.size() > 0) {
					startActivity(new Intent(SelectParamActivity.this, MenuListActivity.class));
					finish();
				}else {
					Toast.makeText(getApplicationContext(), "No data found",Toast.LENGTH_SHORT).show();
				}
			}
		}
		protected void onPostExecute(String result) {}
	}

    @Override
    protected void onDestroy() {
    	/*
        if (loginTask != null && loginTask.getStatus() != AsyncTask.Status.FINISHED)
            loginTask.cancel(true);
        */    
        super.onDestroy();
    }
}

