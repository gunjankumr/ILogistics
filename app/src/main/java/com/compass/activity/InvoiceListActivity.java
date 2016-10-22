package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import com.compass.dbhelper.CheckInCheckOut;
import com.compass.dbhelper.CheckInCheckOutRepo;
import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.Invoice;
import com.compass.utils.AlertDialogManager;
import com.compass.utils.ConnectionDetector;
import com.compass.utils.Constants;
import com.compass.utils.SessionManager;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class InvoiceListActivity extends DashBoardActivity implements NetworkTimeAsyncResponse {
	
	private ValueHolder valueHolder;
	private SessionManager session;
	private ProgressDialog dialog;
	private AlertDialogManager alert = new AlertDialogManager();
	
	private ListView lstView;
	private EditText etFilter;
	private TextView txtCustName,txtCustInvoice;
	private Button btnCheck;
	private Button btnMap;
	private Button btnJob;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String TIMEOUT = "TIMEOUT";
	private static final String SUCCESS = "SUCCESS";

	private LocationManager locationManager;
	private LocationListener locationListener = new MyLocationListener();
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private boolean gpsLocation = false;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
		
	ListAdapter dataAdapter = null;
	
	private String serviceResponse = null;
	//private ArrayList<HashMap<String, String>> serviceResponseList;
	//private ArrayList<CustomerModel> serviceResponseList;
	private ArrayList<HashMap<String, String>> serviceResponseListIn;
	private ArrayList<HashMap<String, String>> serviceResponseListOut;
	private ArrayList<Invoice> routingList = new ArrayList<Invoice>();
	
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	Handler gpsHandler = new Handler();

	NetworkTimeReaderAsyncTask networkTimeAsyncTask = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_list);
        setHeader(getString(R.string.app_name), true, false, true);
        session = new SessionManager(getApplicationContext());
        valueHolder = ValueHolder.getSingletonObject();
        etFilter = (EditText) findViewById(R.id.myFilter);
        lstView = (ListView)findViewById(R.id.list);
        txtCustName = (TextView)findViewById(R.id.custName);
        txtCustInvoice = (TextView)findViewById(R.id.custInvoice);
        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnJob = (Button) findViewById(R.id.btnJob);
        
        CustomerModel customer  = valueHolder.getCustomerList().get(valueHolder.getCustomerSelected());
		for(int i=0;i<customer.getInvoiceList().size();i++) {
			Invoice invoice = new Invoice(
					String.valueOf(i),
					customer.getInvoiceList().get(i).getCOMPANY(),
					customer.getInvoiceList().get(i).getTRANSACTION_TYPE(),
					customer.getInvoiceList().get(i).getINV_BOOK(),
					customer.getInvoiceList().get(i).getINV_NO(),
					customer.getInvoiceList().get(i).getINV_BOOK_NO(),
					customer.getInvoiceList().get(i).getINV_AMOUNT(),
					customer.getInvoiceList().get(i).getPAYMENT_TYPE(),
					customer.getInvoiceList().get(i).getJOB_ORDER(),
					customer.getInvoiceList().get(i).getINV_STATUS(),
					customer.getInvoiceList().get(i).getINV_LATITUDE(),
					customer.getInvoiceList().get(i).getINV_LONGITUDE(),
					customer.getInvoiceList().get(i).getINV_EXPECTDTIME());
			invoice.setINV_PROBLEM(customer.getInvoiceList().get(i).getINV_PROBLEM());
			if(customer.getEXPECTDTIME().length() > 0) {
				invoice.setINV_EXPECTDTIME("");
			}else {
				invoice.setINV_EXPECTDTIME(customer.getInvoiceList().get(i).getINV_EXPECTDTIME());
			}
			
			if(customer.getInvoiceList().get(0).getINV_LATITUDE().length() > 0 && customer.getInvoiceList().get(0).getINV_LONGITUDE().length() > 0) {
				//btnMap.setVisibility(View.VISIBLE);
				btnMap.setEnabled(true); 
			}
			else {
				//btnMap.setVisibility(View.INVISIBLE);
				btnMap.setEnabled(false);
			}
			
			if(customer.getInvoiceList().get(0).getJOB_ORDER().equalsIgnoreCase("No Job Order")) {
				//btnJob.setVisibility(View.INVISIBLE);
				btnJob.setEnabled(false);
			}
			else {
				//btnJob.setVisibility(View.VISIBLE);
				btnJob.setEnabled(true);
			}
			
			routingList.add(invoice);
		}
		
		txtCustName.setText(customer.getCUST_CODE()+ " - "+ customer.getCUST_NAME());
        txtCustInvoice.setText(customer.getInvoiceList().size() +  " " + getString(R.string.invoices));
        if(valueHolder.getMenuID().equalsIgnoreCase("menu_admin"))
        {
        	btnCheck.setVisibility(View.INVISIBLE);
        }
        else
        {
        	if(customer.getCOMPLETE_STATUS().equalsIgnoreCase("Y") || customer.getCOMPLETE_STATUS().equalsIgnoreCase("W"))
        	{
        		 btnCheck.setVisibility(View.INVISIBLE);
        	}
        	else if(customer.getCHECKIN().equalsIgnoreCase("Y")) {
        		 btnCheck.setVisibility(View.VISIBLE);
        		 btnCheck.setText(getString(R.string.checkout));
//        	     setHeader(getString(R.string.app_name), true, false, false);
					btnHomeClickStatus(false);

        		 Button btnHome = (Button) findViewById(R.id.btnHome);
        		 btnHome.setEnabled(false);
        		 Drawable drawableTop = getResources().getDrawable(R.drawable.flag_red32);
        		 btnCheck.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop , null, null);
        	 }else {
        		 if(!session.getPreferenceVal("CHECKIN").equalsIgnoreCase("")) {
        			 btnCheck.setVisibility(View.INVISIBLE);
        		 }else {
        			 btnCheck.setVisibility(View.VISIBLE);
        		 }
        	/*
	        if((valueHolder.getLatitude().length() == 0) && (valueHolder.getLongitude().length() == 0)) {
	        	btnCheck.setVisibility(View.VISIBLE);
	        }else {
	        	btnCheck.setVisibility(View.INVISIBLE);
	        }
	        */
        	}
        }
        
        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.invoice_item, routingList);
        lstView.setAdapter(dataAdapter);
        lstView.setTextFilterEnabled(true);
        /*
        lstView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {			
        		valueHolder.setInvoiceSelected(position);
        		valueHolder.setChildSelected(false);
        		startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
			}
		});
        */
        etFilter.addTextChangedListener(new TextWatcher() {
        	public void afterTextChanged(Editable s) { }			 
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dataAdapter.getFilter().filter(s.toString());
			}
		 });
	
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        
    }

	@Override public void onNetworkDateTimeObtained(String networkDateTime, CheckInOutType checkInOutType) {
		CheckInCheckOut record = new CheckInCheckOut();
		record.username = valueHolder.getUsername();
		record.deviceId = Constants.DEVICE_ID;
		record.companyCode = valueHolder.getCompanyCode();
		record.customerCode = valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).getCUST_CODE();
		record.branchCode = valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).getBRANCH_CODE();
		record.routeCode = valueHolder.getRoutingCode();
		record.deliverySequence = valueHolder.getDeliverySeq();
		record.deliveryDate = valueHolder.getDeliveryDate();
		record.latitude = valueHolder.getLatitude();
		record.longitude = valueHolder.getLongitude();

		CheckInCheckOutRepo repo = new CheckInCheckOutRepo(this);

		switch (checkInOutType) {
			case CHECK_IN:
				record.checkInTime = networkDateTime;

				if (repo.isRecordAvailable(record)) {
					repo.update(record, checkInOutType);
				} else {
					record.checkOutTime = "0";
					repo.insert(record);
				}
				habdleDbCheckIn();
				break;

			case CHECK_OUT:
				record.checkOutTime = networkDateTime;

				if (repo.isRecordAvailable(record)) {
					repo.update(record, checkInOutType);
				} else {
					record.checkInTime = "0";
					repo.insert(record);
				}
				habdleDbCheckOut();
				break;
			default:
		}
	}

	private void habdleDbCheckIn() {
		if(btnCheck.getText().toString().equalsIgnoreCase("CHECK IN") || btnCheck.getText().toString().equalsIgnoreCase("เช็คอิน")) {
			btnCheck.setText(getString(R.string.checkout));
			btnHomeClickStatus(false);

			Drawable drawableTop = getResources().getDrawable(R.drawable.flag_red32);
			btnCheck.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop , null, null);
		}
		String londitude = "Londitude: "+ILogisticsApplication.currentLatitude;
		String latitude = "Latitude: "+ILogisticsApplication.currentLongitude;
		Time now = new Time();
		valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).setCHECKIN("Y");
		valueHolder.setLatitude(Double.toString(ILogisticsApplication.currentLatitude));
		valueHolder.setLongitude(Double.toString(ILogisticsApplication.currentLongitude));
		valueHolder.setCheckInTime(String.valueOf(now.hour)+":"+String.valueOf(now.minute));
	}

	private void habdleDbCheckOut() {
		session.setPreferenceVal("CHECKIN", "");
		valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).setCHECKIN("N");
		valueHolder.setLatitude("");
		valueHolder.setLongitude("");
		valueHolder.setCheckInTime("");
		btnCheck.setText(getString(R.string.checkin));
		Drawable drawableTop = getResources().getDrawable(R.drawable.flag_green32);
		btnCheck.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop , null, null);
		btnCheck.setVisibility(View.INVISIBLE);
		btnHomeClickStatus(true);
	}

	/*************/
    private class ListAdapter extends ArrayAdapter<Invoice> {
      	 
    	  private ArrayList<Invoice> originalList;
    	  private ArrayList<Invoice> filterList;
    	  private CustomerFilter filter;
    	 
    	  public ListAdapter(Context context, int textViewResourceId, ArrayList<Invoice> fList) {
    	   super(context, textViewResourceId, fList);
    	   this.filterList = new ArrayList<Invoice>();
    	   this.filterList.addAll(fList);
    	   this.originalList = new ArrayList<Invoice>();
    	   this.originalList.addAll(fList);
    	  }

	  	 @Override
	  	 public Filter getFilter() {
	  		 if (filter == null){
	  			 filter  = new CustomerFilter();
	  		 }
	  		 return filter;
	  	 }
	 
		 private class ViewHolder {
			 ImageView text1;
			 TextView text2;
			 TextView text3;
			 TextView text4;
			 TextView text5;
			 Button   buttonV;
		 }
    	 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  ViewHolder holder = null;
			  if (convertView == null) {
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.invoice_item, null);
				  holder = new ViewHolder();
				  holder.text1 = (ImageView) convertView.findViewById(R.id.textView1);
				  holder.text2 = (TextView) convertView.findViewById(R.id.textView2);
				  holder.text3 = (TextView) convertView.findViewById(R.id.textView3);
				  holder.text4 = (TextView) convertView.findViewById(R.id.textView4);
				  holder.text5 = (TextView) convertView.findViewById(R.id.textView5);
				  holder.buttonV = (Button) convertView.findViewById(R.id.btnView);
				  convertView.setTag(holder);
			  } else {
			   	holder = (ViewHolder) convertView.getTag();
			  }
		 
			  Invoice routing = filterList.get(position);
			  //holder.text1.setText(routing.getINV_BOOK_NO());
			  if(routing.getINV_STATUS().equalsIgnoreCase("Y")) {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_full32);
				//  btnCheck.setVisibility(View.INVISIBLE);
			  }else if(routing.getINV_STATUS().equalsIgnoreCase("W")) {
				 //holder.text1.setBackgroundResource(R.drawable.checkbox_checkedred_32);
				  holder.text1.setBackgroundResource(R.drawable.checkboxred_full32);
			  }else {
				  holder.text1.setBackgroundResource(R.drawable.checkbox_unchecked_32);
			  }
			  if(routing.getJOB_ORDER().equalsIgnoreCase("No Job Order")) {
				  holder.text2.setText(routing.getINV_BOOK_NO() + "  "+routing.getINV_EXPECTDTIME());
			  }
			  else {
				  holder.text2.setText(routing.getINV_BOOK_NO() + "  "+routing.getJOB_ORDER().trim()+ "  "+routing.getINV_EXPECTDTIME());
			  }
			  if(routing.getPAYMENT_TYPE().equalsIgnoreCase("CASH")) {
				  holder.text3.setText("CASH : "+routing.getINV_AMOUNT());
				  ((LinearLayout)convertView.findViewById(R.id.r2)).setVisibility(View.VISIBLE);
			  }else {
				  ((LinearLayout)convertView.findViewById(R.id.r2)).setVisibility(View.GONE);
				  holder.text3.setText("");
			  }
			  holder.text4.setText("");
			  if(routing.getINV_PROBLEM().equalsIgnoreCase("No Problem")) {
				  holder.text5.setText("");
			  }
			  else {
				  holder.text5.setText(routing.getINV_PROBLEM());
			  }
			  if(routing.getINV_PROBLEM().length() == 0) {
				  ((LinearLayout)convertView.findViewById(R.id.r3)).setVisibility(View.GONE);
			  }
			  holder.buttonV.setTag(Integer.parseInt(routing.getPOSITION()));
			  holder.buttonV.setOnClickListener(onViewClickListener);
  
			  convertView.setBackgroundColor(colors[position % colors.length]);
			   
		   return convertView;
		 
		  }
		  
		  private OnClickListener onViewClickListener = new OnClickListener() {
		        @Override
		        public void onClick(View v) {
		            final int position = lstView.getPositionForView((View) v.getParent());
	      		  	//valueHolder.setInvoiceSelected(position);
		            //if(valueHolder.getLatitude().length() > 0) {
			            valueHolder.setInvoiceSelected((Integer)v.getTag());
		      		  	valueHolder.setChildSelected(false);
	    			//	valueHolder.setIsFromProductProblemTable("False");

		      		  	startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
		            //}
		        }
		    };
    	 
	private class CustomerFilter extends Filter {
    	   @Override
    	   protected FilterResults performFiltering(CharSequence constraint) {
    		   constraint = constraint.toString().toLowerCase();
    		   FilterResults result = new FilterResults();
    		   if(constraint != null && constraint.toString().length() > 0) {
    			   ArrayList<Invoice> filteredItems = new ArrayList<Invoice>();
    			   for(int i = 0, l = originalList.size(); i < l; i++) {
    				   Invoice country = originalList.get(i);
    				   if(country.toString().toLowerCase().contains(constraint))
    					   filteredItems.add(country);
    			   }
    			   result.count = filteredItems.size();
    			   result.values = filteredItems;
    		   } else {
    			   synchronized(this) {
    				   result.values = originalList;
    				   result.count = originalList.size();
    			   }
    		   }
    		   return result;
    	   }
    	 
    	   @SuppressWarnings("unchecked")
    	   @Override
    	   protected void publishResults(CharSequence constraint,FilterResults results) {
    		   filterList = (ArrayList<Invoice>)results.values;
    		   notifyDataSetChanged();
    		   clear();
    		   for(int i = 0, l = filterList.size(); i < l; i++) {
    			   add(filterList.get(i));
    		   }
    		   notifyDataSetInvalidated();
    	   	}
		}
    }
