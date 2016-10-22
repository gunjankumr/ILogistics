package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.Routing;
import com.compass.utils.AlertDialogManager;
import com.compass.utils.SessionManager;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;


public class RoutingListActivity extends DashBoardActivity {
	
	private ValueHolder valueHolder;
	private SessionManager session;
	private ProgressDialog dialog;
	private AlertDialogManager alert = new AlertDialogManager();
	
	private ListView lstView;
	private EditText etDate, etFilter;
	private GetRouting getRouting; 
	private GetRoutingTask getRoutingTask;
	
	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";
	
	ListAdapter dataAdapter = null;
	
	private String serviceResponse = null;
	private ArrayList<HashMap<String, String>> serviceResponseList;
	private ArrayList<Routing> routingList = new ArrayList<Routing>();
	
	private ArrayList<CustomerModel> serviceResponseTaskList;
	
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routing_list);
        setHeader(getString(R.string.app_name), false, false, true);
        session = new SessionManager(getApplicationContext());
        valueHolder = ValueHolder.getSingletonObject();
        etDate=(EditText)findViewById(R.id.deliveryDate);
        etFilter = (EditText) findViewById(R.id.myFilter);
        lstView = (ListView)findViewById(R.id.list);        
        
        etDate.setText(valueHolder.getDeliveryDate());
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
		        	//etDate.setText("");
		        }else {
		        	final Calendar c = Calendar.getInstance();
					int y = c.get(Calendar.YEAR);
					int m = c.get(Calendar.MONTH);
					int d = c.get(Calendar.DAY_OF_MONTH);
					//final String[] MONTH = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
					DatePickerDialog dp = new DatePickerDialog(RoutingListActivity.this,new DatePickerDialog.OnDateSetListener() {
								public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
									String month = String.valueOf(monthOfYear + 1);
									String erg = "";
									erg = String.valueOf(dayOfMonth);
									erg = (erg.length() < 2) ? "0".concat(erg) : erg;
									month = (month.length() < 2) ? "0".concat(month) : month;
									erg += "/" + month;
									erg += "/" + year;
									((TextView) etDate).setText(erg);
									valueHolder.setDeliveryDate(erg);
									startActivity(new Intent(RoutingListActivity.this, RoutingListActivity.class));
									finish();
								}
							}, y, m, d);
					dp.setTitle("Calender");
					dp.setMessage("Select Your Delivery Date Please?");
					dp.show();
		        }
		        return false;
		    }
		});
		

        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.routing_item, routingList);
        lstView.setAdapter(dataAdapter);
        lstView.setTextFilterEnabled(true);

        lstView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				TextView routingCode = (TextView)view.findViewById(R.id.textView2);
				String routeCode = routingCode.getText().toString().substring(0,routingCode.getText().toString().indexOf(","));
				String deliverySeq = routingCode.getText().toString().substring(routingCode.getText().toString().indexOf(",")+1);
				valueHolder.setRoutingCode(routeCode);
				valueHolder.setDeliverySeq(deliverySeq);
				getRoutingTask = new GetRoutingTask();
				getRoutingTask.execute();
			}
		});
        
        etFilter.addTextChangedListener(new TextWatcher() { 
        	public void afterTextChanged(Editable s) { }			 
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dataAdapter.getFilter().filter(s.toString());
			}
		 });
  
        getRouting = new GetRouting();
        getRouting.execute();
    }

    public class GetRouting extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(RoutingListActivity.this, "", getString(R.string.wait));
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
					serviceResponseList = service.getRouting(valueHolder.getUsername(), "123456", valueHolder.getCompanyCode(), valueHolder.getRoutingCode(),"%", valueHolder.getDeliveryDate(),1);
					publishProgress(SUCCESS); //if everything is Okay then publish this message, you may also use onPostExecute() method  
				} catch (UnknownHostException e) {
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
				dialog.dismiss();
			}else if(errorCode[0].toString().equalsIgnoreCase(NETWORK_ERROR)) {	
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Network connection error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SERVER_ERROR)) {	
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Server error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SUCCESS)) {		
				dialog.dismiss();
				routingList = new ArrayList<Routing>();
				for(int i=0;i<serviceResponseList.size();i++) {
					Routing routing = new Routing(
							"Code",
							serviceResponseList.get(i).get("ROUTE_CODE"),
							serviceResponseList.get(i).get("ROUTE_NAME"),
							serviceResponseList.get(i).get("DELIVERY_SEQ"),
							serviceResponseList.get(i).get("PLATE_NO"),
							serviceResponseList.get(i).get("SUP_NAME"),
							serviceResponseList.get(i).get("DELIVER_NAME"),
							serviceResponseList.get(i).get("PICKUP_TIME"),
							serviceResponseList.get(i).get("LOCATION"),
							serviceResponseList.get(i).get("TOTAL_CUST"),
							serviceResponseList.get(i).get("TOTAL_INVOICE"));
					routingList.add(routing);
				}
		        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.routing_item, routingList);
		        lstView.setAdapter(dataAdapter);
			}
		}		
		protected void onPostExecute(String result) {}
	}
  

