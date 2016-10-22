package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import static com.google.android.gcm.demo.app.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.google.android.gcm.demo.app.CommonUtilities.EXTRA_MESSAGE;
import static com.google.android.gcm.demo.app.CommonUtilities.SENDER_ID;
import static com.google.android.gcm.demo.app.CommonUtilities.SERVER_URL;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.compass.dbhelper.InvoiceInfo;
import com.compass.dbhelper.InvoiceInfoRepo;
import com.compass.ilogistics.PhoneNumberActivity;
import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.Invoice;
import com.compass.model.InvoiceModel;
import com.compass.model.ProblemSet;
import com.compass.utils.Constants;
import com.compass.utils.SessionManager;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gcm.demo.app.ServerUtilities;
import com.google.common.base.Strings;

public class LoginActivity extends Activity {
    
	private ValueHolder valueHolder;
	private SessionManager session;
	private ProgressDialog dialog;
	private Intent intent;
	
	private EditText etUsername,etPassword,etOtp,etPhNum,etRound,etAssignRoute,etAssignRound,etAssignDate;
	private LoginTask loginTask; 
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String TIMEOUT = "TIMEOUT";
	private static final String SUCCESS = "SUCCESS";
	private static final String APPNAME = "LOGISTICS";
	private static final String APPVERSION = "2.0"; // Modify every time to change version of application
	private String serviceResponse = null;
	private ArrayList<CustomerModel> serviceResponseList;
	private ArrayList<ProblemSet> serviceResponseProblemList;
	public ArrayList<HashMap<String, String>> serviceResponseListArr;
	public ArrayList<HashMap<String, String>> serviceResponseListSeq;
	private AsyncTask<Void, Void, Void> mRegisterTask;
	private String regIdData = "No Data";
	boolean registered;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        //setHeader(getString(R.string.app_name), false, false);
        session = new SessionManager(getApplicationContext());
        valueHolder = ValueHolder.getSingletonObject();
        
        //doGcmRegistration();
        
        /*
        Intent next = new Intent(LoginActivity.this, LoginActivity.class);       
        intent = PendingIntent.getActivity(LoginActivity.this, 0,next, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(){
            public void uncaughtException(Thread thread, Throwable ex){
                Log.v("MyApplication", "onUncaughtException triggered. Error:");
                ex.printStackTrace();              
                AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);             
                System.exit(2);

            }
       });
         */
        TextView appVersion = (TextView)findViewById(R.id.appVersion);
        appVersion.setText(getString(R.string.appVersiontxt) +" " + APPVERSION);
        etUsername = (EditText) findViewById(R.id.usernameTxt);
        etPassword = (EditText) findViewById(R.id.passwordTxt);
        etOtp	   = (EditText) findViewById(R.id.otpTxt);
        etPhNum    = (EditText) findViewById(R.id.phoneNumberTxt);
//        etRound    = (EditText) findViewById(R.id.roundTxt);
        etAssignRoute   = (EditText) findViewById(R.id.assignRouteTxt);
        etAssignRound   = (EditText) findViewById(R.id.assignRoundTxt);
        etAssignDate	= (EditText) findViewById(R.id.assignDateTxt); 
        