/*************/    
    
    private class NoGpsLock implements Runnable {
        public void run() {
        	Log.d(Constants.LOGD, "------request checkVersion------------");
            //if (!gps_enabled) {
            	//if (locationManager != null) {
        		if(!gpsLocation) {
            		Toast toast = Toast.makeText(getApplicationContext(), "Unable to find Location from GPS", Toast.LENGTH_LONG);
                    toast.show();
            		locationManager.removeUpdates(locationListener);
            		if (network_enabled) {
    					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    				}
            		dialog.dismiss();
            	}
            //}
        }
    }
    

    class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				// This needs to stop getting the location data and save the battery power.
				gpsLocation = true;
				locationManager.removeUpdates(locationListener);
				//gpsHandler.removeCallbacksAndMessages(null);
				session.setPreferenceVal("CHECKIN" , valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).getCUST_CODE()+"-"+valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).getBRANCH_CODE());
				if(btnCheck.getText().toString().equalsIgnoreCase("CHECK IN") || btnCheck.getText().toString().equalsIgnoreCase("เช็คอิน")) {
					btnCheck.setText(getString(R.string.checkout));
//			        setHeader(getString(R.string.app_name), true, false, false);
					btnHomeClickStatus(false);

					Drawable drawableTop = getResources().getDrawable(R.drawable.flag_red32);
					btnCheck.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop , null, null);
				}
