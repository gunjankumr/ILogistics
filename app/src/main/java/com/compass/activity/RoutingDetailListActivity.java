package com.compass.activity;

/**
 * @author Paresh N. Mayani
 * http://www.technotalkative.com
 */

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.compass.ilogistics.R;
import com.compass.model.CustomerModel;
import com.compass.model.InvoiceModel;
import com.compass.utils.AlertDialogManager;
import com.compass.utils.Constants;
import com.compass.utils.ValueHolder;
import com.compass.utils.WebService;

public class RoutingDetailListActivity extends DashBoardActivity {
	
	private ValueHolder valueHolder;
	private ProgressDialog dialog;
	private AlertDialogManager alert = new AlertDialogManager();
	
	private ExpandableListView lstView;
	private EditText etFilter;
	private GetRoutingTask getRoutingTask; 
	private MyListAdapter listAdapter;
	//private MyListAdapter2 listAdapter2;
	
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
        setContentView(R.layout.routing_detail_list);
        setHeader(getString(R.string.app_name), true, false, true);
        valueHolder = ValueHolder.getSingletonObject();
        etFilter = (EditText) findViewById(R.id.myFilter);
        lstView = (ExpandableListView )findViewById(R.id.list);

        
        lstView.setOnGroupClickListener(new OnGroupClickListener() {
        	public boolean onGroupClick(ExpandableListView parent, View v,int groupPosition, long id) { 
        		CustomerModel headerInfo = serviceResponseList.get(groupPosition);
        		valueHolder.setCustomerSelected(groupPosition);
        		valueHolder.setInvoiceSelected(-1);
        		valueHolder.setChildSelected(false);
        		startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
        	    //Toast.makeText(getBaseContext(), "Child on Header " + groupPosition,Toast.LENGTH_LONG).show();
        	    return true;
        	}   
        });
        
        lstView.setOnChildClickListener(new OnChildClickListener() {      	 
        	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {        	    
        		CustomerModel headerInfo = serviceResponseList.get(groupPosition);
        		valueHolder.setCustomerSelected(groupPosition);
        		InvoiceModel detailInfo =  headerInfo.getInvoiceList().get(childPosition);
        		valueHolder.setInvoiceSelected(childPosition);
        		valueHolder.setChildSelected(true);
        		//Toast.makeText(getBaseContext(), "Clicked on Detail " + headerInfo.getCUST_NAME()+"   "+detailInfo.getINV_BOOK_NO(), Toast.LENGTH_SHORT).show();
        		startActivity(new Intent(getApplicationContext(), ProductListActivity.class));
        		return false;
        	}
        });
        	 