 /*       if(valueHolder.getMenuID().equalsIgnoreCase("menu_admin")) {
        	((LinearLayout) findViewById(R.id.login1)).setVisibility(View.GONE);
        	((LinearLayout) findViewById(R.id.login2)).setVisibility(View.GONE);
        	valueHolder.setRoutingCode("%");
        }else if(valueHolder.getMenuID().equalsIgnoreCase("menu_contractor")) {
        	SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
        	String strPhone = sharedPref.getString(Constants.CONTRACTOR_PHONE_NUMBER, null);
        	if(strPhone != null){
//        		Intent intent = new Intent(this, PhoneNumberActivity.class);
//    			startActivity(intent);
        	    //new GetRouteByPhoneNoTask().execute();
        		((LinearLayout) findViewById(R.id.login)).setVisibility(View.GONE);
        		((LinearLayout) findViewById(R.id.login2)).setVisibility(View.GONE);
        		etDate.setText(session.getPreferenceVal("ASSIGN_DATE"));
        		etRoute.setText(session.getPreferenceVal("ROUTE_CODE"));
//        		etRound.setText(session.getPreferenceVal("ROUND"));
    	    	new GetRouteByPhoneNoTask().execute();

        	}
        	else
        	{
//        		Intent intent = new Intent(this, PhoneNumberActivity.class);
//    			startActivity(intent);
//    			finish();
        		showAlertDialog(this, "Error", "Mobile number is not assigned. Please contact to admin.", true);
        		//Toast.makeText(getApplicationContext(), "Mobile number is not assigned. Please contact to admin" ,Toast.LENGTH_LONG).show();
        		finish();
        		return;
        	}
        }else if(valueHolder.getMenuID().equalsIgnoreCase("menu_assign")) {
        	((LinearLayout) findViewById(R.id.login)).setVisibility(View.GONE);
        	((LinearLayout) findViewById(R.id.login1)).setVisibility(View.GONE);        	
        }
        */
        Intent intent = new Intent(this, PhoneNumberActivity.class);
//    			startActivity(intent);
        		//new GetRouteByPhoneNoTask().execute();
        		((LinearLayout) findViewById(R.id.login)).setVisibility(View.GONE);
        		((LinearLayout) findViewById(R.id.login2)).setVisibility(View.GONE);
//        		etDate.setText(session.getPreferenceVal("ASSIGN_DATE"));
//        		etRoute.setText(session.getPreferenceVal("ROUTE_CODE"));
        		
        		SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
	        	String strOtp = sharedPref.getString(Constants.CONTRACTOR_OTP, null);
	        	etOtp.setText(strOtp);
        		