//				String londitude = "Londitude: " + location.getLongitude();
//				String latitude = "Latitude: " + location.getLatitude();
				
				 String londitude = "Londitude: "+ILogisticsApplication.currentLatitude;
		    	String longitude = "Latitude: "+ILogisticsApplication.currentLongitude;
		    	valueHolder.setLatitude(londitude);
		    	valueHolder.setLatitude(longitude);
		    	
				//String altitiude = "Altitiude: " + location.getAltitude();
				//String accuracy = "Accuracy: " + location.getAccuracy();
				Time now = new Time();
				valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).setCHECKIN("Y");
				valueHolder.setLatitude(Double.toString(ILogisticsApplication.currentLatitude));
				valueHolder.setLongitude(Double.toString(ILogisticsApplication.currentLongitude));
//				valueHolder.setDeliveryDate(Utilities.getCurrenceDate());
				valueHolder.setCheckInTime(String.valueOf(now.hour)+":"+String.valueOf(now.minute));
				dialog.dismiss();				
			} 
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), provider ,Toast.LENGTH_SHORT).show();
		}
	}
    
    public void onButtonClicker(View v)
    {
    	Intent intent;
    	switch (v.getId()) {
		case R.id.btnMap:
			intent = new Intent(InvoiceListActivity.this, RouteMapActivity.class);
			intent.putExtra("map", "view_map");
			startActivity(intent);
			break;
		case R.id.btnJob:
			intent = new Intent(InvoiceListActivity.this, RouteJobListActivity.class);
			startActivity(intent);
			break;
		case R.id.btnCheck:						
			if(btnCheck.getText().toString().equalsIgnoreCase("CHECK IN") || btnCheck.getText().toString().equalsIgnoreCase("เช็คอิน")) {
				
//				dialog = ProgressDialog.show(InvoiceListActivity.this, "", getString(R.string.findLocation));
//				dialog.setCancelable(false);								
//				try {
//					gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//				} catch (Exception ex) {
//				}
//				try {
//					network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//				} catch (Exception ex) {
//				}
//				
//				if (!gps_enabled && !network_enabled) {
//					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InvoiceListActivity.this);
//	    			alertDialogBuilder.setTitle("Location Connection Fail");
//	    			alertDialogBuilder
//	    				.setMessage("location is not determined. Please enable location providers")
//	    				.setCancelable(true)
//	    				.setIcon(R.drawable.fail)
//	    				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
//	    					public void onClick(DialogInterface dialog,int id) {
//	    						dialog.cancel();
//	    						startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));   						
//	    						//new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);    							                                
//	    					}
//	    				});
//	    				AlertDialog alertDialog = alertDialogBuilder.create();
//	    				alertDialog.show();	
//				}
				if (new ConnectionDetector(this).isConnectingToInternet()) {
					new CheckinThread().execute();
				} else {
					networkTimeAsyncTask = new NetworkTimeReaderAsyncTask(this);
					networkTimeAsyncTask.setCheckInOutType(CheckInOutType.CHECK_IN);
					networkTimeAsyncTask.execute();
				}
//				
//				if (gps_enabled) {
//					gpsLocation = false;
//					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//					//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
//					//Handler gpsHandler = new Handler();
//					
//					gpsHandler.postDelayed(new NoGpsLock(), 15000); //12seconds			
//				}
				
				/*
				if (network_enabled) {
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
				}
				*/
				
				
				/*
				// You need to specify a Criteria for picking the location data source.
				// The criteria can include power requirements.
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_COARSE);  // Faster, no GPS fix.
				criteria.setAccuracy(Criteria.ACCURACY_FINE);  // More accurate, GPS fix.
				// You can specify the time and distance between location updates.
				// Both are useful for reducing power requirements.
				Toast.makeText(getApplicationContext(), locationManager.getBestProvider(criteria, true) ,Toast.LENGTH_SHORT).show();
				locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, true),MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener , getMainLooper());
				*/

			}else {
	    		//if(!(btnCheck.getText().toString().equalsIgnoreCase("CHECK IN") || btnCheck.getText().toString().equalsIgnoreCase("‡™Á§Õ‘π"))) {
				if (new ConnectionDetector(this).isConnectingToInternet()) {
					new CheckoutThread().execute();
				} else {
					networkTimeAsyncTask = new NetworkTimeReaderAsyncTask(this);
					networkTimeAsyncTask.setCheckInOutType(CheckInOutType.CHECK_OUT);
					networkTimeAsyncTask.execute();
				}
					/*
					session.setPreferenceVal("CHECKIN", "");
					valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).setCHECKIN("N");
					valueHolder.setLatitude("");
					valueHolder.setLongitude("");
					valueHolder.setCheckInTime("");
					btnCheck.setText(getString(R.string.checkin));
					Drawable drawableTop = getResources().getDrawable(R.drawable.flag_green32);
					btnCheck.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop , null, null);
					*/
				//}
			}
			
		default:
			break;
		}
    }
        
    
    public boolean isGpsEnable(){ // ‡¡∏Õ¥µ√«® Õ∫«Ë“‡ª‘¥ GPS À√◊Õ‰¡Ë
        boolean isgpsenable = false;
        String provider = Settings.Secure.getString(getContentResolver(),Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	       if(!provider.equals("")){ //GPS is Enabled
	           isgpsenable = true;                
	       }       
        return isgpsenable;
    }
    
    private class CheckinThread extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(InvoiceListActivity.this, "", getString(R.string.wait));
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
					serviceResponseListIn = service.CheckIn(valueHolder.getUsername(),
							Constants.DEVICE_ID,
							valueHolder.getCompanyCode(),
							valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).getCUST_CODE(),
							valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).getBRANCH_CODE(),
							valueHolder.getRoutingCode(),
							valueHolder.getDeliverySeq(),
							valueHolder.getDeliveryDate(),
							valueHolder.getLatitude(),
							valueHolder.getLongitude());
					System.out.print(serviceResponseListIn);
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
//				if(serviceResponse.equalsIgnoreCase("true")) {
//					
//				}else if(serviceResponse.equalsIgnoreCase("false")) {
//					Toast.makeText(getApplicationContext(), R.string.serviceFail,Toast.LENGTH_SHORT).show();
//				}
				int flag = 0 ;
				String msg = null;
				
				for(int i=0;i<serviceResponseListIn.size();i++) {
					
					flag = Integer.parseInt(serviceResponseListIn.get(i).get("FLAG").toString());
					msg = serviceResponseListIn.get(i).get("MSG");
				}
				System.out.println(msg);
				if(flag == 1)
				{
//					System.out.println(msg);
//					dialog = ProgressDialog.show(InvoiceListActivity.this, "", getString(R.string.findLocation));
//					dialog.setCancelable(false);								
//					try {
//						gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//					} catch (Exception ex) {
//					}
//					try {
//						network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//					} catch (Exception ex) {
//					}
//					
//					if (!gps_enabled && !network_enabled) {
//						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InvoiceListActivity.this);
//		    			alertDialogBuilder.setTitle("Location Connection Fail");
//		    			alertDialogBuilder
//		    				.setMessage("location is not determined. Please enable location providers")
//		    				.setCancelable(true)
//		    				.setIcon(R.drawable.fail)
//		    				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
//		    					public void onClick(DialogInterface dialog,int id) {
//		    						dialog.cancel();
//		    						startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));   						
//		    						//new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);    							                                
//		    					}
//		    				});
//		    				AlertDialog alertDialog = alertDialogBuilder.create();
//		    				alertDialog.show();	
//					}
//				//	new CheckinThread().execute();
//
//					
//					if (gps_enabled) {
//						gpsLocation = false;
//						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//						//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
//						//Handler gpsHandler = new Handler();
//						
//						gpsHandler.postDelayed(new NoGpsLock(), 15000); //12seconds			
//					}
					if(btnCheck.getText().toString().equalsIgnoreCase("CHECK IN") || btnCheck.getText().toString().equalsIgnoreCase("เช็คอิน")) {
						btnCheck.setText(getString(R.string.checkout));
//				        setHeader(getString(R.string.app_name), true, false, false);
						btnHomeClickStatus(false);

						Drawable drawableTop = getResources().getDrawable(R.drawable.flag_red32);
						btnCheck.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop , null, null);
					}
					 String londitude = "Londitude: "+ILogisticsApplication.currentLatitude;
				    	String latitude = "Latitude: "+ILogisticsApplication.currentLongitude;
						//String altitiude = "Altitiude: " + location.getAltitude();
						//String accuracy = "Accuracy: " + location.getAccuracy();
						Time now = new Time();
						valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).setCHECKIN("Y");
						valueHolder.setLatitude(Double.toString(ILogisticsApplication.currentLatitude));
						valueHolder.setLongitude(Double.toString(ILogisticsApplication.currentLongitude));
