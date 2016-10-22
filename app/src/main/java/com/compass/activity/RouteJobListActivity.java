package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.model.RouteJob;
import com.compass.utils.AlertDialogManager;
import com.compass.utils.Constants;
import com.compass.utils.SessionManager;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;

public class RouteJobListActivity extends DashBoardActivity {
	
	private ValueHolder valueHolder;
	private ProgressDialog dialog;
	private AlertDialogManager alert = new AlertDialogManager();
	
	private ListView lstView;
	private EditText etFilter;
	private TextView txtLeft,txtRight;
	private GetRoutingTask getRoutingTask; 
	private SessionManager session;

	private static final String SERVER_ERROR = "SERVER_ERROR";
	private static final String NETWORK_ERROR = "NETWORK_ERROR";
	private static final String CANCELLED = "CANCELLED";
	private static final String SUCCESS = "SUCCESS";
	
	ListAdapter dataAdapter = null;
	
	private String serviceResponse = null;
	//private ArrayList<HashMap<String, String>> serviceResponseList;
	private ArrayList<RouteJob> serviceResponseList = new ArrayList<RouteJob>();
	
	private int[] colors = new int[] {0x30ffffff, 0x30f0f0f0};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_job_list);
        setHeader(getString(R.string.app_name), true, false, true);
        valueHolder = ValueHolder.getSingletonObject();
        session = new SessionManager(getApplicationContext());

        etFilter = (EditText) findViewById(R.id.myFilter);
        lstView = (ListView)findViewById(R.id.list);
        txtLeft = (TextView)findViewById(R.id.left);
        txtRight = (TextView)findViewById(R.id.right);
        
        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.routing_item, serviceResponseList);
        lstView.setAdapter(dataAdapter);
        lstView.setTextFilterEnabled(true);
 
        etFilter.addTextChangedListener(new TextWatcher() {
        	public void afterTextChanged(Editable s) { }			 
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				dataAdapter.getFilter().filter(s.toString());
			}
		 });
        getRoutingTask = new GetRoutingTask();
        getRoutingTask.execute();
    }

    public class GetRoutingTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(RouteJobListActivity.this, "", getString(R.string.wait));
			dialog.setCancelable(true);
		}		
		@Override
		protected String doInBackground(String... params) {
				WebService service = new WebService();
				try {
					if (isCancelled()) {
	                    publishProgress(CANCELLED); //Notify your activity that you had canceled the task
	                    return (null); // don't forget to terminate this method
	                }
					
					serviceResponseList = service.getRouteJob(valueHolder.getUsername(), Constants.DEVICE_ID, valueHolder.getCompanyCode(), valueHolder.getRoutingCode(), session.getPreferenceVal("ROUND"), valueHolder.getDeliveryDate());
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
				txtLeft.setText(serviceResponseList.size() + " " + getString(R.string.remarkJobRoute));
				txtRight.setText(valueHolder.getRoutingCode());
				valueHolder.setRouteJobList(serviceResponseList);
				
		        dataAdapter = new ListAdapter(getApplicationContext(), R.layout.routing_item, serviceResponseList);
		        lstView.setAdapter(dataAdapter);
			}
		}		
		protected void onPostExecute(String result) {}
	}
  

/*************/
    private class ListAdapter extends ArrayAdapter<RouteJob> {
      	 
    	  private ArrayList<RouteJob> originalList;
    	  private ArrayList<RouteJob> filterList;
    	  private CustomerFilter filter;
    	 
    	  public ListAdapter(Context context, int textViewResourceId, ArrayList<RouteJob> fList) {
    	   super(context, textViewResourceId, fList);
    	   this.filterList = new ArrayList<RouteJob>();
    	   this.filterList.addAll(fList);
    	   this.originalList = new ArrayList<RouteJob>();
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
			 TextView textPosition;
			 TextView text1;
			 TextView text2;
			 TextView text3;
			 TextView text4;
		 }
    	 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  ViewHolder holder = null;
			  if (convertView == null) {
				  LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  convertView = vi.inflate(R.layout.route_job_item, null);
				  holder = new ViewHolder();
				  holder.textPosition = (TextView) convertView.findViewById(R.id.textPosition);
				  holder.text1 = (TextView) convertView.findViewById(R.id.textView1);
				  holder.text2 = (TextView) convertView.findViewById(R.id.textView2);
				  holder.text3 = (TextView) convertView.findViewById(R.id.textView3);
				  holder.text4 = (TextView) convertView.findViewById(R.id.textView4);
				  convertView.setTag(holder);
			  } else {
			   	holder = (ViewHolder) convertView.getTag();
			  }
			  
			  RouteJob routing = filterList.get(position);
			  holder.textPosition.setText(routing.getPOSITION());
			  holder.text1.setText(routing.getINV_BOOK_NO()+" - "+routing.getCUST_NAME());
			  holder.text2.setText("");
			  holder.text3.setText(routing.getJOB_ORDER());
			  
			  convertView.setBackgroundColor(colors[position % colors.length]);
			   
		   return convertView;
		 
		  }
 
	private class CustomerFilter extends Filter {
    	   @Override
    	   protected FilterResults performFiltering(CharSequence constraint) {
    		   constraint = constraint.toString().toLowerCase();
    		   FilterResults result = new FilterResults();
    		   if(constraint != null && constraint.toString().length() > 0) {
    			   ArrayList<RouteJob> filteredItems = new ArrayList<RouteJob>();
    			   for(int i = 0, l = originalList.size(); i < l; i++) {
    				   RouteJob country = originalList.get(i);
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
    		   filterList = (ArrayList<RouteJob>)results.values;
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
    
    
    @Override
    protected void onDestroy() {
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (getRoutingTask != null && getRoutingTask.getStatus() != AsyncTask.Status.FINISHED)
        	getRoutingTask.cancel(true);
        super.onDestroy();
    }
    
}