        		SharedPreferences sharedPrefPh = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
	        	String strPhone = sharedPrefPh.getString(Constants.CONTRACTOR_PHONE_NUMBER, null);
	        	etPhNum.setText(strPhone);
	        	
//        		etRound.setText(session.getPreferenceVal("ROUND"));
 //   	    	new GetRouteByPhoneNoTask().execute();
        
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
        /*
        ScrollView scroll = (ScrollView) findViewById(R.id.scrollView);
        scroll.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if the view with focus is EditText
                if (LoginActivity.this.getCurrentFocus() instanceof EditText)
                {
                    EditText ed = (EditText)LoginActivity.this.getCurrentFocus();
                    if (ed.hasFocus()) {
                        // Hide the keyboard
                        InputMethodManager inputManager = (InputMethodManager)LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE); 
                        inputManager.hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        // Clear the focus
                        ed.clearFocus();
                    }
                }
                return false;
            }
        });
        */
        
        
        final Drawable x = getResources().getDrawable(R.drawable.ic_input_delete);
		x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
		etAssignDate.setCompoundDrawables(null, null, x, null);
		etAssignDate.setOnTouchListener(new OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
		        if (etAssignDate.getCompoundDrawables()[2] == null) {
		            return false;
		        }
		        if (event.getAction() != MotionEvent.ACTION_UP) {
		            return false;
		        }
		        if (event.getX() > etAssignDate.getWidth() - etAssignDate.getPaddingRight() - x.getIntrinsicWidth()) {
		        	//etDate.setText("");
		        }else {
		        	final Calendar c = Calendar.getInstance();
					int y = c.get(Calendar.YEAR);
					int m = c.get(Calendar.MONTH);
					int d = c.get(Calendar.DAY_OF_MONTH);
					DatePickerDialog dp = new DatePickerDialog(LoginActivity.this,new DatePickerDialog.OnDateSetListener() {
								public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
									String month = String.valueOf(monthOfYear + 1);
									String erg = "";
									erg = String.valueOf(dayOfMonth);
									erg = (erg.length() < 2) ? "0".concat(erg) : erg;
									month = (month.length() < 2) ? "0".concat(month) : month;
									erg += "/" + month;
									erg += "/" + year;
									((TextView) etAssignDate).setText(erg);								
								}
							}, y, m, d);
					dp.setTitle("Calender");
					dp.setMessage("Select Your Delivery Date Please?");
					dp.show();
		        }
		        return false;
		    }

		});

		GetAppVersion getAppVersion = new GetAppVersion();
		getAppVersion.execute();

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
    
    public void onButtonClicker(View v)
    { 	
    	switch (v.getId()) {
		case R.id.btnLogin:
			//check if any previous task is running, if so then cancel it
			//it can be cancelled if it is not in FINISHED state
            if (loginTask != null && loginTask.getStatus() != AsyncTask.Status.FINISHED) {
                loginTask.cancel(true);
            }   
            String username  = etUsername.getText().toString();
		    if (username.matches("")) 
		    {
		    	 showAlertDialog(this, "Error", "Please enter username.", true);
		    	 break;
		    }
		    
		    String password  = etPassword.getText().toString();
		    if (password.matches("")) 
		    {
		    	 showAlertDialog(this, "Error", "Please enter password.", true);
		    	 break;
		    }
		    valueHolder.setMenuID("menu_admin");
            
            valueHolder.setUsername(etUsername.getText().toString());
        	loginTask = new LoginTask(); //every time create new object, as AsynTask will only be executed one time.
        	loginTask.execute();
			break;
		case R.id.btnLogin1:
//			valueHolder.setDeliveryDate(etDate.getText().toString());
//			valueHolder.setRoutingCode(etRoute.getText().toString().toUpperCase());
//			valueHolder.setDeliverySeq(etRound.getText().toString().toUpperCase());
			//session.setPreferenceVal("ROUTE_CODE", etRoute.getText().toString().toUpperCase());
			
			String otp  = etOtp.getText().toString();
		    if (otp.matches("")) 
		    {
		    	 showAlertDialog(this, "Error", "Please enter OTP.", true);
		    	 break;
		    }
		    else {
		    	SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
		    	SharedPreferences.Editor editor = sharedPref.edit();
		    	editor.putString(Constants.CONTRACTOR_OTP, otp);
		    	editor.commit();
		    }
			
			 String ph  = etPhNum.getText().toString();
			    if (ph.matches("")) 
			    {
			    	 showAlertDialog(this, "Error", "Please contact Admin to set your mobile number.", true);
			    	 break;
			    }
			    
			valueHolder.setMenuID("menu_contractor");
			    
			GetDailyRouteInfoByOTP getDailyRouteInfoByOTP = new GetDailyRouteInfoByOTP();
			getDailyRouteInfoByOTP.execute();
	        
	        
			break;
		case R.id.btnLogin2:
//			session.setPreferenceVal("ROUTE_CODE", etAssignRoute.getText().toString().toUpperCase());
//			session.setPreferenceVal("ROUND", etAssignRound.getText().toString().toUpperCase());
//			session.setPreferenceVal("ASSIGN_DATE", etAssignDate.getText().toString());
			session.setPreferenceVal("CHECKIN", "");
			intent = new Intent(LoginActivity.this, HomeAdminActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.menu_admin:
			valueHolder.setMenuID("menu_admin");
			setContentView(R.layout.login);
		//	intent = new Intent(LoginActivity.this, HomeAdminActivity.class);
		//	startActivity(intent);
			//intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			//overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			//overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
			((LinearLayout) findViewById(R.id.login1)).setVisibility(View.GONE);
        	((LinearLayout) findViewById(R.id.login2)).setVisibility(View.GONE);
        	 etUsername = (EditText) findViewById(R.id.usernameTxt);
             etPassword = (EditText) findViewById(R.id.passwordTxt);
			break;
		default:
			break;
		}
    }
    
    
    private class LoginTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.wait));
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
					