//						valueHolder.setDeliveryDate(Utilities.getCurrenceDate());
						valueHolder.setCheckInTime(String.valueOf(now.hour)+":"+String.valueOf(now.minute));
						dialog.dismiss();	
				}
				else
				{
					Toast.makeText(getApplicationContext(), R.string.serviceFail,Toast.LENGTH_SHORT).show();
				}
			}
		}		
		protected void onPostExecute(String result) {
		}
	}

    private class CheckoutThread extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(InvoiceListActivity.this, "", getString(R.string.wait));
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
					serviceResponseListOut = service.checkOut(valueHolder.getUsername(),
							Constants.DEVICE_ID,
							valueHolder.getCompanyCode(),
							valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).getCUST_CODE(),
							valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).getBRANCH_CODE(),
							valueHolder.getRoutingCode(),
							valueHolder.getDeliverySeq(),
							valueHolder.getDeliveryDate());
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
				
				int flag = 0 ;
				String msg = null;
				
				for(int i=0;i<serviceResponseListOut.size();i++) {
					
					flag = Integer.parseInt(serviceResponseListOut.get(i).get("FLAG").toString());
					msg = serviceResponseListOut.get(i).get("MSG");
				}
				System.out.println(msg);
				if(flag == 1)
				{
					session.setPreferenceVal("CHECKIN", "");
					valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).setCHECKIN("N");
					valueHolder.setLatitude("");
					valueHolder.setLongitude("");
					valueHolder.setCheckInTime("");
					btnCheck.setText(getString(R.string.checkin));
					Drawable drawableTop = getResources().getDrawable(R.drawable.flag_green32);
					btnCheck.setCompoundDrawablesWithIntrinsicBounds(null, drawableTop , null, null);
					btnCheck.setVisibility(View.INVISIBLE);