/*************/
    private class ListAdapter extends ArrayAdapter<Routing> {
      	 
    	  private ArrayList<Routing> originalList;
    	  private ArrayList<Routing> filterList;
    	  private CustomerFilter filter;
    	 
    	  public ListAdapter(Context context, int textViewResourceId, ArrayList<Routing> fList) {
    	   super(context, textViewResourceId, fList);
    	   this.filterList = new ArrayList<Routing>();
    	   this.filterList.addAll(fList);
    	   this.originalList = new ArrayList<Routing>();
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
			 TextView text1;
			 TextView text2;
			 TextView text3;
			 TextView text4;
			 TextView text5;
			 TextView text6;
			 TextView text7;
		 }
    	 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  ViewHolder holder = null;
			  if (convertView == null) {  	 
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.routing_item, null);
				  holder = new ViewHolder();
				  holder.text1 = (TextView) convertView.findViewById(R.id.textView1);
				  holder.text2 = (TextView) convertView.findViewById(R.id.textView2);
				  holder.text3 = (TextView) convertView.findViewById(R.id.textView3);
				  holder.text4 = (TextView) convertView.findViewById(R.id.textView4);
				  holder.text5 = (TextView) convertView.findViewById(R.id.textView5);
				  holder.text6 = (TextView) convertView.findViewById(R.id.textView6);
				  holder.text7 = (TextView) convertView.findViewById(R.id.textView7);
				  convertView.setTag(holder);
			  } else {
			   	holder = (ViewHolder) convertView.getTag();
			  }

			  Routing routing = filterList.get(position);
			  holder.text1.setText("Route : "+routing.getROUTE_CODE() +" - "+routing.getROUTE_NAME()+ " , Round : "+routing.getDELIVERY_SEQ());
			  holder.text2.setText(routing.getROUTE_CODE()+","+routing.getDELIVERY_SEQ());
			  holder.text3.setText("Driver : "+routing.getDRIVER_NAME()+ "  "+ "Truck : "+routing.getPLATE_NO());
			  //holder.text4.setText(routing.getSUP_NAME());
			  holder.text4.setText("");
			  holder.text5.setText("Location : "+routing.getLOCATION()+" Time : "+routing.getPICKUP_TIME());
			  holder.text6.setText("");
			  holder.text7.setText(routing.getTOTAL_CUST()+ " Customers  -  "+routing.getTOTAL_INVOICE()+ " Invoices. ");
  
			  convertView.setBackgroundColor(colors[position % colors.length]);
			   
		   return convertView;
		 
		  }
    	 
	private class CustomerFilter extends Filter {
    	   @Override
    	   protected FilterResults performFiltering(CharSequence constraint) {
    		   constraint = constraint.toString().toLowerCase();
    		   FilterResults result = new FilterResults();
    		   if(constraint != null && constraint.toString().length() > 0) {
    			   ArrayList<Routing> filteredItems = new ArrayList<Routing>();
    			   for(int i = 0, l = originalList.size(); i < l; i++) {
    				   Routing country = originalList.get(i);
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
    		   filterList = (ArrayList<Routing>)results.values;
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
    
    
    
    
    public class GetRoutingTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(RoutingListActivity.this, "", getString(R.string.wait));
			dialog.setCancelable(false);
			/*
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
	            public void onCancel(DialogInterface dialog) {
	            	getRoutingTask.cancel(true);
	            	startActivity(new Intent(getApplicationContext(), HomeActivity.class));
	        		finish();
	            }
			});
			*/
		}		
		@Override
		protected String doInBackground(String... params) {
				WebService service = new WebService();
				try {
					if (isCancelled()) {
	                    publishProgress(CANCELLED); //Notify your activity that you had canceled the task
	                    return (null); // don't forget to terminate this method
	                }
					serviceResponseTaskList = service.getRoutingDetails1(valueHolder.getUsername(), "123456", valueHolder.getCompanyCode(),valueHolder.getRoutingCode(),valueHolder.getDeliverySeq(),valueHolder.getDeliveryDate());
					publishProgress(SUCCESS); //if everything is Okay then publish this message, you may also use onPostExecute() method  
				} catch (UnknownHostException e) {
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
				dialog.dismiss();
			}else if(errorCode[0].toString().equalsIgnoreCase(NETWORK_ERROR)) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Network connection error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SERVER_ERROR)) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Server error",Toast.LENGTH_SHORT).show();
			}else if(errorCode[0].toString().equalsIgnoreCase(SUCCESS)) {
//				dialog.dismiss();
//				valueHolder.setCustomerList(serviceResponseTaskList);
//				startActivity(new Intent(RoutingListActivity.this, MenuListActivity.class));
			}
		}
		protected void onPostExecute(String result) {
			dialog.dismiss();
			valueHolder.setCustomerList(serviceResponseTaskList);				
			startActivity(new Intent(RoutingListActivity.this, MenuListActivity.class));
		}
	}

    @Override
    protected void onDestroy() {
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (getRouting != null && getRouting.getStatus() != AsyncTask.Status.FINISHED)
        	getRouting.cancel(true);
        super.onDestroy();
    }
    
}