//					 String username  = etUsername.getText().toString();
//					 String password  = etPassword.getText().toString();
					    
					
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
					//startActivity(new Intent(LoginActivity.this, HomeAdminActivity.class));
					startActivity(new Intent(LoginActivity.this, PhoneNumberActivity.class));
					finish();
				}else if(serviceResponse.equalsIgnoreCase("false")) {
					Toast.makeText(getApplicationContext(), R.string.loginfail,Toast.LENGTH_SHORT).show();
					//startActivity(new Intent(LoginActivity.this, PhoneNumberActivity.class));
				}else {
					
				}
			}
		}		
		protected void onPostExecute(String result) {
		}
	}
    

    public class GetRoutingTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.wait));
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
//					updateInvoiceStatusFromLocalDatabase();
					startActivity(new Intent(LoginActivity.this, MenuListActivity.class));
					finish();
				}else {
					Toast.makeText(getApplicationContext(), "No data found",Toast.LENGTH_SHORT).show();
				}
			}
		}
		protected void onPostExecute(String result) {}
	}

	private void updateInvoiceStatusFromLocalDatabase() {
		InvoiceInfoRepo repo = new InvoiceInfoRepo(this);
		List<InvoiceInfo> localList = repo.getInvoiceList();
		if (localList != null
			&& !localList.isEmpty()
			&& valueHolder.getCustomerList() != null
			&& !valueHolder.getCustomerList().isEmpty()) {

			for (int i = 0; i < valueHolder.getCustomerList().size(); i++) {
				if (valueHolder.getCustomerList().get(i).getInvoiceList() != null
					&& !valueHolder.getCustomerList().get(i).getInvoiceList().isEmpty()) {
					for (int j = 0; j < valueHolder.getCustomerList().get(i).getInvoiceList().size(); j++) {
						InvoiceStatus invoiceStatus = getInvoiceStatus(localList,
								valueHolder.getCustomerList().get(i).getInvoiceList().get(j));
						if (invoiceStatus == InvoiceStatus.HAS_PROBLEM) {
							valueHolder.getCustomerList().get(i).getInvoiceList().get(j).setINV_STATUS("W");
						} else if (invoiceStatus == InvoiceStatus.COMPLETED) {
							valueHolder.getCustomerList().get(i).getInvoiceList().get(j).setINV_STATUS("Y");
						}
					}
				}
			}
		}
	}

	private InvoiceStatus getInvoiceStatus(List<InvoiceInfo> localList, InvoiceModel invoiceModel) {
		InvoiceStatus invoiceStatus = InvoiceStatus.NOT_FOUND;

		int hasProblem = 0;
		boolean isFound = false;
		for (int i = 0; i < localList.size(); i++) {

			InvoiceInfo localInvoiceInfo = localList.get(i);
			if (localInvoiceInfo != null
				&& localInvoiceInfo.transactionType.equals(invoiceModel.TRANSACTION_TYPE)
				&& localInvoiceInfo.invoiceBook.equals(invoiceModel.INV_BOOK)
				&& localInvoiceInfo.invoiceNumber.equals(invoiceModel.INV_NO)) {

				isFound = true;

				if (!Strings.isNullOrEmpty(localInvoiceInfo.complainCode)) {
					hasProblem ++;
				}
			}
		}

		if (!isFound) {
			invoiceStatus = InvoiceStatus.NOT_FOUND;
		} else if (isFound && hasProblem == 0) {
			invoiceStatus = InvoiceStatus.COMPLETED;
		} else if (isFound && hasProblem > 0) {
			invoiceStatus = InvoiceStatus.HAS_PROBLEM;
		}
		return invoiceStatus;
	}
    
    /*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
      super.onSaveInstanceState(savedInstanceState);
      //savedInstanceState.putInt(STATE_NAV, getSupportActionBar().getSelectedNavigationIndex());
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      //getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_NAV));
    }
    */
    @Override
    protected void onDestroy() {
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (loginTask != null && loginTask.getStatus() != AsyncTask.Status.FINISHED)
            loginTask.cancel(true);
        super.onDestroy();
    }
    
    
  
    private void doGcmRegistration() {
		
		checkNotNull(SERVER_URL, "SERVER_URL");
        checkNotNull(SENDER_ID, "SENDER_ID");
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);      
        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);
        
        if (regId.equals("")) {

            GCMRegistrar.register(this, SENDER_ID);
            regIdData = regId;
            Log.i("Regis ID", "Registration ID =  "+regIdData);
//
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
            	regIdData = regId;
            	Log.i("Already_registered Regis ID", "Registration ID =  "+regIdData);
            }

            final Context context = this;
            mRegisterTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    registered =
                            ServerUtilities.register(context, regId);
                    if (!registered) {
                        //GCMRegistrar.unregister(context);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    mRegisterTask = null;
                }

            };
            mRegisterTask.execute(null, null, null);
        }
	}
	
    private final BroadcastReceiver mHandleMessageReceiver =
            new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
        }
    };//
	
    private void checkNotNull(Object reference, String name) {
    	if (reference == null) {
    		throw new NullPointerException(getString(R.string.error_config, name));
    	}
    }

    private class GetRouteByPhoneNoTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.wait));
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
		        	serviceResponseListArr = service.GetRouteByPhoneNo(valueHolder.getUsername(), Constants.DEVICE_ID, valueHolder.getCompanyCode(), valueHolder.getDeliveryDate(), strPhone);
					
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
				
				if(serviceResponseListArr.size() > 0)
				{
					String route_code = null;
					String deliver_name = null;
					String route_desc = null;
					String plate_no = null;
					String curr_dt = null;
					String del_seq = null;
					for(int i=0;i<serviceResponseListArr.size();i++) {
					
						route_code = serviceResponseListArr.get(i).get("ROUTE_CODE");
						//deliver_name = serviceResponseListArr.get(i).get("DELIVER_NAME");
						//route_desc = serviceResponseListArr.get(i).get("ROUTE_DESCRIPTION");
						//plate_no = serviceResponseListArr.get(i).get("PLATE_NO");
						curr_dt = serviceResponseListArr.get(i).get("DOC_DATE");
						del_seq = serviceResponseListArr.get(i).get("DELIVERY_SEQ");
		
					}
					valueHolder.setDeliveryDate(curr_dt);
					valueHolder.setRoutingCode(route_code.toUpperCase());
					valueHolder.setDeliverySeq(del_seq.toUpperCase());
					session.setPreferenceVal("ROUTE_CODE", route_code.toUpperCase());
					
					valueHolder.setDeliverySeqNew(del_seq);
					valueHolder.setDeliveryDateNew(curr_dt);
					
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
				
//					session.setPreferenceVal("ROUTE_CODE", route_code.toUpperCase());
//					session.setPreferenceVal("ASSIGN_DATE", curr_dt);
//					session.setPreferenceVal("CHECKIN", "");
//					etDate.setText(session.getPreferenceVal("ASSIGN_DATE"));
//	        		etRoute.setText(session.getPreferenceVal("ROUTE_CODE"));
//					GetRouteSeq getRouteSeq = new GetRouteSeq();
//					getRouteSeq.execute();
					}
					else
					{
						
//							SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
//				        	
//			        		SharedPreferences.Editor editor = sharedPref.edit();
//			        		editor.putString(Constants.CONTRACTOR_PHONE_NUMBER, null);
//			        		editor.commit();
//					
//				       	Intent intent = new Intent(getApplicationContext(), PhoneNumberActivity.class);
//						startActivity(intent);
						
		        		//showAlertDialog(this, "Error", "Mobile number registered by admin is not correct or no task assign for this number. Please conatct to admin.", true);

		        		Toast.makeText(getApplicationContext(), "Mobile number registered by admin is not correct or no task assign for this number. Please conatct to admin." ,Toast.LENGTH_LONG).show();

						finish();
						
					}
		       	}else if(serviceResponse.equalsIgnoreCase("false")) {
					Toast.makeText(getApplicationContext(), R.string.loginfail,Toast.LENGTH_SHORT).show();
				}else {
					
				}
		}	
		
		
	
		protected void onPostExecute(String result) {
		}
		
		
		private class GetRouteSeq extends AsyncTask<String, String, String> {
			protected void onPreExecute() {
				dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.wait));
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
						serviceResponseListSeq = service.GetRouteSeq(valueHolder.getUsername(), Constants.DEVICE_ID, valueHolder.getCompanyCode(), session.getPreferenceVal("ROUTE_CODE"), session.getPreferenceVal("ASSIGN_DATE"));
						
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
					 
					String roundVal = null;
					
					for(int i=0;i<serviceResponseListSeq.size();i++) {
						
						roundVal = serviceResponseListSeq.get(i).get("DELIVERY_SEQ");
			
					}