        etFilter.addTextChangedListener(new TextWatcher() { 
        	public void afterTextChanged(Editable s) { }			 
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//listAdapter.filterData(s.toString());
		        //expandAll();
			}
		 });
        getRoutingTask = new GetRoutingTask();
        getRoutingTask.execute();
    }
    
    private void expandAll() {
    	int count = listAdapter.getGroupCount();
    	for (int i = 0; i < count; i++) {
    		lstView.expandGroup(i);
    	}
    }
    
    private void collapseAll() {
    	int count = listAdapter.getGroupCount();
    	for (int i = 0; i < count; i++){
    		lstView.collapseGroup(i);
    	}
	}
     
    public class GetRoutingTask extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
			dialog = ProgressDialog.show(RoutingDetailListActivity.this, "", getString(R.string.wait));
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
					serviceResponseList = service.getRoutingDetails1(valueHolder.getUsername(), Constants.DEVICE_ID, valueHolder.getCompanyCode(),valueHolder.getRoutingCode(),valueHolder.getDeliverySeq(),valueHolder.getDeliveryDate());
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
				listAdapter = new MyListAdapter(RoutingDetailListActivity.this, serviceResponseList);
		        lstView.setAdapter(listAdapter);
		        expandAll();
			}
		}		
		protected void onPostExecute(String result) {}
	}
  


    
    
    public class MyListAdapter extends BaseExpandableListAdapter {
    	 
    	 private Context context;
    	 private ArrayList<CustomerModel> continentList;
    	 private ArrayList<CustomerModel> originalList;
    	  
    	 public MyListAdapter(Context context, ArrayList<CustomerModel> continentList) {
    	  this.context = context;
    	  this.continentList = new ArrayList<CustomerModel>();
    	  this.continentList.addAll(continentList);
    	  this.originalList = new ArrayList<CustomerModel>();
    	  this.originalList.addAll(continentList);
    	 }
    	  
    	 @Override
    	 public Object getChild(int groupPosition, int childPosition) {
    	  ArrayList<InvoiceModel> countryList = continentList.get(groupPosition).getInvoiceList();
    	  return countryList.get(childPosition);
    	 }
    	 
    	 @Override
    	 public long getChildId(int groupPosition, int childPosition) {
    	  return childPosition;
    	 }
    	 
    	 @Override
    	 public View getChildView(int groupPosition, int childPosition, boolean isLastChild,View view, ViewGroup parent) {  
    	  InvoiceModel country = (InvoiceModel) getChild(groupPosition, childPosition);
    	  if (view == null) {
    	   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	   view = layoutInflater.inflate(R.layout.routing_invoice, null);
    	  } 	   
    	  TextView code = (TextView) view.findViewById(R.id.code);
    	  code.setText(country.getINV_BOOK_NO().trim());
    	  
    	  return view;
    		 /*
    		 ArrayList<InvoiceModel> countryList = continentList.get(groupPosition).getInvoiceList();
    		 CustExpListview SecondLevelexplv = new CustExpListview(RoutingDetailListActivity.this);   		 
             SecondLevelexplv.setAdapter(new MyListAdapter2(RoutingDetailListActivity.this, countryList));
             //SecondLevelexplv.setGroupIndicator(null);
             return SecondLevelexplv;
             */
    	 }
    	 
    	 @Override
    	 public int getChildrenCount(int groupPosition) {    	   
    	  ArrayList<InvoiceModel> countryList = continentList.get(groupPosition).getInvoiceList();
    	  return countryList.size();
    	 
    	 }
    	 
    	 @Override
    	 public Object getGroup(int groupPosition) {
    	  return continentList.get(groupPosition);
    	 }
    	 
    	 @Override
    	 public int getGroupCount() {
    	  return continentList.size();
    	 }
    	 
    	 @Override
    	 public long getGroupId(int groupPosition) {
    	  return groupPosition;
    	 }
    	 
    	 @Override
    	 public View getGroupView(int groupPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    	  CustomerModel continent = (CustomerModel) getGroup(groupPosition);
    	  if (convertView  == null) {
    	   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	   convertView  = layoutInflater.inflate(R.layout.routing_header, null);
    	   /*
    	   Button addButton = (Button)convertView .findViewById(R.id.addbutton);
           addButton.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View view) {
            	   Toast.makeText(getBaseContext(), "Child on Header " ,Toast.LENGTH_LONG).show();
               }
           });
           */
    	  }
    	  TextView heading = (TextView) convertView.findViewById(R.id.heading);
    	  heading.setText(continent.getCUST_CODE().trim()+"  "+continent.getCUST_NAME());
        
    	  return convertView;
    	 }
    	 
    	 @Override
    	 public boolean hasStableIds() {
    	  return true;
    	 }
    	 
    	 @Override
    	 public boolean isChildSelectable(int groupPosition, int childPosition) {
    	  return true;
    	 }
    	 
    	 public void filterData(String query) {
    		 //query = query.toLowerCase();
    		 Log.v("MyListAdapter", query);
    		 continentList.clear();   	   
    		 if(query.trim().length() > 0) {
    			 continentList.addAll(originalList);
    		 //} else {
    			 for(CustomerModel continent: originalList) {
    				 ArrayList<InvoiceModel> countryList = continent.getInvoiceList();
    				 ArrayList<InvoiceModel> newList = new ArrayList<InvoiceModel>();
    				 	for(InvoiceModel country: countryList) {
    				 		if(country.getINV_BOOK_NO().toLowerCase().contains(query)) {
    				 			newList.add(country);
    				 		}
    				 	}
    				 	if(newList.size() > 0){
    				 		CustomerModel nContinent = new CustomerModel();
    				 		nContinent.setInvoiceList(newList);
    				 		continentList.add(nContinent);
    				 	}
    			 }
    		 }
    		
    		 notifyDataSetChanged();
    	 }
 
    }

    
    /*
    public class CustExpListview extends ExpandableListView {
        int intGroupPosition, intChildPosition, intGroupid;
        
        public CustExpListview(Context context) {
               super(context);
        }
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
               widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);       
               heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);
               super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
    }
    */

    /*
    public class MyListAdapter2 extends BaseExpandableListAdapter {
  	 
   	 private Context context;
   	 private ArrayList<InvoiceModel> continentList;
   	 private ArrayList<InvoiceModel> originalList;
   	  
   	 public MyListAdapter2(Context context, ArrayList<InvoiceModel> continentList) {
   	  this.context = context;
   	  this.continentList = new ArrayList<InvoiceModel>();
   	  this.continentList.addAll(continentList);
   	  this.originalList = new ArrayList<InvoiceModel>();
   	  this.originalList.addAll(continentList);
   	 }
   	  
   	 @Override
   	 public Object getChild(int groupPosition, int childPosition) {
   	  ArrayList<ProductModel> countryList = continentList.get(groupPosition).getProductList();
   	  return countryList.get(childPosition);
   	 }
   	 
   	 @Override
   	 public long getChildId(int groupPosition, int childPosition) {
   	  return childPosition;
   	 }
   	 
   	 @Override
   	 public View getChildView(int groupPosition, int childPosition, boolean isLastChild,View view, ViewGroup parent) {  
   	  ProductModel country = (ProductModel) getChild(groupPosition, childPosition);
   	  if (view == null) {
   	   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   	   view = layoutInflater.inflate(R.layout.routing_product, null);
   	  }   	   
   	  TextView code = (TextView) view.findViewById(R.id.code);
   	  code.setText(country.getPRODUCT_CODE().trim());
   	  
   	  return view;
   	 }
   	 
   	 @Override
   	 public int getChildrenCount(int groupPosition) {    	   
   	  ArrayList<ProductModel> countryList = continentList.get(groupPosition).getProductList();
   	  return countryList.size();
   	 
   	 }
   	 
   	 @Override
   	 public Object getGroup(int groupPosition) {
   	  return continentList.get(groupPosition);
   	 }
   	 
   	 @Override
   	 public int getGroupCount() {
   	  return continentList.size();
   	 }
   	 
   	 @Override
   	 public long getGroupId(int groupPosition) {
   	  return groupPosition;
   	 }
   	 
   	 @Override
   	 public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
   	  InvoiceModel continent = (InvoiceModel) getGroup(groupPosition);
   	  if (view == null) {
   	   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   	   view = layoutInflater.inflate(R.layout.routing_invoice, null);
   	  }   	   
   	  TextView heading = (TextView) view.findViewById(R.id.code);
   	  heading.setText(continent.getINV_BOOK_NO().trim());
   	   
   	  return view;
   	 }
   	 
   	 @Override
   	 public boolean hasStableIds() {
   	  return true;
   	 }
   	 
   	 @Override
   	 public boolean isChildSelectable(int groupPosition, int childPosition) {
   	  return true;
   	 }
   	
   	}
    */

    
    @Override
    public void onBackPressed() {
    	valueHolder.clearAll();
    	Log.d(Constants.LOGD, "ON BACK");
    	super.onBackPressed();
    }
    
    @Override
    protected void onDestroy() {
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (getRoutingTask != null && getRoutingTask.getStatus() != AsyncTask.Status.FINISHED) {
        	getRoutingTask.cancel(true);
        }
        Log.d(Constants.LOGD, "ON DESTROY");
        super.onDestroy();
    }
    
}