//			        setHeader(getString(R.string.app_name), true, false, false);
					btnHomeClickStatus(true);
				}
				else
				{
					Toast.makeText(getApplicationContext(), R.string.serviceFail,Toast.LENGTH_SHORT).show();
				}
			}
		}		
		protected void onPostExecute(String result) {
		}
	}
    
    /*
    @Override
    protected void onResume() {
    	Log.i(Constants.LOGI, getClass().getName()+" RESUME");
    	super.onResume();
    }
    @Override
    protected void onStart() {
    	Log.i(Constants.LOGI, getClass().getName()+" START");
    	super.onStart();
    }
    @Override
    protected void onPause() {
    	Log.i(Constants.LOGI, getClass().getName()+" PAUSE");
    	super.onPause();
    }
    @Override
    protected void onStop() {
    	Log.i(Constants.LOGI, getClass().getName()+" STOP");
    	super.onStop();
    }
    */
    
    
    
    @Override
    protected void onRestart() {
    	Log.i(Constants.LOGI, getClass().getName()+" RESTART");
    	dataAdapter.clear();
    	CustomerModel customer  = valueHolder.getCustomerList().get(valueHolder.getCustomerSelected());
		for(int i=0;i<customer.getInvoiceList().size();i++) {					
			Invoice invoice = new Invoice(
					String.valueOf(i),
					customer.getInvoiceList().get(i).getCOMPANY(),
					customer.getInvoiceList().get(i).getTRANSACTION_TYPE(),
					customer.getInvoiceList().get(i).getINV_BOOK(),
					customer.getInvoiceList().get(i).getINV_NO(),
					customer.getInvoiceList().get(i).getINV_BOOK_NO(),
					customer.getInvoiceList().get(i).getINV_AMOUNT(),
					customer.getInvoiceList().get(i).getPAYMENT_TYPE(),
					customer.getInvoiceList().get(i).getJOB_ORDER(),
					customer.getInvoiceList().get(i).getINV_STATUS(),
					customer.getInvoiceList().get(i).getINV_LATITUDE(),
					customer.getInvoiceList().get(i).getINV_LONGITUDE(),
					customer.getInvoiceList().get(i).getINV_EXPECTDTIME());
			invoice.setINV_PROBLEM(customer.getInvoiceList().get(i).getINV_PROBLEM());
			if(customer.getEXPECTDTIME().length() > 0) {
				invoice.setINV_EXPECTDTIME("");
			}else {
				invoice.setINV_EXPECTDTIME(customer.getInvoiceList().get(i).getINV_EXPECTDTIME());
			}
			routingList.add(invoice);
		}

        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.invoice_item, routingList);
        lstView.setAdapter(dataAdapter);
        dataAdapter.getFilter().filter(etFilter.getText().toString());
    	super.onRestart();
    }
    
    @Override
    protected void onDestroy() {
    	//valueHolder.clearLocation();
        super.onDestroy();
    }
    
    @Override
    public void onBackPressed() {
    	if(!valueHolder.getCustomerList().get(valueHolder.getCustomerSelected()).getCHECKIN().equalsIgnoreCase("Y")) {
    		super.onBackPressed();
    	}else {
    		Toast.makeText(getApplicationContext(), getString(R.string.checkout_loc),Toast.LENGTH_SHORT).show();
    	}
    }
}