//					valueHolder.setDeliverySeqNew(roundVal);
//					session.setPreferenceVal("ROUND", roundVal.toUpperCase());
//	        		etRound.setText(session.getPreferenceVal("ROUND"));

					}else if(serviceResponse.equalsIgnoreCase("false")) {
						Toast.makeText(getApplicationContext(), R.string.loginfail,Toast.LENGTH_SHORT).show();
					}else {
						
					}
			}		
			protected void onPostExecute(String result) {
			}
		}

		
	}
	
    


private class GetDailyRouteInfoByOTP extends AsyncTask<String, String, String> {
	protected void onPreExecute() {
		dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.wait));
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
	        	String strOTP = sharedPref.getString(Constants.CONTRACTOR_OTP, null);
				//serviceResponse = service.GetRouteByPhoneNo(valueHolder.getUsername(), Constants.DEVICE_ID, Constants.COMP_CODE, valueHolder.getDeliveryDate(), strPhone);
	        	serviceResponseListArr = service.GetDailyRouteInfoByOTP(valueHolder.getUsername(), Constants.DEVICE_ID, "", strPhone, strOTP);
				
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
			
			if(serviceResponseListArr.size() > 0)
			{
				String route_code = null;
				String deliver_name = null;
				String route_desc = null;
				String plate_no = null;
				String curr_dt = null;
				String del_seq = null;
				String company_Code = null;
				for(int i=0;i<serviceResponseListArr.size();i++) {
				
					route_code = serviceResponseListArr.get(i).get("ROUTE_CODE");
					//deliver_name = serviceResponseListArr.get(i).get("DELIVER_NAME");
					//route_desc = serviceResponseListArr.get(i).get("ROUTE_DESCRIPTION");
					//plate_no = serviceResponseListArr.get(i).get("PLATE_NO");
					curr_dt = serviceResponseListArr.get(i).get("DOC_DATE");
					del_seq = serviceResponseListArr.get(i).get("DELIVERY_SEQ");
					company_Code = serviceResponseListArr.get(i).get("COMPANY");
				}
				valueHolder.setRoutingCode(route_code.toUpperCase());
				valueHolder.setDeliverySeq(del_seq);
				valueHolder.setDeliverySeqNew(del_seq);
				valueHolder.setCompanyCode(company_Code.toUpperCase());
				//DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
				valueHolder.setDeliveryDate(curr_dt);
				valueHolder.setDeliveryDateNew(curr_dt);
				
				session.setPreferenceVal("ROUTE_CODE", route_code.toUpperCase());
				session.setPreferenceVal("ROUND", del_seq);
				session.setPreferenceVal("ASSIGN_DATE", curr_dt);
				session.setPreferenceVal("CHECKIN", "");
				
				GetRoutingTask getRoutingTask = new GetRoutingTask();
		        getRoutingTask.execute();
				
				}
				else
				{
					
//						SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
//			        	
//		        		SharedPreferences.Editor editor = sharedPref.edit();
//		        		editor.putString(Constants.CONTRACTOR_PHONE_NUMBER, null);
//		        		editor.commit();
//				
//			       	Intent intent = new Intent(getApplicationContext(), PhoneNumberActivity.class);
//					startActivity(intent);
					
	        		//showAlertDialog(this, "Error", "Mobile number registered by admin is not correct or no task assign for this number. Please conatct to admin.", true);

	        		Toast.makeText(getApplicationContext(), "Mobile number registered by admin is not correct or no task assign for this number. Please conatct to admin." ,Toast.LENGTH_LONG).show();

					//finish();
					
				}
	       	}else if(serviceResponse.equalsIgnoreCase("false")) {
				Toast.makeText(getApplicationContext(), R.string.loginfail,Toast.LENGTH_SHORT).show();
			}else {
				
			}
	}	
	
	

	protected void onPostExecute(String result) {
	}
	
	
	private class GetRouteSeq extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.wait));
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
					serviceResponseListSeq = service.GetRouteSeq(valueHolder.getUsername(), Constants.DEVICE_ID, valueHolder.getCompanyCode(), session.getPreferenceVal("ROUTE_CODE"), session.getPreferenceVal("ASSIGN_DATE"));
					
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
				 
				String roundVal = null;
				
				for(int i=0;i<serviceResponseListSeq.size();i++) {
					
					roundVal = serviceResponseListSeq.get(i).get("DELIVERY_SEQ");
		
				}
//				valueHolder.setDeliverySeqNew(roundVal);
//				session.setPreferenceVal("ROUND", roundVal.toUpperCase());
//        		etRound.setText(session.getPreferenceVal("ROUND"));

				}else if(serviceResponse.equalsIgnoreCase("false")) {
					Toast.makeText(getApplicationContext(), R.string.loginfail,Toast.LENGTH_SHORT).show();
				}else {
					
				}
		}		
		protected void onPostExecute(String result) {
		}
	}

	
	}

private class GetAppVersion extends AsyncTask<String, String, String> {
	protected void onPreExecute() {
		dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.wait));
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
	        	serviceResponseListArr = service.GetAppVersion(APPNAME);
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
			if(serviceResponseListArr.size() > 0)
			{
				String version =  serviceResponseListArr.get(0).get("APP_VERSION");
				final String url = serviceResponseListArr.get(0).get("UPDATE_URL");

//						version = Integer.parseInt(serviceResponseListArr.get(i).get("APP_VERSION").toString());
						if(!(version.equals(APPVERSION))) {
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
//							alertDialog.setMessage("New build is available. Please click Ok to download").setCancelable(false)
							alertDialog.setMessage(getString(R.string.new_build)).setCancelable(false)
							.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {			
									finish();
									dialog.cancel();
									Intent i = new Intent(Intent.ACTION_VIEW);
									i.setData(Uri.parse(url));
									startActivity(i);
								}
                    });
//							AlertDialog.Builder alertDialog1 = alertDialog1.create();
		    				alertDialog.show();	


				}
						else {
							GetProblemList getProblemList = new GetProblemList();
							getProblemList.execute();

						}
	       }
		}
	}	
	
	protected void onPostExecute(String result) {
	}
	}

private class GetProblemList extends AsyncTask<String, String, String> {
	protected void onPreExecute() {
		dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.wait));
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
				serviceResponseProblemList = service.getProblemList("",Constants.DEVICE_ID, "JB");
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
			if(serviceResponseProblemList.size() > 0)
			{
				valueHolder.setProblemList(serviceResponseProblemList);
	       }
			else {
				Toast.makeText(getApplicationContext(), "No data found in problem list. Try again",Toast.LENGTH_SHORT).show();
			}
		}
	}	
	
	protected void onPostExecute(String result) {
	}
		
	}
}






